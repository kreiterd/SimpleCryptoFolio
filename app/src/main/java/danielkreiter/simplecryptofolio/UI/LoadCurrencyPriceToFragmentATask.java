package danielkreiter.simplecryptofolio.UI;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import danielkreiter.simplecryptofolio.CryptocurrencyData.CryptocurrencyDataProvider;

/**
 * Async-Task to load the data from an API to a activity
 */
public class LoadCurrencyPriceToFragmentATask extends AsyncTask<String, String, JSONObject> {

    private ISendDataToUI mISendDataToUI;
    private String mSsourceCurrencyTag;

    /**
     * Instantiates a Async-Task
     *
     * @param sourceCurrencyTag the source currency tag
     * @param fragment          the fragment
     */
    public LoadCurrencyPriceToFragmentATask(String sourceCurrencyTag, Fragment fragment) {
        this.mISendDataToUI = (ISendDataToUI) fragment;
        this.mSsourceCurrencyTag = sourceCurrencyTag;
    }

    /**
     * Triggers the preExecute method
     */
    @Override
    protected void onPreExecute() {
        mISendDataToUI.preExecuteUpdateView();
    }

    /**
     * Loading the data
     */
    @Override
    protected JSONObject doInBackground(String... args) {
        CryptocurrencyDataProvider cryptocurrencyDataProvider = new CryptocurrencyDataProvider();
        JSONObject result = new JSONObject();
        try {
            result.put(this.mSsourceCurrencyTag, Double.toString(cryptocurrencyDataProvider
                    .getCurrencyPrice(this.mSsourceCurrencyTag, "EUR")));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Triggers the postExecute method
     *
     * @param result
     */
    @Override
    protected void onPostExecute(JSONObject result) {
        mISendDataToUI.postExecuteUpdateView(result);
    }
}
