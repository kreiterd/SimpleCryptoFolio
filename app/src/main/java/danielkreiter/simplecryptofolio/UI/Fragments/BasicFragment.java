package danielkreiter.simplecryptofolio.UI.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import danielkreiter.simplecryptofolio.R;

public class BasicFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    protected boolean viewReady = false;
    private int page;

    public static BasicFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        BasicFragment fragment = new BasicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (isVisibleToUser) {

            /*
             *  Hide the keyboard when the fragment is called.
             *  If the tab is changed while the focus is on an edit text, the keyboard remains open in the new tab.
             */
            hideKeyboard();

        }
    }

    void hideKeyboard() {
        if (getActivity() != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            View focused = getActivity().getCurrentFocus();
            if (focused != null) {
                inputManager.hideSoftInputFromWindow(focused.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARG_PAGE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_value, container, false);
        return view;
    }
}