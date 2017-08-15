package danielkreiter.simplecryptofolio.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import danielkreiter.simplecryptofolio.Model.Purchase;

public class DbPurchase {
    private static final String TAG = DbPurchase.class.getName();

    private static final String[] projection = {
            SimpleCryptoFolioContract.Purchase._ID,
            SimpleCryptoFolioContract.Purchase.COLUMN_NAME_CURRENCYTYPE,
            SimpleCryptoFolioContract.Purchase.COLUM_NAME_DATE,
            SimpleCryptoFolioContract.Purchase.COLUMN_NAME_VALUE,
            SimpleCryptoFolioContract.Purchase.COLUMN_NAME_AMOUNT,
            SimpleCryptoFolioContract.Purchase.COLUMN_NAME_PRICEPERCOIN
    };

    public static long writePurchase(Purchase purchase, Context context) {
        SimpleCryptoFolioDbHelper dbHelper = new SimpleCryptoFolioDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_CURRENCYTYPE, purchase.getCurrencytype());
        values.put(SimpleCryptoFolioContract.Purchase.COLUM_NAME_DATE, purchase.getDate());
        values.put(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_VALUE, purchase.getValue());
        values.put(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_AMOUNT, purchase.getAmount());
        values.put(SimpleCryptoFolioContract.Purchase.COLUMN_NAME_PRICEPERCOIN, purchase.getPricepercoin());
        long id = db.insert(SimpleCryptoFolioContract.Purchase.TABLE_NAME, null, values);
        return id;

    }

    public static List<Purchase> readPurchases(Context context) {
        SimpleCryptoFolioDbHelper dbHelper = new SimpleCryptoFolioDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                SimpleCryptoFolioContract.Purchase.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        List<Purchase> purchases = extractPurchases(cursor);
        cursor.close();

        return purchases;
    }

    public static Purchase readPurchase(long id, Context context) {
        SimpleCryptoFolioDbHelper dbHelper = new SimpleCryptoFolioDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String selection = SimpleCryptoFolioContract.Purchase._ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};


        Cursor cursor = db.query(
                SimpleCryptoFolioContract.Purchase.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        List<Purchase> purchases = extractPurchases(cursor);
        cursor.close();
        return purchases.get(0);
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
}
