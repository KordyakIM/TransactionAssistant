package ru.project.TransactionAssistant;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.RequiresApi;
import androidx.annotation.RequiresApi;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

//import ru.project.MBank.R;

public class AboutProgram extends AppCompatActivity {
    TextView info_about;
    Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("PrivateResource")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_about_program);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        info_about = findViewById(R.id.info_about);
        info_about.setMovementMethod(new ScrollingMovementMethod());
        info_about.setText(R.string.about);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.cardview_shadow_start_color), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorGreen), PorterDuff.Mode.SRC_ATOP);
                onBackPressed();
            }
        });
    }
}
