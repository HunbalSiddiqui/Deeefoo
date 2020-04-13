package com.deeefoo.myappl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login_Activity extends AppCompatActivity {
    private EditText phonetxt;
    private Button sendcode;
    private FirebaseAuth mAuth;
    private ConstraintLayout clayout;
    private TextView ninetwo;
    static  String reccode="", user_phone_no="";
    private ImageView v;
    private GradientDrawable shape;
    private ViewGroup.LayoutParams params;
    private LinearLayout phone_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.activity_login_);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        phonetxt=(EditText) findViewById(R.id.phonetxt);
        ninetwo=findViewById(R.id.ninetwo);
        sendcode=(Button) findViewById(R.id.sendcode);
        clayout=findViewById(R.id.login_background);
        clayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonetxt.setFocusable(false);
            }
        });
        phone_layout=findViewById(R.id.view_phone_number);
        phonetxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                phonetxt.setFocusable(true);
                phonetxt.setFocusableInTouchMode(true);
                return false;
            }
        });
        final AnimatedVectorDrawable avd;
        final AnimatedVectorDrawable avd_r;
        v=findViewById(R.id.View_login);
        params=v.getLayoutParams();
        //anim = false;
        final int h=params.height;
        if (Build.VERSION.SDK_INT >= 21) {
            avd =(AnimatedVectorDrawable)getDrawable(R.drawable.round_anim);
            avd_r =(AnimatedVectorDrawable)getDrawable(R.drawable.round_anim_r);
            v.setImageDrawable(avd);

        phonetxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    ObjectAnimator anim= ObjectAnimator.ofFloat(phone_layout,"translationY",-120f);
                    anim.setDuration(400);
                    anim.start();
                    ObjectAnimator anim2= ObjectAnimator.ofFloat(sendcode,"translationY",-100f);
                    anim2.setDuration(400);
                    anim2.start();
                    //phonetxt.setTranslationY(-290f);
                    //ninetwo.setTranslationY(-290f);
                    //sendcode.setTranslationY(-325);//anim=true;
                    v.setImageDrawable(avd);

                }
                if(!b)
                {   ObjectAnimator anim= ObjectAnimator.ofFloat(phone_layout,"translationY",0f);
                    anim.setDuration(400);
                    anim.start();
                    ObjectAnimator anim2= ObjectAnimator.ofFloat(sendcode,"translationY",0f);
                    anim2.setDuration(400);
                    anim2.start();
                    //phonetxt.setTranslationY(0f);
                    //ninetwo.setTranslationY(0f);
                    //sendcode.setTranslationY(0f);
                    v.setImageDrawable(avd_r);

                }
                AnimatedVectorDrawable animatedVectorDrawable=(AnimatedVectorDrawable) v.getDrawable();
                Drawable drawable=animatedVectorDrawable.getCurrent();
                if(drawable instanceof Animatable)
                {
                    ((Animatable) drawable).start();
                }

            }
        });}
//        else phonetxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(b)
//                {
//                    ValueAnimator slideAnimator = ValueAnimator
//                            .ofInt(h, 400)
//                            .setDuration(300);
//                    slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator animation) {
//                            // get the value the interpolator is at
//                            Integer value = (Integer) animation.getAnimatedValue();
//                            // I'm going to set the layout's height 1:1 to the tick
//                            v.getLayoutParams().height = value.intValue();
//                            // force all layouts to see which ones are affected by
//                            // this layouts height change
//                            v.requestLayout();
//                        }
//                    });
//
//// create a new animationset
//                    AnimatorSet set = new AnimatorSet();
//// since this is the only animation we are going to run we just use
//// play
//                    set.play(slideAnimator);
//// this is how you set the parabola which controls acceleration
//                    set.setInterpolator(new AccelerateDecelerateInterpolator());
//// start the animation
//                    set.start();
////
//                }
//            }
//        });

        getSupportActionBar().hide();
        mAuth=FirebaseAuth.getInstance();
//        mAuthlistener=new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user=mAuth.getCurrentUser();
//                if(user!=null)
//                {
//                    Toast.makeText(Login_Activity.this, "Logged In", Toast.LENGTH_SHORT).show();
//                    Intent intent=new Intent(Login_Activity.this,MainActivity.class);
//                    startActivity(intent);
//                }
//                else{
//                    Toast.makeText(Login_Activity.this, "Please Login", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
        sendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(phonetxt.getText().length()>10||phonetxt.getText().length()<10)
                    {
                        phonetxt.setError("In valid mobile number.");
                    }
                   else if(!phonetxt.getText().toString().isEmpty())
                    {
//                        progressBar2.setVisibility(View.VISIBLE);
                        sendVerificationCode("+92"+phonetxt.getText().toString());
//                        codentered.setVisibility(View.VISIBLE);
//                        confirmbtn.setVisibility(View.VISIBLE);

//                        SigninWithUserNameandPass(username.getText().toString(),password.getText().toString());
                    }
                    else{
                        phonetxt.setError("Can not be empty");
                        phonetxt.requestFocus();
                    }
            }
        });

//        confirmbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                verifycode(codentered.getText().toString());
//            }
//        });


    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void sendVerificationCode(String Number)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(Number,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mcallBacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallBacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(Login_Activity.this, "Phone number verified, sending verification code.", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Login_Activity.this, "Cannot verify your phone number.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            reccode=s;
            Intent intent = new Intent(Login_Activity.this,Enter_code.class);
            user_phone_no=("+92"+phonetxt.getText().toString());
            intent.putExtra("User Phone Number",user_phone_no);
            startActivity(intent);
            finish();
        }
    };

//    private void verifycode(String code)
//    {
//        try {
//            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(reccode, code);
//            signinwithcredential(credential);
//            progressBar2.setVisibility(View.VISIBLE);
//        }
//        catch (Exception e)
//        {
//            Toast.makeText(this, "Invalid verification code", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void signinwithcredential(PhoneAuthCredential credential)
//    {
//            mAuth.signInWithCredential(credential).addOnCompleteListener(Login_Activity.this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful())
//                    {
//                        Intent intent=new Intent(Login_Activity.this,MainActivity.class);
//                        startActivity(intent);
//                    }
//                    else{
//                        Toast.makeText(Login_Activity.this, "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//    }


}
