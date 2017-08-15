package danielkreiter.simplecryptofolio.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import danielkreiter.simplecryptofolio.Database.DbPurchase;
import danielkreiter.simplecryptofolio.Model.Purchase;
import danielkreiter.simplecryptofolio.R;

public class PurchaseInputActivity extends AppCompatActivity implements ISendDataToActivity {

    private ProgressBar progressBar;
    private TextView coindata;
    private EditText currencytype;
    private EditText date;
    private EditText value;
    private EditText amount;
    private EditText pricepercoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_input);


        this.date = findViewById(R.id.date_editText);
        this.progressBar = findViewById(R.id.loadCurrencyData_ProgressBar);
        this.currencytype = findViewById(R.id.currencytype_editText);
        this.coindata = findViewById(R.id.coindata_textview);


        progressBar.setVisibility(View.INVISIBLE);
        coindata.setText("");

        // attach listener to date field
        new EditTextDatePicker(PurchaseInputActivity.this, date);


    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadCurrencyData_button:
                String currencytypeStr = this.currencytype.getText().toString();
                LoadCurrencyPriceToActivityATask loadCurrencyPriceToActivityATask
                        = new LoadCurrencyPriceToActivityATask(currencytypeStr, this);
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
        DbPurchase.writePurchase(purchase, this.getApplicationContext());
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
