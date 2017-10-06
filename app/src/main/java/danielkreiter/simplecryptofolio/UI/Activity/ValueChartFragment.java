package danielkreiter.simplecryptofolio.UI.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import danielkreiter.simplecryptofolio.UI.LoadCurrencyPriceToActivityATask;
import danielkreiter.simplecryptofolio.UI.PieChartValueFormatter;

public class ValueChartFragment extends Fragment implements ISendDataToActivity {
    public static final String ARG_PAGE = "ARG_PAGE";
    PieChart mChart;
    Map<String, Double> mTotalAmount;
    List<Integer> mColors;
    List<PieEntry> mEntries;
    List<Purchase> mPurchases;
    PieDataSet mDataSet;
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
        mPage = getArguments().getInt(ARG_PAGE);


        mTotalAmount = new HashMap<>();
        mColors = new ArrayList<>();
        mEntries = new ArrayList<>();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_value_chart, container, false);
        mChart = view.findViewById(R.id.chart);


        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // setRetainInstance(true);
        createCurrentValueChart();
        mPurchases = (new DbPurchase(getActivity())).readPurchases();

        loadCurrencyData();
    }

    void loadCurrencyData() {

        // Add up the values of all purchases for each separate currency
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
        for (Map.Entry<String, Double> entry : mTotalAmount.entrySet())
            (new LoadCurrencyPriceToActivityATask(entry.getKey(), this)).execute();
    }


    void createCurrentValueChart() {


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

    }

    @Override
    public void preExecuteUpdateView() {

    }
}