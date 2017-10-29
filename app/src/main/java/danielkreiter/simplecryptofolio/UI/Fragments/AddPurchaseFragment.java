package danielkreiter.simplecryptofolio.UI.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import danielkreiter.simplecryptofolio.UI.Elements.EditTextDatePicker;
import danielkreiter.simplecryptofolio.UI.Interfaces.ISendDataToUI;
import danielkreiter.simplecryptofolio.UI.Tasks.LoadCurrencyPriceToFragmentATask;


/**
 * The type AddPurchaseFragment
 */
public class AddPurchaseFragment extends Fragment implements ISendDataToUI {

    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "AddPurchaseFragment";
    ArrayAdapter<Cryptocurrency> adapter;
    private AutoCompleteTextView currencyTagAutoCompleteTextView;
    private EditText date_editText;
    private EditText valueEditText;
    private EditText amountEditText;
    private EditText coinPriceEditText;
    private ProgressBar loadCurrencyPriceProgressBar;
    private TextView coindataTextView;
    private ImageButton loadDataImageButton;
    private DbPurchase dbPurchase;
    private DbCryptocurrency dbCryptocurrency;
    private List<Cryptocurrency> cryptocurrency;
    private String currencyName;



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
        getArguments().getInt(ARG_PAGE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_purchase,
                container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        dbPurchase = new DbPurchase(view.getContext());
        dbCryptocurrency = new DbCryptocurrency(view.getContext());

        this.date_editText = view.findViewById(R.id.date_editText);
        this.loadCurrencyPriceProgressBar = view.findViewById(R.id.loadCurrencyData_ProgressBar);
        this.currencyTagAutoCompleteTextView = view.findViewById(R.id.currencytype_editText);
        this.coindataTextView = view.findViewById(R.id.coindata_textview);
        this.currencyTagAutoCompleteTextView = view.findViewById(R.id.currencytype_editText);
        this.valueEditText = view.findViewById(R.id.value_editText);
        this.amountEditText = view.findViewById(R.id.amount_editText);
        this.coinPriceEditText = view.findViewById(R.id.pricepercoin_editText);
        this.coindataTextView = view.findViewById(R.id.coindata_textview);
        loadDataImageButton = view.findViewById(R.id.loadCurrencyData_button);


        loadDataImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currencyName = currencyTagAutoCompleteTextView.getText().toString();
                if (dbCryptocurrency.readCryptocurrency(currencyName) == null) {
                    Cryptocurrency cryptocurrency = new Cryptocurrency(currencyName);
                    dbCryptocurrency.writeCryptocurrency(cryptocurrency);
                    AddPurchaseFragment.this.cryptocurrency.add(cryptocurrency);
                    adapter.add(cryptocurrency);
                    currencyTagAutoCompleteTextView.setAdapter(adapter);
                }
                LoadCurrencyPriceToFragmentATask loadCurrencyPriceToFragmentATask
                        = new LoadCurrencyPriceToFragmentATask(currencyName, AddPurchaseFragment.this
                );
                loadCurrencyPriceToFragmentATask.execute();
            }
        });

        currencyTagAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    currencyName = currencyTagAutoCompleteTextView.getText().toString();
                    if (dbCryptocurrency.readCryptocurrency(currencyName) == null) {
                        Cryptocurrency cryptocurrency = new Cryptocurrency(currencyName);
                        dbCryptocurrency.writeCryptocurrency(cryptocurrency);
                        AddPurchaseFragment.this.cryptocurrency.add(cryptocurrency);
                        adapter.add(cryptocurrency);
                        currencyTagAutoCompleteTextView.setAdapter(adapter);
                    }
                    LoadCurrencyPriceToFragmentATask loadCurrencyPriceToFragmentATask
                            = new LoadCurrencyPriceToFragmentATask(currencyName, AddPurchaseFragment.this
                    );
                    loadCurrencyPriceToFragmentATask.execute();
                }
            }
        });


        Button safeButton = view.findViewById(R.id.safe_button);
        safeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                safeData();
            }
        });


        cryptocurrency = dbCryptocurrency.readCryptocurrencyies();
        adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, cryptocurrency);
        currencyTagAutoCompleteTextView.setAdapter(adapter);

        loadCurrencyPriceProgressBar.setVisibility(View.INVISIBLE);
        coindataTextView.setText("");

        // attach listener to date field
        new EditTextDatePicker(view.getContext(), date_editText);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public void safeData() {
        Purchase purchase = new Purchase();
        purchase.setAmount(Double.parseDouble(amountEditText.getText().toString()));
        purchase.setCurrencytype(currencyTagAutoCompleteTextView.getText().toString());
        purchase.setDate(date_editText.getText().toString());
        purchase.setPricepercoin(Double.parseDouble(coinPriceEditText.getText().toString()));
        purchase.setValue(Double.parseDouble(valueEditText.getText().toString()));
        dbPurchase.writePurchase(purchase);
    }

    @Override
    public void postExecuteUpdateView(JSONObject result) {
        loadCurrencyPriceProgressBar.setVisibility(View.INVISIBLE);
        loadDataImageButton.setVisibility(View.VISIBLE);
        if (result.has(this.currencyName)) {
            if (!result.has(this.currencyName)) {
                coindataTextView.setText(CryptoCompareApiWrapper.NO_DATA_AVAILABLE);
            } else {
                try {

                    coindataTextView.setText(currencyName + ": " + String.valueOf(result.getDouble(currencyName)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void preExecuteUpdateView() {
        loadDataImageButton.setVisibility(View.INVISIBLE);
        loadCurrencyPriceProgressBar.setVisibility(View.VISIBLE);
    }


}