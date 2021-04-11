package ru.project.MobileAssistant.Favorite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.project.MobileAssistant.DB.InputContract;
import ru.project.MobileAssistant.DB.InputDBHelper;
import ru.project.MobileAssistant.MainActivity;
import ru.project.MobileAssistant.R;

//определяем custom adapter для listview всех output*-листов
public class Favorite_Adapter extends ArrayAdapter implements View.OnCreateContextMenuListener {
    private InputDBHelper InputDB; //Адаптируем Favorite_Active для хранения данных в базе данных.
    private Context mContext;
    private ArrayList<String> mArrData;
    private List<Favorite_List> FavList;

//    private MenuItem item;
    Cursor cursor_dataDB;
    int int_columnID_main, int_columnID_favorite;
    Boolean check_equals_while;
    Intent intent_main;

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
//        mainActivity = new MainActivity(mContext); //определяем данные DB
        final ViewHolder mViewHolder = new ViewHolder();
        // inflate the layout for each item of listView
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.adapter_favorite, parent, false);
        intent_main = new Intent(mContext, MainActivity.class);
        // get the reference of textView and button
        mViewHolder.txtTitle = (TextView) view.findViewById(R.id.text_adapter_favorite);
        mViewHolder.btnAction = (Button) view.findViewById(R.id.button_adapter_favorite);
        mViewHolder.txtTitle.setText(mArrData.get(position));
        mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online);
        // Click listener of button
        mViewHolder.btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //transfer
                cursor_dataDB = InputDB.getListContents_transfer();
                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER);
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
                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE);
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
                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE);
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
                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY);
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
                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK);
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