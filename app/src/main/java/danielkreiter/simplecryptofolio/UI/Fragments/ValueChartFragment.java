package danielkreiter.simplecryptofolio.UI.Fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import danielkreiter.simplecryptofolio.UI.Tasks.AsyncTaskResult;
import danielkreiter.simplecryptofolio.UI.Tasks.LoadCurrencyPriceToFragmentATask;

public class ValueChartFragment extends BasicFragment implements ISendDataToUI {
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
    Random rnd;
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
        page = getArguments().getInt(ARG_PAGE);
        loadingTasks = new ArrayList<>();
        totalAmount = new HashMap<>();
        colors = new ArrayList<>();
        entries = new ArrayList<>();
        rnd = new Random();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_value_chart, container, false);


        valuePieChart = view.findViewById(R.id.chart);
        loadValuesProgressBar = view.findViewById(R.id.loading_progressbar);
        loadValuesTextView = view.findViewById(R.id.loading_textview);

        if (!viewReady) {
            loadCurrencyData();
        }

        this.viewReady = true;

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        createCurrentValueChart();

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && viewReady) {
            // reload the chart
            loadCurrencyData();
        }
    }


    @Override
    public void postExecuteUpdateView(AsyncTaskResult<JSONObject> result) {
        if (result.getError() != null) {
            showLoadingError();
            Log.e(TAG, result.getError().getMessage().toString());
        } else {
            JSONObject realResult = result.getResult();
            Iterator<String> iter = realResult.keys();
            while (iter.hasNext()) {
                valueDataSet.addEntry(createPieEntry(iter.next(), realResult));
                // set the color for the pie piece
                // ToDo: let users choose their own color
                Integer color = randomColor();
                while (colors.contains(color))
                    color = randomColor();
                valueDataSet.addColor(color);

            }
            refreshChart();
            updateProgressbar();
        }
    }

    @Override
    public void preExecuteUpdateView() {

    }

    void updateProgressbar() {
        int countFinishedTasks = 1;
        for (LoadCurrencyPriceToFragmentATask loadCurrencyPriceToFragmentATask : loadingTasks) {
            if ((loadCurrencyPriceToFragmentATask.getStatus() == AsyncTask.Status.FINISHED)) {

                countFinishedTasks++;
            }
        }
        int progress = 100 / loadingTasks.size();
        loadValuesProgressBar.setProgress(progress * countFinishedTasks);
        String loadCurrencyData = getResources().getString(R.string.load_currency_data);
        loadValuesTextView.setText(loadCurrencyData + "" + progress * countFinishedTasks + "%");
        if (countFinishedTasks == loadingTasks.size()) {
            loadValuesTextView.setText(loadCurrencyData + "100%");
            showChart();
        }
    }

    void loadCurrencyData() {
        clearValues();
        showProgressBar();
        purchases = (new DbPurchase(getActivity())).readPurchases();
        // add up the values of all purchases for each separate currency
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
            LoadCurrencyPriceToFragmentATask loadCurrencyPriceToFragmentATask
                    = new LoadCurrencyPriceToFragmentATask(entry.getKey(), this);
            loadCurrencyPriceToFragmentATask.execute();
            loadingTasks.add(loadCurrencyPriceToFragmentATask);
        }
    }


    void createCurrentValueChart() {

        // create the dataSet
        valueDataSet = new PieDataSet(entries, "Label");

        // chart settings
        valuePieChart.getLegend().setEnabled(false);
        valuePieChart.getDescription().setEnabled(false);
        valueDataSet.setValueTextSize(12f);
        valueDataSet.setValueTextColor(Color.WHITE);

        // build the pieChart
        PieData pieData = new PieData(valueDataSet);
        pieData.setValueFormatter(new PieChartValueFormatter());
        valuePieChart.setData(pieData);

        // refresh the pieChart
        refreshChart();
    }


    PieEntry createPieEntry(String key, JSONObject result) {
        PieEntry pieEntry = null;
        try {
            pieEntry = new PieEntry((float) result.getDouble(key) *
                    this.totalAmount.get(key).floatValue(), key);
        } catch (JSONException e) {
            showLoadingError();
        } finally {
            return pieEntry;
        }
    }

    void showLoadingError() {
        valuePieChart.setVisibility(View.INVISIBLE);
        loadValuesProgressBar.setVisibility(View.INVISIBLE);
        loadValuesTextView.setVisibility(View.VISIBLE);
        this.loadValuesTextView.setText(getResources().getString(R.string.load_values_error));
    }

    void showLoadingError(String error) {
        valuePieChart.setVisibility(View.INVISIBLE);
        loadValuesProgressBar.setVisibility(View.INVISIBLE);
        loadValuesTextView.setVisibility(View.VISIBLE);
        this.loadValuesTextView.setText(error);
    }

    void clearValues() {
        totalAmount.clear();
        entries.clear();
        loadingTasks.clear();
        loadValuesProgressBar.setProgress(0);
    }

    void showProgressBar() {
        valuePieChart.setVisibility(View.INVISIBLE);
        loadValuesProgressBar.setVisibility(View.VISIBLE);
        loadValuesTextView.setVisibility(View.VISIBLE);
    }

    void showChart() {
        valuePieChart.setVisibility(View.VISIBLE);
        loadValuesProgressBar.setVisibility(View.INVISIBLE);
        loadValuesTextView.setVisibility(View.INVISIBLE);
    }

    Integer randomColor() {
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }


    void refreshChart() {
        if (valuePieChart != null) {
            valuePieChart.notifyDataSetChanged(); // let the chart know it's data changed
            valuePieChart.invalidate(); // refresh
        }
    }
}