package danielkreiter.simplecryptofolio.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import danielkreiter.simplecryptofolio.Model.Purchase;

/**
 * Database model class DbPurchase
 */
public class DbPurchase {
    private static final String TAG = DbPurchase.class.getName();
    private Context mContext;
    private SimpleCryptoFolioDbHelper mSimpleCryptoFolioDbHelper;


    /**
     * Instantiates a new DbPurchase object.
     *
     * @param context the context
     */
    public DbPurchase(Context context) {
        this.mContext = context;
        this.mSimpleCryptoFolioDbHelper = SimpleCryptoFolioDbHelper.getInstance(context);
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

    /**
     * Delete purchase.
     *
     * @param id the id
     */
    public void deletePurchase(long id) {
        String tableName = SimpleCryptoFolioContract.Purchase.TABLE_NAME;
        String whereClause = SimpleCryptoFolioContract.Purchase._ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        mSimpleCryptoFolioDbHelper.deleteFromDatabase(tableName,
                whereClause,
                whereArgs);
    }

    /**
     * Write a purchase to the database
     *
     * @param purchase the purchase
     * @return the long
     */
    public long writePurchase(Purchase purchase) {
        ContentValues values = new ContentValues();
        values.put(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_CURRENCYTYPE, purchase.getCurrencytype());
        values.put(SimpleCryptoFolioContract.Purchase.COLUM_NAME_DATE, purchase.getDate());
        values.put(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_VALUE, purchase.getValue());
        values.put(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_AMOUNT, purchase.getAmount());
        values.put(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_PRICEPERCOIN, purchase.getPricepercoin());
        return mSimpleCryptoFolioDbHelper.writeToDatabase(SimpleCryptoFolioContract.Purchase.TABLE_NAME, values);
    }

    /**
     * Read all purchases
     *
     * @return the list
     */
    public List<Purchase> readPurchases() {

        String tableName = SimpleCryptoFolioContract.Purchase.TABLE_NAME;
        String[] tableColums = SimpleCryptoFolioContract.Purchase.TABLE_COLUMN_NAMES;

        Cursor cursor = mSimpleCryptoFolioDbHelper.readFromDatabase(
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

    /**
     * Read a purchase.
     *
     * @param id the id
     * @return the purchase
     */
    public Purchase readPurchase(long id) {
        String tableName = SimpleCryptoFolioContract.Purchase.TABLE_NAME;
        String[] tableColums = SimpleCryptoFolioContract.Purchase.TABLE_COLUMN_NAMES;
        String whereClause = SimpleCryptoFolioContract.Purchase._ID + " = ?";
        String[] whereArgs = {Long.toString(id)};

        Cursor cursor = mSimpleCryptoFolioDbHelper.readFromDatabase(tableName,
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
