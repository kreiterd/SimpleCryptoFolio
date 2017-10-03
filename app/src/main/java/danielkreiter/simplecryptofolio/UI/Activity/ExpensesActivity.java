package danielkreiter.simplecryptofolio.UI.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import danielkreiter.simplecryptofolio.Database.DbPurchase;
import danielkreiter.simplecryptofolio.Model.Purchase;
import danielkreiter.simplecryptofolio.R;
import danielkreiter.simplecryptofolio.UI.PieChartValueFormatter;


public class ExpensesActivity extends AppCompatActivity {

    List<Purchase> purchases;

    DbPurchase dbPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        dbPurchase = new DbPurchase(this.getApplicationContext());
        purchases = dbPurchase.readPurchases();
        createPieChart(purchases);
    }

    void createPieChart(List<Purchase> purchases) {
        PieChart chart = findViewById(R.id.chart);
        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        Map<String, Double> totalValue = new HashMap<>();

        for (Purchase purchase : purchases) {
            if (totalValue.containsKey(purchase.getCurrencytype())) {
                totalValue.put(purchase.getCurrencytype(), totalValue.get(purchase.getCurrencytype()) + purchase.getValue());
            } else {
                totalValue.put(purchase.getCurrencytype(), purchase.getValue());
            }
        }

        Iterator it = totalValue.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Double d = (Double) pair.getValue();
            entries.add(new PieEntry(d.floatValue(), pair.getKey().toString()));
            // ToDo: Let users choose own colors
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            colors.add(color);
        }


        // create the dataSet
        PieDataSet dataSet = new PieDataSet(entries, "Label");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        // build the pieChart
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PieChartValueFormatter());
        chart.setData(pieData);

        // refresh the pieChart
        chart.invalidate();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buyvalue_button:
                startActivity(new Intent(this, PurchaseInputActivity.class));
                break;
            case R.id.currentvalue_button:
                break;

        }
    }

}
