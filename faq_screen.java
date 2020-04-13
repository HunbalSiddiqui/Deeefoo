package com.deeefoo.myappl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

public class faq_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_screen);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getSupportActionBar().hide();

    }
}
