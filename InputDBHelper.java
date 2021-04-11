package ru.project.MobileAssistant.DB;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ru.project.MobileAssistant.MainActivity;

//Класс InputContract определяет константы, которые используются для доступа к данным в базе данных.
// Вам также понадобится вспомогательный класс InputDbHelper для открытия базы данных.
public class InputDBHelper extends SQLiteOpenHelper  {
    String string_createTable;
    ContentValues contentValues;
    SQLiteDatabase db;
    Cursor cursor_data;
    public InputDBHelper(Context context) {
        super(context, InputContract.DB_NAME, null, InputContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        string_createTable = "CREATE TABLE " + InputContract.TableEntry.TABLE_TRANSFER + "( " +
                InputContract.TableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM_main TEXT,"+
                " ITEM_number TEXT,"+" ITEM_number_name TEXT," + " ITEM_ruble TEXT,"+
                " ITEM_comment TEXT," + " ITEM_date TEXT," + " ITEM_favorite TEXT," + " ITEM_delete TEXT)";
        db.execSQL(string_createTable);
        string_createTable = "CREATE TABLE " + InputContract.TableEntry.TABLE_PHONE + "( " +
                InputContract.TableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM_main TEXT,"+
                " ITEM_number TEXT," + " ITEM_number_name TEXT," + " ITEM_ruble TEXT," +
                " ITEM_date TEXT," + " ITEM_favorite TEXT," + " ITEM_delete TEXT)";
        db.execSQL(string_createTable);
        string_createTable = "CREATE TABLE " + InputContract.TableEntry.TABLE_BALANCE + "( " +
                InputContract.TableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM_main TEXT," +
                " ITEM_card TEXT," + " ITEM_date TEXT," + " ITEM_favorite TEXT," + " ITEM_delete TEXT)";
        db.execSQL(string_createTable);
        string_createTable = "CREATE TABLE " + InputContract.TableEntry.TABLE_HISTORY + "( " +
                InputContract.TableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM_main TEXT," +
                " ITEM_card TEXT," + " ITEM_date TEXT," + " ITEM_favorite TEXT," + " ITEM_delete TEXT)";
        db.execSQL(string_createTable);
        string_createTable = "CREATE TABLE " + InputContract.TableEntry.TABLE_THANK + "( " +
                InputContract.TableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM_main TEXT," +
                " ITEM_card TEXT," + " ITEM_date TEXT," + " ITEM_favorite TEXT," + " ITEM_delete TEXT)";
        db.execSQL(string_createTable);
        string_createTable = "CREATE TABLE " + InputContract.TableEntry_Favorite.TABLE_FAVORITE_TRANSFER + "( " +
                InputContract.TableEntry_Favorite._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM_Favorite_Transfer TEXT,"+
                " ITEM_Favorite_Transfer_number TEXT,"+" ITEM_Favorite_Transfer_number_name TEXT," + " ITEM_Favorite_Transfer_ruble TEXT,"+
                " ITEM_Favorite_Transfer_comment TEXT," + " ITEM_Favorite_Transfer_date TEXT," + " ITEM_Favorite_Transfer_favorite TEXT)";
        db.execSQL(string_createTable);
        string_createTable = "CREATE TABLE " + InputContract.TableEntry_Favorite.TABLE_FAVORITE_PHONE + "( " +
                InputContract.TableEntry_Favorite._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM_Favorite_Phone TEXT,"+
                " ITEM_Favorite_Phone_number TEXT,"+" ITEM_Favorite_Phone_number_name TEXT," + " ITEM_Favorite_Phone_ruble TEXT,"+
                " ITEM_Favorite_Phone_date TEXT," + " ITEM_Favorite_Phone_favorite TEXT)";
        db.execSQL(string_createTable);
        string_createTable = "CREATE TABLE " + InputContract.TableEntry_Favorite.TABLE_FAVORITE_BALANCE + "( " +
                InputContract.TableEntry_Favorite._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM_Favorite_Balance TEXT,"+
                " ITEM_Favorite_Balance_card TEXT,"+" ITEM_Favorite_Balance_number_date TEXT," + " ITEM_Favorite_Balance_favorite TEXT)";
        db.execSQL(string_createTable);
        string_createTable = "CREATE TABLE " + InputContract.TableEntry_Favorite.TABLE_FAVORITE_HISTORY + "( " +
                InputContract.TableEntry_Favorite._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM_Favorite_History TEXT,"+
                " ITEM_Favorite_History_card TEXT,"+" ITEM_Favorite_History_date TEXT," + " ITEM_Favorite_History_favorite TEXT)";
        db.execSQL(string_createTable);
        string_createTable = "CREATE TABLE " + InputContract.TableEntry_Favorite.TABLE_FAVORITE_THANK + "( " +
                InputContract.TableEntry_Favorite._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM_Favorite_Thank TEXT,"+
                " ITEM_Favorite_Thank_card TEXT,"+" ITEM_Favorite_Thank_date TEXT," + " ITEM_Favorite_Thank_favorite TEXT)";
        db.execSQL(string_createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TableEntry.TABLE_TRANSFER);
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TableEntry.TABLE_PHONE);
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TableEntry.TABLE_BALANCE);
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TableEntry.TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TableEntry.TABLE_THANK);
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TableEntry_Favorite.TABLE_FAVORITE_TRANSFER);
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TableEntry_Favorite.TABLE_FAVORITE_PHONE);
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TableEntry_Favorite.TABLE_FAVORITE_BALANCE);
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TableEntry_Favorite.TABLE_FAVORITE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TableEntry_Favorite.TABLE_FAVORITE_THANK);
        onCreate(db);
    }

    public boolean addData_transfer(String message, String number,String number_name, String ruble, String comment, String date, String favorite, String delete) {
        db = this.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(InputContract.TableEntry.COL_TRANSFER, message);
        contentValues.put(InputContract.TableEntry.COL_TRANSFER_number, number);
        contentValues.put(InputContract.TableEntry.COL_TRANSFER_number_name, number_name);
        contentValues.put(InputContract.TableEntry.COL_TRANSFER_ruble, ruble);
        contentValues.put(InputContract.TableEntry.COL_TRANSFER_comment, comment);
        contentValues.put(InputContract.TableEntry.COL_TRANSFER_date, date);
        contentValues.put(InputContract.TableEntry.COL_TRANSFER_favorite, favorite);
        contentValues.put(InputContract.TableEntry.COL_TRANSFER_delete, delete);
        long result = db.insert(InputContract.TableEntry.TABLE_TRANSFER, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

        public boolean addData_phone(String message, String number, String number_name, String ruble, String date, String favorite, String delete){
            db = this.getWritableDatabase();
            contentValues = new ContentValues();
            contentValues.put(InputContract.TableEntry.COL_PHONE, message);
            contentValues.put(InputContract.TableEntry.COL_PHONE_number, number);
            contentValues.put(InputContract.TableEntry.COL_PHONE_number_name, number_name);
            contentValues.put(InputContract.TableEntry.COL_PHONE_ruble, ruble);
            contentValues.put(InputContract.TableEntry.COL_PHONE_date, date);
            contentValues.put(InputContract.TableEntry.COL_PHONE_favorite, favorite);
            contentValues.put(InputContract.TableEntry.COL_PHONE_delete, delete);
            long result = db.insert(InputContract.TableEntry.TABLE_PHONE, null, contentValues);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
            public boolean addData_balance(String message, String card, String date, String favorite, String delete){
                db = this.getWritableDatabase();
                contentValues = new ContentValues();
                contentValues.put(InputContract.TableEntry.COL_BALANCE, message);
                contentValues.put(InputContract.TableEntry.COL_BALANCE_card, card);
                contentValues.put(InputContract.TableEntry.COL_BALANCE_date, date);
                contentValues.put(InputContract.TableEntry.COL_BALANCE_favorite, favorite);
                contentValues.put(InputContract.TableEntry.COL_BALANCE_delete, delete);
                long result = db.insert(InputContract.TableEntry.TABLE_BALANCE, null, contentValues);
                if (result == -1) {
                    return false;
                } else {
                    return true;
                }
            }
                public boolean addData_history(String message, String card, String date, String favorite, String delete){
                    db = this.getWritableDatabase();
                    contentValues = new ContentValues();
                    contentValues.put(InputContract.TableEntry.COL_HISTORY, message);
                    contentValues.put(InputContract.TableEntry.COL_HISTORY_card, card);
                    contentValues.put(InputContract.TableEntry.COL_HISTORY_date, date);
                    contentValues.put(InputContract.TableEntry.COL_HISTORY_favorite, favorite);
                    contentValues.put(InputContract.TableEntry.COL_HISTORY_delete, delete);
                    long result = db.insert(InputContract.TableEntry.TABLE_HISTORY, null, contentValues);
                    if (result == -1) {
                        return false;
                    } else {
                        return true;
                    }
                }
                    public boolean addData_thank(String message, String card, String date, String favorite, String delete){
                        db = this.getWritableDatabase();
                        contentValues = new ContentValues();
                        contentValues.put(InputContract.TableEntry.COL_THANK, message);
                        contentValues.put(InputContract.TableEntry.COL_THANK_card, card);
                        contentValues.put(InputContract.TableEntry.COL_THANK_date, date);
                        contentValues.put(InputContract.TableEntry.COL_THANK_favorite, favorite);
                        contentValues.put(InputContract.TableEntry.COL_THANK_delete, delete);
                        long result = db.insert(InputContract.TableEntry.TABLE_THANK, null, contentValues);
                        if (result == -1) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                        public boolean addData_favorite_transfer(String message, String number,String number_name, String ruble, String comment, String date, String favorite) {
                            db = this.getWritableDatabase();
                            contentValues = new ContentValues();
                            contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_TRANSFER, message);
                            contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_TRANSFER_number, number);
                            contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_TRANSFER_number_name, number_name);
                            contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_TRANSFER_ruble, ruble);
                            contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_TRANSFER_comment, comment);
                            contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_TRANSFER_date, date);
                            contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_TRANSFER_favorite, favorite);
                            long result = db.insert(InputContract.TableEntry_Favorite.TABLE_FAVORITE_TRANSFER, null, contentValues);
                            if (result == -1) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                            public boolean addData_favorite_phone(String message, String number,String number_name, String ruble, String date, String favorite) {
                                db = this.getWritableDatabase();
                                contentValues = new ContentValues();
                                contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_PHONE, message);
                                contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_PHONE_number, number);
                                contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_PHONE_number_name, number_name);
                                contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_PHONE_ruble, ruble);
                                contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_PHONE_date, date);
                                contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_PHONE_favorite, favorite);
                                long result = db.insert(InputContract.TableEntry_Favorite.TABLE_FAVORITE_TRANSFER, null, contentValues);
                                if (result == -1) {
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                                public boolean addData_favorite_balance(String message, String card, String date, String favorite) {
                                    db = this.getWritableDatabase();
                                    contentValues = new ContentValues();
                                    contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_BALANCE, message);
                                    contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_BALANCE_card, card);
                                    contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_BALANCE_date, date);
                                    contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_BALANCE_favorite, favorite);
                                    long result = db.insert(InputContract.TableEntry_Favorite.TABLE_FAVORITE_TRANSFER, null, contentValues);
                                    if (result == -1) {
                                        return false;
                                    } else {
                                        return true;
                                    }
                                }
                                    public boolean addData_favorite_history(String message, String card, String date, String favorite) {
                                        db = this.getWritableDatabase();
                                        contentValues = new ContentValues();
                                        contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_HISTORY, message);
                                        contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_HISTORY_card, card);
                                        contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_HISTORY_date, date);
                                        contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_HISTORY_favorite, favorite);
                                        long result = db.insert(InputContract.TableEntry_Favorite.TABLE_FAVORITE_TRANSFER, null, contentValues);
                                        if (result == -1) {
                                            return false;
                                        } else {
                                            return true;
                                        }
                                    }
                                        public boolean addData_favorite_thank(String message, String card, String date, String favorite) {
                                            db = this.getWritableDatabase();
                                            contentValues = new ContentValues();
                                            contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_THANK, message);
                                            contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_THANK_card, card);
                                            contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_THANK_date, date);
                                            contentValues.put(InputContract.TableEntry_Favorite.COL_FAVORITE_THANK_favorite, favorite);
                                            long result = db.insert(InputContract.TableEntry_Favorite.TABLE_FAVORITE_TRANSFER, null, contentValues);
                                            if (result == -1) {
                                                return false;
                                            } else {
                                                return true;
                                            }
                                        }
//метод rawQuery(), он частично используется
public Cursor getListContents_where_transfer(){
    db = this.getWritableDatabase();
    cursor_data = db.rawQuery("SELECT * FROM " + InputContract.TableEntry.TABLE_TRANSFER + " WHERE " + InputContract.TableEntry.COL_TRANSFER_favorite +" = 1" , null);
    return cursor_data;
}
    public Cursor getListContents_where_phone(){
        db = this.getWritableDatabase();
        cursor_data = db.rawQuery("SELECT * FROM " + InputContract.TableEntry.TABLE_PHONE + " WHERE " + InputContract.TableEntry.COL_PHONE_favorite +" = 1" , null);
        return cursor_data;
    }
    public Cursor getListContents_where_balance(){
        db = this.getWritableDatabase();
        cursor_data = db.rawQuery("SELECT * FROM " + InputContract.TableEntry.TABLE_BALANCE + " WHERE " + InputContract.TableEntry.COL_BALANCE_favorite +" = 1" , null);
        return cursor_data;
    }
    public Cursor getListContents_where_history(){
        db = this.getWritableDatabase();
        cursor_data = db.rawQuery("SELECT * FROM " + InputContract.TableEntry.TABLE_HISTORY + " WHERE " + InputContract.TableEntry.COL_HISTORY_favorite +" = 1" , null);
        return cursor_data;
    }
    public Cursor getListContents_where_thank(){
        db = this.getWritableDatabase();
        cursor_data = db.rawQuery("SELECT * FROM " + InputContract.TableEntry.TABLE_THANK + " WHERE " + InputContract.TableEntry.COL_THANK_favorite +" = 1" , null);
        return cursor_data;
    }
    public Cursor getListContents_transfer() {
        db = this.getWritableDatabase();
        cursor_data = db.rawQuery("SELECT * FROM " + InputContract.TableEntry.TABLE_TRANSFER + " ORDER BY " + InputContract.TableEntry._ID+" DESC", null);
        return cursor_data;
    }
        public Cursor getListContents_phone() {
            db = this.getWritableDatabase();
            cursor_data = db.rawQuery("SELECT * FROM " + InputContract.TableEntry.TABLE_PHONE + " ORDER BY " + InputContract.TableEntry._ID+" DESC", null);
            return cursor_data;
        }
            public Cursor getListContents_balance() {
                db = this.getWritableDatabase();
                cursor_data = db.rawQuery("SELECT * FROM " + InputContract.TableEntry.TABLE_BALANCE + " ORDER BY " + InputContract.TableEntry._ID+" DESC", null);
                return cursor_data;
            }
                public Cursor getListContents_history() {
                    db = this.getWritableDatabase();
                    cursor_data = db.rawQuery("SELECT * FROM " + InputContract.TableEntry.TABLE_HISTORY + " ORDER BY " + InputContract.TableEntry._ID+" DESC", null);
                    return cursor_data;
                }
                    public Cursor getListContents_thank(){
                        db = this.getWritableDatabase();
                        cursor_data = db.rawQuery("SELECT * FROM "+InputContract.TableEntry.TABLE_THANK + " ORDER BY " + InputContract.TableEntry._ID+" DESC", null);
                        return cursor_data;
                    }
                        public Cursor getListContents_favorite_transfer(){
                            db = this.getWritableDatabase();
                            cursor_data = db.rawQuery("SELECT * FROM " + InputContract.TableEntry_Favorite.TABLE_FAVORITE_TRANSFER + " ORDER BY " + InputContract.TableEntry_Favorite._ID+" DESC", null);
                            return cursor_data;
                        }
                            public Cursor getListContents_favorite_phone() {
                                db = this.getWritableDatabase();
                                cursor_data = db.rawQuery("SELECT * FROM " + InputContract.TableEntry_Favorite.TABLE_FAVORITE_PHONE + " ORDER BY " + InputContract.TableEntry_Favorite._ID+" DESC", null);
                                return cursor_data;
                            }
                                public Cursor getListContents_favorite_balance() {
                                    db = this.getWritableDatabase();
                                    cursor_data = db.rawQuery("SELECT * FROM " + InputContract.TableEntry_Favorite.TABLE_FAVORITE_BALANCE + " ORDER BY " + InputContract.TableEntry_Favorite._ID+" DESC", null);
                                    return cursor_data;
                                }
                                    public Cursor getListContents_favorite_history() {
                                        db = this.getWritableDatabase();
                                        cursor_data = db.rawQuery("SELECT * FROM " + InputContract.TableEntry_Favorite.TABLE_FAVORITE_HISTORY + " ORDER BY " + InputContract.TableEntry_Favorite._ID+" DESC", null);
                                        return cursor_data;
                                    }
                                        public Cursor getListContents_favorite_thank(){
                                            db = this.getWritableDatabase();
                                            cursor_data = db.rawQuery("SELECT * FROM "+InputContract.TableEntry_Favorite.TABLE_FAVORITE_THANK + " ORDER BY " + InputContract.TableEntry_Favorite._ID+" DESC", null);
                                            return cursor_data;
                                        }


    public void delete_Data_transfer(int id) {
        db = this.getWritableDatabase();
        db.execSQL("Delete FROM "+InputContract.TableEntry.TABLE_TRANSFER + " WHERE " + InputContract.TableEntry._ID+" = "+id);
    }
        public void delete_Data_phone(int id) {
            db = this.getWritableDatabase();
    //        Log.d ("My_log","InputDB"+id);
            db.execSQL("Delete FROM "+InputContract.TableEntry.TABLE_PHONE + " WHERE " + InputContract.TableEntry._ID+" = "+id);
        }
            public void delete_Data_balance(int id) {
                db = this.getWritableDatabase();
        //        Log.d ("My_log","InputDB"+id);
                db.execSQL("Delete FROM "+InputContract.TableEntry.TABLE_BALANCE + " WHERE " + InputContract.TableEntry._ID+" = "+id);
            }
                public void delete_Data_history(int id) {
                    db = this.getWritableDatabase();
            //        Log.d ("My_log","InputDB"+id);
                    db.execSQL("Delete FROM "+InputContract.TableEntry.TABLE_HISTORY + " WHERE " + InputContract.TableEntry._ID+" = "+id);
                }
                    public void delete_Data_thanks(int id) {
                        db = this.getWritableDatabase();
                //        Log.d ("My_log","InputDB"+id);
                        db.execSQL("Delete FROM "+InputContract.TableEntry.TABLE_THANK + " WHERE " + InputContract.TableEntry._ID+" = "+id);
                    }
    public void delete_Data_favorite_transfer(int id) {
        db = this.getWritableDatabase();
        db.execSQL("Delete FROM "+InputContract.TableEntry_Favorite.TABLE_FAVORITE_TRANSFER + " WHERE " + InputContract.TableEntry_Favorite._ID+" = "+id);
    }
        public void delete_Data_favorite_phone(int id) {
            db = this.getWritableDatabase();
            //        Log.d ("My_log","InputDB"+id);
            db.execSQL("Delete FROM "+InputContract.TableEntry_Favorite.TABLE_FAVORITE_PHONE + " WHERE " + InputContract.TableEntry_Favorite._ID+" = "+id);
        }
            public void delete_Data_favorite_balance(int id) {
                db = this.getWritableDatabase();
                //        Log.d ("My_log","InputDB"+id);
                db.execSQL("Delete FROM "+InputContract.TableEntry_Favorite.TABLE_FAVORITE_BALANCE + " WHERE " + InputContract.TableEntry_Favorite._ID+" = "+id);
            }
                public void delete_Data_favorite_history(int id) {
                    db = this.getWritableDatabase();
                    //        Log.d ("My_log","InputDB"+id);
                    db.execSQL("Delete FROM "+InputContract.TableEntry_Favorite.TABLE_FAVORITE_HISTORY + " WHERE " + InputContract.TableEntry_Favorite._ID+" = "+id);
                }
                    public void delete_Data_favorite_thanks(int id) {
                        db = this.getWritableDatabase();
                        //        Log.d ("My_log","InputDB"+id);
                        db.execSQL("Delete FROM "+InputContract.TableEntry_Favorite.TABLE_FAVORITE_THANK + " WHERE " + InputContract.TableEntry_Favorite._ID+" = "+id);
                    }

    public void update_Data_transfer_favorite(int id, String favorite_number) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ITEM_favorite",favorite_number);
        db.update(InputContract.TableEntry.TABLE_TRANSFER, cv, "_id = ?",new String[]{String.valueOf(id)});
    }
        public void update_Data_phone_favorite(int id, String favorite_number) {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("ITEM_favorite",favorite_number);
            db.update(InputContract.TableEntry.TABLE_PHONE, cv, "_id = ?",new String[]{String.valueOf(id)});
        }
            public void update_Data_balance_favorite(int id, String favorite_number) {
                db = this.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("ITEM_favorite",favorite_number);
                db.update(InputContract.TableEntry.TABLE_BALANCE, cv, "_id = ?",new String[]{String.valueOf(id)});
            }
                public void update_Data_history_favorite(int id, String favorite_number) {
                    db = this.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put("ITEM_favorite",favorite_number);
                    db.update(InputContract.TableEntry.TABLE_HISTORY, cv, "_id = ?",new String[]{String.valueOf(id)});
                }
                    public void update_Data_thanks_favorite(int id, String favorite_number) {
                        db = this.getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put("ITEM_favorite",favorite_number);
                        db.update(InputContract.TableEntry.TABLE_THANK, cv, "_id = ?",new String[]{String.valueOf(id)});
                    }
    public void update_Data_transfer_delete(int id, String delete) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ITEM_delete",delete);
        db.update(InputContract.TableEntry.TABLE_TRANSFER, cv, "_id = ?",new String[]{String.valueOf(id)});
    }
        public void update_Data_phone_delete(int id, String delete) {
            db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("ITEM_delete",delete);
            db.update(InputContract.TableEntry.TABLE_PHONE, cv, "_id = ?",new String[]{String.valueOf(id)});
        }
            public void update_Data_balance_delete(int id, String delete) {
                db = this.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("ITEM_delete",delete);
                db.update(InputContract.TableEntry.TABLE_BALANCE, cv, "_id = ?",new String[]{String.valueOf(id)});
            }
                public void update_Data_history_delete(int id, String delete) {
                    db = this.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put("ITEM_delete",delete);
                    db.update(InputContract.TableEntry.TABLE_HISTORY, cv, "_id = ?",new String[]{String.valueOf(id)});
                }
                    public void update_Data_thank_delete(int id, String delete) {
                        db = this.getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put("ITEM_delete",delete);
                        db.update(InputContract.TableEntry.TABLE_THANK, cv, "_id = ?",new String[]{String.valueOf(id)});
                    }
}
