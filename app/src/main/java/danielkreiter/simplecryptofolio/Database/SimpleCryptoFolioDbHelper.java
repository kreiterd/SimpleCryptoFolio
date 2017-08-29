package danielkreiter.simplecryptofolio.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SimpleCryptoFolioDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SimpleCryptoFolio.db";
    private static SimpleCryptoFolioDbHelper instance = null;

    private Context mContext;

    private SimpleCryptoFolioDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    public static synchronized SimpleCryptoFolioDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SimpleCryptoFolioDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SimpleCryptoFolioContract.Purchase.SQL_CREATE_QUERY);
    }

    // ToDo: implement
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // ToDo: implement
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long writeToDatabase(String tablename, ContentValues values) {
        return this.getWritableDatabase().insert(
                tablename,
                null,
                values);
    }

    public Cursor readFromDatabase(String tableName, String[] tableColumns, String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {
        return this.getReadableDatabase().query(
                tableName,
                tableColumns,
                whereClause,
                whereArgs,
                groupBy,
                having,
                orderBy
        );
    }

    public void deleteFromDatabase(String tableName, String whereClause, String[] whereArgs) {
        this.getWritableDatabase().delete(
                tableName,
                whereClause,
                whereArgs);
    }

}
