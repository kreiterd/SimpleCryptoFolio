package danielkreiter.simplecryptofolio.CryptocurrencyData;

import java.io.IOException;

public interface IApiWrapper {

    double requestCurrencyPrice(String sourceCurrencyTage, String destinationCurrencyTag) throws IOException;

}
