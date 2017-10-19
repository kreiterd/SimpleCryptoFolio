package danielkreiter.simplecryptofolio.Model;


import danielkreiter.simplecryptofolio.UI.Elements.CheckableListItem;

public class Purchase extends CheckableListItem {


    private long id;
    private String currencytag;
    private String date;
    private double value;
    private double amount;
    private double coinPrice;

    public Purchase() {

    }

    public Purchase(String currencytype, String date, Double value, Double amount, Double pricepercoin) {
        this.currencytag = currencytype;
        this.date = date;
        this.value = value;
        this.amount = amount;
        this.coinPrice = pricepercoin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getCurrencytype() {
        return currencytag;
    }

    public void setCurrencytype(String currencytype) {
        this.currencytag = currencytype;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPricepercoin() {
        return coinPrice;
    }

    public void setPricepercoin(double pricepercoin) {
        this.coinPrice = pricepercoin;
    }


    public String toString() {
        return "Id: " + this.getId() + "currencytype: " + this.getCurrencytype() + "; date: " + this.getDate() + "; value: " + this.getValue() + "; amount: " + this.getAmount() + "; pricepercoin: " + this.getPricepercoin();
    }
}
