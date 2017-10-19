package danielkreiter.simplecryptofolio.UI.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import danielkreiter.simplecryptofolio.Database.DbPurchase;
import danielkreiter.simplecryptofolio.Model.Purchase;
import danielkreiter.simplecryptofolio.R;
import danielkreiter.simplecryptofolio.UI.Adapter.PurchaseOverviewAdapter;

public class AllPurchasesFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "AddPurchaseFragment";

    ListView overviewListView;
    Button deleteButton;

    List<Purchase> purchases;
    PurchaseOverviewAdapter purchaseOverviewAdapter;

    private DbPurchase dbPurchase;

    private int page;

    public static AllPurchasesFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        AllPurchasesFragment fragment = new AllPurchasesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARG_PAGE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_all_purchases,
                container, false);

        // set views
        this.overviewListView = view.findViewById(R.id.overview_listview);
        this.overviewListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        this.deleteButton = view.findViewById(R.id.delete_button);


        // logic
        dbPurchase = new DbPurchase(getActivity());
        purchases = dbPurchase.readPurchases();

        // purchaseOverviewAdapter
        purchaseOverviewAdapter = new PurchaseOverviewAdapter(this.getActivity(), purchases);
        overviewListView.setAdapter(purchaseOverviewAdapter);
        Button deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePurchases();
            }
        });
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            reloadAdapterWithNewPurchases();
        }
    }


    private void reloadAdapterWithNewPurchases() {
        if (dbPurchase != null && purchaseOverviewAdapter != null) {
            purchases = dbPurchase.readPurchases();
            purchaseOverviewAdapter.clear();
            for (Purchase p : purchases) {
                purchaseOverviewAdapter.add(p);
            }
            purchaseOverviewAdapter.notifyDataSetChanged();
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void deletePurchases() {
        for (Purchase p : purchaseOverviewAdapter.getAllChecked()) {
            dbPurchase.deletePurchase(p.getId());
            purchases.remove(p);
        }
        reloadAdapterWithNewPurchases();
    }

}