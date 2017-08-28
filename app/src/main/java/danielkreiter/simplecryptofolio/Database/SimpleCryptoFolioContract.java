package danielkreiter.simplecryptofolio.Database;

import android.provider.BaseColumns;

public class SimpleCryptoFolioContract {

    private SimpleCryptoFolioContract() {
    }

    public static class Purchase implements BaseColumns {
        public static final String TABLE_NAME = "purchase";
        public static final String COLUMN_NAME_CURRENCYTYPE = "currencytype";
        public static final String COLUM_NAME_DATE = "date";
        public static final String COLUMN_NAME_VALUE = "value";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_PRICEPERCOIN = "pricepercoin";

        public static final String SQL_CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_VALUE + " REAL, " +
                COLUMN_NAME_AMOUNT + " REAL, " +
                COLUMN_NAME_PRICEPERCOIN + " REAL, " +
                COLUMN_NAME_CURRENCYTYPE + " TEXT," +
                COLUM_NAME_DATE + " TEXT)";


        public static final String[] TABLE_COLUMN_NAMES = {
                _ID,
                COLUMN_NAME_CURRENCYTYPE,
                COLUM_NAME_DATE,
                COLUMN_NAME_VALUE,
                COLUMN_NAME_AMOUNT,
                COLUMN_NAME_PRICEPERCOIN
        };
    }
}
