package danielkreiter.simplecryptofolio.CryptocurrencyData;

public interface IApiWrapper {

    double requestCurrencyPrice(String sourceCurrencyTage, String destinationCurrencyTag);

}
