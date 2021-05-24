package ru.project.TransactionAssistant.DB;
import android.provider.BaseColumns;
//Android поставляется со встроенной базой данных SQLite.
// База данных нуждается в таблице, прежде чем она сможет хранить какие-либо данные, называемые «InputTable»
public class InputContract {
    public static final String DB_NAME = "ru.MobileAssistant.db";
    public static final int DB_VERSION = 1;

    public static class TableEntry implements BaseColumns {
        public static final String TABLE_TRANSFER = "List_Transfer";
        public static final String COL_TRANSFER_title = "ITEM_title";
        public static final String COL_TRANSFER_main = "ITEM_main";
        public static final String COL_TRANSFER_number = "ITEM_number";
        public static final String COL_TRANSFER_number_name = "ITEM_number_name";
        public static final String COL_TRANSFER_ruble = "ITEM_ruble";
        public static final String COL_TRANSFER_comment = "ITEM_comment";
        public static final String COL_TRANSFER_date = "ITEM_date";
        public static final String COL_TRANSFER_favorite = "ITEM_favorite";
        public static final String COL_TRANSFER_delete = "ITEM_delete";
        public static final String TABLE_PHONE = "List_Phone";
        public static final String COL_PHONE_title = "ITEM_title";
        public static final String COL_PHONE_main = "ITEM_main";
        public static final String COL_PHONE_number = "ITEM_number";
        public static final String COL_PHONE_number_name = "ITEM_number_name";
        public static final String COL_PHONE_ruble = "ITEM_ruble";
        public static final String COL_PHONE_date = "ITEM_date";
        public static final String COL_PHONE_favorite = "ITEM_favorite";
        public static final String COL_PHONE_delete = "ITEM_delete";
        public static final String TABLE_BALANCE = "List_Balance";
        public static final String COL_BALANCE_title = "ITEM_title";
        public static final String COL_BALANCE_main = "ITEM_main";
        public static final String COL_BALANCE_card = "ITEM_card";
        public static final String COL_BALANCE_date = "ITEM_date";
        public static final String COL_BALANCE_favorite = "ITEM_favorite";
        public static final String COL_BALANCE_delete = "ITEM_delete";
        public static final String TABLE_HISTORY = "List_History";
        public static final String COL_HISTORY_title = "ITEM_title";
        public static final String COL_HISTORY_main = "ITEM_main";
        public static final String COL_HISTORY_card = "ITEM_card";
        public static final String COL_HISTORY_date = "ITEM_date";
        public static final String COL_HISTORY_favorite = "ITEM_favorite";
        public static final String COL_HISTORY_delete = "ITEM_delete";
        public static final String TABLE_THANK = "List_Thank";
        public static final String COL_THANK_title = "ITEM_title";
        public static final String COL_THANK_main = "ITEM_main";
        public static final String COL_THANK_card = "ITEM_card";
        public static final String COL_THANK_date = "ITEM_date";
        public static final String COL_THANK_favorite = "ITEM_favorite";
        public static final String COL_THANK_delete = "ITEM_delete";
    }
}
