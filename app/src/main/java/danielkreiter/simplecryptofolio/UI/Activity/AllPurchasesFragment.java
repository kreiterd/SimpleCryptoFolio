package danielkreiter.simplecryptofolio.UI.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import danielkreiter.simplecryptofolio.Database.DbPurchase;
import danielkreiter.simplecryptofolio.Model.Purchase;
import danielkreiter.simplecryptofolio.R;
import danielkreiter.simplecryptofolio.UI.PurchaseOverviewAdapter;

public class AllPurchasesFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String TAG = "AddPurchaseFragment";

    ListView mOverviewList;
    Button mGetChecked;

    List<Purchase> mPurchases;
    PurchaseOverviewAdapter mAdapter;

    private DbPurchase dbPurchase;

    private int mPage;

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
        Log.i(TAG, "onCreate called.");
        mPage = getArguments().getInt(ARG_PAGE);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_all_purchases, container, false);
        // set views
        this.mOverviewList = view.findViewById(R.id.overview_listview);
        this.mOverviewList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        this.mGetChecked = view.findViewById(R.id.delete_button);


        // logic
        dbPurchase = new DbPurchase(getActivity());
        mPurchases = dbPurchase.readPurchases();

        // mAdapter
        mAdapter = new PurchaseOverviewAdapter(this.getActivity(), mPurchases);
        mOverviewList.setAdapter(mAdapter);
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
        // if dbPurchase and mAdapter are null, onCreateView has not been called yet
        if (dbPurchase != null && mAdapter != null) {
            mPurchases = dbPurchase.readPurchases();
            mAdapter.clear();
            for (Purchase p : mPurchases) {
                mAdapter.add(p);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void deletePurchases() {
        for (Purchase p : mAdapter.getAllChecked()) {
            dbPurchase.deletePurchase(p.getId());
            mPurchases.remove(p);
        }
        mAdapter.notifyDataSetChanged();
    }

}