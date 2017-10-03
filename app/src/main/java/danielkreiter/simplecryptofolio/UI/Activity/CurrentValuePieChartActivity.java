package danielkreiter.simplecryptofolio.UI.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

import danielkreiter.simplecryptofolio.Database.DbPurchase;
import danielkreiter.simplecryptofolio.Model.Purchase;
import danielkreiter.simplecryptofolio.R;
import danielkreiter.simplecryptofolio.UI.LoadCurrencyPriceToActivityATask;
import danielkreiter.simplecryptofolio.UI.PieChartValueFormatter;

public class CurrentValuePieChartActivity extends AppCompatActivity implements ISendDataToActivity {


    DbPurchase dbPurchase;
    PieChart chart;
    Map<String, Double> totalAmount;
    List<Integer> colors;
    List<PieEntry> entries;
    List<Purchase> purchases;

    PieDataSet dataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_value_pie_chart);
        dbPurchase = new DbPurchase(this.getApplicationContext());
        chart = findViewById(R.id.chart);
        totalAmount = new HashMap<>();
        colors = new ArrayList<>();
        entries = new ArrayList<>();
        purchases = dbPurchase.readPurchases();
        createCurrentValueChart();
    }


    void loadCurrencyData() {


        Iterator it = totalAmount.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String key = pair.getKey().toString();

            LoadCurrencyPriceToActivityATask loadCurrencyPriceToActivityATask
                    = new LoadCurrencyPriceToActivityATask(key, this);
            loadCurrencyPriceToActivityATask.execute();
        }

    }


    void createCurrentValueChart() {
        for (Purchase purchase : purchases) {
            if (totalAmount.containsKey(purchase.getCurrencytype())) {
                totalAmount.put(purchase.getCurrencytype(), totalAmount.get(purchase.getCurrencytype()) + purchase.getAmount());
            } else {
                totalAmount.put(purchase.getCurrencytype(), purchase.getAmount());
            }
        }


        // create the dataSet
        dataSet = new PieDataSet(entries, "Label");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        // build the pieChart
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PieChartValueFormatter());
        chart.setData(pieData);

        // refresh the pieChart
        chart.invalidate();
        loadCurrencyData();

    }


    @Override
    public void postExecuteUpdateView(JSONObject result) {


        Iterator<String> iter = result.keys();

        while (iter.hasNext()) {
            String key = iter.next();
            try {
                PieEntry pieEntry = new PieEntry((float) result.getDouble(key), key);
                dataSet.addEntry(pieEntry);
                chart.notifyDataSetChanged(); // let the chart know it's data changed
                chart.invalidate(); // refresh
            } catch (JSONException e) {
                // Something went wrong!
            }
        }

    }

    @Override
    public void preExecuteUpdateView() {

    }
}
