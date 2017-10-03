package danielkreiter.simplecryptofolio.UI;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import danielkreiter.simplecryptofolio.CryptocurrencyData.CryptocurrencyDataProvider;
import danielkreiter.simplecryptofolio.UI.Activity.ISendDataToActivity;

public class LoadCurrencyPriceToActivityATask extends AsyncTask<String, String, JSONObject> {

    private ISendDataToActivity mISendDataToActivity;
    private String mSsourceCurrencyTag;

    public LoadCurrencyPriceToActivityATask(String sourceCurrencyTag, Activity activity) {
        this.mISendDataToActivity = (ISendDataToActivity) activity;
        this.mSsourceCurrencyTag = sourceCurrencyTag;
    }

    @Override
    protected void onPreExecute() {
        mISendDataToActivity.preExecuteUpdateView();
    }

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

    @Override
    protected void onPostExecute(JSONObject result) {
        mISendDataToActivity.postExecuteUpdateView(result);
    }
}
