package danielkreiter.simplecryptofolio.UI.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

public class CurrentValuePieChartActivity extends AppCompatActivity implements ISendDataToActivity {


    PieChart mChart;
    Map<String, Double> mTotalAmount;
    List<Integer> mColors;
    List<PieEntry> mEntries;
    List<Purchase> mPurchases;
    PieDataSet mDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_value_pie_chart);

        mChart = findViewById(R.id.chart);
        mTotalAmount = new HashMap<>();
        mColors = new ArrayList<>();
        mEntries = new ArrayList<>();

        mPurchases = (new DbPurchase(this.getApplicationContext())).readPurchases();

        createCurrentValueChart();
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

    public void onClick(View view) {
    }
}
