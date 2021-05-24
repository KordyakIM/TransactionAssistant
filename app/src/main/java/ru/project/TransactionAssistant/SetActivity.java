package ru.project.TransactionAssistant;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.RequiresApi;
import androidx.annotation.RequiresApi;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.Objects;

//import ru.project.MBank.R;

//import static android.app.PendingIntent.getActivity;

public class SetActivity extends AppCompatActivity {
    Switch switch_text, switch_vibrate, switch_sound;
//    NotificationManager notificationManager;
    SharedPreferences sharedPreferences;
    public static final String save_switch_text = "switch_text";
    public static final String save_switch_vibrate = "switch_vibrate";
    public static final String save_switch_sound = "switch_sound";
    Toolbar toolbar;
    Boolean switchOnOff_text, switchOnOff_vibrate, switchOnOff_sound;
    Intent intent_main;

    @SuppressLint("PrivateResource")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_set);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.cardview_shadow_start_color), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorGreen), PorterDuff.Mode.SRC_ATOP);
                 onBackPressed();
             }
         });
        switch_text = findViewById(R.id.switch_text);
        switch_vibrate = findViewById(R.id.switch_vibrate);
        switch_sound = findViewById(R.id.switch_sound);
        sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        intent_main = new Intent(this, MainActivity.class);

        switch_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                save_switch_text();
            }
        });
        switch_vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                save_switch_vibrate();
            }
        });
        switch_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                save_switch_sound();
            }
        });
        load_switch_text();
        load_switch_vibrate();
        load_switch_sound();
    }
//    private List<Uri> loadLocalRingtonesUris() {
//        List<Uri> alarms = new ArrayList<>();
//        try {
//            RingtoneManager ringtoneMgr = new RingtoneManager(this);
//            ringtoneMgr.setType(RingtoneManager.TYPE_RINGTONE);
//
//            Cursor alarmsCursor = ringtoneMgr.getCursor();
//            int alarmsCount = alarmsCursor.getCount();
//            if (alarmsCount == 0 && !alarmsCursor.moveToFirst()) {
//                alarmsCursor.close();
//                return null;
//            }
//
//            while (!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
//                int currentPosition = alarmsCursor.getPosition();
//                alarms.add(ringtoneMgr.getRingtoneUri(currentPosition));
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return alarms;
//    }
    public void save_switch_text() {
        SharedPreferences.Editor editor_text = sharedPreferences.edit();
        editor_text.putBoolean(save_switch_text, switch_text.isChecked());
        editor_text.apply();
        intent_main.putExtra("switchOnOff_text",switch_text.isChecked());
        setResult(RESULT_OK, intent_main);
    }
    public void save_switch_vibrate() {
        SharedPreferences.Editor editor_vibrate = sharedPreferences.edit();
        editor_vibrate.putBoolean(save_switch_vibrate, switch_vibrate.isChecked());
        editor_vibrate.apply();
        intent_main.putExtra("switchOnOff_vibrate",switch_vibrate.isChecked());
        setResult(RESULT_OK, intent_main);
    }
    public void save_switch_sound() {
        SharedPreferences.Editor editor_sound = sharedPreferences.edit();
        editor_sound.putBoolean(save_switch_sound, switch_sound.isChecked());
        editor_sound.apply();
        intent_main.putExtra("switchOnOff_sound",switch_sound.isChecked());
        setResult(RESULT_OK, intent_main);
    }
    public void load_switch_text() {
        switchOnOff_text = sharedPreferences.getBoolean(save_switch_text, true);
        switch_text.setChecked(switchOnOff_text);
    }
    public void load_switch_vibrate() {
        switchOnOff_vibrate = sharedPreferences.getBoolean(save_switch_vibrate, true);
        switch_vibrate.setChecked(switchOnOff_vibrate);
    }
    public void load_switch_sound() {
        switchOnOff_sound = sharedPreferences.getBoolean(save_switch_sound, true);
        switch_sound.setChecked(switchOnOff_sound);
    }
}
