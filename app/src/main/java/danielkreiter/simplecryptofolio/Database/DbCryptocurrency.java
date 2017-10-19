package danielkreiter.simplecryptofolio.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import danielkreiter.simplecryptofolio.Model.Cryptocurrency;

public class DbCryptocurrency {

    private static final String TAG = DbPurchase.class.getName();
    private Context context;
    private SimpleCryptoFolioDbHelper simpleCryptofolioDbHelper;


    public DbCryptocurrency(Context context) {
        this.context = context;
        this.simpleCryptofolioDbHelper = SimpleCryptoFolioDbHelper.getInstance(context);
    }

    private static List<Cryptocurrency> extractCryptocurrencys(Cursor cursor) {
        List<Cryptocurrency> cryptocurrencys = new ArrayList<>();
        while (cursor.moveToNext()) {
            Cryptocurrency cryptocurrency = new Cryptocurrency();
            cryptocurrency.setId(cursor.getLong((cursor.getColumnIndexOrThrow(SimpleCryptoFolioContract.Purchase._ID))));
            cryptocurrency.setName(cursor.getString((cursor.getColumnIndexOrThrow(SimpleCryptoFolioContract.Cryptocurrency.COLUMN_NAME_NAME))));
            cryptocurrencys.add(cryptocurrency);
        }
        return cryptocurrencys;
    }

    public void deleteCryptocurrency(long id) {
        String tableName = SimpleCryptoFolioContract.Cryptocurrency.TABLE_NAME;
        String whereClause = SimpleCryptoFolioContract.Cryptocurrency._ID + " = ?";
        String[] whereArgs = {String.valueOf(id)};
        simpleCryptofolioDbHelper.deleteFromDatabase(tableName,
                whereClause,
                whereArgs);
    }

    public long writeCryptocurrency(Cryptocurrency cryptocurrency) {
        ContentValues values = new ContentValues();
        values.put(SimpleCryptoFolioContract.Cryptocurrency.COLUMN_NAME_NAME, cryptocurrency.getName());
        return simpleCryptofolioDbHelper.writeToDatabase(SimpleCryptoFolioContract.Cryptocurrency.TABLE_NAME, values);
    }

    public List<Cryptocurrency> readCryptocurrencyies() {

        String tableName = SimpleCryptoFolioContract.Cryptocurrency.TABLE_NAME;
        String[] tableColums = SimpleCryptoFolioContract.Cryptocurrency.TABLE_COLUMN_NAMES;

        Cursor cursor = simpleCryptofolioDbHelper.readFromDatabase(
                tableName,
                tableColums,
                null,
                null,
                null,
                null,
                null);

        List<Cryptocurrency> cryptocurrencies = extractCryptocurrencys(cursor);
        cursor.close();
        return cryptocurrencies;
    }

    public Cryptocurrency readCryptocurrency(long id) {
        String tableName = SimpleCryptoFolioContract.Cryptocurrency.TABLE_NAME;
        String[] tableColums = SimpleCryptoFolioContract.Cryptocurrency.TABLE_COLUMN_NAMES;
        String whereClause = SimpleCryptoFolioContract.Cryptocurrency._ID + " = ?";
        String[] whereArgs = {Long.toString(id)};

        Cursor cursor = simpleCryptofolioDbHelper.readFromDatabase(tableName,
                tableColums,
                whereClause,
                whereArgs,
                null,
                null,
                null);


        List<Cryptocurrency> cryptocurrencies = extractCryptocurrencys(cursor);
        cursor.close();
        return cryptocurrencies.get(0);
    }

    public Cryptocurrency readCryptocurrency(String name) {
        String tableName = SimpleCryptoFolioContract.Cryptocurrency.TABLE_NAME;
        String[] tableColums = SimpleCryptoFolioContract.Cryptocurrency.TABLE_COLUMN_NAMES;
        String whereClause = SimpleCryptoFolioContract.Cryptocurrency.COLUMN_NAME_NAME + " = ?";
        String[] whereArgs = {name};

        Cursor cursor = simpleCryptofolioDbHelper.readFromDatabase(tableName,
                tableColums,
                whereClause,
                whereArgs,
                null,
                null,
                null);


        List<Cryptocurrency> cryptocurrencies = extractCryptocurrencys(cursor);
        cursor.close();
        if (cryptocurrencies.size() == 1) return cryptocurrencies.get(0);
        else return null;

    }

}
