package danielkreiter.simplecryptofolio.CryptocurrencyData;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CryptoCompareApiWrapper implements IApiWrapper {


    public static final int NO_DATA_AVAILABLE = -1;
    public static final int API_READ_TIMEOUT = 2000;
    public static final int API_OPENCONNECTION_TIMEOUT = 2000;
    private static final String BASE_URL = "https://min-api.cryptocompare.com";
    private HttpURLConnection urlConnection;

    public CryptoCompareApiWrapper() {
    }

    public double requestCurrencyPrice(String sourceCurrencyTag, String destinationCurrencyTag) throws IOException {
        StringBuilder result = new StringBuilder();
        if (sourceCurrencyTag.equals("") || destinationCurrencyTag.equals("")) return 0;
        try {
            String request = BASE_URL + "/data/price?fsym=" + sourceCurrencyTag + "&tsyms=" + destinationCurrencyTag;
            URL url = new URL(request);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(API_READ_TIMEOUT);
            urlConnection.setConnectTimeout(API_OPENCONNECTION_TIMEOUT);
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } finally {
            urlConnection.disconnect();
        }
        // ToDo: Errorhandling when no matching currency is found and the api returns an error
        // ToDo: implement proper excepton handling
        if (result.equals("")) {
            return NO_DATA_AVAILABLE;
        }
        JsonObject jsonObject = new JsonParser().parse(result.toString()).getAsJsonObject();
        if (!jsonObject.has(destinationCurrencyTag)) {
            return NO_DATA_AVAILABLE;
        } else {
            return jsonObject.get(destinationCurrencyTag).getAsDouble();
        }
    }
}
