package danielkreiter.simplecryptofolio.UI.Tasks;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import danielkreiter.simplecryptofolio.CryptocurrencyData.CryptocurrencyDataProvider;
import danielkreiter.simplecryptofolio.UI.Interfaces.ISendDataToUI;

/**
 * Async-Task to load the data from an API to a activity
 */
public class LoadCurrencyPriceToFragmentATask extends AsyncTask<String, String, AsyncTaskResult<JSONObject>> {

    private ISendDataToUI iSendDataToUi;
    private String sourceCurrencyTag;

    /**
     * Instantiates a Async-Task
     *
     * @param sourceCurrencyTag the source currency tag
     * @param fragment          the fragment
     */
    public LoadCurrencyPriceToFragmentATask(String sourceCurrencyTag, Fragment fragment) {
        this.iSendDataToUi = (ISendDataToUI) fragment;
        this.sourceCurrencyTag = sourceCurrencyTag;
    }

    /**
     * Triggers the preExecute method
     */
    @Override
    protected void onPreExecute() {
        iSendDataToUi.preExecuteUpdateView();
    }

    /**
     * Loading the data
     */
    @Override
    protected AsyncTaskResult<JSONObject> doInBackground(String... args) {
        CryptocurrencyDataProvider cryptocurrencyDataProvider = new CryptocurrencyDataProvider();
        JSONObject result = new JSONObject();

        try {
            result.put(this.sourceCurrencyTag, Double.toString(cryptocurrencyDataProvider
                    .getCurrencyPrice(this.sourceCurrencyTag, "EUR")));
            return new AsyncTaskResult<>(result);
        } catch (JSONException e) {
            return new AsyncTaskResult<>(e);
        } catch (IOException e) {
            return new AsyncTaskResult<>(e);
        }
    }

    /**
     * Triggers the postExecute method
     *
     * @param result
     */
    @Override
    protected void onPostExecute(AsyncTaskResult<JSONObject> result) {
        iSendDataToUi.postExecuteUpdateView(result);
    }
}
