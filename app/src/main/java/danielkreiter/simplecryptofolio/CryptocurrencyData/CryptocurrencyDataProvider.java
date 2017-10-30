package danielkreiter.simplecryptofolio.CryptocurrencyData;

import android.util.Log;

import java.io.IOException;

public class CryptocurrencyDataProvider {

    private static final String TAG = "CryptocurrencyDataPr...";
    private IApiWrapper apiWrapper;

    public CryptocurrencyDataProvider() {
        this.apiWrapper = new CryptoCompareApiWrapper();
    }

    CryptocurrencyDataProvider(IApiWrapper apiWrapper) {
        this.apiWrapper = apiWrapper;
    }

    public double getCurrencyPrice(String sourceCurrencyTag, String destinationCurrencyTag) throws IOException {
        Log.i(TAG, "API-READ: Source: " + sourceCurrencyTag + " Destination: " + destinationCurrencyTag);
        return apiWrapper.requestCurrencyPrice(sourceCurrencyTag, destinationCurrencyTag);

    }

}
