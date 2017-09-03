package danielkreiter.simplecryptofolio.UI.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import danielkreiter.simplecryptofolio.Database.DbCryptocurrency;
import danielkreiter.simplecryptofolio.Database.DbPurchase;
import danielkreiter.simplecryptofolio.Model.Cryptocurrency;
import danielkreiter.simplecryptofolio.Model.Purchase;
import danielkreiter.simplecryptofolio.R;
import danielkreiter.simplecryptofolio.UI.EditTextDatePicker;
import danielkreiter.simplecryptofolio.UI.LoadCurrencyPriceToActivityATask;

public class PurchaseInputActivity extends AppCompatActivity implements ISendDataToActivity {

    ArrayAdapter<Cryptocurrency> adapter;
    private ProgressBar progressBar;
    private TextView coindata;
    private AutoCompleteTextView currencytype;
    private EditText date;
    private EditText value;
    private EditText amount;
    private EditText pricepercoin;
    private DbPurchase dbPurchase;
    private DbCryptocurrency dbCryptocurrency;
    private List<Cryptocurrency> mCryptocurrencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_input);
        dbPurchase = new DbPurchase(this.getApplicationContext());
        dbCryptocurrency = new DbCryptocurrency(this.getApplicationContext());
        this.date = findViewById(R.id.date_editText);
        this.progressBar = findViewById(R.id.loadCurrencyData_ProgressBar);
        this.currencytype = findViewById(R.id.currencytype_editText);
        this.coindata = findViewById(R.id.coindata_textview);

        mCryptocurrencies = dbCryptocurrency.readCryptocurrencyies();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mCryptocurrencies);
        currencytype.setAdapter(adapter);

        progressBar.setVisibility(View.INVISIBLE);
        coindata.setText("");

        // attach listener to date field
        new EditTextDatePicker(PurchaseInputActivity.this, date);


    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadCurrencyData_button:
                String currencyname = this.currencytype.getText().toString();
                if (dbCryptocurrency.readCryptocurrency(currencyname) == null) {
                    Cryptocurrency cryptocurrency = new Cryptocurrency(currencyname);
                    dbCryptocurrency.writeCryptocurrency(cryptocurrency);
                    mCryptocurrencies.add(cryptocurrency);
                    //adapter.notifyDataSetChanged();
                    adapter.add(cryptocurrency);
                    currencytype.setAdapter(adapter);
                }

                LoadCurrencyPriceToActivityATask loadCurrencyPriceToActivityATask
                        = new LoadCurrencyPriceToActivityATask(currencyname, this);
                loadCurrencyPriceToActivityATask.execute();
                break;
            case R.id.safe_button:
                safeData();
                break;
        }
    }


    public void safeData() {
        this.currencytype = findViewById(R.id.currencytype_editText);
        this.date = findViewById(R.id.date_editText);
        this.value = findViewById(R.id.value_editText);
        this.amount = findViewById(R.id.amount_editText);
        this.pricepercoin = findViewById(R.id.pricepercoin_editText);
        Purchase purchase = new Purchase();
        purchase.setAmount(Double.parseDouble(amount.getText().toString()));
        purchase.setCurrencytype(currencytype.getText().toString());
        purchase.setDate(date.getText().toString());
        purchase.setPricepercoin(Double.parseDouble(pricepercoin.getText().toString()));
        purchase.setValue(Double.parseDouble(value.getText().toString()));
        dbPurchase.writePurchase(purchase);
    }

    @Override
    public void postExecuteUpdateView(String str) {
        progressBar.setVisibility(View.INVISIBLE);
        coindata.setText(str);
    }

    @Override
    public void preExecuteUpdateView() {
        this.coindata = findViewById(R.id.coindata_textview);
        progressBar.setVisibility(View.VISIBLE);
    }

}
