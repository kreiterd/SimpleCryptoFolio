package danielkreiter.simplecryptofolio.UI;

import android.app.Activity;
import android.os.AsyncTask;

import danielkreiter.simplecryptofolio.CryptocurrencyData.CryptocurrencyDataProvider;

public class LoadCurrencyPriceToActivityATask extends AsyncTask<String, String, String> {

    private ISendDataToActivity iSendDataToActivity;
    private String sourceCurrencyTag;

    public LoadCurrencyPriceToActivityATask(String sourceCurrencyTag, Activity activity) {
        this.iSendDataToActivity = (ISendDataToActivity) activity;
        this.sourceCurrencyTag = sourceCurrencyTag;
    }

    @Override
    protected void onPreExecute() {
        iSendDataToActivity.preExecuteUpdateView();
    }

    @Override
    protected String doInBackground(String... args) {
        CryptocurrencyDataProvider cryptocurrencyDataProvider = new CryptocurrencyDataProvider();
        return Double.toString(cryptocurrencyDataProvider
                .getCurrencyPrice(this.sourceCurrencyTag, "EUR"));
    }

    @Override
    protected void onPostExecute(String result) {
        iSendDataToActivity.postExecuteUpdateView(result);
    }
}
