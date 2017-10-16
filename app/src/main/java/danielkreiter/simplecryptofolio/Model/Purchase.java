package danielkreiter.simplecryptofolio.Model;


import danielkreiter.simplecryptofolio.UI.Elements.CheckableListItem;

public class Purchase extends CheckableListItem {


    private long mId;
    private String mCurrencytype;
    private String mDate;
    private double mValue;
    private double mAmount;
    private double mPricepercoin;

    public Purchase() {

    }

    public Purchase(String currencytype, String date, Double value, Double amount, Double pricepercoin) {
        this.mCurrencytype = currencytype;
        this.mDate = date;
        this.mValue = value;
        this.mAmount = amount;
        this.mPricepercoin = pricepercoin;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }


    public String getCurrencytype() {
        return mCurrencytype;
    }

    public void setCurrencytype(String currencytype) {
        this.mCurrencytype = currencytype;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public double getValue() {
        return mValue;
    }

    public void setValue(double value) {
        this.mValue = value;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double amount) {
        this.mAmount = amount;
    }

    public double getPricepercoin() {
        return mPricepercoin;
    }

    public void setPricepercoin(double pricepercoin) {
        this.mPricepercoin = pricepercoin;
    }


    public String toString() {
        return "Id: " + this.getId() + "currencytype: " + this.getCurrencytype() + "; date: " + this.getDate() + "; value: " + this.getValue() + "; amount: " + this.getAmount() + "; pricepercoin: " + this.getPricepercoin();
    }
}
