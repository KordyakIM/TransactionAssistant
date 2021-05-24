package ru.project.TransactionAssistant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.Settings;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
//import android.support.annotation.RequiresApi;
import androidx.annotation.RequiresApi;
//import androidx.constraintlayout.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.constraintlayout.ConstraintSet;
import androidx.constraintlayout.widget.ConstraintSet;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.NavUtils;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.content.CursorLoader;
//import android.support.v7.app.AlertDialog;
import androidx.appcompat.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
//import android.widget.ListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
//import android.widget.Toolbar;

//import ru.project.MBank.R;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.project.TransactionAssistant.DB.InputContract;
import ru.project.TransactionAssistant.DB.InputDBHelper;
import ru.project.TransactionAssistant.Favorite.Favorite_List;

public class MainActivity extends AppCompatActivity implements MessageListener {
    private static int count_list_item = 1; //для посчета выбранных пунктов в Listview
    //объект для отображения котактов
    private static final int PICK_CONTACT = 100;
    private static final int PICK_SWITCH = 1;
    private static final int PICK_FAVORITE = 2;
    private Button card, contact, send, favorite_button;
    private Button clear_text_thank, clear_text_history, clear_text_balance, clear_text_phone, clear_text_transfer, clear_money_phone, clear_money_transfer, clear_text_comment;
    //определяем автозаполнение
    EditText money_transfer, money_phone, text_thank, text_history, text_balance, text_phone, text_transfer, text_transfer_comment;
    TextView text_transfer_contact, text_phone_contact, text_transfer_contact_number, text_phone_contact_number;
    ListView output_transfer, output_phone, output_balance, output_history, output_thank; //лист вывода
    Spinner dropdown;
    AlertDialog.Builder dialog_main, dialog_confirm, dialog_info, dialog_permission, dialog_get_card, dialog_confirm1;
    AlertDialog info_alert, dialog_card_list, dialog_main_list;
    ArrayAdapter<String> adapter_card, adapter_number;
    ArrayAdapter<String> output_transfer_adapter, output_phone_adapter, output_balance_adapter, output_history_adapter, output_thank_adapter;
    ArrayList<String> arraylist_contact, search_arraylist_contact; //выгрузка и поиск контактов в справочнике
    ArrayList<String> output_transfer_arrayList, output_phone_arrayList, output_balance_arrayList, output_history_arrayList, output_thank_arrayList; //лист для выгрузки информации перевода, телефона, баланса, истории, спасибо
    ArrayList<String> list_balance, list_number;
    ActionMode action_mode; //создаем переменную для выносного меню удаления и детализации
    ProgressDialog progress_dialog;
    int count_receive_message;
    int margin_dropdown;
    int margin_rubl1;
    int margin_rubl2;
//    int colorId; //получает ID серого цвета Color.LT
    int itemCount; //количестве пунктов в list
    int int_columnID_main, int_columnID_phone, int_columnID_phone_name, int_columnID_ruble, int_columnID_comment, int_columnID_date, int_columnID_card, int_row_position, int_columnID_favorite, int_columnID_delete;
//    ColorDrawable listViewColor, qwertyy;
    Boolean boolean_check_favoriteID_and_turn_on_favoriteList = false, boolean_longclick=true;
    Boolean check_update_card_dialog = false;
    Boolean check_dialog = false;
    Boolean check_card = false;
    Boolean send_5 = false;
    Boolean check_put_button_send = false; //проверяет нажал ли пользователь поддтверждение оплаты в программе
    Boolean check_button_send = false; //проверяет нажал ли пользователь кнопку send
    Boolean isPermissionGiven_read_contacts = false;
    Boolean isPermissionGiven_read_sms = false;
    Boolean isPermissionGiven_read_sms_dropdown = false;
    //    Boolean check_equals_while = false; //проверяет совпадение в базе данных
    Date date_today;
    Menu menu;
    MenuItem menuItem;
    Intent intentContact, intent_settings, intent_about, intent_ringtone, resultIntent, intent_favorite;
    SharedPreferences sharedPreferences;
    //    Environment environment;
    Runnable runnable;
    Handler handler;
    View view;
    Vibrator vibe;
    String search_string_contact; //найденные контакты по номеру
    String message, string_phoneNumber, string_message_convert;
    String string_title_transfer, string_title_phone, string_title_balance, string_title_history, string_title_thanks;
    String[] items_dropdown, mes_name, mes_number, string_list_card;
//    String string_extra_dropdown_title, string_extra_dropdown_phone, string_extra_dropdown_phone_name, string_extra_dropdown_ruble, string_extra_dropdown_comment; //для передачи данных с favarite listview
    SparseBooleanArray sparseBooleanArray_checked; //получаем позицию выделенного пункта в листе, через boolean
    Cursor cursor_dataDB, cursor_dataDB_where_transfer, cursor_dataDB_where_phone, cursor_dataDB_where_balance, cursor_dataDB_where_history, cursor_dataDB_where_thank;
    SmsManager smsManager;
    ConstraintSet constraintSet;
    ConstraintLayout constraintLayout;
    ImageView ic_rubl;
    NotificationManager notificationManager;
    Toolbar toolbar;
    private InputDBHelper InputDB; //Адаптируем MainActivity для хранения данных в базе данных.
    //    private Favorite_Adapter mAdapter;
    private final int REQUEST_CODE_PERMISSION = 1;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT_card = "shared_card";
    public static final String CHECK_card = "boolean";
    public static final String TEXT_autoCompleteTextView1_transfer = "shared_autoCompleteTextView1_transfer";
    public static final String TEXT_text_transfer_contact = "shared_text_transfer_contact";
    public static final String TEXT_text_transfer_contact_number = "shared_text_transfer_contact_number";
    public static final String TEXT_text_transfer_comment = "shared_text_transfer_comment";
    public static final String TEXT_autoCompleteTextView1_phone = "shared_autoCompleteTextView1_phone";
    public static final String TEXT_text_phone_contact = "shared_text_phone_contact";
    public static final String TEXT_text_phone_contact_number = "shared_text_phone_contact_number";
    public static final String TEXT_money_transfer = "shared_money_transfer";
    public static final String TEXT_money_phone = "shared_money_phone";
    public static final String TEXT_autoCompleteTextView2_balance = "shared_autoCompleteTextView2_balance";
    public static final String TEXT_autoCompleteTextView2_history = "shared_autoCompleteTextView2_history";
    public static final String TEXT_autoCompleteTextView2_thank = "shared_autoCompleteTextView2_thank";
//    public static final String TEXT_output_transfer = "shared_output_transfer";
//    public static final String TEXT_output_phone = "shared_output_phone";
//    public static final String TEXT_output_balance = "shared_output_balance";
//    public static final String TEXT_output_history = "shared_output_history";
//    public static final String TEXT_output_thank = "shared_output_thank";
    public static final String TEXT_dropdown = "shared_dropdown";
    public static final String save_switch_text = "switch_text";
    public static final String save_switch_vibrate = "switch_vibrate";
    public static final String save_switch_sound = "switch_sound";
    public static final String string_extra_dropdown_title = "extra_dropdown_title";
    public static final String string_extra_dropdown_phone = "extra_dropdown_phone";
    public static final String string_extra_dropdown_phone_name = "extra_dropdown_phone_name";
    public static final String string_extra_dropdown_ruble = "extra_dropdown_ruble";
    public static final String string_extra_dropdown_comment = "extra_dropdown_comment";
    public static final String string_extra_dropdown_card = "extra_dropdown_card";
//    Bundle switch_text_bundle, switch_vibrate_bundle;
    Boolean switch_boolean_text, switch_boolean_vibrate, switch_boolean_sound, check_color, check_equals_while, check_equals_while_favorite;
    NotificationChannel mChannel;
    PendingIntent resultPendingIntent;

    @SuppressLint("WrongViewCast")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_main);
        constraintLayout = findViewById(R.id.activity_main);
        //определяем все объекты на страничке
        constraintSet = new ConstraintSet();
        //определяем данные DB
        InputDB = new InputDBHelper(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list_number = new ArrayList<>();
        adapter_number = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, list_number); //подвязываем обновление списка телефонных номеров, когда выбираем в справочнике
        arraylist_contact = new ArrayList<String>();  //выгрузка и поиск контактов в справочнике
        intent_settings = new Intent(this, SetActivity.class);
        intent_about = new Intent(this, AboutProgram.class);
        intent_favorite = new Intent(this, Favorite_List.class);
        smsManager = SmsManager.getDefault();
        margin_dropdown = 50;
        margin_rubl1 = 32;
        margin_rubl2 = 0;
        count_receive_message = 0;
        handler = new Handler(); //таймер прогресс бара
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); //описываем вибрацию
        items_dropdown = new String[]{"Перевод ", "Телефон ", "Баланс ", "История ", "Спасибо "};
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        intentContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); //вызов меню контактов
        intent_ringtone = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER); //вызов меню рингтонов
        dialog_main = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        dialog_get_card = new AlertDialog.Builder(this, R.style.AlertDialogStyle); // диалог билдер для получения списка карт
        dialog_confirm = new AlertDialog.Builder(this, R.style.AlertDialogStyle); // диалог билдер для подтверждения переводов и оплаты и для получения списка карт
        dialog_info = new AlertDialog.Builder(this, R.style.AlertDialogStyle); //диалог описывает возможные опции перевода
        dialog_permission = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        progress_dialog = new ProgressDialog(this, R.style.AlertDialogStyle);
        contact = findViewById(R.id.contact);
        card = findViewById(R.id.card);
        clear_text_thank = findViewById(R.id.clear_text_thank);
        clear_text_history = findViewById(R.id.clear_text_history);
        clear_text_balance = findViewById(R.id.clear_text_balance);
        clear_text_phone = findViewById(R.id.clear_text_phone);
        clear_text_transfer = findViewById(R.id.clear_text_transfer);
        clear_money_phone = findViewById(R.id.clear_money_phone);
        clear_money_transfer = findViewById(R.id.clear_money_transfer);
        clear_text_comment = findViewById(R.id.clear_text_comment);
        send = findViewById(R.id.send);
        dropdown = findViewById(R.id.dropdown);
        money_transfer = findViewById(R.id.money_transfer);
        money_phone = findViewById(R.id.money_phone);
        text_thank = findViewById(R.id.text_thank);
        text_history = findViewById(R.id.text_history);
        text_balance = findViewById(R.id.text_balance);
        text_phone = findViewById(R.id.text_phone);
        text_phone_contact = findViewById(R.id.text_phone_contact);
        text_phone_contact.setMovementMethod(new ScrollingMovementMethod()); //возможность двигать скролом в textview
        text_transfer = findViewById(R.id.text_transfer);
        text_transfer_contact = findViewById(R.id.text_transfer_contact);
        text_transfer_contact.setMovementMethod(new ScrollingMovementMethod()); //возможность двигать скролом в textview
        text_transfer_contact_number = findViewById(R.id.text_transfer_contact_number);
        text_phone_contact_number = findViewById(R.id.text_phone_contact_number);
        text_transfer_comment = findViewById(R.id.text_transfer_comment);
        ic_rubl = findViewById(R.id.ic_rubl);
        //загаловк для каждой таблице
        string_title_transfer = "Перевод ";
        string_title_phone = "Телефон ";
        string_title_balance = "Баланс ";
        string_title_history = "История ";
        string_title_thanks = "Спасибо ";
        //находим объект автозаполнения
        output_transfer = findViewById(R.id.output_transfer);
        output_phone = findViewById(R.id.output_phone);
        output_balance = findViewById(R.id.output_balance);
        output_history = findViewById(R.id.output_history);
        output_thank = findViewById(R.id.output_thank);
        //лист для выводов
        output_transfer_arrayList = new ArrayList<String>();
        output_phone_arrayList = new ArrayList<String>();
        output_balance_arrayList = new ArrayList<String>();
        output_history_arrayList = new ArrayList<String>();
        output_thank_arrayList = new ArrayList<String>();
        output_transfer_adapter = new FavoriteAdapter(this, output_transfer_arrayList);
        output_phone_adapter = new FavoriteAdapter(this, output_phone_arrayList);
        output_balance_adapter = new FavoriteAdapter(this, output_balance_arrayList);
        output_history_adapter = new FavoriteAdapter(this, output_history_arrayList);
        output_thank_adapter = new FavoriteAdapter(this, output_thank_arrayList);
        // Привязываем массив через адаптер к ListView
        output_transfer.setAdapter(output_transfer_adapter);
        output_phone.setAdapter(output_phone_adapter);
        output_balance.setAdapter(output_balance_adapter);
        output_history.setAdapter(output_history_adapter);
        output_thank.setAdapter(output_thank_adapter);
        // Create PendingIntent
        resultIntent = new Intent(this, MainActivity.class);
        resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        MessageReceiver.bindListener(MainActivity.this);
        loadData_card();
        loadData_transfer();
        loadData_phone();
        loadData_balance();
        loadData_history();
        loadData_thank();
        //get the spinner from the xml.
        dropdown();
        loadData_dropdown();
        load_switch_sound();
        load_switch_text();
        load_switch_vibrate();
        closeKeyboard();
        LoadData_output_Cash(); //выгрузка сохраненного кэша из таблиц базы данны SQLite

        //проверяет наличие текста и включает кнопку clear
        text_transfer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (text_transfer.getText().toString().isEmpty()) {
                    clear_text_transfer.setVisibility(View.INVISIBLE);
                    send.setEnabled(false);
                } else {
                    clear_text_transfer.setVisibility(View.VISIBLE);
                    if (!money_transfer.getText().toString().isEmpty()) {
                        send.setEnabled(true);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (text_transfer.getText().toString().isEmpty()) {
                    clear_text_transfer.setVisibility(View.INVISIBLE);
                    send.setEnabled(false);
                    text_transfer_contact.setText(null);
                    text_transfer_contact_number.setText(null);
                } else {
                    clear_text_transfer.setVisibility(View.VISIBLE);
                    if (!money_transfer.getText().toString().isEmpty()) {
                        send.setEnabled(true);
                    }
                    if ((ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) && (arraylist_contact.size() > 0)) {
                        //настройка поиска контакта в справочнике
                        search_arraylist_contact = new ArrayList<String>();
                        String text = text_transfer.getText().toString().replaceAll("[^\\d.]", "");
                        for (String temp : arraylist_contact) {
                            if (temp.matches("(?is)" + ".*" + Pattern.quote(text) + ".*")) {
                                search_arraylist_contact.add(temp);
                            }
                        }
                        search_string_contact = "";
                        for (String temp : search_arraylist_contact) {
                            if (search_string_contact.toString().isEmpty()) {
                                search_string_contact += temp.replaceAll("[$$$$].*", "");
                            } else {
                                search_string_contact += "\n" + temp.replaceAll("[$$$$].*", "");
                            }
                        }
                        text_transfer_contact.setText(search_string_contact);
                        if (text_transfer_contact.getText().toString().isEmpty()) {
                            text_transfer_contact_number.setText(null);
                        } else {
                            int count = search_string_contact.split("[\n]").length; //подсчитывает количество строк в области найденных контактов
                            text_transfer_contact_number.setText(String.valueOf(count));
                        }
                        //---------------------------------------
                    }
                }
            }
        });
        text_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (text_phone.getText().toString().isEmpty()) {
                    clear_text_phone.setVisibility(View.INVISIBLE);
                    send.setEnabled(false);
                } else {
                    clear_text_phone.setVisibility(View.VISIBLE);
                    if (!money_phone.getText().toString().isEmpty()) {
                        send.setEnabled(true);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (text_phone.getText().toString().isEmpty()) {
                    clear_text_phone.setVisibility(View.INVISIBLE);
                    send.setEnabled(false);
                    text_phone_contact.setText(null);
                    text_phone_contact_number.setText(null);
                } else {
                    clear_text_phone.setVisibility(View.VISIBLE);
                    if (!money_phone.getText().toString().isEmpty()) {
                        send.setEnabled(true);
                    }
//                        //настройка поиска контакта в справочнике
                    if ((ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) && (arraylist_contact.size() > 0)) {
                        search_arraylist_contact = new ArrayList<String>();
                        String text = text_phone.getText().toString().replaceAll("[^\\d.]", "");
                        for (String temp : arraylist_contact) {
                            if (temp.matches("(?is)" + ".*" + Pattern.quote(text) + ".*")) {
                                search_arraylist_contact.add(temp);
                            }
                        }
                        search_string_contact = "";
                        for (String temp : search_arraylist_contact) {
                            if (search_string_contact.toString().isEmpty()) {
                                search_string_contact += temp.replaceAll("[$$$$].*", "");
                            } else {
                                search_string_contact += "\n" + temp.replaceAll("[$$$$].*", "");
                            }
                        }
                        text_phone_contact.setText(search_string_contact);
                        if (text_phone_contact.getText().toString().isEmpty()) {
                            text_phone_contact_number.setText(null);
                        } else {
                            int count = search_string_contact.split("[\n]").length; //подсчитывает количество строк в области найденных контактов
                            text_phone_contact_number.setText(String.valueOf(count));
                        }
                        //---------------------------------------
                    }
                }

            }
        });
        //проверяет наличие текста и включает кнопку clear
        money_transfer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (money_transfer.getText().toString().isEmpty()) {
                    clear_money_transfer.setVisibility(View.INVISIBLE);
                    send.setEnabled(false);
                } else {
                    if (!text_transfer.getText().toString().isEmpty()) {
                        send.setEnabled(true);
                    }
                    clear_money_transfer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (money_transfer.getText().toString().isEmpty()) {
                    clear_money_transfer.setVisibility(View.INVISIBLE);
                    send.setEnabled(false);
                    constraintSet.clone(constraintLayout);
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.connect(R.id.ic_rubl, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin_rubl1);
                    constraintSet.applyTo(constraintLayout);
                } else {
                    if (!text_transfer.getText().toString().isEmpty()) {
                        send.setEnabled(true);
                    }
                    constraintSet.clone(constraintLayout);
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.connect(R.id.ic_rubl, ConstraintSet.END, R.id.clear_money_transfer, ConstraintSet.START, margin_rubl2);
                    constraintSet.applyTo(constraintLayout);
                    clear_money_transfer.setVisibility(View.VISIBLE);
                }
            }
        });
        //проверяет наличие текста и включает кнопку clear
        text_transfer_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (text_transfer_comment.getText().toString().isEmpty()) {
                    clear_text_comment.setVisibility(View.INVISIBLE);
//                    send.setEnabled(false);
                } else {
                    clear_text_comment.setVisibility(View.VISIBLE);
                    if (!text_transfer.getText().toString().isEmpty() && !money_transfer.getText().toString().isEmpty()) {
                        send.setEnabled(true);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (text_transfer_comment.getText().toString().isEmpty()) {
                    clear_text_comment.setVisibility(View.INVISIBLE);
//                    send.setEnabled(false);
                } else {
                    clear_text_comment.setVisibility(View.VISIBLE);
                    if (!text_transfer.getText().toString().isEmpty() && !money_transfer.getText().toString().isEmpty()) {
                        send.setEnabled(true);
                    }
                }
            }
        });
        money_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (money_phone.getText().toString().isEmpty()) {
                    clear_money_phone.setVisibility(View.INVISIBLE);
                    send.setEnabled(false);
                } else {
                    clear_money_phone.setVisibility(View.VISIBLE);
                    if (!text_phone.getText().toString().isEmpty()) {
                        send.setEnabled(true);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (money_phone.getText().toString().isEmpty()) {
                    clear_money_phone.setVisibility(View.INVISIBLE);
                    send.setEnabled(false);
                    constraintSet.clone(constraintLayout);
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.connect(R.id.ic_rubl, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin_rubl1);
                    constraintSet.applyTo(constraintLayout);
                } else {
                    if (!text_phone.getText().toString().isEmpty()) {
                        send.setEnabled(true);
                    }
//                        if (money_phone.getText().length()==1) {
                    constraintSet.clone(constraintLayout);
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.connect(R.id.ic_rubl, ConstraintSet.END, R.id.clear_money_phone, ConstraintSet.START, margin_rubl2);
                    constraintSet.applyTo(constraintLayout);
//                        }
                    clear_money_phone.setVisibility(View.VISIBLE);
                }
            }
        });
        text_balance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (text_balance.getText().toString().isEmpty()) {
                    clear_text_balance.setVisibility(View.INVISIBLE);
                } else {
                    clear_text_balance.setVisibility(View.VISIBLE);
                }
                if (text_balance.getText().toString().isEmpty() && dropdown.getSelectedItem().toString().contains("История")) {
                    send.setEnabled(false);
                } else {
                    send.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (text_balance.getText().toString().isEmpty()) {
                    clear_text_balance.setVisibility(View.INVISIBLE);
                } else {
                    clear_text_balance.setVisibility(View.VISIBLE);
                }
                if (text_balance.getText().toString().isEmpty() && dropdown.getSelectedItem().toString().contains("История")) {
                    send.setEnabled(false);
                } else {
                    send.setEnabled(true);
                }
            }
        });
        text_history.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (text_history.getText().toString().isEmpty()) {
                    clear_text_history.setVisibility(View.INVISIBLE);
                } else {
                    clear_text_history.setVisibility(View.VISIBLE);
                }
                if (text_history.getText().toString().isEmpty() && dropdown.getSelectedItem().toString().contains("История")) {
                    send.setEnabled(false);
                } else {
                    send.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (text_history.getText().toString().isEmpty()) {
                    clear_text_history.setVisibility(View.INVISIBLE);
                } else {
                    clear_text_history.setVisibility(View.VISIBLE);
                }
                if (text_history.getText().toString().isEmpty() && dropdown.getSelectedItem().toString().contains("История")) {
                    send.setEnabled(false);
                } else {
                    send.setEnabled(true);
                }
            }
        });
        text_thank.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (text_thank.getText().toString().isEmpty()) {
                    clear_text_thank.setVisibility(View.INVISIBLE);
                } else {
                    clear_text_thank.setVisibility(View.VISIBLE);
                }
                if (text_thank.getText().toString().isEmpty() && dropdown.getSelectedItem().toString().contains("История")) {
                    send.setEnabled(false);
                } else {
                    send.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (text_thank.getText().toString().isEmpty()) {
                    clear_text_thank.setVisibility(View.INVISIBLE);
                } else {
                    clear_text_thank.setVisibility(View.VISIBLE);
                }
                if (text_thank.getText().toString().isEmpty() && dropdown.getSelectedItem().toString().contains("История")) {
                    send.setEnabled(false);
                } else {
                    send.setEnabled(true);
                }
            }
        });
        //прослушивам dropdown когда нажимаем на него
        dropdown.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeKeyboard();
                if (!toolbar.getTitle().toString().equals(getString(R.string.Label))) {
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.main);
                    getSupportActionBar().setTitle(R.string.Label);
                    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                    if (dropdown.getSelectedItem().equals("Перевод ")) {
                        output_transfer_adapter = new FavoriteAdapter(MainActivity.this, output_transfer_arrayList);
                        output_transfer.setAdapter(output_transfer_adapter);
                        cursor_dataDB = InputDB.getListContents_transfer();
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext()) {
                                InputDB.update_Data_transfer_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                            }
                        }
                        cursor_dataDB.close();
                    } else if (dropdown.getSelectedItem().equals("Телефон ")) {
                        output_phone_adapter = new FavoriteAdapter(MainActivity.this, output_phone_arrayList);
                        output_phone.setAdapter(output_phone_adapter);
                        cursor_dataDB = InputDB.getListContents_phone();
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext()) {
                                InputDB.update_Data_phone_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                            }
                        }
                        cursor_dataDB.close();
                    } else if (dropdown.getSelectedItem().equals("Баланс ")) {
                        output_balance_adapter = new FavoriteAdapter(MainActivity.this, output_balance_arrayList);
                        output_balance.setAdapter(output_balance_adapter);
                        cursor_dataDB = InputDB.getListContents_balance();
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext()) {
                                InputDB.update_Data_balance_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                            }
                        }
                        cursor_dataDB.close();
                    } else if (dropdown.getSelectedItem().equals("История ")) {
                        output_history_adapter = new FavoriteAdapter(MainActivity.this, output_history_arrayList);
                        output_history.setAdapter(output_history_adapter);
                        cursor_dataDB = InputDB.getListContents_history();
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext()) {
                                InputDB.update_Data_history_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                            }
                        }
                        cursor_dataDB.close();
                    } else if (dropdown.getSelectedItem().equals("Спасибо ")) {
                        output_thank_adapter = new FavoriteAdapter(MainActivity.this, output_thank_arrayList);
                        output_thank.setAdapter(output_thank_adapter);
                        cursor_dataDB = InputDB.getListContents_thank();
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext()) {
                                InputDB.update_Data_thank_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                            }
                        }
                        cursor_dataDB.close();
                    }
                    cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                    if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                        menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                    }else {
                        menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                    }
                    cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                }
                return false;
            }
        });

//-----------------------------------------------------------------------------------------------------------------
// -----------------------------------------------------------------------------------------------------------------
        //следим за долгим нажатием на пункт листа
        output_transfer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                boolean_longclick=false;
                vibe.vibrate(10);
                output_transfer_adapter = new DeleteAdapter(MainActivity.this, position, output_transfer_arrayList);
                output_transfer.setAdapter(output_transfer_adapter);
                if (toolbar.getTitle().toString().equals(getString(R.string.Label))) {
                    view.setBackgroundColor(Color.LTGRAY);
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_template);
                    count_list_item = 1; //по умолчанию всегда должен быть один
                    getSupportActionBar().setTitle("" + count_list_item);
                    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                    Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.cardview_shadow_start_color), PorterDuff.Mode.SRC_ATOP);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.main);
                            getSupportActionBar().setTitle(R.string.Label);
                            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                            output_transfer_adapter = new FavoriteAdapter(MainActivity.this, output_transfer_arrayList);
                            output_transfer.setAdapter(output_transfer_adapter);
                            cursor_dataDB = InputDB.getListContents_transfer();
                            if (cursor_dataDB.getCount() == 0) {
                            } else {
                                while (cursor_dataDB.moveToNext()) {
                                    InputDB.update_Data_transfer_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                }
                            }
                            cursor_dataDB.close();
                            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                            if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                            }else {
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                            }
                            cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                        }
                    });
                }
                return true;
            }
        });
        output_phone.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                boolean_longclick=false;
                vibe.vibrate(10);
                output_phone_adapter = new DeleteAdapter(MainActivity.this, position, output_phone_arrayList);
                output_phone.setAdapter(output_phone_adapter);
                if (toolbar.getTitle().toString().equals(getString(R.string.Label))) {
                    view.setBackgroundColor(Color.LTGRAY);
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_template);
                    count_list_item = 1; //по умолчанию всегда должен быть один
                    getSupportActionBar().setTitle("" + count_list_item);
                    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                    Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.cardview_shadow_start_color), PorterDuff.Mode.SRC_ATOP);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.main);
                            getSupportActionBar().setTitle(R.string.Label);
                            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                            output_phone_adapter = new FavoriteAdapter(MainActivity.this, output_phone_arrayList);
                            output_phone.setAdapter(output_phone_adapter);
                            cursor_dataDB = InputDB.getListContents_phone();
                            if (cursor_dataDB.getCount() == 0) {
                            } else {
                                while (cursor_dataDB.moveToNext()) {
                                    InputDB.update_Data_phone_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                }
                            }
                            cursor_dataDB.close();
                            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                            if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                            }else {
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                            }
                            cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                        }
                    });
                }
                return true;
            }
        });
        output_balance.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                boolean_longclick=false;
                vibe.vibrate(10);
                output_balance_adapter = new DeleteAdapter(MainActivity.this, position, output_balance_arrayList);
                output_balance.setAdapter(output_balance_adapter);

                if (toolbar.getTitle().toString().equals(getString(R.string.Label))) {
                    view.setBackgroundColor(Color.LTGRAY);
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_template);
                    count_list_item = 1; //по умолчанию всегда должен быть один
                    getSupportActionBar().setTitle("" + count_list_item);
                    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                    Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.cardview_shadow_start_color), PorterDuff.Mode.SRC_ATOP);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.main);
                            getSupportActionBar().setTitle(R.string.Label);
                            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                            output_balance_adapter = new FavoriteAdapter(MainActivity.this, output_balance_arrayList);
                            output_balance.setAdapter(output_balance_adapter);
                            cursor_dataDB = InputDB.getListContents_balance();
                            if (cursor_dataDB.getCount() == 0) {
                            } else {
                                while (cursor_dataDB.moveToNext()) {
                                    InputDB.update_Data_balance_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                }
                            }
                            cursor_dataDB.close();
                            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                            if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                            }else {
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                            }
                            cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                        }
                    });
                }
                return true;
            }
        });
        output_history.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                boolean_longclick=false;
                vibe.vibrate(10);
                output_history_adapter = new DeleteAdapter(MainActivity.this, position, output_history_arrayList);
                output_history.setAdapter(output_history_adapter);

                if (toolbar.getTitle().toString().equals(getString(R.string.Label))) {
                    view.setBackgroundColor(Color.LTGRAY);
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_template);
                    count_list_item = 1; //по умолчанию всегда должен быть один
                    getSupportActionBar().setTitle("" + count_list_item);
                    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                    Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.cardview_shadow_start_color), PorterDuff.Mode.SRC_ATOP);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.main);
                            getSupportActionBar().setTitle(R.string.Label);
                            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                            output_history_adapter = new FavoriteAdapter(MainActivity.this, output_history_arrayList);
                            output_history.setAdapter(output_history_adapter);
                            cursor_dataDB = InputDB.getListContents_history();
                            if (cursor_dataDB.getCount() == 0) {
                            } else {
                                while (cursor_dataDB.moveToNext()) {
                                    InputDB.update_Data_history_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                }
                            }
                            cursor_dataDB.close();
                            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                            if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                            }else {
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                            }
                            cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                        }
                    });
                }
                return true;
            }
        });
        output_thank.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                boolean_longclick=false;
                vibe.vibrate(10);
                output_thank_adapter = new DeleteAdapter(MainActivity.this, position, output_transfer_arrayList);
                output_thank.setAdapter(output_thank_adapter);
                if (toolbar.getTitle().toString().equals(getString(R.string.Label))) {
                    view.setBackgroundColor(Color.LTGRAY);
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_template);
                    count_list_item = 1; //по умолчанию всегда должен быть один
                    getSupportActionBar().setTitle("" + count_list_item);
                    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                    Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.cardview_shadow_start_color), PorterDuff.Mode.SRC_ATOP);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.main);
                            getSupportActionBar().setTitle(R.string.Label);
                            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                            output_thank_adapter = new FavoriteAdapter(MainActivity.this, output_thank_arrayList);
                            output_thank.setAdapter(output_transfer_adapter);
                            cursor_dataDB = InputDB.getListContents_thank();
                            if (cursor_dataDB.getCount() == 0) {
                            } else {
                                while (cursor_dataDB.moveToNext()) {
                                    InputDB.update_Data_transfer_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                }
                            }
                            cursor_dataDB.close();
                            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                            if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                            }else {
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                            }
                            cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                        }
                    });
                }
                return true;
            }
        });
//--------------------------------------------------------------------------------------------
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if (extras.getString(string_extra_dropdown_title).equals(string_title_transfer)) {
                dropdown.setSelection(0, true);
                text_transfer.setText(extras.getString(string_extra_dropdown_phone));
                text_transfer_contact.setText(extras.getString(string_extra_dropdown_phone_name));
                money_transfer.setText(extras.getString(string_extra_dropdown_ruble));
                text_transfer_comment.setText(extras.getString(string_extra_dropdown_comment));
            } else if (extras.getString(string_extra_dropdown_title).equals(string_title_phone)) {
                dropdown.setSelection(1, true);
                text_phone.setText(extras.getString(string_extra_dropdown_phone));
                text_phone_contact.setText(extras.getString(string_extra_dropdown_phone_name));
                money_phone.setText(extras.getString(string_extra_dropdown_ruble));
            } else if (extras.getString(string_extra_dropdown_title).equals(string_title_balance)) {
                dropdown.setSelection(2, true);
                text_balance.setText(extras.getString(string_extra_dropdown_card));
            } else if (extras.getString(string_extra_dropdown_title).equals(string_title_history)) {
                dropdown.setSelection(3, true);
                text_history.setText(extras.getString(string_extra_dropdown_card));
            } else if (extras.getString(string_extra_dropdown_title).equals(string_title_thanks)) {
                dropdown.setSelection(4, true);
                text_thank.setText(extras.getString(string_extra_dropdown_card));
            }
        }
        //спрашиваем разрешения читать список контаков
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            //запрос контактов
            get_contact_List();
        }
    }//onCreate
    //получение справочника контактов
    public void get_contact_List() {
        //создаем объект массива - list
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String phoneNo1 = phoneNo.replaceAll("[^\\d.]", "");
                        if (!arraylist_contact.toString().contains(phoneNo1)) {
                            arraylist_contact.add(name + "$$$$" + phoneNo1);
                        }
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
    }
    //определяем шаблон main menu
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.menu = menu; //определяем кнопки меню для всего activity_main
//        Log.d("My_log", "Menu ");
        getMenuInflater().inflate(R.menu.main, menu);
        if(boolean_check_favoriteID_and_turn_on_favoriteList) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
        }
        return super.onCreateOptionsMenu(menu);
    }

    //создаем метод прослушивания main menu
    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        this.menuItem = item;
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.item_settings:
                startActivityForResult(intent_settings, PICK_SWITCH);
                return true;
            case R.id.item_info_aboute:
                startActivity(intent_about);
                return true;
            case R.id.item_favorite:
                cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                if(cursor_dataDB_where_transfer.getCount() != 0 || cursor_dataDB_where_phone.getCount() != 0 || cursor_dataDB_where_balance.getCount() != 0 || cursor_dataDB_where_history.getCount() != 0 || cursor_dataDB_where_thank.getCount() != 0 ) {
                    startActivityForResult(intent_favorite, PICK_FAVORITE);
                }
                cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                return true;
            case R.id.template:
                if (dropdown.getSelectedItem().equals("Перевод ")) {
                            cursor_dataDB = InputDB.getListContents_transfer(); //Cursor
                            if (cursor_dataDB.getCount() == 0) {
                            } else {
                                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_main);
                                int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_delete);
                                int_columnID_phone = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_number);
                                int_columnID_phone_name = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_number_name);
                                int_columnID_ruble = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_ruble);
                                int_columnID_comment = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_comment);
                                int_columnID_date = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_date);
                                check_equals_while = true;
                                while (cursor_dataDB.moveToNext() && check_equals_while) {
                                    if (cursor_dataDB.getString(int_columnID_delete).equals("1")) {
                                        check_equals_while = false;
                                        int_row_position = cursor_dataDB.getPosition();
                                        dialog_confirm = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle);
                                        dialog_confirm.setMessage("Номер: " + cursor_dataDB.getString(int_columnID_phone) + "\n" + "Имя: " + cursor_dataDB.getString(int_columnID_phone_name) +
                                                "\n" + "Сумма: " + cursor_dataDB.getString(int_columnID_ruble) + "\n" + "Комментарий: " + cursor_dataDB.getString(int_columnID_comment) +
                                                "\n" + "Дата: " + cursor_dataDB.getString(int_columnID_date))
                                                .setCancelable(false)
                                                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        cursor_dataDB.moveToPosition(int_row_position);
                                                        text_transfer.setText(cursor_dataDB.getString(int_columnID_phone));
                                                        money_transfer.setText(cursor_dataDB.getString(int_columnID_ruble));
                                                        text_transfer_comment.setText(cursor_dataDB.getString(int_columnID_comment));
                                                        cursor_dataDB.close();
                                                        dialog.cancel();
                                                        closeKeyboard(); //убираем клавиатуру, чтоб не мешала
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
                                    }
                                }
                            }
                } else if (dropdown.getSelectedItem().equals("Телефон ")) {
                            cursor_dataDB = InputDB.getListContents_phone(); //Cursor
                            if (cursor_dataDB.getCount() == 0) {
                            } else {
                                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_main);
                                int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_delete);
                                int_columnID_phone = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_number);
                                int_columnID_phone_name = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_number_name);
                                int_columnID_ruble = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_ruble);
                                int_columnID_date = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_date);
                                check_equals_while = true;
                                while (cursor_dataDB.moveToNext() && check_equals_while) { //&& (output_transfer_arrayList.get(i) == dataDB_transfer.getString(idx))) {
                                    if (cursor_dataDB.getString(int_columnID_delete).equals("1")) {
                                        check_equals_while = false;
                                        int_row_position = cursor_dataDB.getPosition();
                                        dialog_confirm = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle);
                                        dialog_confirm.setMessage("Телефон: " + cursor_dataDB.getString(int_columnID_phone) + "\n" + "Имя: " + cursor_dataDB.getString(int_columnID_phone_name) +
                                                "\n" + "Сумма: " + cursor_dataDB.getString(int_columnID_ruble) + "\n" + "Дата: " + cursor_dataDB.getString(int_columnID_date))
                                                .setCancelable(false)
                                                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        cursor_dataDB.moveToPosition(int_row_position);
                                                        text_phone.setText(cursor_dataDB.getString(int_columnID_phone));
                                                        money_phone.setText(cursor_dataDB.getString(int_columnID_ruble));
                                                        cursor_dataDB.close();
                                                        dialog.cancel();
                                                        closeKeyboard(); //убираем клавиатуру, чтоб не мешала
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
                                    }
                                }
                            }
                } else if (dropdown.getSelectedItem().equals("Баланс ")) {
                            cursor_dataDB = InputDB.getListContents_balance(); //Cursor
                            if (cursor_dataDB.getCount() == 0) {
                            } else {
                                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_main);
                                int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_delete);
                                int_columnID_card = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_card);
                                int_columnID_date = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_date);
                                check_equals_while = true;
                                while (cursor_dataDB.moveToNext() && check_equals_while) { //&& (output_transfer_arrayList.get(i) == dataDB_transfer.getString(idx))) {
                                    if (cursor_dataDB.getString(int_columnID_delete).equals("1")) {
                                        check_equals_while = false;
                                        dialog_info.setMessage("Карта: " + cursor_dataDB.getString(int_columnID_card) + "\n" + "Дата: " + cursor_dataDB.getString(int_columnID_date));
                                        info_alert = dialog_info.create();
                                        info_alert.setTitle("Детали");
                                        info_alert.show();
                                    }
                                }
                            }
                } else if (dropdown.getSelectedItem().equals("История ")) {
                            cursor_dataDB = InputDB.getListContents_history(); //Cursor
                            if (cursor_dataDB.getCount() == 0) {
                            } else {
                                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_main);
                                int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_delete);
                                int_columnID_card = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_card);
                                int_columnID_date = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_date);
                                check_equals_while = true;
                                while (cursor_dataDB.moveToNext() && check_equals_while) { //&& (output_transfer_arrayList.get(i) == dataDB_transfer.getString(idx))) {
                                    if (cursor_dataDB.getString(int_columnID_delete).equals("1")) {
                                        check_equals_while = false;
                                        dialog_info.setMessage("Карта: " + cursor_dataDB.getString(int_columnID_card) + "\n" + "Дата: " + cursor_dataDB.getString(int_columnID_date));
                                        info_alert = dialog_info.create();
                                        info_alert.setTitle("Детали");
                                        info_alert.show();
                                    }
                                }
                            }
                } else if (dropdown.getSelectedItem().equals("Спасибо ")) {
                            cursor_dataDB = InputDB.getListContents_thank(); //Cursor
                            if (cursor_dataDB.getCount() == 0) {
                            } else {
                                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_main);
                                int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_delete);
                                int_columnID_card = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_card);
                                int_columnID_date = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_date);
                                check_equals_while = true;
                                while (cursor_dataDB.moveToNext() && check_equals_while) { //&& (output_transfer_arrayList.get(i) == dataDB_transfer.getString(idx))) {
                                    if (cursor_dataDB.getString(int_columnID_delete).equals("1")) {
                                        check_equals_while = false;
                                        dialog_info.setMessage("Карта: " + cursor_dataDB.getString(int_columnID_card) + "\n" + "Дата: " + cursor_dataDB.getString(int_columnID_date));
                                        info_alert = dialog_info.create();
                                        info_alert.setTitle("Детали");
                                        info_alert.show();
                                    }
                                }
                            }
                }
                return true;
            case R.id.delete:
                if (dropdown.getSelectedItem().equals("Перевод ")) {
                    dialog_confirm = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                    dialog_confirm.setMessage("Удалить?")
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    itemCount = output_transfer_adapter.getCount();
                                    for (int i = itemCount - 1; i >= 0; i--) { //данный метод реализцется если ты хочешь выполнить удаление, т.к. при удалении пункт список сдвигается вверх на один пункт
                                        check_equals_while = true;
                                        cursor_dataDB = InputDB.getListContents_transfer();
                                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_main);
                                        int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_delete);
                                        if (cursor_dataDB.getCount() == 0) {
                                        } else {
                                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                                if (cursor_dataDB.getString(int_columnID_delete).equals("1")) {
                                                    if(output_transfer_arrayList.get(i).equals(cursor_dataDB.getString(int_columnID_main))) {
                                                        check_equals_while = false;
                                                        output_transfer_adapter.remove(output_transfer_arrayList.get(i));
                                                        InputDB.delete_Data_transfer(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                                        //появление основного меню с проверкой favorite list
                                                        toolbar.getMenu().clear();
                                                        toolbar.inflateMenu(R.menu.main);
                                                        getSupportActionBar().setTitle(R.string.Label);
                                                        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                                                        output_transfer_adapter = new FavoriteAdapter(MainActivity.this, output_transfer_arrayList);
                                                        output_transfer.setAdapter(output_transfer_adapter);
                                                        //проверка favorite list
                                                        cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                                                        if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                                                            menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                                                        }else{
                                                            menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                                                        }
                                                        cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                                                    }
                                                }
                                            }
                                            cursor_dataDB.close();
                                        }
                                    }
                                    output_transfer_adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = dialog_confirm.create();
                    alert.show();
                } else if (dropdown.getSelectedItem().equals("Телефон ")) {
                    dialog_confirm = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                    dialog_confirm.setMessage("Удалить?")
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    itemCount = output_phone_adapter.getCount();
                                    for (int i = itemCount - 1; i >= 0; i--) { //данный метод реализцется если ты хочешь выполнить удаление, т.к. при удалении пункт список сдвигается вверх на один пункт
                                        check_equals_while = true;
                                        cursor_dataDB = InputDB.getListContents_phone();
                                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_main);
                                        int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_delete);
                                        if (cursor_dataDB.getCount() == 0) {
                                        } else {
                                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                                if (cursor_dataDB.getString(int_columnID_delete).equals("1")) {
                                                    if(output_phone_arrayList.get(i).equals(cursor_dataDB.getString(int_columnID_main))) {
                                                        check_equals_while = false;
                                                        output_phone_adapter.remove(output_phone_arrayList.get(i));
                                                        InputDB.delete_Data_phone(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                                        //появление основного меню с проверкой favorite list
                                                        toolbar.getMenu().clear();
                                                        toolbar.inflateMenu(R.menu.main);
                                                        getSupportActionBar().setTitle(R.string.Label);
                                                        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                                                        output_phone_adapter = new FavoriteAdapter(MainActivity.this, output_phone_arrayList);
                                                        output_phone.setAdapter(output_phone_adapter);
                                                        //проверка favorite list
                                                        cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                                                        if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                                                            menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                                                        }else{
                                                            menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                                                        }
                                                        cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                                                    }
                                                }
                                            }
                                            cursor_dataDB.close();
                                        }
                                    }
                                    output_phone_adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = dialog_confirm.create();
                    alert.show();
                } else if (dropdown.getSelectedItem().equals("Баланс ")) {
                    dialog_confirm = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                    dialog_confirm.setMessage("Удалить?")
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    itemCount = output_balance_adapter.getCount();
                                    for (int i = itemCount - 1; i >= 0; i--) { //данный метод реализцется если ты хочешь выполнить удаление, т.к. при удалении пункт список сдвигается вверх на один пункт
                                        check_equals_while = true;
                                        cursor_dataDB = InputDB.getListContents_balance();
                                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_main);
                                        int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_delete);
                                        if (cursor_dataDB.getCount() == 0) {
                                        } else {
                                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                                if (cursor_dataDB.getString(int_columnID_delete).equals("1")) {
                                                    if(output_balance_arrayList.get(i).equals(cursor_dataDB.getString(int_columnID_main))) {
                                                        check_equals_while = false;
                                                        output_balance_adapter.remove(output_balance_arrayList.get(i));
                                                        InputDB.delete_Data_balance(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                                        //появление основного меню с проверкой favorite list
                                                        toolbar.getMenu().clear();
                                                        toolbar.inflateMenu(R.menu.main);
                                                        getSupportActionBar().setTitle(R.string.Label);
                                                        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                                                        output_balance_adapter = new FavoriteAdapter(MainActivity.this, output_balance_arrayList);
                                                        output_balance.setAdapter(output_balance_adapter);
                                                        //проверка favorite list
                                                        cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                                                        if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                                                            menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                                                        }else{
                                                            menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                                                        }
                                                        cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                                                    }
                                                }
                                            }
                                            cursor_dataDB.close();
                                        }
                                    }
                                    output_balance_adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = dialog_confirm.create();
                    alert.show();
                } else if (dropdown.getSelectedItem().equals("История ")) {
                    dialog_confirm = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                    dialog_confirm.setMessage("Удалить?")
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    itemCount = output_history_adapter.getCount();
                                    for (int i = itemCount - 1; i >= 0; i--) { //данный метод реализцется если ты хочешь выполнить удаление, т.к. при удалении пункт список сдвигается вверх на один пункт
                                        check_equals_while = true;
                                        cursor_dataDB = InputDB.getListContents_history();
                                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_main);
                                        int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_delete);
                                        if (cursor_dataDB.getCount() == 0) {
                                        } else {
                                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                                if (cursor_dataDB.getString(int_columnID_delete).equals("1")) {
                                                    if(output_history_arrayList.get(i).equals(cursor_dataDB.getString(int_columnID_main))) {
                                                        check_equals_while = false;
                                                        output_history_adapter.remove(output_history_arrayList.get(i));
                                                        InputDB.delete_Data_history(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                                        //появление основного меню с проверкой favorite list
                                                        toolbar.getMenu().clear();
                                                        toolbar.inflateMenu(R.menu.main);
                                                        getSupportActionBar().setTitle(R.string.Label);
                                                        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                                                        output_history_adapter = new FavoriteAdapter(MainActivity.this, output_history_arrayList);
                                                        output_history.setAdapter(output_history_adapter);
                                                        //проверка favorite list
                                                        cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                                                        if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                                                            menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                                                        }else{
                                                            menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                                                        }
                                                        cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                                                    }
                                                }
                                            }
                                            cursor_dataDB.close();
                                        }
                                    }
                                    output_history_adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = dialog_confirm.create();
                    alert.show();
                } else if (dropdown.getSelectedItem().equals("Спасибо ")) {
                    dialog_confirm = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                    dialog_confirm.setMessage("Удалить?")
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    itemCount = output_thank_adapter.getCount();
                                    for (int i = itemCount - 1; i >= 0; i--) { //данный метод реализцется если ты хочешь выполнить удаление, т.к. при удалении пункт список сдвигается вверх на один пункт
                                        check_equals_while = true;
                                        cursor_dataDB = InputDB.getListContents_thank();
                                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_main);
                                        int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_delete);
                                        if (cursor_dataDB.getCount() == 0) {
                                        } else {
                                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                                if (cursor_dataDB.getString(int_columnID_delete).equals("1")) {
                                                    if(output_thank_arrayList.get(i).equals(cursor_dataDB.getString(int_columnID_main))) {
                                                        check_equals_while = false;
                                                        output_thank_adapter.remove(output_thank_arrayList.get(i));
                                                        InputDB.delete_Data_thanks(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                                        //появление основного меню с проверкой favorite list
                                                        toolbar.getMenu().clear();
                                                        toolbar.inflateMenu(R.menu.main);
                                                        getSupportActionBar().setTitle(R.string.Label);
                                                        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                                                        output_thank_adapter = new FavoriteAdapter(MainActivity.this, output_thank_arrayList);
                                                        output_thank.setAdapter(output_thank_adapter);
                                                        //проверка favorite list
                                                        cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                                                        if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                                                            menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                                                        }else{
                                                            menu.getItem(0).setIcon(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                                                        }
                                                        cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                                                    }
                                                }
                                            }
                                            cursor_dataDB.close();
                                        }
                                    }
                                    output_thank_adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = dialog_confirm.create();
                    alert.show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data == null) {return;}
//        switch_text_boolean = data.getBooleanExtra("switchOnOff_text");
////        tvName.setText("Your name is " + name);
//    }
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    //это уведомления, которые пользователь видит в верхней части экрана, когда ему приходит новое письмо, сообщение, обновление и т.п.
    @SuppressLint("MissingPermission")
    public void notification(final String message) {
        if (switch_boolean_text) {
            Random random = new Random();
            int notifyID = random.nextInt(9999 - 1000) + 1000;
            if (dropdown.getSelectedItem().equals("Перевод ")) {
                String CHANNEL_TRANSFER = "channel_transfer";// The id of the channel.
                CharSequence name = getString(R.string.Label);// The user-visible name of the channel.
                int importance = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    importance = NotificationManager.IMPORTANCE_HIGH;
                }
//                NotificationChannel mChannel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(CHANNEL_TRANSFER, name, importance);
                }
//                if(mChannel.toString().equals("")) {
//                    NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle("You");
//                    messagingStyle.setConversationTitle("Android chat")
//                            .addMessage(message, System.currentTimeMillis(), "900");
                Notification builder = new NotificationCompat.Builder(this, CHANNEL_TRANSFER)
                        .setSmallIcon(R.drawable.ic_transfer)
                        .setContentTitle(dropdown.getSelectedItem().toString())
//                        .setContentText(output_transfer.getText().toString())
                        .setContentText(message)
                        .setContentIntent(resultPendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
//                            .setStyle(messagingStyle)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_mobilebank))
                        .setAutoCancel(true)
                        .setChannelId(CHANNEL_TRANSFER).build();
                builder.flags |= Notification.FLAG_AUTO_CANCEL; //PendingIntent.FLAG_ONE_SHOT
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(mChannel);
                }
                notificationManager.notify(notifyID, builder);
//                }
//                notifyID++;
            } else if (dropdown.getSelectedItem().equals("Телефон ")) {
                String CHANNEL_PHONE = "channel_phone";// The id of the channel.
                CharSequence name = getString(R.string.Label);// The user-visible name of the channel.
                int importance = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    importance = NotificationManager.IMPORTANCE_HIGH;
                }
//                NotificationChannel mChannel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(CHANNEL_PHONE, name, importance);
                }
//                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification builder = new NotificationCompat.Builder(this, CHANNEL_PHONE)
                        .setSmallIcon(R.drawable.ic_phone)
                        .setContentTitle(dropdown.getSelectedItem().toString())
                        //.setContentText(output_phone.getText().toString())
                        .setContentIntent(resultPendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_mobilebank))
                        .setAutoCancel(true)
                        .setChannelId(CHANNEL_PHONE).build();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(mChannel);
                }
                notificationManager.notify(notifyID, builder);
//                notifyID++;
            } else if (dropdown.getSelectedItem().equals("Баланс ")) {
                String CHANNEL_BALANCE = "channel_balance";// The id of the channel.
                CharSequence name = getString(R.string.Label);// The user-visible name of the channel.
                int importance = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    importance = NotificationManager.IMPORTANCE_HIGH;
                }
//                NotificationChannel mChannel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(CHANNEL_BALANCE, name, importance);
                }
//                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification builder = new NotificationCompat.Builder(this, CHANNEL_BALANCE)
                        .setSmallIcon(R.drawable.ic_balance)
                        .setContentTitle(dropdown.getSelectedItem().toString())
//                        .setContentText(output_balance.getText().toString())
                        .setContentText(message)
                        .setContentIntent(resultPendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_mobilebank))
                        .setAutoCancel(true)
                        .setChannelId(CHANNEL_BALANCE).build();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(mChannel);
                }
                notificationManager.notify(notifyID, builder);
//                notifyID++;
            } else if (dropdown.getSelectedItem().equals("История ")) {
                String CHANNEL_HISTORY = "channel_history";// The id of the channel.
                CharSequence name = getString(R.string.Label);// The user-visible name of the channel.
                int importance = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    importance = NotificationManager.IMPORTANCE_HIGH;
                }
//                NotificationChannel mChannel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(CHANNEL_HISTORY, name, importance);
                }
//                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification builder = new NotificationCompat.Builder(this, CHANNEL_HISTORY)
                        .setSmallIcon(R.drawable.ic_history)
                        .setContentTitle(dropdown.getSelectedItem().toString())
//                        .setContentText(output_history.getText().toString())
                        .setContentText(message)
                        .setContentIntent(resultPendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_mobilebank))
                        .setAutoCancel(true)
                        .setChannelId(CHANNEL_HISTORY).build();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(mChannel);
                }
                notificationManager.notify(notifyID, builder);
//                notifyID++;
            } else if (dropdown.getSelectedItem().equals("Спасибо ")) {
                String CHANNEL_THANK = "channel_thank";// The id of the channel.
                CharSequence name = getString(R.string.Label);// The user-visible name of the channel.
                int importance = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    importance = NotificationManager.IMPORTANCE_HIGH;
                }
//                NotificationChannel mChannel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(CHANNEL_THANK, name, importance);
                }
//                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification builder = new NotificationCompat.Builder(this, CHANNEL_THANK)
                        .setSmallIcon(R.drawable.ic_thank)
                        .setContentTitle(dropdown.getSelectedItem().toString())
//                        .setContentText(output_thank.getText().toString())
                        .setContentText(message)
                        .setContentIntent(resultPendingIntent)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_mobilebank))
                        .setAutoCancel(true)
                        .setChannelId(CHANNEL_THANK).build();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.createNotificationChannel(mChannel);
                }
                notificationManager.notify(notifyID, builder);
//                notifyID++;
            }
        }
        if (switch_boolean_vibrate) {
            vibe.vibrate(500);
        }
        if (switch_boolean_sound) {
            final MediaPlayer mp = MediaPlayer.create(this, R.raw.signal);
            mp.start();
//            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone ringtone = RingtoneManager.getRingtone(this,R.raw.signal);
//            ringtone.play();
//            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            NotificationCompat.Builder builder_sound = new NotificationCompat.Builder(this)
//                    .setSound(alarmSound);
//            Notification notification = builder_sound.build();
//            notificationManager.notify(3, notification);
        }
    }

    public void onButtonInfo_dropdown(View v) {
        if (dropdown.getSelectedItem().equals("Перевод ")) {
            dialog_info.setMessage(R.string.dialog_info_transfer);
            info_alert = dialog_info.create();
            info_alert.setTitle("Позволяет перевести деньги по номеру телефона или карты");
            info_alert.show();
        } else if (dropdown.getSelectedItem().equals("Телефон ")) {
            dialog_info.setMessage(R.string.dialog_info_phone);
            AlertDialog info_alert = dialog_info.create();
            info_alert.setTitle("Позволяет пополнить баланс мобильного телефона");
            info_alert.show();
        } else if (dropdown.getSelectedItem().equals("Баланс ")) {
            dialog_info.setMessage(R.string.dialog_info_balance);
            AlertDialog info_alert = dialog_info.create();
            info_alert.setTitle("Позволяет узнать, сколько денег на карте");
            info_alert.show();
        } else if (dropdown.getSelectedItem().equals("История ")) {
            dialog_info.setMessage(R.string.dialog_info_history);
            AlertDialog info_alert = dialog_info.create();
            info_alert.setTitle("Позволяет узнать, историю операци по карте");
            info_alert.show();
        } else if (dropdown.getSelectedItem().equals("Спасибо ")) {
            dialog_info.setMessage(R.string.dialog_info_thank);
            AlertDialog info_alert = dialog_info.create();
            info_alert.setTitle("Позволяет узнать, сколько бонусов СПАСИБО накоплено");
            info_alert.show();
        }
    }

    public void onButtonContact(View v) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_CODE_PERMISSION);
            onRequestPermissions_contact();
            isPermissionGiven_read_contacts = true;
            isPermissionGiven_read_sms = false;
            isPermissionGiven_read_sms_dropdown = false;
        } else {
            //загрузил справочник
//            get_contact_List();
            //проваливается в справочник контактов
            startActivityForResult(intentContact, PICK_CONTACT);  //проваливаемся в встроенный справочник
        }
    }

    //полчение разрешения к контактам
    public void onRequestPermissions_contact() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
            dialog_permission.setMessage("У вас нет разрешения для работы с контактами. Получить разрешения?")
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_CODE_PERMISSION);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = dialog_permission.create();
            alert.setTitle("Доступ");
            alert.show();
        } else {
            dialog_permission.setMessage("У вас нет разрешения для работы с контактами. " +
                    "Для получения разрешений Вам необходимо пройти в настройки приложения.")
                    .setCancelable(false)
                    .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(appSettingsIntent, REQUEST_CODE_PERMISSION);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = dialog_permission.create();
            alert.setTitle("Доступ");
            alert.show();
        }
    }

    //полчение разрешений к SMS
    public void onRequestPermissions_sms() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) ||
                !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS) ||
                !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
            dialog_permission.setMessage("У вас нет разрешения для работы с SMS, данный доступ необходим для отправки/получения запросов по SMS. Получить разрешение?")
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.SEND_SMS,
                                    android.Manifest.permission.READ_SMS,
                                    Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_PERMISSION);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = dialog_permission.create();
            alert.setTitle("Доступ");
            alert.show();
        } else {
            dialog_permission.setMessage("У вас нет разрешения для работы с SMS, данный доступ необходим для отправки/получения запросов по SMS. " +
                    "Для получения разрешения Вам необходимо пройти в настройки приложения.")
                    .setCancelable(false)
                    .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(appSettingsIntent, REQUEST_CODE_PERMISSION);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = dialog_permission.create();
            alert.setTitle("Доступ");
            alert.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                if (isPermissionGiven_read_contacts) {
                    contact.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_contact); //изменяем ярлык
                    get_contact_List(); //перед проваливанием в справочник, получаем список контактов
                    startActivityForResult(intentContact, PICK_CONTACT); //проваливаемся в встроенный справочник
                }
                if (isPermissionGiven_read_sms) {
                    onButtonSend(view);
                }
                if (isPermissionGiven_read_sms_dropdown && !check_dialog) {
                    dialog_get_card
                            .setTitle("Получить список карт?")
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @SuppressLint("UnlocalizedSms")
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pd_time_wait();
                                    smsManager.sendTextMessage("900", null, "Справка", null, null);
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = dialog_get_card.create();
                    alert.show();
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("My_log","_come back2");
        if (requestCode == PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                getContactInfo(intent); //передаем контакт в приложени
                // Your class variables now have the data, so do something with it.
            }
        }
        if (requestCode == PICK_SWITCH) {
            if (resultCode == RESULT_OK) {
                save_switch_text(intent);
                save_switch_vibrate(intent);
                save_switch_sound(intent);
            }
        }
        if (requestCode == PICK_FAVORITE) {
            Log.d("My_log","_come back");
            if (dropdown.getSelectedItem().equals("Перевод ")) {
                output_transfer.setAdapter(output_transfer_adapter);
            }else if (dropdown.getSelectedItem().equals("Телефон ")) {
                output_phone.setAdapter(output_phone_adapter);
            }else if (dropdown.getSelectedItem().equals("Баланс ")) {
                output_balance.setAdapter(output_balance_adapter);
            }else if (dropdown.getSelectedItem().equals("История ")) {
                output_history.setAdapter(output_history_adapter);
            }else if (dropdown.getSelectedItem().equals("Спасибо ")) {
                output_thank.setAdapter(output_thank_adapter);
            }
            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
            if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
            }
            cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
        }
    }//onActivityResult

    //Получает список контактов
    protected void getContactInfo(Intent intent) {
        list_number.clear();
        @SuppressWarnings("deprecation") Cursor cursor = managedQuery(intent.getData(), null, null, null, null);
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (hasPhone.equalsIgnoreCase("1"))
                hasPhone = "true";
            else
                hasPhone = "false";
            if (Boolean.parseBoolean(hasPhone)) {
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                while (phones != null && phones.moveToNext()) {
                    string_phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    String phone_name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    int_row_position = phones.getPosition();
                    //проверяем дубликат в полученном массиве телефонных номеров
                    if (!list_number.contains(string_phoneNumber)) {
                        list_number.add(0, string_phoneNumber);
                    }
                    adapter_number.notifyDataSetChanged();
                }
                phones.close();
            }
        }  //while (cursor.moveToNext())

        if (adapter_number.getCount() > 1) {
            dialog_main = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
            dialog_main.setAdapter(adapter_number, new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                public void onClick(DialogInterface dialog, int which) {
                    if (dropdown.getSelectedItem().toString().equals("Перевод ")) {
                        text_transfer.setText(adapter_number.getItem(which));
                    } else if (dropdown.getSelectedItem().toString().equals("Телефон ")) {
                        text_phone.setText(adapter_number.getItem(which));
                    }
                }
            });
            dialog_main_list = dialog_main.create();
            dialog_main_list.show();
        } else {
            if (dropdown.getSelectedItem().toString().equals("Перевод ")) {
                text_transfer.setText(string_phoneNumber);
            } else if (dropdown.getSelectedItem().toString().equals("Телефон ")) {
                text_phone.setText(string_phoneNumber);
            }
        }
    }//getContactInfo

    //появляется сообщение о полученных сообщениях
    @SuppressLint("UnlocalizedSms")
    @Override
    public void messageReceived(final String message) {
        if (check_button_send) {
            if (dropdown.getSelectedItem().equals("Перевод ")) {
                date_today = new Date();
                //Для перевода 2р получателю АНДРЕЙ МИХАЙЛОВИЧ К. на ECMC6294 с карты VISA2236 отправьте код 12766 на 900. Комиссия составляет 0.02р
                Pattern p_transfer01 = Pattern.compile("^Для перевода .*");
                Matcher m_transfer01 = p_transfer01.matcher(message);
                if (m_transfer01.find()) {
                    handler.removeCallbacks(runnable);
                    progress_dialog.cancel();
                    notification(message); //уведомление в верхней части экрана
                    final String[] mes = message.split(" на");
                    final String[] mes_name_split = message.split("получателю ");
                    mes_name = mes_name_split[1].split(" на");
                    dialog_confirm.setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pd_time_wait();
                                    if (text_transfer_comment.toString().isEmpty()) {
                                        smsManager.sendTextMessage("900", null, mes[1].substring(mes[1].length() - 5), null, null);
                                    } else {
                                        smsManager.sendTextMessage("900", null, mes[1].substring(mes[1].length() - 5) + " " + text_transfer_comment.getText().toString(), null, null);
                                    }
                                    check_put_button_send = true;
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    check_button_send = false;
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = dialog_confirm.create();
                    alert.setTitle("Подтвердить?");
                    alert.show();
                } else if (check_put_button_send && message.matches("(?is)" + ".*" + Pattern.quote("перевод") + ".*")) {
                    //VISA2233 09:56 перевод 25р с комиссией 0ю25р Баланс: 10260р
                    check_put_button_send = false; //отключаем маркер
                    handler.removeCallbacks(runnable);
                    progress_dialog.cancel();
                    notification(message); //уведомление в верхней части экрана
                    // Прослушиваем появление сообщения и добавляем в лист
                    string_message_convert = "Перевод для " + mes_name[0] + " выполнен." + " Комментарий: " + text_transfer_comment.getText().toString() + "." + "\n" + message;
                    if (output_transfer_arrayList.contains(string_message_convert)) {
                        check_equals_while = true;
                        cursor_dataDB = InputDB.getListContents_transfer();
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_main);
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (string_message_convert.equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    output_transfer_arrayList.remove(string_message_convert);
                                    output_transfer_arrayList.add(0, string_message_convert);
                                    InputDB.delete_Data_transfer(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                    InputDB.addData_transfer(string_title_transfer, string_message_convert, text_transfer.getText().toString(), text_transfer_contact.getText().toString(), money_transfer.getText().toString(), text_transfer_comment.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                                }
                            }
                            cursor_dataDB.close();
                        }
                    } else {
                        output_transfer_arrayList.add(0, string_message_convert);
                        InputDB.addData_transfer(string_title_transfer, string_message_convert, text_transfer.getText().toString(), text_transfer_contact.getText().toString(), money_transfer.getText().toString(), text_transfer_comment.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                    }
                    output_transfer_adapter.notifyDataSetChanged(); //обновляем адаптер вывода
                    dialog_confirm = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                    dialog_confirm.setMessage(string_message_convert)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = dialog_confirm.create();
                    alert.show();
                } else if (check_put_button_send && message.matches("(?is)" + ".*" + Pattern.quote("Получен повторный запрос") + ".*") || message.matches("(?is)" + ".*" + Pattern.quote("Операция не выполнена") + ".*") || message.matches("(?is)" + ".*" + Pattern.quote("Перевод не выполнен") + ".*")) {
                    check_put_button_send = false; //отключаем маркер
                    handler.removeCallbacks(runnable);
                    progress_dialog.cancel();
                    notification(message); //уведомление в верхней части экрана
                    // Прослушиваем появление сообщения и добавляем в лист
                    if (output_transfer_arrayList.contains(message)) {
                        check_equals_while = true;
                        cursor_dataDB = InputDB.getListContents_transfer();
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_main);
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (message.equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    output_transfer_arrayList.remove(message);
                                    output_transfer_arrayList.add(0, message);
                                    InputDB.delete_Data_transfer(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                    InputDB.addData_transfer(string_title_transfer, message, text_transfer.getText().toString(), text_transfer_contact.getText().toString(), money_transfer.getText().toString(), text_transfer_comment.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                                }
                            }
                            cursor_dataDB.close();
                        }
                    } else {
                        output_transfer_arrayList.add(0, message);
                        InputDB.addData_transfer(string_title_transfer, message, text_transfer.getText().toString(), text_transfer_contact.getText().toString(), money_transfer.getText().toString(), text_transfer_comment.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                    }
                    output_transfer_adapter.notifyDataSetChanged(); //обновляем адаптер вывода
                    dialog_confirm = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                    dialog_confirm.setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = dialog_confirm.create();
                    alert.show();
                }
            } else if (dropdown.getSelectedItem().equals("Телефон ")) {
                date_today = new Date();
                Pattern p_phone01 = Pattern.compile("^Для оплаты .* отправьте .* 900");
                Matcher m_phone01 = p_phone01.matcher(message);
                if (m_phone01.find()) { //Для оплаты 9135315392 на 10р с ECMC8836 отправьте код 73609 на 900
                    handler.removeCallbacks(runnable);
                    progress_dialog.cancel();
                    notification(message); //уведомление в верхней части экрана
                    final String[] mes = message.split(" на");
                    final String[] mes_number_split = message.split("оплаты ");
                    mes_number = mes_number_split[1].split(" на");
                    dialog_confirm.setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pd_time_wait();
                                    smsManager.sendTextMessage("900", null, mes[1].substring(mes[1].length() - 5), null, null);
                                    check_put_button_send = true;
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    check_button_send = false;
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = dialog_confirm.create();
                    alert.setTitle("Подтвердить?");
                    alert.show();
                } else if (check_put_button_send && message.matches("(?is)" + ".*" + Pattern.quote("оплата") + ".*" + Pattern.quote("баланс:") + ".*")) {
                    //ECMC8836 17:08 Оплата 10р MTS Баланс: 132224.65р
                    check_put_button_send = false; //отключаем маркер
                    handler.removeCallbacks(runnable);
                    progress_dialog.cancel(); //закрываем окно ожидания
                    notification(message); //уведомление в верхней части экрана
                    // Прослушиваем появление сообщения и добавляем в лист
                    string_message_convert = "Оплата телефона для " + mes_number[0] + " выполнена." + "\n" + message;
                    if (output_phone_arrayList.contains(string_message_convert)) {
                        check_equals_while = true;
                        cursor_dataDB = InputDB.getListContents_phone();
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_main);
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (string_message_convert.equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    output_phone_arrayList.remove(string_message_convert);
                                    output_phone_arrayList.add(0, string_message_convert);
                                    InputDB.delete_Data_phone(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                    InputDB.addData_phone(string_title_phone, string_message_convert, text_phone.getText().toString(), text_phone_contact.getText().toString(), money_phone.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                                }
                            }
                            cursor_dataDB.close();
                        }
                    } else {
                        output_phone_arrayList.add(0, string_message_convert);
                        InputDB.addData_phone(string_title_phone, string_message_convert, text_phone.getText().toString(), text_phone_contact.getText().toString(), money_phone.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                    }
                    output_phone_adapter.notifyDataSetChanged(); //обновляем адаптер вывода
                    dialog_confirm = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                    dialog_confirm.setMessage(string_message_convert)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = dialog_confirm.create();
                    alert.show();
                } else if (check_put_button_send && message.matches("(?is)" + ".*" + Pattern.quote("Получен повторный запрос") + ".*") || message.matches("(?is)" + ".*" + Pattern.quote("Операция не выполнена") + ".*")) {
                    //Операция не выполнена. Получен повторный запрос. Измените текст запроса или направьте его повторно через 10 мин.
                    check_put_button_send = false; //отключаем маркер
                    handler.removeCallbacks(runnable);
                    progress_dialog.cancel(); //закрываем окно ожидания
                    notification(message); //уведомление в верхней части экрана
                    // Прослушиваем появление сообщения и добавляем в лист
                    if (output_phone_arrayList.contains(message)) {
                        check_equals_while = true;
                        cursor_dataDB = InputDB.getListContents_phone();
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_main);
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (message.equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    output_phone_arrayList.remove(message);
                                    output_phone_arrayList.add(0, message);
                                    InputDB.delete_Data_phone(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                    InputDB.addData_phone(string_title_phone, message, text_phone.getText().toString(), text_phone_contact.getText().toString(), money_phone.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                                }
                            }
                            cursor_dataDB.close();
                        }
                    } else {
                        output_phone_arrayList.add(0, message);
                        InputDB.addData_phone(string_title_phone, message, text_phone.getText().toString(), text_phone_contact.getText().toString(), money_phone.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                    }
                    output_phone_adapter.notifyDataSetChanged(); //обновляем адаптер вывода
                    dialog_confirm = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                    dialog_confirm.setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = dialog_confirm.create();
                    alert.show();
                } else if (message.matches("(?is)" + ".*" + Pattern.quote("оплата") + ".*" + Pattern.quote("баланс:") + ".*")) { //ECMC8836 17:08 Оплата 10р MTS Баланс: 132224.65р
                    check_put_button_send = false; //отключаем маркер
                    handler.removeCallbacks(runnable);
                    progress_dialog.cancel(); //закрываем окно ожидания
                    notification(message); //уведомление в верхней части экрана
                    // Прослушиваем появление сообщения и добавляем в лист
                    string_message_convert = "Оплата телефона для " + text_phone.getText().toString() + " выполнена." + "\n" + message;
                    if (output_phone_arrayList.contains(string_message_convert)) {
                        check_equals_while = true;
                        cursor_dataDB = InputDB.getListContents_phone();
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_main);
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (string_message_convert.equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    output_phone_arrayList.remove(string_message_convert);
                                    output_phone_arrayList.add(0, string_message_convert);
                                    InputDB.delete_Data_phone(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                    InputDB.addData_phone(string_title_phone, string_message_convert, text_phone.getText().toString(), text_phone_contact.getText().toString(), money_phone.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                                }
                            }
                            cursor_dataDB.close();
                        }
                    } else {
                        output_phone_arrayList.add(0, string_message_convert);
                        InputDB.addData_phone(string_title_phone, string_message_convert, text_phone.getText().toString(), text_phone_contact.getText().toString(), money_phone.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                    }
                    output_phone_adapter.notifyDataSetChanged(); //обновляем адаптер вывода
                    dialog_confirm = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                    dialog_confirm.setMessage(string_message_convert)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = dialog_confirm.create();
                    alert.show();
                }
            } else if (dropdown.getSelectedItem().equals("Баланс ")) {
                date_today = new Date();
                Pattern p_get_card = Pattern.compile("карты, подключенные к мобильному банку");
                Matcher m_get_card = p_get_card.matcher(message);
                if (m_get_card.find()) {
                    //останавливает счет времени прогресс бара
                    handler.removeCallbacks(runnable);
                    pd_time_wait();
                    smsManager.sendTextMessage("900", null, "5", null, null);
                    send_5 = true;
                } else if (message.matches("(?is)" + ".*" + Pattern.quote("отправьте на 900:") + ".*")) {
                    //останавливает счет времени прогресс бара
                    handler.removeCallbacks(runnable);
                    pd_time_wait();
                    smsManager.sendTextMessage("900", null, "Карты", null, null);
                    send_5 = true;
                } else if (send_5 && !check_card) {
                    handler.removeCallbacks(runnable);
                    progress_dialog.cancel();
                    notification(message); //уведомление в верхней части экрана
                    // Прослушиваем появление сообщения и добавляем в лист
                    if (output_balance_arrayList.contains(message)) {
                        check_equals_while = true;
                        cursor_dataDB = InputDB.getListContents_balance();
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_main);
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (message.equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    output_balance_arrayList.remove(message);
                                    output_balance_arrayList.add(0, message);
                                    InputDB.delete_Data_balance(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                    InputDB.addData_balance(string_title_balance, message, text_balance.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                                }
                            }
                            cursor_dataDB.close();
                        }
                    } else {
                        output_balance_arrayList.add(0, message);
                        InputDB.addData_balance(string_title_balance, message, text_balance.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                    }
                    output_balance_adapter.notifyDataSetChanged(); //обновляем адаптер вывода
                    credit_card(message); //обрабатываем получен список карт
                    //                Список подключенных карт:
                    //                VISA2236 (вкл)
                    //                ECMC8836 (вкл)
                } else if (check_put_button_send && message.matches("(?is)" + ".*" + Pattern.quote("Операция не выполнена") + ".*") || message.matches("(?is)" + ".*" + Pattern.quote("Баланс по карт") + ".*")) {
                    //Баланс по карте: VISA2236: 1201,86р
                    handler.removeCallbacks(runnable);
                    progress_dialog.cancel();
                    notification(message); //уведомление в верхней части экрана
                    // Прослушиваем появление сообщения и добавляем в лист
                    if (output_balance_arrayList.contains(message)) {
                        check_equals_while = true;
                        cursor_dataDB = InputDB.getListContents_balance();
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_main);
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (message.equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    output_balance_arrayList.remove(message);
                                    output_balance_arrayList.add(0, message);
                                    InputDB.delete_Data_balance(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                    InputDB.addData_balance(string_title_balance, message, text_balance.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                                }
                            }
                            cursor_dataDB.close();
                        }
                    } else {
                        output_balance_arrayList.add(0, message);
                        InputDB.addData_balance(string_title_balance, message, text_balance.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                    }
                    output_balance_adapter.notifyDataSetChanged(); //обновляем адаптер вывода
                    dialog_confirm = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                    dialog_confirm.setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = dialog_confirm.create();
                    alert.show();
                }
            } else if (dropdown.getSelectedItem().equals("История ")) {
                date_today = new Date();
                Pattern p_get_card = Pattern.compile("карты, подключенные к мобильному банку");
                Matcher m_get_card = p_get_card.matcher(message);
                if (m_get_card.find()) {
                    //останавливает счет времени прогресс бара
                    handler.removeCallbacks(runnable);
                    pd_time_wait();
                    smsManager.sendTextMessage("900", null, "5", null, null);
                    send_5 = true;
                } else if (message.matches("(?is)" + ".*" + Pattern.quote("отправьте на 900:") + ".*")) {
                    //останавливает счет времени прогресс бара
                    handler.removeCallbacks(runnable);
                    pd_time_wait();
                    smsManager.sendTextMessage("900", null, "Карты", null, null);
                    send_5 = true;
                } else if (send_5 && !check_card) {
                    handler.removeCallbacks(runnable);
                    progress_dialog.cancel();
                    notification(message); //уведомление в верхней части экрана
                    // Прослушиваем появление сообщения и добавляем в лист
                    if (output_history_arrayList.contains(message)) {
                        check_equals_while = true;
                        cursor_dataDB = InputDB.getListContents_history();
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_main);
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (message.equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    output_history_arrayList.remove(message);
                                    output_history_arrayList.add(0, message);
                                    InputDB.delete_Data_history(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                    InputDB.addData_history(string_title_history, message, text_history.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                                }
                            }
                            cursor_dataDB.close();
                        }
                    } else {
                        output_history_arrayList.add(0, message);
                        InputDB.addData_history(string_title_history, message, text_history.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                    }
                    output_history_adapter.notifyDataSetChanged(); //обновляем адаптер вывода
                    credit_card(message); //обрабатываем получен список карт
                    //                Список подключенных карт:
                    //                VISA2236 (вкл)
                    //                ECMC8836 (вкл)
                } else if (check_put_button_send && message.matches("(?is)" + ".*" + Pattern.quote("Операция не выполнена") + ".*") || message.matches("(?is)" + ".*" + Pattern.quote("выписка по карте") + ".*")) {
                    handler.removeCallbacks(runnable);
                    progress_dialog.cancel();
                    notification(message); //уведомление в верхней части экрана
                    // Прослушиваем появление сообщения и добавляем в лист
                    if (output_history_arrayList.contains(message)) {
                        check_equals_while = true;
                        cursor_dataDB = InputDB.getListContents_history();
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_main);
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (message.equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    output_history_arrayList.remove(message);
                                    output_history_arrayList.add(0, message);
                                    InputDB.delete_Data_history(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                    InputDB.addData_history(string_title_history, message, text_history.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                                }
                            }
                            cursor_dataDB.close();
                        }
                    } else {
                        output_history_arrayList.add(0, message);
                        InputDB.addData_history(string_title_history, message, text_history.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                    }
                    output_history_adapter.notifyDataSetChanged(); //обновляем адаптер вывода
                    dialog_confirm = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                    dialog_confirm.setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = dialog_confirm.create();
                    alert.show();
                }
            } else if (dropdown.getSelectedItem().equals("Спасибо ")) {
                date_today = new Date();
                Pattern p_get_card = Pattern.compile("карты, подключенные к мобильному банку");
                Matcher m_get_card = p_get_card.matcher(message);
                if (m_get_card.find()) {
                    //останавливает счет времени прогресс бара
                    handler.removeCallbacks(runnable);
                    pd_time_wait();
                    smsManager.sendTextMessage("900", null, "5", null, null);
                    send_5 = true;
                } else if (message.matches("(?is)" + ".*" + Pattern.quote("отправьте на 900:") + ".*")) {
                    //останавливает счет времени прогресс бара
                    handler.removeCallbacks(runnable);
                    pd_time_wait();
                    smsManager.sendTextMessage("900", null, "Карты", null, null);
                    send_5 = true;
                } else if (send_5 && !check_card) {
                    handler.removeCallbacks(runnable);
                    progress_dialog.cancel();
                    notification(message); //уведомление в верхней части экрана
                    // Прослушиваем появление сообщения и добавляем в лист
                    if (output_thank_arrayList.contains(message)) {
                        check_equals_while = true;
                        cursor_dataDB = InputDB.getListContents_thank();
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_main);
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (message.equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    output_thank_arrayList.remove(message);
                                    output_thank_arrayList.add(0, message);
                                    InputDB.delete_Data_transfer(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                    InputDB.addData_thank(string_title_thanks, message, text_thank.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                                }
                            }
                            cursor_dataDB.close();
                        }
                    } else {
                        output_thank_arrayList.add(0, message);
                        InputDB.addData_thank(string_title_thanks, message, text_thank.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                    }
                    output_thank_adapter.notifyDataSetChanged(); //обновляем адаптер вывода
                    credit_card(message); //обрабатываем получен список карт
                    //Список подключенных карт:
                    //VISA2236 (вкл)
                    //ECMC8836 (вкл)
                } else if (message.matches("(?is)" + ".*" + Pattern.quote("Операция не выполнена") + ".*") || message.matches("(?is)" + ".*" + Pattern.quote("спасибо") + ".*")) {
                    handler.removeCallbacks(runnable);
                    progress_dialog.cancel();
                    notification(message); //уведомление в верхней части экрана
                    // Прослушиваем появление сообщения и добавляем в лист
                    if (output_thank_arrayList.contains(message)) {
                        check_equals_while = true;
                        cursor_dataDB = InputDB.getListContents_thank();
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_main);
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (message.equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    output_thank_arrayList.remove(message);
                                    output_thank_arrayList.add(0, message);
                                    InputDB.delete_Data_transfer(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)));
                                    InputDB.addData_thank(string_title_thanks, message, text_thank.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                                }
                            }
                            cursor_dataDB.close();
                        }
                    } else {
                        output_thank_arrayList.add(0, message);
                        InputDB.addData_thank(string_title_thanks, message, text_thank.getText().toString(), date_today.toString(), "0", "0"); //добавляем в базу
                    }
                    output_thank_adapter.notifyDataSetChanged(); //обновляем адаптер вывода
                    dialog_confirm = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                    dialog_confirm.setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = dialog_confirm.create();
                    alert.show();
                }
            }
        }
    }

    //обрабатываем полученный список карт
    public void credit_card(final String message) {
        list_balance = new ArrayList<>();
        string_list_card = message.split(":\\s+");
        if (string_list_card[1].contains(" ")) {
            string_list_card[1] = string_list_card[1].replace(" ", "");
        }
        string_list_card = string_list_card[1].split("\\s+");
        Collections.addAll(list_balance, string_list_card);
        adapter_card = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, list_balance);
        if (adapter_card.getCount() > 0) {
            check_dialog = true;
            check_card = true;
            saveData_card();
            card.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_card);
            if (check_update_card_dialog) {
                check_update_card_dialog = false;
                onCreateDialog();
            }
        }
    }

    //изменяется иконка при выборе пункта в dropdown
    private class DropDownAdapter extends ArrayAdapter<String> {
        private DropDownAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        private View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.spinner_row, parent, false);
            TextView label = row.findViewById(R.id.item);
            label.setText(items_dropdown[position]);
            ImageView icon = row.findViewById(R.id.icon);
            switch (items_dropdown[position]) {
                case "Перевод ":
                    if (dropdown.getSelectedItem().toString().equals("Перевод ")) {
                        icon.setImageResource(R.drawable.ic_transfer);
                    } else {
                        icon.setImageResource(R.drawable.ic_transfer_unactive);
                    }
                    break;
                case "Телефон ":
                    if (dropdown.getSelectedItem().toString().equals("Телефон ")) {
                        icon.setImageResource(R.drawable.ic_phone);
                    } else {
                        icon.setImageResource(R.drawable.ic_phone_unactive);
                    }
                    break;
                case "Баланс ":
                    if (dropdown.getSelectedItem().toString().equals("Баланс ")) {
                        icon.setImageResource(R.drawable.ic_balance);
                    } else {
                        icon.setImageResource(R.drawable.ic_balance_unactive);
                    }
                    break;
                case "История ":
                    if (dropdown.getSelectedItem().toString().equals("История ")) {
                        icon.setImageResource(R.drawable.ic_history);
                    } else {
                        icon.setImageResource(R.drawable.ic_history_unactive);
                    }
                    break;
                case "Спасибо ":
                    if (dropdown.getSelectedItem().toString().equals("Спасибо ")) {
                        icon.setImageResource(R.drawable.ic_thank);
                    } else {
                        icon.setImageResource(R.drawable.ic_thank_unactive);
                    }
                    break;
            }
            return row;
        }
    }

    //определяем custom adapter для listview всех output*-листов
    public class FavoriteAdapter extends ArrayAdapter implements OnCreateContextMenuListener {
        private ArrayList <String> array_data;
        private Context context;

        private FavoriteAdapter(Context Context, ArrayList <String> ArrData) {
            super(Context, R.layout.adapter_favorite, ArrData);
            this.context = Context;
            this.array_data = ArrData;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @SuppressLint("ResourceType")
        public View getCustomView(final int position, View view, ViewGroup parent) {
//            LayoutInflater inflater = getLayoutInflater();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_favorite, parent, false);
            final ViewHolder mViewHolder = new ViewHolder();

            mViewHolder.txtTitle = (TextView) view.findViewById(R.id.text_adapter_favorite);
            mViewHolder.txtTitle.setText(array_data.get(position));
            mViewHolder.btnAction = (Button) view.findViewById(R.id.button_adapter_favorite);
            //transfer
            cursor_dataDB = InputDB.getListContents_transfer();
            if (cursor_dataDB.getCount() == 0) {
            } else {
                check_equals_while = true;
                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_main);
                int_columnID_favorite = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_favorite);
                while (cursor_dataDB.moveToNext() && check_equals_while) {
                    if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                        check_equals_while = false;
                        if (cursor_dataDB.getString(int_columnID_favorite).equals("0")) {
                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_offline); //измененяем иконку кнопки favorite в listview
                        } else {
                            boolean_check_favoriteID_and_turn_on_favoriteList = true;
                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online); //измененяем иконку кнопки favorite в listview
                        }
                    }
                }
            }
            cursor_dataDB.close();
            //phone
            cursor_dataDB = InputDB.getListContents_phone();
            if (cursor_dataDB.getCount() == 0) {
            } else {
                check_equals_while = true;
                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_main);
                int_columnID_favorite = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_favorite);
                while (cursor_dataDB.moveToNext() && check_equals_while) {
                    if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                        check_equals_while = false;
                        if (cursor_dataDB.getString(int_columnID_favorite).equals("0")) {
                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_offline); //измененяем иконку кнопки favorite в listview
                        } else {
                            boolean_check_favoriteID_and_turn_on_favoriteList = true;
                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online); //измененяем иконку кнопки favorite в listview
                        }
                    }
                }
            }
            cursor_dataDB.close();
            //balance
            cursor_dataDB = InputDB.getListContents_balance();
            if (cursor_dataDB.getCount() == 0) {
            } else {
                check_equals_while = true;
                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_main);
                int_columnID_favorite = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_favorite);
                while (cursor_dataDB.moveToNext() && check_equals_while) {
                    if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                        check_equals_while = false;
                        if (cursor_dataDB.getString(int_columnID_favorite).equals("0")) {
                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_offline); //измененяем иконку кнопки favorite в listview
                        } else {
                            boolean_check_favoriteID_and_turn_on_favoriteList = true;
                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online); //измененяем иконку кнопки favorite в listview
                        }
                    }
                }
            }
            cursor_dataDB.close();
            //history
            cursor_dataDB = InputDB.getListContents_history();
            if (cursor_dataDB.getCount() == 0) {
            } else {
                check_equals_while = true;
                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_main);
                int_columnID_favorite = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_favorite);
                while (cursor_dataDB.moveToNext() && check_equals_while) {
                    if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                        check_equals_while = false;
                        if (cursor_dataDB.getString(int_columnID_favorite).equals("0")) {
                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_offline); //измененяем иконку кнопки favorite в listview
                        } else {
                            boolean_check_favoriteID_and_turn_on_favoriteList = true;
                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online); //измененяем иконку кнопки favorite в listview
                        }
                    }
                }
            }
            cursor_dataDB.close();
            //thank
            cursor_dataDB = InputDB.getListContents_thank();
            if (cursor_dataDB.getCount() == 0) {
            } else {
                check_equals_while = true;
                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_main);
                int_columnID_favorite = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_favorite);
                while (cursor_dataDB.moveToNext() && check_equals_while) {
                    if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                        check_equals_while = false;
                        if (cursor_dataDB.getString(int_columnID_favorite).equals("0")) {
                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_offline); //измененяем иконку кнопки favorite в listview
                        } else {
                            boolean_check_favoriteID_and_turn_on_favoriteList = true;
                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online); //измененяем иконку кнопки favorite в listview
                        }
                    }
                }
            }
            cursor_dataDB.close();
            mViewHolder.btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (dropdown.getSelectedItem().equals("Перевод ")) {
                            cursor_dataDB = InputDB.getListContents_transfer();
                            check_equals_while = true;
                            if (cursor_dataDB.getCount() == 0) {
                            } else {
                                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_main);
                                int_columnID_favorite = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_favorite);
                                while (cursor_dataDB.moveToNext() && check_equals_while) {
                                    if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                        check_equals_while = false;
                                        if (cursor_dataDB.getString(int_columnID_favorite).equals("0")) {
                                            InputDB.update_Data_transfer_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online); //измененяем иконку кнопки favorite в listview
                                            menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                                        } else {
                                            InputDB.update_Data_transfer_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_offline); //измененяем иконку кнопки favorite в listview
                                            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();
                                            cursor_dataDB_where_phone = InputDB.getListContents_where_phone();
                                            cursor_dataDB_where_balance = InputDB.getListContents_where_balance();
                                            cursor_dataDB_where_history = InputDB.getListContents_where_history();
                                            cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                                            if (cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0) {
                                                menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                                            }
                                            cursor_dataDB_where_transfer.close();
                                            cursor_dataDB_where_phone.close();
                                            cursor_dataDB_where_balance.close();
                                            cursor_dataDB_where_history.close();
                                            cursor_dataDB_where_thank.close();
                                        }
                                    }
                                }
                                cursor_dataDB.close();
                            }
                        } else if (dropdown.getSelectedItem().equals("Телефон ")) {
                            cursor_dataDB = InputDB.getListContents_phone();
                            check_equals_while = true;
                            if (cursor_dataDB.getCount() == 0) {
                            } else {
                                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_main);
                                int_columnID_favorite = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_favorite);
                                while (cursor_dataDB.moveToNext() && check_equals_while) {
                                    if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                        check_equals_while = false;
                                        if (cursor_dataDB.getString(int_columnID_favorite).equals("0")) {
                                            InputDB.update_Data_phone_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online); //измененяем иконку кнопки favorite в listview
                                            menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                                        } else {
                                            InputDB.update_Data_phone_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_offline); //измененяем иконку кнопки favorite в listview
                                            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();
                                            cursor_dataDB_where_phone = InputDB.getListContents_where_phone();
                                            cursor_dataDB_where_balance = InputDB.getListContents_where_balance();
                                            cursor_dataDB_where_history = InputDB.getListContents_where_history();
                                            cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                                            if (cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0) {
                                                menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                                            }
                                            cursor_dataDB_where_transfer.close();
                                            cursor_dataDB_where_phone.close();
                                            cursor_dataDB_where_balance.close();
                                            cursor_dataDB_where_history.close();
                                            cursor_dataDB_where_thank.close();
                                        }
                                    }
                                }
                                cursor_dataDB.close();
                            }
                        } else if (dropdown.getSelectedItem().equals("Баланс ")) {
                            cursor_dataDB = InputDB.getListContents_balance();
                            check_equals_while = true;
                            if (cursor_dataDB.getCount() == 0) {
                            } else {
                                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_main);
                                int_columnID_favorite = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_favorite);
                                while (cursor_dataDB.moveToNext() && check_equals_while) {
                                    if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                        check_equals_while = false;
                                        if (cursor_dataDB.getString(int_columnID_favorite).equals("0")) {
                                            InputDB.update_Data_balance_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online); //измененяем иконку кнопки favorite в listview
                                            menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                                        } else {
                                            InputDB.update_Data_balance_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_offline); //измененяем иконку кнопки favorite в listview
                                            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();
                                            cursor_dataDB_where_phone = InputDB.getListContents_where_phone();
                                            cursor_dataDB_where_balance = InputDB.getListContents_where_balance();
                                            cursor_dataDB_where_history = InputDB.getListContents_where_history();
                                            cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                                            if (cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0) {
                                                menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                                            }
                                            cursor_dataDB_where_transfer.close();
                                            cursor_dataDB_where_phone.close();
                                            cursor_dataDB_where_balance.close();
                                            cursor_dataDB_where_history.close();
                                            cursor_dataDB_where_thank.close();
                                        }
                                    }
                                }
                                cursor_dataDB.close();
                            }
                        } else if (dropdown.getSelectedItem().equals("История ")) {
                            cursor_dataDB = InputDB.getListContents_history();
                            check_equals_while = true;
                            if (cursor_dataDB.getCount() == 0) {
                            } else {
                                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_main);
                                int_columnID_favorite = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_favorite);
                                while (cursor_dataDB.moveToNext() && check_equals_while) {
                                    if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                        check_equals_while = false;
                                        if (cursor_dataDB.getString(int_columnID_favorite).equals("0")) {
                                            InputDB.update_Data_history_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online); //измененяем иконку кнопки favorite в listview
                                            menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                                        } else {
                                            InputDB.update_Data_history_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_offline); //измененяем иконку кнопки favorite в listview
                                            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();
                                            cursor_dataDB_where_phone = InputDB.getListContents_where_phone();
                                            cursor_dataDB_where_balance = InputDB.getListContents_where_balance();
                                            cursor_dataDB_where_history = InputDB.getListContents_where_history();
                                            cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                                            if (cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0) {
                                                menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                                            }
                                            cursor_dataDB_where_transfer.close();
                                            cursor_dataDB_where_phone.close();
                                            cursor_dataDB_where_balance.close();
                                            cursor_dataDB_where_history.close();
                                            cursor_dataDB_where_thank.close();
                                        }
                                    }
                                }
                                cursor_dataDB.close();
                            }
                        } else if (dropdown.getSelectedItem().equals("Спасибо ")) {
                            cursor_dataDB = InputDB.getListContents_thank();
                            check_equals_while = true;
                            if (cursor_dataDB.getCount() == 0) {
                            } else {
                                int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_main);
                                int_columnID_favorite = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_favorite);
                                while (cursor_dataDB.moveToNext() && check_equals_while) {
                                    if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                        check_equals_while = false;
                                        if (cursor_dataDB.getString(int_columnID_favorite).equals("0")) {
                                            InputDB.update_Data_thanks_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_online); //измененяем иконку кнопки favorite в listview
                                            menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                                        } else {
                                            InputDB.update_Data_thanks_favorite(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                            mViewHolder.btnAction.setBackgroundResource(R.drawable.ic_favorite_offline); //измененяем иконку кнопки favorite в listview
                                            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();
                                            cursor_dataDB_where_phone = InputDB.getListContents_where_phone();
                                            cursor_dataDB_where_balance = InputDB.getListContents_where_balance();
                                            cursor_dataDB_where_history = InputDB.getListContents_where_history();
                                            cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                                            if (cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0) {
                                                menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                                            }
                                            cursor_dataDB_where_transfer.close();
                                            cursor_dataDB_where_phone.close();
                                            cursor_dataDB_where_balance.close();
                                            cursor_dataDB_where_history.close();
                                            cursor_dataDB_where_thank.close();
                                        }
                                    }
                                }
                                cursor_dataDB.close();
                            }
                        }
                    }
                });

            view.setOnCreateContextMenuListener((OnCreateContextMenuListener) context);
            return view;
        }
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        }
        class ViewHolder{
            TextView txtTitle;
            Button btnAction;
        }
    }

    //определяем custom adapter для listview всех output*-листов
    public class DeleteAdapter extends ArrayAdapter implements OnCreateContextMenuListener {
        private ArrayList<String> array_data;
        private Context context;
        private int int_txt;

        private DeleteAdapter(Context Context,int txt, ArrayList<String> ArrData) {
            super(Context, R.layout.adapter_delete, ArrData);
            this.context = Context;
            this.int_txt = txt;
            this.array_data = ArrData;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @SuppressLint("ResourceType")
        public View getCustomView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_delete, parent, false);
            final ViewHolder viewholder_adapter_delete = new ViewHolder();
            viewholder_adapter_delete.txtTitle = (TextView) view.findViewById(R.id.text_adapter_delete);
            viewholder_adapter_delete.txtTitle.setText(array_data.get(position));
            viewholder_adapter_delete.btnAction = (Button) view.findViewById(R.id.button_adapter_delete);

            if (dropdown.getSelectedItem().equals("Перевод ")) {
                if(array_data.get(int_txt).equals(array_data.get(position))) {
                    cursor_dataDB = InputDB.getListContents_transfer();
                    check_equals_while = true;
                    if (cursor_dataDB.getCount() == 0) {
                    } else {
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_main);
                        int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_delete);
                        while (cursor_dataDB.moveToNext() && check_equals_while) {
                            if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                check_equals_while = false;
                                InputDB.update_Data_transfer_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_checked); //измененяем иконку кнопки favorite в listview
                            }
                        }
                        cursor_dataDB.close();
                    }
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View view) {
                        cursor_dataDB = InputDB.getListContents_transfer();
                        check_equals_while = true;
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_main);
                            int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_delete);
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    if (cursor_dataDB.getString(int_columnID_delete).equals("0")) {
                                        count_list_item++;
                                        getSupportActionBar().setTitle("" + count_list_item);
                                        InputDB.update_Data_transfer_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_checked); //измененяем иконку кнопки favorite в listview
                                    } else {
                                        count_list_item--;
                                        getSupportActionBar().setTitle("" + count_list_item);
                                        InputDB.update_Data_transfer_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_unchecked); //измененяем иконку кнопки favorite в listview
                                    }
                                }
                            }
                            cursor_dataDB.close();
                        }
                        //Если при нажатии на лист уже не ничего выделено
                        if (getSupportActionBar().getTitle().toString().equals("0")) {
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.main);
                            getSupportActionBar().setTitle(R.string.Label);
                            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                            output_transfer_adapter = new FavoriteAdapter(MainActivity.this, output_transfer_arrayList);
                            output_transfer.setAdapter(output_transfer_adapter);
                            //проверка favorite list
                            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                            if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                            }else{
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                            }
                            cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                            //настройка панели
                        }else if(getSupportActionBar().getTitle().toString().equals("1")){
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.menu_template);
                        }else{
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.menu_contexual);
                        }
                    }
                });
                viewholder_adapter_delete.btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cursor_dataDB = InputDB.getListContents_transfer();
                        check_equals_while = true;
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_main);
                            int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_delete);
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    if (cursor_dataDB.getString(int_columnID_delete).equals("0")) {
                                        count_list_item++;
                                        getSupportActionBar().setTitle(""+count_list_item);
                                        InputDB.update_Data_transfer_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_checked); //измененяем иконку кнопки favorite в listview
                                    } else {
                                        count_list_item--;
                                        getSupportActionBar().setTitle(""+count_list_item);
                                        InputDB.update_Data_transfer_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_unchecked); //измененяем иконку кнопки favorite в listview
                                    }
                                }
                            }
                            cursor_dataDB.close();
                        }
                    }
                });
            } else if (dropdown.getSelectedItem().equals("Телефон ")) {
                if(array_data.get(int_txt).equals(array_data.get(position))) {
                    cursor_dataDB = InputDB.getListContents_phone();
                    check_equals_while = true;
                    if (cursor_dataDB.getCount() == 0) {
                    } else {
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_main);
                        int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_favorite);
                        while (cursor_dataDB.moveToNext() && check_equals_while) {
                            if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                check_equals_while = false;
                                InputDB.update_Data_phone_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_checked); //измененяем иконку кнопки favorite в listview
                            }
                        }
                        cursor_dataDB.close();
                    }
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View view) {
                        cursor_dataDB = InputDB.getListContents_phone();
                        check_equals_while = true;
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_main);
                            int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_delete);
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    if (cursor_dataDB.getString(int_columnID_delete).equals("0")) {
                                        count_list_item++;
                                        getSupportActionBar().setTitle("" + count_list_item);
                                        InputDB.update_Data_phone_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_checked); //измененяем иконку кнопки favorite в listview
                                    } else {
                                        count_list_item--;
                                        getSupportActionBar().setTitle("" + count_list_item);
                                        InputDB.update_Data_phone_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_unchecked); //измененяем иконку кнопки favorite в listview
                                    }
                                }
                            }
                            cursor_dataDB.close();
                        }
                        //Если при нажатии на лист уже не ничего выделено
                        if (getSupportActionBar().getTitle().toString().equals("0")) {
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.main);
                            getSupportActionBar().setTitle(R.string.Label);
                            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                            output_phone_adapter = new FavoriteAdapter(MainActivity.this, output_phone_arrayList);
                            output_phone.setAdapter(output_phone_adapter);
                            //проверка favorite list
                            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                            if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                            }else{
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                            }
                            cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                            //настройка панели
                        }else if(getSupportActionBar().getTitle().toString().equals("1")){
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.menu_template);
                        }else{
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.menu_contexual);
                        }
                    }

                });
                viewholder_adapter_delete.btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cursor_dataDB = InputDB.getListContents_phone();
                        check_equals_while = true;
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_main);
                            int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_delete);
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    if (cursor_dataDB.getString(int_columnID_delete).equals("0")) {
                                        count_list_item++;
                                        getSupportActionBar().setTitle(""+count_list_item);
                                        InputDB.update_Data_phone_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_checked); //измененяем иконку кнопки favorite в listview
                                    } else {
                                        count_list_item--;
                                        getSupportActionBar().setTitle(""+count_list_item);
                                        InputDB.update_Data_phone_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_unchecked); //измененяем иконку кнопки favorite в listview
                                    }
                                }
                            }
                            cursor_dataDB.close();
                        }
                    }
                });
            } else if (dropdown.getSelectedItem().equals("Баланс ")) {
                if(array_data.get(int_txt).equals(array_data.get(position))) {
                    cursor_dataDB = InputDB.getListContents_balance();
                    check_equals_while = true;
                    if (cursor_dataDB.getCount() == 0) {
                    } else {
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_main);
                        int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_delete);
                        while (cursor_dataDB.moveToNext() && check_equals_while) {
                            if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                check_equals_while = false;
                                InputDB.update_Data_balance_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_checked); //измененяем иконку кнопки favorite в listview
                            }
                        }
                        cursor_dataDB.close();
                    }
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View view) {
                        cursor_dataDB = InputDB.getListContents_balance();
                        check_equals_while = true;
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_main);
                            int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_delete);
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    if (cursor_dataDB.getString(int_columnID_delete).equals("0")) {
                                        count_list_item++;
                                        getSupportActionBar().setTitle("" + count_list_item);
                                        InputDB.update_Data_balance_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_checked); //измененяем иконку кнопки favorite в listview
                                    } else {
                                        count_list_item--;
                                        getSupportActionBar().setTitle("" + count_list_item);
                                        InputDB.update_Data_balance_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_unchecked); //измененяем иконку кнопки favorite в listview
                                    }
                                }
                            }
                            cursor_dataDB.close();
                        }
                        //Если при нажатии на лист уже не ничего выделено
                        if (getSupportActionBar().getTitle().toString().equals("0")) {
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.main);
                            getSupportActionBar().setTitle(R.string.Label);
                            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                            output_balance_adapter = new FavoriteAdapter(MainActivity.this, output_balance_arrayList);
                            output_balance.setAdapter(output_balance_adapter);
                            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                            if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                            }else{
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                            }
                            cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                            //настройка панели
                        }else if(getSupportActionBar().getTitle().toString().equals("1")){
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.menu_template);
                        }else{
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.menu_contexual);
                        }
                    }

                });
                viewholder_adapter_delete.btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cursor_dataDB = InputDB.getListContents_balance();
                        check_equals_while = true;
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_main);
                            int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_favorite);
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    if (cursor_dataDB.getString(int_columnID_delete).equals("0")) {
                                        count_list_item++;
                                        getSupportActionBar().setTitle(""+count_list_item);
                                        InputDB.update_Data_balance_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_checked); //измененяем иконку кнопки favorite в listview
                                    } else {
                                        count_list_item--;
                                        getSupportActionBar().setTitle(""+count_list_item);
                                        InputDB.update_Data_balance_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_unchecked); //измененяем иконку кнопки favorite в listview
                                    }
                                }
                            }
                            cursor_dataDB.close();
                        }
                    }
                });
            } else if (dropdown.getSelectedItem().equals("История ")) {
                if(array_data.get(int_txt).equals(array_data.get(position))) {
                    cursor_dataDB = InputDB.getListContents_history();
                    check_equals_while = true;
                    if (cursor_dataDB.getCount() == 0) {
                    } else {
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_main);
                        int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_delete);
                        while (cursor_dataDB.moveToNext() && check_equals_while) {
                            if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                check_equals_while = false;
                                InputDB.update_Data_history_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_checked); //измененяем иконку кнопки favorite в listview
                            }
                        }
                        cursor_dataDB.close();
                    }
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View view) {
                        cursor_dataDB = InputDB.getListContents_history();
                        check_equals_while = true;
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_main);
                            int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_delete);
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    if (cursor_dataDB.getString(int_columnID_delete).equals("0")) {
                                        count_list_item++;
                                        getSupportActionBar().setTitle("" + count_list_item);
                                        InputDB.update_Data_history_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_checked); //измененяем иконку кнопки favorite в listview
                                    } else {
                                        count_list_item--;
                                        getSupportActionBar().setTitle("" + count_list_item);
                                        InputDB.update_Data_history_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_unchecked); //измененяем иконку кнопки favorite в listview
                                    }
                                }
                            }
                            cursor_dataDB.close();
                        }
                        //Если при нажатии на лист уже не ничего выделено
                        if (getSupportActionBar().getTitle().toString().equals("0")) {
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.main);
                            getSupportActionBar().setTitle(R.string.Label);
                            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                            output_history_adapter = new FavoriteAdapter(MainActivity.this, output_history_arrayList);
                            output_history.setAdapter(output_history_adapter);
                            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                            if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                            }else{
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                            }
                            cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                            //настройка панели
                        }else if(getSupportActionBar().getTitle().toString().equals("1")){
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.menu_template);
                        }else{
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.menu_contexual);
                        }
                    }

                });
                viewholder_adapter_delete.btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cursor_dataDB = InputDB.getListContents_history();
                        check_equals_while = true;
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_main);
                            int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_delete);
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    if (cursor_dataDB.getString(int_columnID_delete).equals("0")) {
                                        count_list_item++;
                                        getSupportActionBar().setTitle(""+count_list_item);
                                        InputDB.update_Data_history_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_checked); //измененяем иконку кнопки favorite в listview
                                    } else {
                                        count_list_item--;
                                        getSupportActionBar().setTitle(""+count_list_item);
                                        InputDB.update_Data_history_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_unchecked); //измененяем иконку кнопки favorite в listview
                                    }
                                }
                            }
                            cursor_dataDB.close();
                        }
                    }
                });
            } else if (dropdown.getSelectedItem().equals("Спасибо ")) {
                if(array_data.get(int_txt).equals(array_data.get(position))) {
                    cursor_dataDB = InputDB.getListContents_thank();
                    check_equals_while = true;
                    if (cursor_dataDB.getCount() == 0) {
                    } else {
                        int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_main);
                        int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_delete);
                        while (cursor_dataDB.moveToNext() && check_equals_while) {
                            if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                check_equals_while = false;
                                InputDB.update_Data_thank_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_checked); //измененяем иконку кнопки favorite в listview
                            }
                        }
                        cursor_dataDB.close();
                    }
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View view) {
                        cursor_dataDB = InputDB.getListContents_thank();
                        check_equals_while = true;
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_main);
                            int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_delete);
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    if (cursor_dataDB.getString(int_columnID_delete).equals("0")) {
                                        count_list_item++;
                                        getSupportActionBar().setTitle("" + count_list_item);
                                        InputDB.update_Data_thank_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_checked); //измененяем иконку кнопки favorite в listview
                                    } else {
                                        count_list_item--;
                                        getSupportActionBar().setTitle("" + count_list_item);
                                        InputDB.update_Data_thank_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_unchecked); //измененяем иконку кнопки favorite в listview
                                    }
                                }
                            }
                            cursor_dataDB.close();
                        }
                        //Если при нажатии на лист уже не ничего выделено
                        if (getSupportActionBar().getTitle().toString().equals("0")) {
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.main);
                            getSupportActionBar().setTitle(R.string.Label);
                            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                            output_thank_adapter = new FavoriteAdapter(MainActivity.this, output_thank_arrayList);
                            output_thank.setAdapter(output_thank_adapter);
                            cursor_dataDB_where_transfer = InputDB.getListContents_where_transfer();cursor_dataDB_where_phone = InputDB.getListContents_where_phone();cursor_dataDB_where_balance = InputDB.getListContents_where_balance();cursor_dataDB_where_history = InputDB.getListContents_where_history();cursor_dataDB_where_thank = InputDB.getListContents_where_thank();
                            if(cursor_dataDB_where_transfer.getCount() == 0 && cursor_dataDB_where_phone.getCount() == 0 && cursor_dataDB_where_balance.getCount() == 0 && cursor_dataDB_where_history.getCount() == 0 && cursor_dataDB_where_thank.getCount() == 0 ) {
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_offline_tab)); //измененяем иконку кнопки favorite в main menu
                            }else{
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_online_tab)); //измененяем иконку кнопки favorite в main menu
                            }
                            cursor_dataDB_where_transfer.close();cursor_dataDB_where_phone.close();cursor_dataDB_where_balance.close();cursor_dataDB_where_history.close();cursor_dataDB_where_thank.close();
                            //настройка панели
                        }else if(getSupportActionBar().getTitle().toString().equals("1")){
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.menu_template);
                        }else{
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.menu_contexual);
                        }
                    }

                });
                viewholder_adapter_delete.btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cursor_dataDB = InputDB.getListContents_thank();
                        check_equals_while = true;
                        if (cursor_dataDB.getCount() == 0) {
                        } else {
                            int_columnID_main = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_main);
                            int_columnID_delete = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_delete);
                            while (cursor_dataDB.moveToNext() && check_equals_while) {
                                if (array_data.get(position).equals(cursor_dataDB.getString(int_columnID_main))) {
                                    check_equals_while = false;
                                    if (cursor_dataDB.getString(int_columnID_delete).equals("0")) {
                                        count_list_item++;
                                        getSupportActionBar().setTitle(""+count_list_item);
                                        InputDB.update_Data_thank_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "1"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_checked); //измененяем иконку кнопки favorite в listview
                                    } else {
                                        count_list_item--;
                                        getSupportActionBar().setTitle(""+count_list_item);
                                        InputDB.update_Data_thank_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                                        viewholder_adapter_delete.btnAction.setBackgroundResource(R.drawable.btn_checkbox_unchecked); //измененяем иконку кнопки favorite в listview
                                    }
                                }
                            }
                            cursor_dataDB.close();
                        }
                    }
                });
            }

            view.setOnCreateContextMenuListener((OnCreateContextMenuListener) context);
            return view;
        }
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        }
        class ViewHolder{
            TextView txtTitle;
            Button btnAction;
        }
    }

    //действия при работе с dropdown
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void dropdown() {
        dropdown.setAdapter(new DropDownAdapter(MainActivity.this, R.layout.spinner_row, items_dropdown));
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view, int position, long id) {
//                Log.d("My_log","DropDown");
                closeKeyboard();
                saveData_dropdown();
                if (dropdown.getSelectedItem().equals("Перевод ")) {
                    text_balance.setVisibility(View.INVISIBLE);
                    text_history.setVisibility(View.INVISIBLE);
                    text_phone.setVisibility(View.INVISIBLE);
                    text_phone_contact.setVisibility(View.INVISIBLE);
                    text_thank.setVisibility(View.INVISIBLE);
                    money_phone.setVisibility(View.INVISIBLE);
                    card.setVisibility(View.INVISIBLE);
                    clear_money_phone.setVisibility(View.INVISIBLE);
                    clear_text_balance.setVisibility(View.INVISIBLE);
                    clear_text_history.setVisibility(View.INVISIBLE);
                    clear_text_phone.setVisibility(View.INVISIBLE);
                    clear_text_thank.setVisibility(View.INVISIBLE);
                    output_balance.setVisibility(View.INVISIBLE);
                    output_history.setVisibility(View.INVISIBLE);
                    output_phone.setVisibility(View.INVISIBLE);
                    output_thank.setVisibility(View.INVISIBLE);
                    text_phone_contact_number.setVisibility(View.INVISIBLE);
                    ic_rubl.setVisibility(View.VISIBLE);
                    output_transfer.setVisibility(View.VISIBLE);
                    money_transfer.setVisibility(View.VISIBLE);
                    text_transfer.setVisibility(View.VISIBLE);
                    text_transfer_contact.setVisibility(View.VISIBLE);
                    text_transfer_comment.setVisibility(View.VISIBLE);
                    text_transfer_contact_number.setVisibility(View.VISIBLE);
                    contact.setVisibility(View.VISIBLE);
                    if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        contact.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_contact); //активирует кнопку контактов, когда есть разрешения
                    }
                    if (text_transfer.getText().toString().isEmpty()) {
                        clear_text_transfer.setVisibility(View.INVISIBLE);
                        send.setEnabled(false);
                    } else {
                        clear_text_transfer.setVisibility(View.VISIBLE);
                        if (!money_transfer.getText().toString().isEmpty()) {
                            send.setEnabled(true);
                        }
                    }
                    if (text_transfer_comment.getText().toString().isEmpty()) {
                        clear_text_comment.setVisibility(View.INVISIBLE);
                    } else {
                        clear_text_comment.setVisibility(View.VISIBLE);
                    }
                    if (money_transfer.getText().toString().isEmpty()) {
                        clear_money_transfer.setVisibility(View.INVISIBLE);
                        //Constraint
//                        constraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);
                        TransitionManager.beginDelayedTransition(constraintLayout);
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(R.id.ic_rubl, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin_rubl1);
                    } else {
                        clear_money_transfer.setVisibility(View.VISIBLE);
                        //Constraint
//                        constraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);
                        TransitionManager.beginDelayedTransition(constraintLayout);
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(R.id.ic_rubl, ConstraintSet.END, R.id.clear_money_transfer, ConstraintSet.START, margin_rubl2);
                    }
                    constraintSet.connect(R.id.send, ConstraintSet.TOP, R.id.text_transfer_comment, ConstraintSet.BOTTOM, margin_dropdown);
                    constraintSet.applyTo(constraintLayout);
                } else if (dropdown.getSelectedItem().equals("Телефон ")) {
//                    saveData_dropdown();
                    text_balance.setVisibility(View.INVISIBLE);
                    text_history.setVisibility(View.INVISIBLE);
                    text_transfer.setVisibility(View.INVISIBLE);
                    text_thank.setVisibility(View.INVISIBLE);
                    text_transfer_contact.setVisibility(View.INVISIBLE);
                    text_transfer_comment.setVisibility(View.INVISIBLE);
                    money_transfer.setVisibility(View.INVISIBLE);
                    card.setVisibility(View.INVISIBLE);
                    clear_money_transfer.setVisibility(View.INVISIBLE);
                    clear_text_balance.setVisibility(View.INVISIBLE);
                    clear_text_history.setVisibility(View.INVISIBLE);
                    clear_text_transfer.setVisibility(View.INVISIBLE);
                    clear_text_thank.setVisibility(View.INVISIBLE);
                    clear_text_comment.setVisibility(View.INVISIBLE);
                    output_balance.setVisibility(View.INVISIBLE);
                    output_history.setVisibility(View.INVISIBLE);
                    output_phone.setVisibility(View.VISIBLE);
                    output_thank.setVisibility(View.INVISIBLE);
                    output_transfer.setVisibility(View.INVISIBLE);
                    text_transfer_contact_number.setVisibility(View.INVISIBLE);
                    ic_rubl.setVisibility(View.VISIBLE);
                    money_phone.setVisibility(View.VISIBLE);
                    text_phone.setVisibility(View.VISIBLE);
                    text_phone_contact.setVisibility(View.VISIBLE);
                    text_phone_contact_number.setVisibility(View.VISIBLE);
                    contact.setVisibility(View.VISIBLE);
                    if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        contact.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_contact); //активирует кнопку контактов, когда есть разрешения
                    }
                    if (text_phone.getText().toString().isEmpty()) {
                        clear_text_phone.setVisibility(View.INVISIBLE);
                        send.setEnabled(false);
                    } else {
                        clear_text_phone.setVisibility(View.VISIBLE);
                        if (!money_phone.getText().toString().isEmpty()) {
                            send.setEnabled(true);
                        }
                    }
                    if (money_phone.getText().toString().isEmpty()) {
                        clear_money_phone.setVisibility(View.INVISIBLE);
                        //Constraint
//                        constraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);
                        TransitionManager.beginDelayedTransition(constraintLayout);
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(R.id.ic_rubl, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin_rubl1);
                    } else {
                        clear_money_phone.setVisibility(View.VISIBLE);
                        //Constraint
//                        constraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);
                        TransitionManager.beginDelayedTransition(constraintLayout);
                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(R.id.ic_rubl, ConstraintSet.END, R.id.clear_money_transfer, ConstraintSet.START, margin_rubl2);
                    }
                    constraintSet.connect(R.id.send, ConstraintSet.TOP, R.id.money_phone, ConstraintSet.BOTTOM, margin_dropdown);
                    constraintSet.applyTo(constraintLayout);
                } else if (dropdown.getSelectedItem().equals("Баланс ")) {
//                    saveData_dropdown();
                    text_history.setVisibility(View.INVISIBLE);
                    text_transfer.setVisibility(View.INVISIBLE);
                    text_thank.setVisibility(View.INVISIBLE);
                    text_transfer_contact.setVisibility(View.INVISIBLE);
                    text_phone.setVisibility(View.INVISIBLE);
                    text_phone_contact.setVisibility(View.INVISIBLE);
                    text_transfer_comment.setVisibility(View.INVISIBLE);
                    money_transfer.setVisibility(View.INVISIBLE);
                    money_phone.setVisibility(View.INVISIBLE);
                    contact.setVisibility(View.INVISIBLE);
                    clear_money_transfer.setVisibility(View.INVISIBLE);
                    clear_money_phone.setVisibility(View.INVISIBLE);
                    clear_text_transfer.setVisibility(View.INVISIBLE);
                    clear_text_history.setVisibility(View.INVISIBLE);
                    clear_text_phone.setVisibility(View.INVISIBLE);
                    clear_text_thank.setVisibility(View.INVISIBLE);
                    clear_text_comment.setVisibility(View.INVISIBLE);
                    output_balance.setVisibility(View.VISIBLE);
                    output_history.setVisibility(View.INVISIBLE);
                    output_phone.setVisibility(View.INVISIBLE);
                    output_thank.setVisibility(View.INVISIBLE);
                    output_transfer.setVisibility(View.INVISIBLE);
                    ic_rubl.setVisibility(View.INVISIBLE);
                    text_balance.setVisibility(View.VISIBLE);
                    text_phone_contact_number.setVisibility(View.INVISIBLE);
                    text_transfer_contact_number.setVisibility(View.INVISIBLE);
//                    Drawable icon=this.getResources().getDrawable( R.drawable.ic_card);
                    if (check_dialog) {
                        card.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_card);
                    } else {
                        card.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_card_empty);
                    }
                    card.setVisibility(View.VISIBLE);
                    send.setEnabled(true);
                    if (text_balance.getText().toString().isEmpty()) {
                        clear_text_balance.setVisibility(View.INVISIBLE);
                    } else {
                        clear_text_balance.setVisibility(View.VISIBLE);
                    }
                    //Constraint
//                    constraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.send, ConstraintSet.TOP, R.id.text_balance, ConstraintSet.BOTTOM, margin_dropdown);
                    constraintSet.applyTo(constraintLayout);
                } else if (dropdown.getSelectedItem().equals("История ")) {
//                    saveData_dropdown();
                    text_balance.setVisibility(View.INVISIBLE);
                    text_transfer.setVisibility(View.INVISIBLE);
                    text_transfer_contact.setVisibility(View.INVISIBLE);
                    text_thank.setVisibility(View.INVISIBLE);
                    text_phone.setVisibility(View.INVISIBLE);
                    text_phone_contact.setVisibility(View.INVISIBLE);
                    text_transfer_comment.setVisibility(View.INVISIBLE);
                    money_transfer.setVisibility(View.INVISIBLE);
                    money_phone.setVisibility(View.INVISIBLE);
                    contact.setVisibility(View.INVISIBLE);
                    clear_money_transfer.setVisibility(View.INVISIBLE);
                    clear_money_phone.setVisibility(View.INVISIBLE);
                    clear_text_transfer.setVisibility(View.INVISIBLE);
                    clear_text_balance.setVisibility(View.INVISIBLE);
                    clear_text_phone.setVisibility(View.INVISIBLE);
                    clear_text_thank.setVisibility(View.INVISIBLE);
                    clear_text_comment.setVisibility(View.INVISIBLE);
                    output_balance.setVisibility(View.INVISIBLE);
                    output_history.setVisibility(View.VISIBLE);
                    output_phone.setVisibility(View.INVISIBLE);
                    output_thank.setVisibility(View.INVISIBLE);
                    output_transfer.setVisibility(View.INVISIBLE);
                    ic_rubl.setVisibility(View.INVISIBLE);
                    text_history.setVisibility(View.VISIBLE);
                    text_phone_contact_number.setVisibility(View.INVISIBLE);
                    text_transfer_contact_number.setVisibility(View.INVISIBLE);
                    if (check_dialog) {
                        card.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_card);
                    } else {
                        card.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_card_empty);
                    }
                    card.setVisibility(View.VISIBLE);
                    send.setEnabled(true);
                    if (text_history.getText().toString().isEmpty()) {
//                        send.setEnabled(false);
                        clear_text_history.setVisibility(View.INVISIBLE);
                    } else {
//                        send.setEnabled(true);
                        clear_text_history.setVisibility(View.VISIBLE);
                    }
//                    if(check_dialog == true) { card.setEnabled(true); }else{ card.setEnabled(false); }
                    //Constraint
//                    constraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.send, ConstraintSet.TOP, R.id.text_history, ConstraintSet.BOTTOM, margin_dropdown);
                    constraintSet.applyTo(constraintLayout);
                } else if (dropdown.getSelectedItem().equals("Спасибо ")) {
//                    saveData_dropdown();
                    text_balance.setVisibility(View.INVISIBLE);
                    text_transfer.setVisibility(View.INVISIBLE);
                    text_transfer_contact.setVisibility(View.INVISIBLE);
                    text_history.setVisibility(View.INVISIBLE);
                    text_phone.setVisibility(View.INVISIBLE);
                    text_phone_contact.setVisibility(View.INVISIBLE);
                    text_transfer_comment.setVisibility(View.INVISIBLE);
                    money_transfer.setVisibility(View.INVISIBLE);
                    money_phone.setVisibility(View.INVISIBLE);
                    contact.setVisibility(View.INVISIBLE);
                    clear_money_transfer.setVisibility(View.INVISIBLE);
                    clear_money_phone.setVisibility(View.INVISIBLE);
                    clear_text_transfer.setVisibility(View.INVISIBLE);
                    clear_text_balance.setVisibility(View.INVISIBLE);
                    clear_text_phone.setVisibility(View.INVISIBLE);
                    clear_text_history.setVisibility(View.INVISIBLE);
                    clear_text_comment.setVisibility(View.INVISIBLE);
                    output_balance.setVisibility(View.INVISIBLE);
                    output_history.setVisibility(View.INVISIBLE);
                    output_phone.setVisibility(View.INVISIBLE);
                    output_thank.setVisibility(View.VISIBLE);
                    output_transfer.setVisibility(View.INVISIBLE);
                    ic_rubl.setVisibility(View.INVISIBLE);
                    text_thank.setVisibility(View.VISIBLE);
                    text_phone_contact_number.setVisibility(View.INVISIBLE);
                    text_transfer_contact_number.setVisibility(View.INVISIBLE);
                    if (check_dialog) {
                        card.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_card);
                    } else {
                        card.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_card_empty);
                    }
                    card.setVisibility(View.VISIBLE);
                    if (text_thank.getText().toString().isEmpty()) {
                        send.setEnabled(false);
                        clear_text_thank.setVisibility(View.INVISIBLE);
                    } else {
                        send.setEnabled(true);
                        clear_text_thank.setVisibility(View.VISIBLE);
                    }
                    //Constraint
//                    constraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.send, ConstraintSet.TOP, R.id.text_thank, ConstraintSet.BOTTOM, margin_dropdown);
                    constraintSet.applyTo(constraintLayout);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @SuppressLint("UnlocalizedSms")
    public void onButtonSend(View v) {
//        check_button_send=true;
        closeKeyboard();
//        if(count_receive_message>2){
//            count_receive_message_boolean=true;
//        }
        //спрашиваем разрешения отправлять SMS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, REQUEST_CODE_PERMISSION);
            onRequestPermissions_sms();
            isPermissionGiven_read_sms = true;
            isPermissionGiven_read_contacts = false;
            isPermissionGiven_read_sms_dropdown = false;
        } else {
            if (dropdown.getSelectedItem().toString().equals("Перевод ")) {
                saveData_transfer();
                smsManager.sendTextMessage("900", null, dropdown.getSelectedItem().toString() + text_transfer.getText().toString() + " " + money_transfer.getText().toString(), null, null);
                try {
                    pd_time_wait();
                } catch (ActivityNotFoundException ex) {
                    Toast toast = Toast.makeText(MainActivity.this, "Запрос не отправлен, пожалуйста, попробуйте позже.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            } else if (dropdown.getSelectedItem().toString().equals("Телефон ")) {
                saveData_phone();
                smsManager.sendTextMessage("900", null, dropdown.getSelectedItem().toString() + text_phone.getText().toString() + " " + money_phone.getText().toString(), null, null);
                try {
                    pd_time_wait();
                } catch (ActivityNotFoundException ex) {
                    Toast toast = Toast.makeText(MainActivity.this, "Запрос не отправлен, пожалуйста, попробуйте позже.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            } else if (dropdown.getSelectedItem().equals("Баланс ")) {
                check_put_button_send = true; //включаем проверку клавиши send, чтоб нас пустило в метод public void messageReceived(final String message)
                saveData_balance();
                smsManager.sendTextMessage("900", null, dropdown.getSelectedItem().toString() + text_balance.getText().toString(), null, null);
                try {
                    pd_time_wait();
                } catch (ActivityNotFoundException ex) {
                    Toast toast = Toast.makeText(MainActivity.this, "Запрос не отправлен, пожалуйста, попробуйте позже.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
//                }
            } else if (dropdown.getSelectedItem().equals("История ")) {
                check_put_button_send = true; //включаем проверку клавиши send, чтоб нас пустило в метод public void messageReceived(final String message)
                saveData_history();
                smsManager.sendTextMessage("900", null, dropdown.getSelectedItem().toString() + " " + text_history.getText().toString(), null, null);
                try {
                    pd_time_wait();
                } catch (ActivityNotFoundException ex) {
                    Toast toast = Toast.makeText(MainActivity.this, "Запрос не отправлен, пожалуйста, попробуйте позже.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            } else if (dropdown.getSelectedItem().equals("Спасибо ")) {
                check_put_button_send = true; //включаем проверку клавиши send, чтоб нас пустило в метод public void messageReceived(final String message)
                saveData_thank();
                smsManager.sendTextMessage("900", null, dropdown.getSelectedItem().toString() + " " + text_thank.getText().toString(), null, null);
                try {
                    pd_time_wait();
                } catch (ActivityNotFoundException ex) {
                    Toast toast = Toast.makeText(MainActivity.this, "Запрос не отправлен, пожалуйста, попробуйте позже.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }
        }
    }

    public void pd_time_wait() {
        check_button_send = true;
        message = "Ответ на запрос не получен, пожалуйста, попробуйте позже. Возможно у Вас превышен лимит в 20 запросов, в сутки или не пришло запрашиваемое SMS-сообщение.";
        progress_dialog.setTitle("Запрос отправлен");
        progress_dialog.setMessage("Ожидание ответа...");
        progress_dialog.setCancelable(false);
        progress_dialog.setIndeterminate(true);
        progress_dialog.show();
        runnable = new Runnable() {
            @Override
            public void run() {
                progress_dialog.cancel();
                dialog_confirm = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle);
                dialog_confirm.setMessage(message)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = dialog_confirm.create();
                alert.setTitle("Уведомление");
                alert.show();
//                Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
//                toast.show();
                notification(message);
            }
        };
        handler.postDelayed(runnable, 120000);
    }

    public void onButtonCard(View v) {
        closeKeyboard();
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            onRequestPermissions_sms();
            isPermissionGiven_read_sms_dropdown = true;
            isPermissionGiven_read_contacts = false;
            isPermissionGiven_read_sms = false;
        } else if (!check_dialog) {
            dialog_get_card
                    .setTitle("Получить список карт?")
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @SuppressLint("UnlocalizedSms")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pd_time_wait();
                            check_update_card_dialog = true;
//                            check_button_send=true;
                            smsManager.sendTextMessage("900", null, "Справка", null, null);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = dialog_get_card.create();
            //2. now setup to change color of the button
//            alert.setOnShowListener( new DialogInterface.OnShowListener() {
//                @Override
//                public void onShow(DialogInterface arg0) {
//                    alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
//                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
//                }
//            });
            alert.show();
        } else {
            onCreateDialog();
//        dialog_card_list = adb.;
//        dialog_card_list.setTitle("Выберите номер карты");
//        alert.show();
//            return dialog_card_list;
//            showDialog(IDD_THREE_BUTTONS);
        }
//        Intent intent = new Intent(".CardList");
//        startActivity(intent);
    }

//    @Override
//    protected Dialog onCreateDialog(int id) {

    //выбираем карты------------------------------------------------------------------------------------------------------------
    public void onCreateDialog() {
        //создаем алерт диалог и меняем цвет текста
//        adb = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        dialog_main.setAdapter(adapter_card, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int which) {
//            lv = ((AlertDialog) dialog).getListView();
//            if (which == Dialog.BUTTON_POSITIVE){
//                String get_item_Adapter = adapter_card.getItem(which).split("\\(")[0].substring(adapter_card.getItem(which).split("\\(")[0].length()-4);
//                String get_itemAdapter = get_item_Adapter.toString().substring(.length(-4));
                if (dropdown.getSelectedItem().toString().equals("Баланс ")) {
//                    text_balance.setText(adapter_card.getItem(which).split("\\(")[0].substring(adapter_card.getItem(which).split("\\(")[0].length()-4));
                    text_balance.setText(Objects.requireNonNull(adapter_card.getItem(which)).split("\\(")[0].substring(Objects.requireNonNull(adapter_card.getItem(which)).split("\\(")[0].length() - 4));
                } else if (dropdown.getSelectedItem().toString().equals("История ")) {
                    text_history.setText(Objects.requireNonNull(adapter_card.getItem(which)).split("\\(")[0].substring(Objects.requireNonNull(adapter_card.getItem(which)).split("\\(")[0].length() - 4));
                } else if (dropdown.getSelectedItem().toString().equals("Спасибо ")) {
                    text_thank.setText(Objects.requireNonNull(adapter_card.getItem(which)).split("\\(")[0].substring(Objects.requireNonNull(adapter_card.getItem(which)).split("\\(")[0].length() - 4));
                }
            }
        });
        dialog_main.setPositiveButton(R.string.update, myClickListener);
        dialog_main.setNegativeButton(R.string.cancel, myClickListener);
        dialog_card_list = dialog_main.create();
        //2. now setup to change color of the button
//    dialog_card_list.setOnShowListener( new DialogInterface.OnShowListener() {
//        @Override
//        public void onShow(DialogInterface arg0) {
//            dialog_card_list.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
//            dialog_card_list.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
//        }
//    });
        dialog_card_list.show();
//        dialog_main.setCursor(1, myClickListener)
//        dialog_card_list = null;
//        dialog_card_list = dialog_main.create();
//        dialog_card_list = adb.;
//        dialog_card_list.setTitle("Выберите номер карты");
//        alert.show();
//        return dialog_card_list;
//        dialog_card_list.show();
//        return null;
    }

    //         обработчик нажатия на пункт списка диалога или кнопку
    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        @SuppressLint("UnlocalizedSms")
        public void onClick(DialogInterface dialog, int which) {
//            lv = ((AlertDialog) dialog).getListView();
//            if (which == Dialog.BUTTON_POSITIVE){
//                if(dropdown.getSelectedItem().toString().equals("Баланс ")){
//                    text_balance.setText(adapter_card.getItem(which).split("-")[0]);
//                }else if(dropdown.getSelectedItem().toString().equals("История ")){
//                    text_history.setText(adapter_card.getItem(which).split("-")[0]);
//                }else if(dropdown.getSelectedItem().toString().equals("Спасибо ")){
//                    text_thank.setText(adapter_card.getItem(which).split("-")[0]);
//                }
////            if (which == Dialog.BUTTON_NEGATIVE){
//                dialog.dismiss();
            if (which == Dialog.BUTTON_POSITIVE) {
                pd_time_wait();
                check_card = false;
                check_update_card_dialog = true;
//                check_button_send=true;
                smsManager.sendTextMessage("900", null, "Справка", null, null);
            }
            if (which == Dialog.BUTTON_NEGATIVE) {
                dialog.cancel();
            }
        }
    };

    //кнопки очищающие тектосвые боксы
    public void onButtonClear_clear_money_transfer(View v) {
        money_transfer.setText(null);
        money_transfer.requestFocus();
        openKeyboard();
    }

    public void onButtonClear_clear_money_phone(View v) {
        money_phone.setText(null);
        money_phone.requestFocus();
        openKeyboard();
    }

    public void onButtonClear_clear_text_transfer(View v) {
        text_transfer.setText(null);
        text_transfer_contact.setText(null);
        text_transfer_contact_number.setText(null);
        text_transfer.requestFocus();
        openKeyboard();
    }

    public void onButtonClear_clear_text_comment(View v) {
        text_transfer_comment.setText(null);
        text_transfer_comment.requestFocus();
        openKeyboard();
    }

    public void onButtonClear_clear_text_phone(View v) {
        text_phone.setText(null);
        text_phone_contact.setText(null);
        text_phone_contact_number.setText(null);
        text_phone.requestFocus();
        openKeyboard();
    }

    public void onButtonClear_clear_text_balance(View v) {
        text_balance.setText(null);
        text_balance.requestFocus();
        openKeyboard();
    }

    public void onButtonClear_clear_text_history(View v) {
        text_history.setText(null);
        text_history.requestFocus();
        openKeyboard();
    }

    public void onButtonClear_clear_text_thank(View v) {
        text_thank.setText(null);
        text_thank.requestFocus();
        openKeyboard();
    }

    //сохранение кэша
//Затем добавьте следующий метод в MainActivity для обновления пользовательского интерфейса
    private void LoadData_output_Cash() {
        cursor_dataDB = InputDB.getListContents_transfer(); //Cursor
        if (cursor_dataDB.getCount() == 0) {
        } else {
            int idx = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_TRANSFER_main);
            while (cursor_dataDB.moveToNext()) {
                InputDB.update_Data_transfer_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                output_transfer_arrayList.add(cursor_dataDB.getString(idx));
                output_transfer_adapter.notifyDataSetChanged();
            }
        }
        cursor_dataDB.close();

        cursor_dataDB = InputDB.getListContents_phone(); //Cursor
        if (cursor_dataDB.getCount() == 0) {
        } else {
            int idx = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_PHONE_main);
            while (cursor_dataDB.moveToNext()) {
                InputDB.update_Data_phone_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                output_phone_arrayList.add(cursor_dataDB.getString(idx));
                output_phone_adapter.notifyDataSetChanged();
            }
        }
        cursor_dataDB.close();

        cursor_dataDB = InputDB.getListContents_balance(); //Cursor
        if (cursor_dataDB.getCount() == 0) {
        } else {
            int idx = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_BALANCE_main);
            while (cursor_dataDB.moveToNext()) {
                InputDB.update_Data_balance_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                output_balance_arrayList.add(cursor_dataDB.getString(idx));
                output_balance_adapter.notifyDataSetChanged();
            }
        }
        cursor_dataDB.close();

        cursor_dataDB = InputDB.getListContents_history(); //Cursor
        if (cursor_dataDB.getCount() == 0) {
        } else {
            int idx = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_HISTORY_main);
            while (cursor_dataDB.moveToNext()) {
                InputDB.update_Data_history_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                output_history_arrayList.add(cursor_dataDB.getString(idx));
                output_history_adapter.notifyDataSetChanged();
            }
        }
        cursor_dataDB.close();

        cursor_dataDB = InputDB.getListContents_thank(); //Cursor
        if (cursor_dataDB.getCount() == 0) {
        } else {
            int idx = cursor_dataDB.getColumnIndex(InputContract.TableEntry.COL_THANK_main);
            while (cursor_dataDB.moveToNext()) {
                InputDB.update_Data_thank_delete(cursor_dataDB.getInt(cursor_dataDB.getColumnIndex(InputContract.TableEntry._ID)), "0"); //записываем значение в базу
                output_thank_arrayList.add(cursor_dataDB.getString(idx));
                output_thank_adapter.notifyDataSetChanged();
            }
        }
        cursor_dataDB.close();
    }

    public void saveData_card() {
        SharedPreferences.Editor editor_card = sharedPreferences.edit();
        //Retrieve the values
//        Set<String> set = editor_card.getStringSet("key", null);
        Set<String> set = new HashSet<>(list_balance);
        editor_card.putStringSet(TEXT_card, set);
        editor_card.putBoolean(CHECK_card, true);
        editor_card.apply();
    }

    public void saveData_transfer() {
        SharedPreferences.Editor editor_transfer = sharedPreferences.edit();
        editor_transfer.putString(TEXT_autoCompleteTextView1_transfer, text_transfer.getText().toString());
        editor_transfer.putString(TEXT_text_transfer_contact, text_transfer_contact.getText().toString());
        editor_transfer.putString(TEXT_money_transfer, money_transfer.getText().toString());
        editor_transfer.putString(TEXT_text_transfer_comment, text_transfer_comment.getText().toString());
        editor_transfer.putString(TEXT_text_transfer_contact_number, text_transfer_contact_number.getText().toString());
        editor_transfer.apply();
    }

    public void saveData_phone() {
        SharedPreferences.Editor editor_phone = sharedPreferences.edit();
        editor_phone.putString(TEXT_autoCompleteTextView1_phone, text_phone.getText().toString());
        editor_phone.putString(TEXT_text_phone_contact, text_phone_contact.getText().toString());
        editor_phone.putString(TEXT_text_phone_contact_number, text_phone_contact_number.getText().toString());
        editor_phone.putString(TEXT_money_phone, money_phone.getText().toString());
        editor_phone.apply();
    }

    public void saveData_balance() {
        SharedPreferences.Editor editor_balance = sharedPreferences.edit();
        editor_balance.putString(TEXT_autoCompleteTextView2_balance, text_balance.getText().toString());
        editor_balance.apply();
    }

    public void saveData_history() {
        SharedPreferences.Editor editor_history = sharedPreferences.edit();
        editor_history.putString(TEXT_autoCompleteTextView2_history, text_history.getText().toString());
        editor_history.apply();
    }

    public void saveData_thank() {
        SharedPreferences.Editor editor_thank = sharedPreferences.edit();
        editor_thank.putString(TEXT_autoCompleteTextView2_thank, text_thank.getText().toString());
        editor_thank.apply();
    }

    public void saveData_dropdown() {
        SharedPreferences.Editor editor_dropdown = sharedPreferences.edit();
        editor_dropdown.putInt(TEXT_dropdown, dropdown.getSelectedItemPosition());
        editor_dropdown.apply();
    }

    public void save_switch_text(Intent intent) {
        switch_boolean_text = intent.getBooleanExtra("switchOnOff_text", false);
        SharedPreferences.Editor editor_text = sharedPreferences.edit();
        editor_text.putBoolean(save_switch_text, switch_boolean_text);
        editor_text.apply();
    }

    public void save_switch_vibrate(Intent intent) {
        switch_boolean_vibrate = intent.getBooleanExtra("switchOnOff_vibrate", false);
        SharedPreferences.Editor editor_vibrate = sharedPreferences.edit();
        editor_vibrate.putBoolean(save_switch_vibrate, switch_boolean_vibrate);
        editor_vibrate.apply();
    }

    public void save_switch_sound(Intent intent) {
        switch_boolean_sound = intent.getBooleanExtra("switchOnOff_sound", false);
        SharedPreferences.Editor editor_sound = sharedPreferences.edit();
        editor_sound.putBoolean(save_switch_sound, switch_boolean_sound);
        editor_sound.apply();
    }

    //загрузка кэша
    public void loadData_card() {
//        if (sharedPreferences.getBoolean(CHECK_card, false)) {
//            list_balance = new ArrayList<String>();
//            final String[] list_card = sharedPreferences.getString(TEXT_card, "").split("\\r?\\n");
//            final String[] list_card1 = list_card[1].toString().split("\\)");
//            for (int i = 0; i < list_card1.length; i++) {
////                if (list_card[i].toString().contains(": ")) {
//                list_balance.add(list_card1[i].toString().replace("(","-"));
////                list_balance.add(list_card[i].toString());
////                }
//            }
//            adapter_card = new ArrayAdapter<>(this, android.R.layout.select_dialog_singlechoice, list_balance);
//            check_dialog = true;
//        }
        if (sharedPreferences.getBoolean(CHECK_card, false)) {
            list_balance = new ArrayList<>();
            Set<String> set = sharedPreferences.getStringSet(TEXT_card, null);
            if (set != null) {
                list_balance.addAll(set);
            }
            adapter_card = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, list_balance);
            check_dialog = true;
        }
    }

    public void loadData_transfer() {
        text_transfer.setText(sharedPreferences.getString(TEXT_autoCompleteTextView1_transfer, ""));
        text_transfer_contact.setText(sharedPreferences.getString(TEXT_text_transfer_contact, ""));
        text_transfer_contact_number.setText(sharedPreferences.getString(TEXT_text_transfer_contact_number, ""));
        money_transfer.setText(sharedPreferences.getString(TEXT_money_transfer, ""));
        text_transfer_comment.setText(sharedPreferences.getString(TEXT_text_transfer_comment, ""));
    }

    public void loadData_phone() {
        text_phone.setText(sharedPreferences.getString(TEXT_autoCompleteTextView1_phone, ""));
        text_phone_contact.setText(sharedPreferences.getString(TEXT_text_phone_contact, ""));
        text_phone_contact_number.setText(sharedPreferences.getString(TEXT_text_phone_contact_number, ""));
        money_phone.setText(sharedPreferences.getString(TEXT_money_phone, ""));
    }

    public void loadData_balance() {
        text_balance.setText(sharedPreferences.getString(TEXT_autoCompleteTextView2_balance, ""));
    }

    public void loadData_history() {
        text_history.setText(sharedPreferences.getString(TEXT_autoCompleteTextView2_history, ""));
    }

    public void loadData_thank() {
        text_thank.setText(sharedPreferences.getString(TEXT_autoCompleteTextView2_thank, ""));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void loadData_dropdown() {
        dropdown.setSelection(sharedPreferences.getInt(TEXT_dropdown, 0));
//        constraintLayout = (ConstraintLayout) findViewById(R.id.activity_main);
        constraintSet.clone(constraintLayout);
        TransitionManager.beginDelayedTransition(constraintLayout);
//                constraintSet.clear(R.id.send, ConstraintSet.TOP);
        if (dropdown.getSelectedItem().equals("Перевод ")) {
            if (money_transfer.getText().toString().isEmpty()) {
//                clear_money_transfer.setVisibility(View.INVISIBLE);
                constraintSet.connect(R.id.ic_rubl, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin_rubl1);
            } else {
//                clear_money_transfer.setVisibility(View.VISIBLE);
                constraintSet.connect(R.id.ic_rubl, ConstraintSet.END, R.id.clear_money_transfer, ConstraintSet.START, margin_rubl2);
            }
            constraintSet.connect(R.id.send, ConstraintSet.TOP, R.id.text_transfer_comment, ConstraintSet.BOTTOM, margin_dropdown);
        } else if (dropdown.getSelectedItem().equals("Телефон ")) {
            if (money_phone.getText().toString().isEmpty()) {
//                clear_money_transfer.setVisibility(View.INVISIBLE);
                constraintSet.connect(R.id.ic_rubl, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, margin_rubl1);
            } else {
//                clear_money_transfer.setVisibility(View.VISIBLE);
                constraintSet.connect(R.id.ic_rubl, ConstraintSet.END, R.id.clear_money_phone, ConstraintSet.START, margin_rubl2);
            }
            constraintSet.connect(R.id.send, ConstraintSet.TOP, R.id.money_phone, ConstraintSet.BOTTOM, margin_dropdown);
        } else if (dropdown.getSelectedItem().equals("Баланс ")) {
            constraintSet.connect(R.id.send, ConstraintSet.TOP, R.id.text_balance, ConstraintSet.BOTTOM, margin_dropdown);
        } else if (dropdown.getSelectedItem().equals("История ")) {
            constraintSet.connect(R.id.send, ConstraintSet.TOP, R.id.text_history, ConstraintSet.BOTTOM, margin_dropdown);
        } else if (dropdown.getSelectedItem().equals("Спасибо ")) {
            constraintSet.connect(R.id.send, ConstraintSet.TOP, R.id.text_thank, ConstraintSet.BOTTOM, margin_dropdown);
        }
        constraintSet.applyTo(constraintLayout);
    }

    public void load_switch_text() {
        switch_boolean_text = sharedPreferences.getBoolean(save_switch_text, true);
    }

    public void load_switch_vibrate() {
        switch_boolean_vibrate = sharedPreferences.getBoolean(save_switch_vibrate, true);
    }

    public void load_switch_sound() {
        switch_boolean_sound = sharedPreferences.getBoolean(save_switch_sound, true);
    }

    //отключение клавиуатуры
    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //включить клавиуатуры
    public void openKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
