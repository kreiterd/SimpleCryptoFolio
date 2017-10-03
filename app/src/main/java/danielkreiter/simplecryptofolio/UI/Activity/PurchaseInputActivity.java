package danielkreiter.simplecryptofolio.UI.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import danielkreiter.simplecryptofolio.CryptocurrencyData.CryptoCompareApiWrapper;
import danielkreiter.simplecryptofolio.Database.DbCryptocurrency;
import danielkreiter.simplecryptofolio.Database.DbPurchase;
import danielkreiter.simplecryptofolio.Model.Cryptocurrency;
import danielkreiter.simplecryptofolio.Model.Purchase;
import danielkreiter.simplecryptofolio.R;
import danielkreiter.simplecryptofolio.UI.EditTextDatePicker;
import danielkreiter.simplecryptofolio.UI.LoadCurrencyPriceToActivityATask;

public class PurchaseInputActivity extends AppCompatActivity implements ISendDataToActivity {

    ArrayAdapter<Cryptocurrency> adapter;
    private ProgressBar mProgressBar;
    private TextView mCoindata;
    private AutoCompleteTextView mCurrencytype;
    private EditText mDate;
    private EditText mValue;
    private EditText mAmount;
    private EditText mPricepercoin;
    private DbPurchase mDbPurchase;
    private DbCryptocurrency mDbCryptocurrency;
    private List<Cryptocurrency> mCryptocurrencies;
    private String mCurrencyname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_input);
        mDbPurchase = new DbPurchase(this.getApplicationContext());
        mDbCryptocurrency = new DbCryptocurrency(this.getApplicationContext());
        this.mDate = findViewById(R.id.date_editText);
        this.mProgressBar = findViewById(R.id.loadCurrencyData_ProgressBar);
        this.mCurrencytype = findViewById(R.id.currencytype_editText);
        this.mCoindata = findViewById(R.id.coindata_textview);

        mCryptocurrencies = mDbCryptocurrency.readCryptocurrencyies();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mCryptocurrencies);
        mCurrencytype.setAdapter(adapter);

        mProgressBar.setVisibility(View.INVISIBLE);
        mCoindata.setText("");

        // attach listener to date field
        new EditTextDatePicker(PurchaseInputActivity.this, mDate);


    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadCurrencyData_button:
                mCurrencyname = this.mCurrencytype.getText().toString();
                if (mDbCryptocurrency.readCryptocurrency(mCurrencyname) == null) {
                    Cryptocurrency cryptocurrency = new Cryptocurrency(mCurrencyname);
                    mDbCryptocurrency.writeCryptocurrency(cryptocurrency);
                    mCryptocurrencies.add(cryptocurrency);
                    //adapter.notifyDataSetChanged();
                    adapter.add(cryptocurrency);
                    mCurrencytype.setAdapter(adapter);
                }

                LoadCurrencyPriceToActivityATask loadCurrencyPriceToActivityATask
                        = new LoadCurrencyPriceToActivityATask(mCurrencyname, this);
                loadCurrencyPriceToActivityATask.execute();
                break;
            case R.id.safe_button:
                safeData();
                break;
        }
    }


    public void safeData() {
        this.mCurrencytype = findViewById(R.id.currencytype_editText);
        this.mDate = findViewById(R.id.date_editText);
        this.mValue = findViewById(R.id.value_editText);
        this.mAmount = findViewById(R.id.amount_editText);
        this.mPricepercoin = findViewById(R.id.pricepercoin_editText);
        Purchase purchase = new Purchase();
        purchase.setAmount(Double.parseDouble(mAmount.getText().toString()));
        purchase.setCurrencytype(mCurrencytype.getText().toString());
        purchase.setDate(mDate.getText().toString());
        purchase.setPricepercoin(Double.parseDouble(mPricepercoin.getText().toString()));
        purchase.setValue(Double.parseDouble(mValue.getText().toString()));
        mDbPurchase.writePurchase(purchase);
    }

    @Override
    public void postExecuteUpdateView(JSONObject result) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (result.has(this.mCurrencyname)) {
            if (!result.has(this.mCurrencyname)) {
                mCoindata.setText(CryptoCompareApiWrapper.NO_DATA_AVAILABLE);
            } else {
                try {

                    mCoindata.setText(mCurrencyname + ": " + String.valueOf(result.getDouble(mCurrencyname)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void preExecuteUpdateView() {
        this.mCoindata = findViewById(R.id.coindata_textview);
        mProgressBar.setVisibility(View.VISIBLE);
    }

}
