package danielkreiter.simplecryptofolio.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import danielkreiter.simplecryptofolio.Database.DbPurchase;
import danielkreiter.simplecryptofolio.Model.Purchase;
import danielkreiter.simplecryptofolio.R;


public class MainActivity extends AppCompatActivity {

    private DbPurchase dbPurchase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inizialise facebooks stetho tool
        Stetho.initializeWithDefaults(this);


        this.dbPurchase = new DbPurchase(this.getApplicationContext());

        // write some samples into the database
        writeExamplePurchases();


    }

    void writeExamplePurchases() {
        Purchase p1 = new Purchase();
        p1.setCurrencytype("Stratis");
        p1.setAmount(50.0);
        p1.setPricepercoin(2);
        p1.setValue(105);
        p1.setDate("08.08.20");
        long l = dbPurchase.writePurchase(p1);
        long id = l;
        Toast.makeText(getApplicationContext(), "Written: " + id, Toast.LENGTH_LONG).show();

        Purchase p2 = new Purchase();
        p2.setCurrencytype("Ethereum");
        p2.setAmount(1.0);
        p2.setPricepercoin(200);
        p2.setValue(205);
        p2.setDate("06.08.20");
        id = dbPurchase.writePurchase(p2);
        Toast.makeText(getApplicationContext(), "Written: " + id, Toast.LENGTH_LONG).show();

        Purchase p3 = new Purchase();
        p3.setCurrencytype("Bitcoin");
        p3.setAmount(0.10);
        p3.setPricepercoin(2000);
        p3.setValue(205);
        p3.setDate("04.08.20");
        id = dbPurchase.writePurchase(p3);
        Toast.makeText(getApplicationContext(), "Written: " + id, Toast.LENGTH_LONG).show();
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expenses:
                Intent goToNextActivity = new Intent(getApplicationContext(), ExpensesActivity.class);
                startActivity(goToNextActivity);
                break;
            case R.id.button_purchaseInputActivity:
                startActivity(new Intent(this, PurchaseInputActivity.class));
                break;
            case R.id.overview:
                startActivity(new Intent(this, OverviewActivity.class));
                break;
        }
    }

}
