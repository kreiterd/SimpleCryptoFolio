package danielkreiter.simplecryptofolio.CryptocurrencyData;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CryptoCompareApiWrapper implements IApiWrapper {


    public static final int NO_DATA_AVAILABLE = -1;
    private final static String baseURL = "https://min-api.cryptocompare.com";
    private HttpURLConnection urlConnection;

    public CryptoCompareApiWrapper() {

    }

    public double requestCurrencyPrice(String sourceCurrencyTag, String destinationCurrencyTag) {
        StringBuilder result = new StringBuilder();
        if (sourceCurrencyTag.equals("") || destinationCurrencyTag.equals("")) return 0;
        try {
            String request = baseURL + "/data/price?fsym=" + sourceCurrencyTag + "&tsyms=" + destinationCurrencyTag;
            URL url = new URL(request);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
