package ru.project.MobileAssistant.Favorite;

import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Objects;

import ru.project.MobileAssistant.DB.InputContract;
import ru.project.MobileAssistant.DB.InputDBHelper;
import ru.project.MobileAssistant.MainActivity;
import ru.project.MobileAssistant.R;

public class Favorite_List extends AppCompatActivity {
    ArrayList<String> arrayList_favorite; //лист для выгрузки информации
    ArrayAdapter<String> adapter_favorite;
    Cursor cursor_dataDB_transfer, cursor_dataDB_phone, cursor_dataDB_balance, cursor_dataDB_history, cursor_dataDB_thank;
    ListView listview_favorite; //лист вывода
    private InputDBHelper InputDB; //Адаптируем Favorite_List для хранения данных в базе данных.
//    private Favorite_Adapter mAdapter;
    private MainActivity mainActivity;
    Toolbar toolbar;
    ViewGroup parent;
    Menu menu;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //определяем toolbar для перехода выше
        setContentView(R.layout.toolbar_favorite_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        mainActivity = new MainActivity(this);
//        this.menu = FavoriteAdapter.onCreateOptionsMenu(menu);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.cardview_shadow_start_color), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorGreen), PorterDuff.Mode.SRC_ATOP);
//                menu.getItem(0).setIcon(ContextCompat.getDrawable(FavoriteAdapter, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
//                MainActivity.FavoriteAdapter;
//                Log.d("My_log","BackPressed Favorite list");
                onBackPressed();
            }
        });
        onUpdateFavorite_Adapter();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    public void onUpdateFavorite_Adapter() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_favorite, parent, false);
        Button btnAction = (Button) view.findViewById(R.id.button_adapter_favorite);
        InputDB = new InputDBHelper(this);//определяем данные DB
        listview_favorite = findViewById(R.id.listview_favorite);
        arrayList_favorite = new ArrayList<String>();
        adapter_favorite = new Favorite_Adapter(this, R.layout.my_simple_list_item_multiple_choice, arrayList_favorite);
        listview_favorite.setAdapter(adapter_favorite);
        cursor_dataDB_transfer = InputDB.getListContents_transfer();
        cursor_dataDB_phone = InputDB.getListContents_phone();
        cursor_dataDB_balance = InputDB.getListContents_balance();
        cursor_dataDB_history = InputDB.getListContents_history();
        cursor_dataDB_thank = InputDB.getListContents_thank();
        if(cursor_dataDB_transfer.getCount() == 0 & cursor_dataDB_phone.getCount() == 0 & cursor_dataDB_balance.getCount() == 0 & cursor_dataDB_history.getCount() == 0 & cursor_dataDB_thank.getCount() == 0) {
        }else{
            int int_columnID_transfer_main = cursor_dataDB_transfer.getColumnIndex(InputContract.TableEntry.COL_TRANSFER);
            int int_columnID_transfer_favorite = cursor_dataDB_transfer.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_favorite);
            while (cursor_dataDB_transfer.moveToNext()) {
                if (cursor_dataDB_transfer.getString(int_columnID_transfer_favorite).equals("1")) {
                    arrayList_favorite.add(cursor_dataDB_transfer.getString(int_columnID_transfer_main));
                    adapter_favorite.notifyDataSetChanged();
                }
            }
            int int_columnID_phone_main = cursor_dataDB_phone.getColumnIndex(InputContract.TableEntry.COL_PHONE);
            int int_columnID_phone_favorite = cursor_dataDB_phone.getColumnIndex(InputContract.TableEntry.COL_PHONE_favorite);
            while (cursor_dataDB_phone.moveToNext()) {
                if (cursor_dataDB_phone.getString(int_columnID_phone_favorite).equals("1")) {
                    arrayList_favorite.add(cursor_dataDB_phone.getString(int_columnID_phone_main));
                    adapter_favorite.notifyDataSetChanged();
                }
            }
            int int_columnID_balance_main = cursor_dataDB_balance.getColumnIndex(InputContract.TableEntry.COL_BALANCE);
            int int_columnID_balance_favorite = cursor_dataDB_balance.getColumnIndex(InputContract.TableEntry.COL_BALANCE_favorite);
            while (cursor_dataDB_balance.moveToNext()) {
                if (cursor_dataDB_balance.getString(int_columnID_balance_favorite).equals("1")) {
                    arrayList_favorite.add(cursor_dataDB_balance.getString(int_columnID_balance_main));
                    adapter_favorite.notifyDataSetChanged();
                }
            }
            int int_columnID_history_main = cursor_dataDB_history.getColumnIndex(InputContract.TableEntry.COL_HISTORY);
            int int_columnID_history_favorite = cursor_dataDB_history.getColumnIndex(InputContract.TableEntry.COL_HISTORY_favorite);
            while (cursor_dataDB_history.moveToNext()) {
                if (cursor_dataDB_history.getString(int_columnID_history_favorite).equals("1")) {
                    arrayList_favorite.add(cursor_dataDB_history.getString(int_columnID_history_main));
                    adapter_favorite.notifyDataSetChanged();
                }
            }
            int int_columnID_thank_main = cursor_dataDB_thank.getColumnIndex(InputContract.TableEntry.COL_THANK);
            int int_columnID_thank_favorite = cursor_dataDB_thank.getColumnIndex(InputContract.TableEntry.COL_THANK_favorite);
            while (cursor_dataDB_thank.moveToNext()) {
                if (cursor_dataDB_thank.getString(int_columnID_thank_favorite).equals("1")) {
                    arrayList_favorite.add(cursor_dataDB_thank.getString(int_columnID_thank_main));
                    adapter_favorite.notifyDataSetChanged();
                }
            }
        }
        cursor_dataDB_transfer.close();
        cursor_dataDB_phone.close();
        cursor_dataDB_balance.close();
        cursor_dataDB_history.close();
        cursor_dataDB_thank.close();
    }
}
