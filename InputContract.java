package ru.project.MobileAssistant.DB;
import android.provider.BaseColumns;
//Android поставляется со встроенной базой данных SQLite.
// База данных нуждается в таблице, прежде чем она сможет хранить какие-либо данные, называемые «InputTable»
public class InputContract {
    public static final String DB_NAME = "ru.MobileAssistant.db";
    public static final int DB_VERSION = 1;

    public static class TableEntry implements BaseColumns {
        public static final String TABLE_TRANSFER = "List_Transfer";
        public static final String COL_TRANSFER = "ITEM_main";
        public static final String COL_TRANSFER_number = "ITEM_number";
        public static final String COL_TRANSFER_number_name = "ITEM_number_name";
        public static final String COL_TRANSFER_ruble = "ITEM_ruble";
        public static final String COL_TRANSFER_comment = "ITEM_comment";
        public static final String COL_TRANSFER_date = "ITEM_date";
        public static final String COL_TRANSFER_favorite = "ITEM_favorite";
        public static final String COL_TRANSFER_delete = "ITEM_delete";
        public static final String TABLE_PHONE = "List_Phone";
        public static final String COL_PHONE = "ITEM_main";
        public static final String COL_PHONE_number = "ITEM_number";
        public static final String COL_PHONE_number_name = "ITEM_number_name";
        public static final String COL_PHONE_ruble = "ITEM_ruble";
        public static final String COL_PHONE_date = "ITEM_date";
        public static final String COL_PHONE_favorite = "ITEM_favorite";
        public static final String COL_PHONE_delete = "ITEM_delete";
        public static final String TABLE_BALANCE = "List_Balance";
        public static final String COL_BALANCE = "ITEM_main";
        public static final String COL_BALANCE_card = "ITEM_card";
        public static final String COL_BALANCE_date = "ITEM_date";
        public static final String COL_BALANCE_favorite = "ITEM_favorite";
        public static final String COL_BALANCE_delete = "ITEM_delete";
        public static final String TABLE_HISTORY = "List_History";
        public static final String COL_HISTORY = "ITEM_main";
        public static final String COL_HISTORY_card = "ITEM_card";
        public static final String COL_HISTORY_date = "ITEM_date";
        public static final String COL_HISTORY_favorite = "ITEM_favorite";
        public static final String COL_HISTORY_delete = "ITEM_delete";
        public static final String TABLE_THANK = "List_Thank";
        public static final String COL_THANK = "ITEM_main";
        public static final String COL_THANK_card = "ITEM_card";
        public static final String COL_THANK_date = "ITEM_date";
        public static final String COL_THANK_favorite = "ITEM_favorite";
        public static final String COL_THANK_delete = "ITEM_delete";
    }
    public static class TableEntry_Favorite implements BaseColumns {
        public static final String TABLE_FAVORITE_TRANSFER = "List_Favorite_Transfer";
        public static final String COL_FAVORITE_TRANSFER = "ITEM_Favorite_Transfer";
        public static final String COL_FAVORITE_TRANSFER_number = "ITEM_Favorite_Transfer_number";
        public static final String COL_FAVORITE_TRANSFER_number_name = "ITEM_Favorite_Transfer_number_name";
        public static final String COL_FAVORITE_TRANSFER_ruble = "ITEM_Favorite_Transfer_ruble";
        public static final String COL_FAVORITE_TRANSFER_comment = "ITEM_Favorite_Transfer_comment";
        public static final String COL_FAVORITE_TRANSFER_date = "ITEM_Favorite_Transfer_date";
        public static final String COL_FAVORITE_TRANSFER_favorite = "ITEM_Favorite_Transfer_favorite";
        public static final String TABLE_FAVORITE_PHONE = "List_Favorite_Phone";
        public static final String COL_FAVORITE_PHONE = "ITEM_Favorite_Phone";
        public static final String COL_FAVORITE_PHONE_number = "ITEM_Favorite_Phone_number";
        public static final String COL_FAVORITE_PHONE_number_name = "ITEM_Favorite_Phone_number_name";
        public static final String COL_FAVORITE_PHONE_ruble = "ITEM_Favorite_Phone_ruble";
        public static final String COL_FAVORITE_PHONE_date = "ITEM_Favorite_Phone_date";
        public static final String COL_FAVORITE_PHONE_favorite = "ITEM_Favorite_Phone_favorite";
        public static final String TABLE_FAVORITE_BALANCE = "List_Favorite_Balance";
        public static final String COL_FAVORITE_BALANCE = "ITEM_Favorite_Balance";
        public static final String COL_FAVORITE_BALANCE_card = "ITEM_Favorite_Balance_card";
        public static final String COL_FAVORITE_BALANCE_date = "ITEM_Favorite_Balance_number_date";
        public static final String COL_FAVORITE_BALANCE_favorite = "ITEM_Favorite_Balance_favorite";
        public static final String TABLE_FAVORITE_HISTORY = "List_Favorite_History";
        public static final String COL_FAVORITE_HISTORY = "ITEM_Favorite_History";
        public static final String COL_FAVORITE_HISTORY_card = "ITEM_Favorite_History_card";
        public static final String COL_FAVORITE_HISTORY_date = "ITEM_Favorite_History_date";
        public static final String COL_FAVORITE_HISTORY_favorite = "ITEM_Favorite_History_favorite";
        public static final String TABLE_FAVORITE_THANK = "List_Favorite_Thank";
        public static final String COL_FAVORITE_THANK = "ITEM_Favorite_Thank";
        public static final String COL_FAVORITE_THANK_card = "ITEM_Favorite_Thank_card";
        public static final String COL_FAVORITE_THANK_date = "ITEM_Favorite_Thank_date";
        public static final String COL_FAVORITE_THANK_favorite = "ITEM_Favorite_Thank_favorite";
    }
}
