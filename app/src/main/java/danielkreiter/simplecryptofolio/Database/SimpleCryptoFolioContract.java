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
    }
}
