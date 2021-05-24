package ru.project.TransactionAssistant.DB;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                InputContract.TableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM_title TEXT," + " ITEM_main TEXT," +
                " ITEM_number TEXT,"+" ITEM_number_name TEXT," + " ITEM_ruble TEXT,"+
                " ITEM_comment TEXT," + " ITEM_date TEXT," + " ITEM_favorite TEXT," + " ITEM_delete TEXT)";
        db.execSQL(string_createTable);
        string_createTable = "CREATE TABLE " + InputContract.TableEntry.TABLE_PHONE + "( " +
                InputContract.TableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM_title TEXT," + " ITEM_main TEXT," +
                " ITEM_number TEXT," + " ITEM_number_name TEXT," + " ITEM_ruble TEXT," +
                " ITEM_date TEXT," + " ITEM_favorite TEXT," + " ITEM_delete TEXT)";
        db.execSQL(string_createTable);
        string_createTable = "CREATE TABLE " + InputContract.TableEntry.TABLE_BALANCE + "( " +
                InputContract.TableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM_title TEXT," + " ITEM_main TEXT," +
                " ITEM_card TEXT," + " ITEM_date TEXT," + " ITEM_favorite TEXT," + " ITEM_delete TEXT)";
        db.execSQL(string_createTable);
        string_createTable = "CREATE TABLE " + InputContract.TableEntry.TABLE_HISTORY + "( " +
                InputContract.TableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM_title TEXT," + " ITEM_main TEXT," +
                " ITEM_card TEXT," + " ITEM_date TEXT," + " ITEM_favorite TEXT," + " ITEM_delete TEXT)";
        db.execSQL(string_createTable);
        string_createTable = "CREATE TABLE " + InputContract.TableEntry.TABLE_THANK + "( " +
                InputContract.TableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + " ITEM_title TEXT," + " ITEM_main TEXT," +
                " ITEM_card TEXT," + " ITEM_date TEXT," + " ITEM_favorite TEXT," + " ITEM_delete TEXT)";
        db.execSQL(string_createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TableEntry.TABLE_TRANSFER);
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TableEntry.TABLE_PHONE);
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TableEntry.TABLE_BALANCE);
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TableEntry.TABLE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + InputContract.TableEntry.TABLE_THANK);
        onCreate(db);
    }

    public boolean addData_transfer(String title, String message, String number,String number_name, String ruble, String comment, String date, String favorite, String delete) {
        db = this.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(InputContract.TableEntry.COL_TRANSFER_title, title);
        contentValues.put(InputContract.TableEntry.COL_TRANSFER_main, message);
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
        public boolean addData_phone(String title, String message, String number, String number_name, String ruble, String date, String favorite, String delete){
            db = this.getWritableDatabase();
            contentValues = new ContentValues();
            contentValues.put(InputContract.TableEntry.COL_PHONE_title, title);
            contentValues.put(InputContract.TableEntry.COL_PHONE_main, message);
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
            public boolean addData_balance(String title, String message, String card, String date, String favorite, String delete){
                db = this.getWritableDatabase();
                contentValues = new ContentValues();
                contentValues.put(InputContract.TableEntry.COL_BALANCE_title, title);
                contentValues.put(InputContract.TableEntry.COL_BALANCE_main, message);
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
                public boolean addData_history(String title, String message, String card, String date, String favorite, String delete){
                    db = this.getWritableDatabase();
                    contentValues = new ContentValues();
                    contentValues.put(InputContract.TableEntry.COL_HISTORY_title, title);
                    contentValues.put(InputContract.TableEntry.COL_HISTORY_main, message);
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
                    public boolean addData_thank(String title, String message, String card, String date, String favorite, String delete){
                        db = this.getWritableDatabase();
                        contentValues = new ContentValues();
                        contentValues.put(InputContract.TableEntry.COL_THANK_title, title);
                        contentValues.put(InputContract.TableEntry.COL_THANK_main, message);
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

    public void delete_Data_transfer(int id) {
        db = this.getWritableDatabase();
        db.execSQL("Delete FROM "+InputContract.TableEntry.TABLE_TRANSFER + " WHERE " + InputContract.TableEntry._ID+" = "+id);
    }
        public void delete_Data_phone(int id) {
            db = this.getWritableDatabase();
            db.execSQL("Delete FROM "+InputContract.TableEntry.TABLE_PHONE + " WHERE " + InputContract.TableEntry._ID+" = "+id);
        }
            public void delete_Data_balance(int id) {
                db = this.getWritableDatabase();
                db.execSQL("Delete FROM "+InputContract.TableEntry.TABLE_BALANCE + " WHERE " + InputContract.TableEntry._ID+" = "+id);
            }
                public void delete_Data_history(int id) {
                    db = this.getWritableDatabase();
                    db.execSQL("Delete FROM "+InputContract.TableEntry.TABLE_HISTORY + " WHERE " + InputContract.TableEntry._ID+" = "+id);
                }
                    public void delete_Data_thanks(int id) {
                        db = this.getWritableDatabase();
                        db.execSQL("Delete FROM "+InputContract.TableEntry.TABLE_THANK + " WHERE " + InputContract.TableEntry._ID+" = "+id);
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
