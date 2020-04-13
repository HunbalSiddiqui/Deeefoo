package com.deeefoo.myappl;

import androidx.annotation.NonNull;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Enter_code extends AppCompatActivity {
    private EditText codenteredtxt;
    private Button btnconfirmed;
    private TextView resend;
    private FirebaseAuth mAuth;
    private ImageView b;
    private ProgressBar progressbar3;
    private ConstraintLayout codelayout;
    private LinearLayout border_entercode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.activity_enter_code);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        codelayout=findViewById(R.id.codelayout);
        border_entercode=findViewById(R.id.border_entercode);
        codelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codenteredtxt.setFocusable(false);
                //.setFocusableInTouchMode(false);
            }
        });
        getSupportActionBar().hide();
        codenteredtxt=(EditText) findViewById(R.id.codeenteredtxt);
        btnconfirmed=(Button) findViewById(R.id.btnconfirm);
        resend=(TextView) findViewById(R.id.resend);
        mAuth = FirebaseAuth.getInstance();
        progressbar3=findViewById(R.id.progressBar3);
        progressbar3.setVisibility(View.GONE);
        btnconfirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!codenteredtxt.getText().toString().isEmpty())
                { verifycode(codenteredtxt.getText().toString());}
                else{
                    codenteredtxt.setError("Can not be empty");
                    codenteredtxt.requestFocus();
                }
            }
        });
        codenteredtxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                codenteredtxt.setFocusable(true);
                codenteredtxt.setFocusableInTouchMode(true);
                return false;
            }
        });
        final AnimatedVectorDrawable avd;
        final AnimatedVectorDrawable avd_r;
        b=findViewById(R.id.imageView_entercode);
        if (Build.VERSION.SDK_INT >= 21) {
            avd =(AnimatedVectorDrawable)getDrawable(R.drawable.round_anim);
            avd_r =(AnimatedVectorDrawable)getDrawable(R.drawable.round_anim_r);
            b.setImageDrawable(avd);

            codenteredtxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean c) {
                    if(c){
                        ObjectAnimator anim= ObjectAnimator.ofFloat(border_entercode,"translationY",-100f);
                        anim.setDuration(400);
                        anim.start();
                        ObjectAnimator anim1= ObjectAnimator.ofFloat(resend,"translationY",-100f);
                        anim1.setDuration(400);
                        anim1.start();
                        ObjectAnimator anim2= ObjectAnimator.ofFloat(progressbar3,"translationY",-100f);
                        anim2.setDuration(400);
                        anim2.start();
                        ObjectAnimator anim3= ObjectAnimator.ofFloat(btnconfirmed,"translationY",-80f);
                        anim3.setDuration(400);
                        anim3.start();
//                        codenteredtxt.setTranslationY(-210f);
//                        resend.setTranslationY(-210f);
//                        progressbar3.setTranslationY(-210f);
//                        btnconfirmed.setTranslationY(-225);//anim=true;
                        b.setImageDrawable(avd);

                    }
                    if(!c)
                    {  ObjectAnimator anim= ObjectAnimator.ofFloat(border_entercode,"translationY",0f);
                        anim.setDuration(400);
                        anim.start();
                        ObjectAnimator anim1= ObjectAnimator.ofFloat(resend,"translationY",0f);
                        anim1.setDuration(400);
                        anim1.start();
                        ObjectAnimator anim2= ObjectAnimator.ofFloat(progressbar3,"translationY",0f);
                        anim2.setDuration(400);
                        anim2.start();
                        ObjectAnimator anim3= ObjectAnimator.ofFloat(btnconfirmed,"translationY",0f);
                        anim3.setDuration(400);
                        anim3.start();
//                        codenteredtxt.setTranslationY(0f);
//                        resend.setTranslationY(0f);
//                        progressbar3.setTranslationY(0f);
//                        btnconfirmed.setTranslationY(0);
                        b.setImageDrawable(avd_r);
                    }
                    AnimatedVectorDrawable animatedVectorDrawable=(AnimatedVectorDrawable) b.getDrawable();
                    Drawable drawable=animatedVectorDrawable.getCurrent();
                    if(drawable instanceof Animatable)
                    {
                        ((Animatable) drawable).start();
                    }

                }
            });}
        final String phone_number_intent=getIntent().getStringExtra("User Phone Number");
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode(phone_number_intent);
                Toast.makeText(Enter_code.this, "Resending verification code. Please wait.", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void sendVerificationCode(String Number)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(Number,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mcallBacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallBacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(Enter_code.this, "Phone number verified, sending verification code.", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Enter_code.this, "Cannot verify your phone number.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Login_Activity.reccode=s;
//            Intent intent = new Intent(Login_Activity.this,Enter_code.class);
//            user_phone_no=("+92"+phonetxt.getText().toString());
//            intent.putExtra("User Phone Number",user_phone_no);
//            startActivity(intent);
//            finish();
        }
    };
    private void verifycode(String code)
    {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(Login_Activity.reccode, code);
            signinwithcredential(credential);
            progressbar3.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            progressbar3.setVisibility(View.GONE);
            codenteredtxt.setError("In-valid code");
            codenteredtxt.requestFocus();
        }
    }

    private void signinwithcredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential).addOnCompleteListener(Enter_code.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                    Intent intent=new Intent(Enter_code.this,Take_UserName.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    codenteredtxt.setError("In-valid code");
                    codenteredtxt.requestFocus();
                    progressbar3.setVisibility(View.GONE);
                }
            }
        });
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
