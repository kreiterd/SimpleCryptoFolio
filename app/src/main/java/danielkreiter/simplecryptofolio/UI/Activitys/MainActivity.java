package danielkreiter.simplecryptofolio.UI.Activitys;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.facebook.stetho.Stetho;

import danielkreiter.simplecryptofolio.Database.DbCryptocurrency;
import danielkreiter.simplecryptofolio.Database.DbPurchase;
import danielkreiter.simplecryptofolio.Database.SimpleCryptoFolioContract;
import danielkreiter.simplecryptofolio.Model.Cryptocurrency;
import danielkreiter.simplecryptofolio.Model.Purchase;
import danielkreiter.simplecryptofolio.R;
import danielkreiter.simplecryptofolio.UI.Adapter.TabFragmentPagerAdapter;


/**
 * Main activity.
 */
public class MainActivity extends AppCompatActivity {

    private DbPurchase dbPurchase;
    private DbCryptocurrency dbCryptocurrency;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_pie_chart_3x,
            R.drawable.ic_add_3x,
            R.drawable.ic_delete_3x,
            R.drawable.ic_settings_black_24dp};

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getApplicationContext().deleteDatabase(SimpleCryptoFolioContract.DATABASE_NAME);

        // inizialise facebooks stetho tool
        Stetho.initializeWithDefaults(this);


        this.dbPurchase = new DbPurchase(this.getApplicationContext());
        this.dbCryptocurrency = new DbCryptocurrency(this.getApplicationContext());

        // write some samples into the database
        writeExamplePurchases();
        writeExampleCryptocurrencies();
        writeExamplePurchases();
        writeExampleCryptocurrencies();
        writeExamplePurchases();
        writeExampleCryptocurrencies();
        writeExamplePurchases();
        writeExampleCryptocurrencies();
        writeExamplePurchases();
        writeExampleCryptocurrencies();
        writeExamplePurchases();
        writeExampleCryptocurrencies();


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));


        // Give the TabLayout the ViewPager
        tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(
                tabLayout));

        setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    /**
     * Write example cryptocurrencies.
     */
    void writeExampleCryptocurrencies() {
        Cryptocurrency cryptocurrency1 = new Cryptocurrency();
        cryptocurrency1.setName("ETH");
        Cryptocurrency cryptocurrency2 = new Cryptocurrency();
        cryptocurrency2.setName("BTC");
        dbCryptocurrency.writeCryptocurrency(cryptocurrency1);
        dbCryptocurrency.writeCryptocurrency(cryptocurrency2);

    }

    /**
     * Write example purchases.
     */
    void writeExamplePurchases() {
        Purchase p1 = new Purchase();
        p1.setCurrencytype("STRAT");
        p1.setAmount(50.0);
        p1.setPricepercoin(2);
        p1.setValue(105);
        p1.setDate("08.08.20");
        long l = dbPurchase.writePurchase(p1);
        long id = l;


        Purchase p2 = new Purchase();
        p2.setCurrencytype("ETH");
        p2.setAmount(1.0);
        p2.setPricepercoin(200);
        p2.setValue(205);
        p2.setDate("06.08.20");
        id = dbPurchase.writePurchase(p2);


        Purchase p3 = new Purchase();
        p3.setCurrencytype("BTC");
        p3.setAmount(0.10);
        p3.setPricepercoin(2000);
        p3.setValue(205);
        p3.setDate("04.08.20");
        id = dbPurchase.writePurchase(p3);

        Purchase p4 = new Purchase();
        p4.setCurrencytype("IOT");
        p4.setAmount(20);
        p4.setPricepercoin(0.50);
        p4.setValue(205);
        p4.setDate("09.08.20");
        id = dbPurchase.writePurchase(p4);
    }

}
