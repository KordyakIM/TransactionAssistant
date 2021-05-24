package ru.project.TransactionAssistant.Favorite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import ru.project.TransactionAssistant.DB.InputContract;
import ru.project.TransactionAssistant.DB.InputDBHelper;
import ru.project.TransactionAssistant.MainActivity;
import ru.project.TransactionAssistant.R;

//определяем custom adapter для listview всех output*-листов
public class Favorite_Adapter extends ArrayAdapter implements View.OnCreateContextMenuListener {
    private InputDBHelper InputDB; //Адаптируем Favorite_Active для хранения данных в базе данных.
    private Context mContext;
    private ArrayList<String> mArrData;
    private List<Favorite_List> FavList;

//    private MenuItem item;
    Cursor cursor_dataDB;
    int int_columnID_main, int_columnID_title, int_columnID_favorite, int_columnID_delete, int_columnID_phone, int_columnID_phone_name, int_columnID_ruble, int_columnID_comment, int_columnID_date, int_columnID_card;
    String string_column_title, string_column_phone, string_column_phone_name, string_column_ruble, string_column_comment,string_column_date, string_column_card;
    String string_extra_dropdown_title, string_extra_dropdown_phone, string_extra_dropdown_phone_name, string_extra_dropdown_ruble, string_extra_dropdown_comment, string_extra_dropdown_card;
    Boolean check_equals_while, boolean_dropdown_state;
//    SharedPreferences sharedPreferences;
//    public static final String save_dropdown_state = "dropdown_state";
    Intent intent_main;
    AlertDialog.Builder dialog_confirm;

    public Favorite_Adapter(Context context, int textViewResourceId, ArrayList<String> arrData) {
        super(context, textViewResourceId, arrData);
        this.mContext = context;
        this.mArrData = arrData;
    }

    public int getCount() {
        // return the number of records
        return mArrData.size();
    }

    // getView method is called for each item of ListView
    @SuppressLint("ResourceType")
    public View getView(final int position, View view, ViewGroup parent) {
        InputDB = new InputDBHelper(mContext); //определяем данные DB
        final ViewHolder mViewHolder = new ViewHolder();
        // inflate the layout for each item of listView
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.adapter_favorite, parent, false);
        intent_main = new Intent(mContext, MainActivity.class);
        dialog_confirm = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle); // диалог билдер для подтверждения переводов и оплаты и для получения списка карт
        string_extra_dropdown_title = "extra_dropdown_title";
        string_extra_dropdown_phone = "extra_dropdown_phone";
        string_extra_dropdown_phone_name = "extra_dropdown_phone_name";
        string_extra_dropdown_ruble = "extra_dropdown_ruble";
        string_extra_dropdown_comment = "extra_dropdown_comment";
        string_extra_dropdown_card = "extra_dropdown_card";
        // get the reference of textView and button
        mViewHolder.txtTitle = (TextView) view.findViewById(R.id.text_adapter_favorite);
        mViewHolder.btnAction = (Button) view.findViewById(R.id.button_adapter_favorite);
        mViewHolder.txtTitle.setText(mArrData.get(position));
        mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online);
//         Click listener of text
        view.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 //transfer
                 cursor_dataDB = InputDB.getListContents_where_transfer();
                 check_equals_while = true;
                 if(cursor_dataDB.getCount() == 0) {
                 }else {
                     while (cursor_dataDB.moveToNext() && check_equals_while) {
                             if (mArrData.get(position).equals(cursor_dataDB.getString(2))) {
                                 check_equals_while = false;
                                 int_columnID_title = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_title);
                                 int_columnID_phone = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_number);
                                 int_columnID_phone_name = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_number_name);
                                 int_columnID_ruble = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_ruble);
                                 int_columnID_comment = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_comment);
                                 int_columnID_date = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_date);
                                 string_column_title = cursor_dataDB.getString(int_columnID_title);
                                 string_column_phone = cursor_dataDB.getString(int_columnID_phone);
                                 string_column_phone_name = cursor_dataDB.getString(int_columnID_phone_name);
                                 string_column_ruble = cursor_dataDB.getString(int_columnID_ruble);
                                 string_column_comment = cursor_dataDB.getString(int_columnID_comment);
                                 string_column_date = cursor_dataDB.getString(int_columnID_date);
                                 dialog_confirm = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
                                 dialog_confirm.setMessage("Заголовок: " + cursor_dataDB.getString(int_columnID_title) + "\n" + "Номер: " + cursor_dataDB.getString(int_columnID_phone) +
                                         "\n" + "Имя: " + cursor_dataDB.getString(int_columnID_phone_name) +
                                         "\n" + "Сумма: " + cursor_dataDB.getString(int_columnID_ruble) + "\n" + "Комментарий: " + cursor_dataDB.getString(int_columnID_comment) +
                                         "\n" + "Дата: " + cursor_dataDB.getString(int_columnID_date))
                                         .setCancelable(false)
                                         .setPositiveButton(R.string.forward, new DialogInterface.OnClickListener() {
                                             @Override
                                             public void onClick(DialogInterface dialog, int which) {
                                                 intent_main.putExtra(string_extra_dropdown_title, string_column_title);
                                                 intent_main.putExtra(string_extra_dropdown_phone, string_column_phone);
                                                 intent_main.putExtra(string_extra_dropdown_phone_name, string_column_phone_name);
                                                 intent_main.putExtra(string_extra_dropdown_ruble, string_column_ruble);
                                                 intent_main.putExtra(string_extra_dropdown_comment, string_column_comment);
                                                 //переход в основной активити
                                                 intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                 mContext.startActivity(intent_main);
                                             }
                                         })
                                         .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                             @Override
                                             public void onClick(DialogInterface dialog, int which) {
                                                 cursor_dataDB.close();
                                                 dialog.cancel();
                                             }
                                         })
                                         .setTitle("Детали");
                                 final AlertDialog alert = dialog_confirm.create();
                                 alert.show();
                                 cursor_dataDB.close();
                                 return;
                             }
                     }
                 }
                 cursor_dataDB = InputDB.getListContents_where_phone();
                 if(cursor_dataDB.getCount() == 0) {
                 }else {
                     while (cursor_dataDB.moveToNext() && check_equals_while) {
                         if (mArrData.get(position).equals(cursor_dataDB.getString(2))) {
                             check_equals_while = false;
                             int_columnID_title = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_title);
                             int_columnID_phone = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_number);
                             int_columnID_phone_name = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_number_name);
                             int_columnID_ruble = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_ruble);
                             int_columnID_date = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_date);
                             string_column_title = cursor_dataDB.getString(int_columnID_title);
                             string_column_phone = cursor_dataDB.getString(int_columnID_phone);
                             string_column_phone_name = cursor_dataDB.getString(int_columnID_phone_name);
                             string_column_ruble = cursor_dataDB.getString(int_columnID_ruble);
                             string_column_date = cursor_dataDB.getString(int_columnID_date);
                             dialog_confirm = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
                             dialog_confirm.setMessage("Заголовок: " + cursor_dataDB.getString(int_columnID_title) + "\n" + "Телефон: " + cursor_dataDB.getString(int_columnID_phone) + "\n" + "Имя: " + cursor_dataDB.getString(int_columnID_phone_name) +
                                     "\n" + "Сумма: " + cursor_dataDB.getString(int_columnID_ruble) + "\n" + "Дата: " + cursor_dataDB.getString(int_columnID_date))
                                     .setCancelable(false)
                                     .setPositiveButton(R.string.forward, new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             intent_main.putExtra(string_extra_dropdown_title, string_column_title);
                                             intent_main.putExtra(string_extra_dropdown_phone, string_column_phone);
                                             intent_main.putExtra(string_extra_dropdown_phone_name, string_column_phone_name);
                                             intent_main.putExtra(string_extra_dropdown_ruble, string_column_ruble);
                                             //переход в основной активити
                                             intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                             mContext.startActivity(intent_main);
                                         }
                                     })
                                     .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             cursor_dataDB.close();
                                             dialog.cancel();
                                         }
                                     })
                                     .setTitle("Детали");
                             final AlertDialog alert = dialog_confirm.create();
                             alert.show();
                             cursor_dataDB.close();
                             return;
                         }
                     }
                 }
                 cursor_dataDB = InputDB.getListContents_where_balance();
                 if(cursor_dataDB.getCount() == 0) {
                 }else {
                     while (cursor_dataDB.moveToNext() && check_equals_while) {
                         if (mArrData.get(position).equals(cursor_dataDB.getString(2))) {
                             check_equals_while = false;
                             int_columnID_title = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_title);
                             int_columnID_card = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_card);
                             int_columnID_date = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_date);
                             string_column_title = cursor_dataDB.getString(int_columnID_title);
                             string_column_card = cursor_dataDB.getString(int_columnID_card);
                             string_column_date = cursor_dataDB.getString(int_columnID_date);
                             dialog_confirm = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
                             dialog_confirm.setMessage("Заголовок: " + cursor_dataDB.getString(int_columnID_title) + "\n" + "Карта: " + cursor_dataDB.getString(int_columnID_card) + "\n" + "Дата: " + cursor_dataDB.getString(int_columnID_date))
                                     .setCancelable(false)
                                     .setPositiveButton(R.string.forward, new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             intent_main.putExtra(string_extra_dropdown_title, string_column_title);
                                             intent_main.putExtra(string_extra_dropdown_card, string_column_card);
                                             //переход в основной активити
                                             intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                             mContext.startActivity(intent_main);
                                         }
                                     })
                                     .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             cursor_dataDB.close();
                                             dialog.cancel();
                                         }
                                     })
                                     .setTitle("Детали");
                             final AlertDialog alert = dialog_confirm.create();
                             alert.show();
                             cursor_dataDB.close();
                             return;
                         }
                     }
                 }
                 cursor_dataDB = InputDB.getListContents_where_history();
                 if(cursor_dataDB.getCount() == 0) {
                 }else {
                     while (cursor_dataDB.moveToNext() && check_equals_while) {
                         if (mArrData.get(position).equals(cursor_dataDB.getString(2))) {
                             check_equals_while = false;
                             int_columnID_title = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_title);
                             int_columnID_card = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_card);
                             int_columnID_date = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_date);
                             string_column_title = cursor_dataDB.getString(int_columnID_title);
                             string_column_card = cursor_dataDB.getString(int_columnID_card);
                             string_column_date = cursor_dataDB.getString(int_columnID_date);
                             dialog_confirm = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
                             dialog_confirm.setMessage("Заголовок: " + cursor_dataDB.getString(int_columnID_title) + "\n" + "Карта: " + cursor_dataDB.getString(int_columnID_card) + "\n" + "Дата: " + cursor_dataDB.getString(int_columnID_date))
                                     .setCancelable(false)
                                     .setPositiveButton(R.string.forward, new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             intent_main.putExtra(string_extra_dropdown_title, string_column_title);
                                             intent_main.putExtra(string_extra_dropdown_card, string_column_card);
                                             //переход в основной активити
                                             intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                             mContext.startActivity(intent_main);
                                         }
                                     })
                                     .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             cursor_dataDB.close();
                                             dialog.cancel();
                                         }
                                     })
                                     .setTitle("Детали");
                             final AlertDialog alert = dialog_confirm.create();
                             alert.show();
                             cursor_dataDB.close();
                             return;
                         }
                     }
                 }
                 cursor_dataDB = InputDB.getListContents_where_thank();
                 if(cursor_dataDB.getCount() == 0) {
                 }else {
                     while (cursor_dataDB.moveToNext() && check_equals_while) {
                         if (mArrData.get(position).equals(cursor_dataDB.getString(2))) {
                             check_equals_while = false;
                             int_columnID_title = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_title);
                             int_columnID_card = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_card);
                             int_columnID_date = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_date);
                             string_column_title = cursor_dataDB.getString(int_columnID_title);
                             string_column_card = cursor_dataDB.getString(int_columnID_card);
                             string_column_date = cursor_dataDB.getString(int_columnID_date);
                             dialog_confirm = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
                             dialog_confirm.setMessage("Заголовок: " + cursor_dataDB.getString(int_columnID_title) + "\n" + "Карта: " + cursor_dataDB.getString(int_columnID_card) + "\n" + "Дата: " + cursor_dataDB.getString(int_columnID_date))
                                     .setCancelable(false)
                                     .setPositiveButton(R.string.forward, new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             intent_main.putExtra(string_extra_dropdown_title, string_column_title);
                                             intent_main.putExtra(string_extra_dropdown_card, string_column_card);
                                             //переход в основной активити
                                             intent_main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                             mContext.startActivity(intent_main);
                                         }
                                     })
                                     .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             cursor_dataDB.close();
                                             dialog.cancel();
                                         }
                                     })
                                     .setTitle("Детали");
                             final AlertDialog alert = dialog_confirm.create();
                             alert.show();
                             cursor_dataDB.close();
                             return;
                         }
                     }
                 }
             }
         });
        // Click listener of button
        mViewHolder.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //transfer
                cursor_dataDB = InputDB.getListContents_transfer();
                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_main);
                int_columnID_favorite = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_favorite);
                check_equals_while = true;
                if(cursor_dataDB.getCount() == 0) {
                }else {
                    while (cursor_dataDB.moveToNext() && check_equals_while) {
                        if(mArrData.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                            check_equals_while = false;
                            if(cursor_dataDB.getString(int_columnID_favorite).equals("0")){
                                InputDB.update_Data_transfer_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)),"1");
                                mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online);
                            } else {
                                InputDB.update_Data_transfer_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)),"0");
                                mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_offline);
                                mArrData.remove(mArrData.get(position));
                                notifyDataSetChanged();
                            }
                            cursor_dataDB.close();
                            return;
                        }
                    }
                }
                //phone
                cursor_dataDB = InputDB.getListContents_phone();
                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_main);
                int_columnID_favorite = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_favorite);
                check_equals_while = true;
                if(cursor_dataDB.getCount() == 0) {
                }else {
                    while (cursor_dataDB.moveToNext() && check_equals_while) {
                        if(mArrData.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                            check_equals_while = false;
                            if(cursor_dataDB.getString(int_columnID_favorite).equals("0")){
                                InputDB.update_Data_phone_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)),"1");
                                mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online);
                            } else {
                                InputDB.update_Data_phone_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)),"0");
                                mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_offline);
                                mArrData.remove(mArrData.get(position));
                                notifyDataSetChanged();
                            }
                            cursor_dataDB.close();
                            return;
                        }
                    }
                }
                //balance
                cursor_dataDB = InputDB.getListContents_balance();
                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_main);
                int_columnID_favorite = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_favorite);
                check_equals_while = true;
                if(cursor_dataDB.getCount() == 0) {
                }else {
                    while (cursor_dataDB.moveToNext() && check_equals_while) {
                        if(mArrData.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                            check_equals_while = false;
                            if(cursor_dataDB.getString(int_columnID_favorite).equals("0")){
                                InputDB.update_Data_balance_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)),"1");
                                mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online);
                            } else {
                                InputDB.update_Data_balance_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)),"0");
                                mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_offline);
                                mArrData.remove(mArrData.get(position));
                                notifyDataSetChanged();
                            }
                            cursor_dataDB.close();
                            return;
                        }
                    }
                }
                //history
                cursor_dataDB = InputDB.getListContents_history();
                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_main);
                int_columnID_favorite = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_favorite);
                check_equals_while = true;
                if(cursor_dataDB.getCount() == 0) {
                }else {
                    while (cursor_dataDB.moveToNext() && check_equals_while) {
                        if(mArrData.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                            check_equals_while = false;
                            if(cursor_dataDB.getString(int_columnID_favorite).equals("0")){
                                InputDB.update_Data_history_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)),"1");
                                mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online);
                            } else {
                                InputDB.update_Data_history_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)),"0");
                                mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_offline);
                                mArrData.remove(mArrData.get(position));
                                notifyDataSetChanged();
                            }
                            cursor_dataDB.close();
                            return;
                        }
                    }
                }
                //thank
                cursor_dataDB = InputDB.getListContents_thank();
                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_main);
                int_columnID_favorite = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_favorite);
                check_equals_while = true;
                if(cursor_dataDB.getCount() == 0) {
                }else {
                    while (cursor_dataDB.moveToNext() && check_equals_while) {
                        if(mArrData.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                            check_equals_while = false;
                            if(cursor_dataDB.getString(int_columnID_favorite).equals("0")){
                                InputDB.update_Data_thanks_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)),"1");
                                mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online);
                            } else {
                                InputDB.update_Data_thanks_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)),"0");
                                mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_offline);
                                mArrData.remove(mArrData.get(position));
                                notifyDataSetChanged();
                            }
                            cursor_dataDB.close();
                            return;
                        }
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

    }

    static class ViewHolder{
        TextView txtTitle;
        Button btnAction;
    }
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}