package danielkreiter.simplecryptofolio.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SimpleCryptoFolioDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SimpleCryptoFolio.db";


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + "Purchase" + " (" +
                    SimpleCryptoFolioContract.Purchase._ID + " INTEGER PRIMARY KEY," +
                    SimpleCryptoFolioContract.Purchase.COLUMN_NAME_VALUE + " REAL, " +
                    SimpleCryptoFolioContract.Purchase.COLUMN_NAME_AMOUNT + " REAL, " +
                    SimpleCryptoFolioContract.Purchase.COLUMN_NAME_PRICEPERCOIN + " REAL, " +
                    SimpleCryptoFolioContract.Purchase.COLUMN_NAME_CURRENCYTYPE + " TEXT," +
                    SimpleCryptoFolioContract.Purchase.COLUM_NAME_DATE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SimpleCryptoFolioContract.Purchase.TABLE_NAME;


    public SimpleCryptoFolioDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}
