package danielkreiter.simplecryptofolio.UI.Fragments;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import danielkreiter.simplecryptofolio.Database.DbPurchase;
import danielkreiter.simplecryptofolio.Model.Purchase;
import danielkreiter.simplecryptofolio.R;
import danielkreiter.simplecryptofolio.UI.Elements.PieChartValueFormatter;
import danielkreiter.simplecryptofolio.UI.Interfaces.ISendDataToUI;
import danielkreiter.simplecryptofolio.UI.Tasks.LoadCurrencyPriceToFragmentATask;

public class ValueChartFragment extends Fragment implements ISendDataToUI {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "ValueChartFragment";
    PieChart valuePieChart;
    ProgressBar loadValuesProgressBar;
    TextView loadValuesTextView;
    Map<String, Double> totalAmount;
    List<Integer> colors;
    List<PieEntry> entries;
    List<Purchase> purchases;
    PieDataSet valueDataSet;

    private ArrayList<LoadCurrencyPriceToFragmentATask> loadingTasks;
    private int page;

    public static ValueChartFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ValueChartFragment fragment = new ValueChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate called.");
        page = getArguments().getInt(ARG_PAGE);

        loadingTasks = new ArrayList<>();
        totalAmount = new HashMap<>();
        colors = new ArrayList<>();
        entries = new ArrayList<>();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_value_chart, container, false);
        valuePieChart = view.findViewById(R.id.chart);
        loadValuesProgressBar = view.findViewById(R.id.loading_progressbar);
        loadValuesTextView = view.findViewById(R.id.loading_textview);

        Log.i(TAG, "onCreateView called.");

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createCurrentValueChart();

        loadCurrencyData();
    }


    void loadCurrencyData() {
        valuePieChart.setVisibility(View.INVISIBLE);
        loadValuesProgressBar.setVisibility(View.VISIBLE);
        purchases = (new DbPurchase(getActivity())).readPurchases();
        totalAmount.clear();
        entries.clear();
        createCurrentValueChart();

        // Add up the values of all purchases for each separate currency
        // ToDo: save the total amount for each currency in the database
        for (Purchase purchase : purchases) {

            String currencyName = purchase.getCurrencytype();
            Double amount = purchase.getAmount();

            if (totalAmount.containsKey(currencyName))
                totalAmount.put(currencyName, totalAmount.get(currencyName) + amount);
            else
                totalAmount.put(currencyName, amount);

        }


        // load the actual value of each currency and pass the results to postExecuteUpdateView(...)
        for (Map.Entry<String, Double> entry : totalAmount.entrySet()) {
            LoadCurrencyPriceToFragmentATask loadCurrencyPriceToFragmentATask = new LoadCurrencyPriceToFragmentATask(entry.getKey(), this);
            loadCurrencyPriceToFragmentATask.execute();
            loadingTasks.add(loadCurrencyPriceToFragmentATask);
        }
    }


    void createCurrentValueChart() {

        valuePieChart.getLegend().setEnabled(false);
        valuePieChart.getDescription().setEnabled(false);

        // create the dataSet
        valueDataSet = new PieDataSet(entries, "Label");

        // chart settings
        valueDataSet.setValueTextSize(12f);
        valueDataSet.setValueTextColor(Color.WHITE);

        // build the pieChart
        PieData pieData = new PieData(valueDataSet);
        pieData.setValueFormatter(new PieChartValueFormatter());
        valuePieChart.setData(pieData);

        // refresh the pieChart
        valuePieChart.invalidate();

    }


    @Override
    public void postExecuteUpdateView(JSONObject result) {

        Iterator<String> iter = result.keys();
        while (iter.hasNext()) {
            String key = iter.next();

            try {
                PieEntry pieEntry = new PieEntry((float) result.getDouble(key) * this.totalAmount.get(key).floatValue(), key);
                valueDataSet.addEntry(pieEntry);
                // ToDo: let users choose their own color
                Random rnd = new Random();
                Integer color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                while (colors.contains(color)) {
                    color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                }
                valueDataSet.addColor(color);
                valuePieChart.notifyDataSetChanged(); // let the chart know it's data changed
                valuePieChart.invalidate(); // refresh
            } catch (JSONException e) {
                // ToDo: handle exceptions! don't leave this empty!
            }
        }

        int taskCounter = 1;
        for (LoadCurrencyPriceToFragmentATask loadCurrencyPriceToFragmentATask : loadingTasks) {
            if ((loadCurrencyPriceToFragmentATask.getStatus() == AsyncTask.Status.FINISHED)) {
                taskCounter++;
            }
        }
        int p = 100 / loadingTasks.size();

        loadValuesProgressBar.setProgress(p * taskCounter);
        Resources res = getResources();
        String loadCurrencyData = getResources().getString(R.string.load_currency_data);
        loadValuesTextView.setText(loadCurrencyData + "" + p * taskCounter + "%");
        if (taskCounter == loadingTasks.size()) {
            loadValuesTextView.setText(loadCurrencyData + "100%");
            valuePieChart.setVisibility(View.VISIBLE);
            loadValuesProgressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void preExecuteUpdateView() {

    }
}