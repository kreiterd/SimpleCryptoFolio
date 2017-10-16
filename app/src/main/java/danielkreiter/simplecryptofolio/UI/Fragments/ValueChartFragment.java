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
    PieChart mChart;
    ProgressBar progressBar;
    TextView mLoadingATextView;
    Map<String, Double> mTotalAmount;
    List<Integer> mColors;
    List<PieEntry> mEntries;
    List<Purchase> mPurchases;
    PieDataSet mDataSet;
    private ArrayList<LoadCurrencyPriceToFragmentATask> mLoadCurrencyTasks;
    private int mPage;

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
        mPage = getArguments().getInt(ARG_PAGE);

        mLoadCurrencyTasks = new ArrayList<>();
        mTotalAmount = new HashMap<>();
        mColors = new ArrayList<>();
        mEntries = new ArrayList<>();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_value_chart, container, false);
        mChart = view.findViewById(R.id.chart);
        progressBar = view.findViewById(R.id.loading_progressbar);
        mLoadingATextView = view.findViewById(R.id.loading_textview);

        Log.i(TAG, "onCreateView called.");

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated called.");
        // setRetainInstance(true);
        createCurrentValueChart();

        loadCurrencyData();
    }

    void loadCurrencyData() {
        mChart.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        mPurchases = (new DbPurchase(getActivity())).readPurchases();
        mTotalAmount.clear();
        mEntries.clear();
        createCurrentValueChart();

        // Add up the values of all mPurchases for each separate currency
        // ToDo: save the whole amount for each currency in the database
        for (Purchase purchase : mPurchases) {

            String currencyName = purchase.getCurrencytype();
            Double amount = purchase.getAmount();

            if (mTotalAmount.containsKey(currencyName))
                mTotalAmount.put(currencyName, mTotalAmount.get(currencyName) + amount);
            else
                mTotalAmount.put(currencyName, amount);

        }


        // load the actual value of each currency and pass the results to postExecuteUpdateView(...)
        for (Map.Entry<String, Double> entry : mTotalAmount.entrySet()) {
            LoadCurrencyPriceToFragmentATask loadCurrencyPriceToFragmentATask = new LoadCurrencyPriceToFragmentATask(entry.getKey(), this);
            loadCurrencyPriceToFragmentATask.execute();
            mLoadCurrencyTasks.add(loadCurrencyPriceToFragmentATask);
        }
    }


    void createCurrentValueChart() {

        mChart.getLegend().setEnabled(false);
        mChart.getDescription().setEnabled(false);

        // create the dataSet
        mDataSet = new PieDataSet(mEntries, "Label");

        // chart settings
        mDataSet.setValueTextSize(12f);
        mDataSet.setValueTextColor(Color.WHITE);

        // build the pieChart
        PieData pieData = new PieData(mDataSet);
        pieData.setValueFormatter(new PieChartValueFormatter());
        mChart.setData(pieData);

        // refresh the pieChart
        mChart.invalidate();

    }


    @Override
    public void postExecuteUpdateView(JSONObject result) {


        Iterator<String> iter = result.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                PieEntry pieEntry = new PieEntry((float) result.getDouble(key) * this.mTotalAmount.get(key).floatValue(), key);
                mDataSet.addEntry(pieEntry);
                // ToDo: let users choose their own color
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                mDataSet.addColor(color);
                mChart.notifyDataSetChanged(); // let the chart know it's data changed
                mChart.invalidate(); // refresh
            } catch (JSONException e) {
                // ToDo: handle exceptions! don't leave this empty!
            }
        }

        int taskCounter = 1;
        for (LoadCurrencyPriceToFragmentATask loadCurrencyPriceToFragmentATask : mLoadCurrencyTasks) {
            if ((loadCurrencyPriceToFragmentATask.getStatus() == AsyncTask.Status.FINISHED)) {
                taskCounter++;
            }
        }
        int p = 100 / mLoadCurrencyTasks.size();

        progressBar.setProgress(p * taskCounter);
        Resources res = getResources();
        String loadCurrencyData = res.getString(R.string.load_currency_data);
        mLoadingATextView.setText(loadCurrencyData + "" + p * taskCounter + "%");
        if (taskCounter == mLoadCurrencyTasks.size()) {
            mLoadingATextView.setText(loadCurrencyData + "100%");
            mChart.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void preExecuteUpdateView() {

    }
}