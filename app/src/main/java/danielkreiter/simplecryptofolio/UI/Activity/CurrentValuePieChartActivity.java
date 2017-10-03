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


    DbPurchase mDbPurchase;
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
        mDbPurchase = new DbPurchase(this.getApplicationContext());
        mChart = findViewById(R.id.chart);
        mTotalAmount = new HashMap<>();
        mColors = new ArrayList<>();
        mEntries = new ArrayList<>();
        mPurchases = mDbPurchase.readPurchases();
        createCurrentValueChart();
    }


    void loadCurrencyData() {

        for (Purchase purchase : mPurchases) {
            if (mTotalAmount.containsKey(purchase.getCurrencytype())) {
                mTotalAmount.put(purchase.getCurrencytype(), mTotalAmount.get(purchase.getCurrencytype()) + purchase.getAmount());
            } else {
                mTotalAmount.put(purchase.getCurrencytype(), purchase.getAmount());
            }
        }

        Iterator it = mTotalAmount.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String key = pair.getKey().toString();

            LoadCurrencyPriceToActivityATask loadCurrencyPriceToActivityATask
                    = new LoadCurrencyPriceToActivityATask(key, this);
            loadCurrencyPriceToActivityATask.execute();
        }

    }


    void createCurrentValueChart() {


        // create the dataSet
        mDataSet = new PieDataSet(mEntries, "Label");
        //mDataSet.setColors(mColors);
        mDataSet.setValueTextSize(12f);
        mDataSet.setValueTextColor(Color.WHITE);

        // build the pieChart
        PieData pieData = new PieData(mDataSet);
        pieData.setValueFormatter(new PieChartValueFormatter());
        mChart.setData(pieData);

        // refresh the pieChart
        mChart.invalidate();
        loadCurrencyData();

    }


    @Override
    public void postExecuteUpdateView(JSONObject result) {


        Iterator<String> iter = result.keys();

        while (iter.hasNext()) {
            String key = iter.next();
            try {
                PieEntry pieEntry = new PieEntry((float) result.getDouble(key) * this.mTotalAmount.get(key).floatValue(), key);

                mDataSet.addEntry(pieEntry);
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                mDataSet.addColor(color);
                mChart.notifyDataSetChanged(); // let the chart know it's data changed
                mChart.invalidate(); // refresh
            } catch (JSONException e) {
                // Something went wrong!
            }
        }

    }

    @Override
    public void preExecuteUpdateView() {

    }

    public void onClick(View view) {
    }
}
