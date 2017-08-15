package danielkreiter.simplecryptofolio.CryptocurrencyData;

public class CryptocurrencyDataProvider {

    private IApiWrapper apiWrapper;

    public CryptocurrencyDataProvider() {
        this.apiWrapper = new CryptoCompareApiWrapper();
    }

    CryptocurrencyDataProvider(IApiWrapper apiWrapper) {
        this.apiWrapper = apiWrapper;
    }

    public double getCurrencyPrice(String sourceCurrencyTag, String destinationCurrencyTag) {
        return apiWrapper.requestCurrencyPrice(sourceCurrencyTag, destinationCurrencyTag);

    }

}
