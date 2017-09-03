package danielkreiter.simplecryptofolio.Model;


import danielkreiter.simplecryptofolio.UI.CheckableListItem;

public class Purchase extends CheckableListItem {


    private long id;
    private String currencytype;
    private String date;
    private double value;
    private double amount;
    private double pricepercoin;

    public Purchase() {

    }

    public Purchase(String currencytype, String date, Double value, Double amount, Double pricepercoin) {
        this.currencytype = currencytype;
        this.date = date;
        this.value = value;
        this.amount = amount;
        this.pricepercoin = pricepercoin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getCurrencytype() {
        return currencytype;
    }

    public void setCurrencytype(String currencytype) {
        this.currencytype = currencytype;
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
        return pricepercoin;
    }

    public void setPricepercoin(double pricepercoin) {
        this.pricepercoin = pricepercoin;
    }


    public String toString() {
        return "Id: " + this.getId() + "currencytype: " + this.getCurrencytype() + "; date: " + this.getDate() + "; value: " + this.getValue() + "; amount: " + this.getAmount() + "; pricepercoin: " + this.pricepercoin;
    }
}
