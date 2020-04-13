package com.deeefoo.myappl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash_final extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser fbuser;
    private final int time=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {if (Build.VERSION.SDK_INT >= 21) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //  getWindow().setStatusBarColor(this.getResources().getColor(R.color.Main));
    }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_test);
        // getSupportActionBar().hide();
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        mAuth= FirebaseAuth.getInstance();
        fbuser=mAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(fbuser!=null)
                {
                    Verifying.stop_signup_during_login=true;
                    Intent intent = new Intent(splash_final.this,MainActivity.class);
                    startActivity(intent);
                    splash_final.this.finish();
                }

                else if(Verifying.stop_signup_during_login==false) {
                    Intent intent = new Intent(splash_final.this, Login_Activity.class);
                    startActivity(intent);
                    splash_final.this.finish();
                }
            }
        },time);
    }
        
    }

