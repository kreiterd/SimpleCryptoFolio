package danielkreiter.simplecryptofolio.UI.Fragments;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.Random;

import danielkreiter.simplecryptofolio.Database.DbPurchase;
import danielkreiter.simplecryptofolio.Model.Purchase;
import danielkreiter.simplecryptofolio.R;
import danielkreiter.simplecryptofolio.UI.Adapter.PurchaseOverviewAdapter;
import danielkreiter.simplecryptofolio.UI.Adapter.RecyclerListAdapter;
import danielkreiter.simplecryptofolio.UI.Elements.PieChartValueFormatter;
import danielkreiter.simplecryptofolio.UI.Elements.PortfolioListItem;
import danielkreiter.simplecryptofolio.UI.Helper.ItemTouchHelperCallback;
import danielkreiter.simplecryptofolio.UI.Interfaces.ISendDataToUI;
import danielkreiter.simplecryptofolio.UI.Tasks.AsyncTaskResult;
import danielkreiter.simplecryptofolio.UI.Tasks.LoadCurrencyPriceToFragmentATask;

/**
 * A simple {@link Fragment} subclass.
 */
public class PortfolioFragment extends BasicFragment implements ISendDataToUI {

    RecyclerView recyclerView;
    ProgressBar loadValuesProgressBar;
    TextView loadValuesTextView;
    Map<String, Double> totalAmount;
    List<Integer> colors;
    List<PieEntry> entries;
    List<Purchase> purchases;
    PieDataSet valueDataSet;
    Random rnd;
    private ArrayList<LoadCurrencyPriceToFragmentATask> loadingTasks;
    private int page;
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "PortfolioFragment";


    public static PortfolioFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PortfolioFragment fragment = new PortfolioFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARG_PAGE);
        loadingTasks = new ArrayList<>();
        totalAmount = new HashMap<>();
        colors = new ArrayList<>();
        entries = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_portfolio, container, false);


        recyclerView = view.findViewById(R.id.portfolioRecyclerView);
        loadValuesProgressBar = view.findViewById(R.id.loading_progressbar);
        loadValuesTextView = view.findViewById(R.id.loading_textview);

        PortfolioListItem portfolioListItem = new PortfolioListItem();
        portfolioListItem.setContent("Test");
        List<PortfolioListItem> portfolioListItems = new ArrayList<>();
        portfolioListItems.add(portfolioListItem);
        portfolioListItems.add(portfolioListItem);
        portfolioListItems.add(portfolioListItem);
        portfolioListItems.add(portfolioListItem);
        portfolioListItems.add(portfolioListItem);
        portfolioListItems.add(portfolioListItem);

        final RecyclerListAdapter adapter = new RecyclerListAdapter(portfolioListItems);




        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);


        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);


        RecyclerListAdapter recyclerListAdapter = new RecyclerListAdapter(portfolioListItems);
        recyclerView.setAdapter(recyclerListAdapter);


        if (!viewReady) {
            loadCurrencyData();
        }

        this.viewReady = true;

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       //  createCurrentValueChart();

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && viewReady) {
            // reload the chart
            loadCurrencyData();
        }
    }

    @Override
    public void postExecuteUpdateView(AsyncTaskResult<JSONObject> result) {
        if (result.getError() != null) {
            //showLoadingError();
            Log.e(TAG, result.getError().getMessage().toString());
        } else {
            JSONObject realResult = result.getResult();
            Iterator<String> iter = realResult.keys();
            while (iter.hasNext()) {
               // do smth

            }
            // refreshChart();
        }
    }

    @Override
    public void preExecuteUpdateView() {

    }

    void loadCurrencyData() {
        purchases = (new DbPurchase(getActivity())).readPurchases();
        // add up the values of all purchases for each separate currency
        for (Purchase purchase : purchases) {
            String currencyName = purchase.getCurrencytype();
            Double amount = purchase.getAmount();
            if (totalAmount.containsKey(currencyName))
                totalAmount.put(currencyName, totalAmount.get(currencyName) + amount);
            else
                totalAmount.put(currencyName, amount);
        }

        // load the actual value of each currency and pass the results to postExecuteUpdateView(...)
        for (Map.Entry<String, Double> entry : totalAmount.entrySet()) {
            LoadCurrencyPriceToFragmentATask loadCurrencyPriceToFragmentATask
                    = new LoadCurrencyPriceToFragmentATask(entry.getKey(), this);
            loadCurrencyPriceToFragmentATask.execute();
            loadingTasks.add(loadCurrencyPriceToFragmentATask);
        }
    }








}
