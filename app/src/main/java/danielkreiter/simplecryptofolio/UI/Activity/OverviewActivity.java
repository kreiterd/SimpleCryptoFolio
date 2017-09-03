package danielkreiter.simplecryptofolio.UI.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import danielkreiter.simplecryptofolio.Database.DbPurchase;
import danielkreiter.simplecryptofolio.Model.Purchase;
import danielkreiter.simplecryptofolio.R;
import danielkreiter.simplecryptofolio.UI.PurchaseOverviewAdapter;

public class OverviewActivity extends AppCompatActivity {

    ListView overviewList;
    Button getChecked;

    List<Purchase> purchases;
    PurchaseOverviewAdapter adapter;

    private DbPurchase dbPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        // set views
        this.overviewList = findViewById(R.id.overview_listview);
        this.overviewList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        this.getChecked = findViewById(R.id.delete_button);

        // logic
        dbPurchase = new DbPurchase(this.getApplicationContext());
        purchases = dbPurchase.readPurchases();

        // adapter
        adapter = new PurchaseOverviewAdapter(this, purchases);
        overviewList.setAdapter(adapter);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_button:
                deletePurchases();
                break;
        }
    }

    private void deletePurchases() {
        for (Purchase p : adapter.getAllChecked()) {
            dbPurchase.deletePurchase(p.getId());
            purchases.remove(p);
        }
        adapter.notifyDataSetChanged();
    }

}

