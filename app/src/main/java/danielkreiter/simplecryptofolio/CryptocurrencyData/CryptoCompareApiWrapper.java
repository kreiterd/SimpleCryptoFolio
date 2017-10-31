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
    private final static String baseUrl = "https://min-api.cryptocompare.com";
    private HttpURLConnection urlConnection;

    public CryptoCompareApiWrapper() {

    }

    public double requestCurrencyPrice(String sourceCurrencyTag, String destinationCurrencyTag) throws IOException {
        StringBuilder result = new StringBuilder();
        if (sourceCurrencyTag.equals("") || destinationCurrencyTag.equals("")) return 0;
        try {
            String request = baseUrl + "/data/price?fsym=" + sourceCurrencyTag + "&tsyms=" + destinationCurrencyTag;
            URL url = new URL(request);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(2000);
            urlConnection.setConnectTimeout(2000);
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
