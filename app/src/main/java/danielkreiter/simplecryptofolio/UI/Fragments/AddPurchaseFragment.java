package danielkreiter.simplecryptofolio.UI.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import danielkreiter.simplecryptofolio.UI.ISendDataToUI;
import danielkreiter.simplecryptofolio.UI.LoadCurrencyPriceToFragmentATask;


/**
 * The type AddPurchaseFragment
 */
public class AddPurchaseFragment extends Fragment implements ISendDataToUI {

    public static final String ARG_PAGE = "ARG_PAGE";

    public static final String TAG = "AddPurchaseFragment";


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


    /**
     * @param page the page
     * @return the add purchase fragment
     */
    public static AddPurchaseFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        AddPurchaseFragment fragment = new AddPurchaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate called.");
        getArguments().getInt(ARG_PAGE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_purchase, container, false);
        mDbPurchase = new DbPurchase(view.getContext());
        mDbCryptocurrency = new DbCryptocurrency(view.getContext());
        this.mDate = view.findViewById(R.id.date_editText);
        this.mProgressBar = view.findViewById(R.id.loadCurrencyData_ProgressBar);
        this.mCurrencytype = view.findViewById(R.id.currencytype_editText);
        this.mCoindata = view.findViewById(R.id.coindata_textview);
        this.mCurrencytype = view.findViewById(R.id.currencytype_editText);
        this.mDate = view.findViewById(R.id.date_editText);
        this.mValue = view.findViewById(R.id.value_editText);
        this.mAmount = view.findViewById(R.id.amount_editText);
        this.mPricepercoin = view.findViewById(R.id.pricepercoin_editText);
        this.mCoindata = view.findViewById(R.id.coindata_textview);


        Button loadCurrencyButton = view.findViewById(R.id.loadCurrencyData_button);
        loadCurrencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrencyname = mCurrencytype.getText().toString();
                if (mDbCryptocurrency.readCryptocurrency(mCurrencyname) == null) {
                    Cryptocurrency cryptocurrency = new Cryptocurrency(mCurrencyname);
                    mDbCryptocurrency.writeCryptocurrency(cryptocurrency);
                    mCryptocurrencies.add(cryptocurrency);
                    adapter.add(cryptocurrency);
                    mCurrencytype.setAdapter(adapter);
                }
                LoadCurrencyPriceToFragmentATask loadCurrencyPriceToFragmentATask
                        = new LoadCurrencyPriceToFragmentATask(mCurrencyname, AddPurchaseFragment.this
                );
                loadCurrencyPriceToFragmentATask.execute();
            }
        });


        Button safeButton = view.findViewById(R.id.safe_button);
        safeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                safeData();
            }
        });


        mCryptocurrencies = mDbCryptocurrency.readCryptocurrencyies();
        adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, mCryptocurrencies);
        mCurrencytype.setAdapter(adapter);

        mProgressBar.setVisibility(View.INVISIBLE);
        mCoindata.setText("");

        // attach listener to date field
        new EditTextDatePicker(view.getContext(), mDate);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public void safeData() {
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
        mProgressBar.setVisibility(View.VISIBLE);
    }


}