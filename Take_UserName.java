package com.deeefoo.myappl;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class Take_UserName extends AppCompatActivity {
    EditText fullname;
    Button procbtn;
    FirebaseDatabase fbDatabase;
    DatabaseReference dref,activeusersdbref;
    User user;
    ImageView c;
    String ph_no;
    private int Points=0;
    private ConstraintLayout clayout;
    private LinearLayout border_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.activity_take__user_name);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        fullname=(EditText) findViewById(R.id.fullname);
        c=findViewById(R.id.imageView_username);
        procbtn=(Button) findViewById(R.id.procbtn);
        border_name=findViewById(R.id.border_entername);
        fbDatabase= FirebaseDatabase.getInstance();
        activeusersdbref=fbDatabase.getReference();
        dref=fbDatabase.getReference("Users");
        clayout=findViewById(R.id.user_layout);
        clayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname.setFocusable(false);
            }
        });
        fullname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                fullname.setFocusable(true);
                fullname.setFocusableInTouchMode(true);
                return false;
            }
        });
        procbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!fullname.getText().toString().isEmpty())
                {
                    getValues();
                //active user k  node bana kr har number k status 1 krdo aur logout pr 0.
                    dref.child(Login_Activity.user_phone_no).setValue(user);
                    Intent intent = new Intent(Take_UserName.this,user_guidance_activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        final AnimatedVectorDrawable avd;
        final AnimatedVectorDrawable avd_r;
        if (Build.VERSION.SDK_INT >= 21) {
            avd =(AnimatedVectorDrawable)getDrawable(R.drawable.round_anim);
            avd_r =(AnimatedVectorDrawable)getDrawable(R.drawable.round_anim_r);
            c.setImageDrawable(avd);

            fullname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b){
                        ObjectAnimator anim= ObjectAnimator.ofFloat(border_name,"translationY",-120f);
                        anim.setDuration(400);
                        anim.start();
                        ObjectAnimator anim1= ObjectAnimator.ofFloat(procbtn,"translationY",-100f);
                        anim1.setDuration(400);
                        anim1.start();
//                        fullname.setTranslationY(-290f);
//                        procbtn.setTranslationY(-325);//anim=true;
                        c.setImageDrawable(avd);

                    }
                    if(!b)
                    {   ObjectAnimator anim= ObjectAnimator.ofFloat(border_name,"translationY",0f);
                        anim.setDuration(400);
                        anim.start();
                        ObjectAnimator anim1= ObjectAnimator.ofFloat(procbtn,"translationY",0f);
                        anim1.setDuration(400);
                        anim1.start();
//                        fullname.setTranslationY(0f);
//                        procbtn.setTranslationY(0);
                        c.setImageDrawable(avd_r);

                    }
                    AnimatedVectorDrawable animatedVectorDrawable=(AnimatedVectorDrawable) c.getDrawable();
                    Drawable drawable=animatedVectorDrawable.getCurrent();
                    if(drawable instanceof Animatable)
                    {
                        ((Animatable) drawable).start();
                    }

                }
            });}

    }

    private void getValues()
    {
        user = new User(Login_Activity.user_phone_no,fullname.getText().toString(),Points);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
