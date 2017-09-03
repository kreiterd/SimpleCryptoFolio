package danielkreiter.simplecryptofolio.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import danielkreiter.simplecryptofolio.Model.Purchase;

public class DbPurchase {
    private static final String TAG = DbPurchase.class.getName();
    private Context mContext;
    private SimpleCryptoFolioDbHelper simpleCryptoFolioDbHelper;


    public DbPurchase(Context context) {
        this.mContext = context;
        this.simpleCryptoFolioDbHelper = SimpleCryptoFolioDbHelper.getInstance(context);
    }

    private static List<Purchase> extractPurchases(Cursor cursor) {
        List<Purchase> purchases = new ArrayList<>();
        while (cursor.moveToNext()) {
            Purchase purchase = new Purchase();
            purchase.setId(cursor.getLong((cursor.getColumnIndexOrThrow(SimpleCryptoFolioContract.Purchase._ID))));
            purchase.setCurrencytype(cursor.getString((cursor.getColumnIndexOrThrow(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_CURRENCYTYPE))));
            purchase.setDate(cursor.getString((cursor.getColumnIndexOrThrow(SimpleCryptoFolioContract.Purchase.COLUM_NAME_DATE))));
            purchase.setValue(cursor.getDouble((cursor.getColumnIndexOrThrow(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_VALUE))));
            purchase.setAmount(cursor.getDouble((cursor.getColumnIndexOrThrow(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_AMOUNT))));
            purchase.setPricepercoin(cursor.getDouble((cursor.getColumnIndexOrThrow(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_PRICEPERCOIN))));
            purchases.add(purchase);
        }
        return purchases;
    }

    public void deletePurchase(long id) {
        String tableName = SimpleCryptoFolioContract.Purchase.TABLE_NAME;
        String whereClause = SimpleCryptoFolioContract.Purchase._ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        simpleCryptoFolioDbHelper.deleteFromDatabase(tableName,
                whereClause,
                whereArgs);
    }

    public long writePurchase(Purchase purchase) {
        ContentValues values = new ContentValues();
        values.put(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_CURRENCYTYPE, purchase.getCurrencytype());
        values.put(SimpleCryptoFolioContract.Purchase.COLUM_NAME_DATE, purchase.getDate());
        values.put(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_VALUE, purchase.getValue());
        values.put(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_AMOUNT, purchase.getAmount());
        values.put(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_PRICEPERCOIN, purchase.getPricepercoin());
        return simpleCryptoFolioDbHelper.writeToDatabase(SimpleCryptoFolioContract.Purchase.TABLE_NAME, values);
    }

    public List<Purchase> readPurchases() {

        String tableName = SimpleCryptoFolioContract.Purchase.TABLE_NAME;
        String[] tableColums = SimpleCryptoFolioContract.Purchase.TABLE_COLUMN_NAMES;

        Cursor cursor = simpleCryptoFolioDbHelper.readFromDatabase(
                tableName,
                tableColums,
                null,
                null,
                null,
                null,
                null);

        List<Purchase> purchases = extractPurchases(cursor);
        cursor.close();
        return purchases;
    }

    public Purchase readPurchase(long id) {
        String tableName = SimpleCryptoFolioContract.Purchase.TABLE_NAME;
        String[] tableColums = SimpleCryptoFolioContract.Purchase.TABLE_COLUMN_NAMES;
        String whereClause = SimpleCryptoFolioContract.Purchase._ID + " = ?";
        String[] whereArgs = {Long.toString(id)};

        Cursor cursor = simpleCryptoFolioDbHelper.readFromDatabase(tableName,
                tableColums,
                whereClause,
                whereArgs,
                null,
                null,
                null);


        List<Purchase> purchases = extractPurchases(cursor);
        cursor.close();
        return purchases.get(0);
    }


}
