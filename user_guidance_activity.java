package com.deeefoo.myappl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class user_guidance_activity extends AppCompatActivity {

    private ViewPager slide_view_pager;
    private LinearLayout dot_layout;
    private slideAdapter slide_giudance;
    private TextView[] dots;
    private Button finish_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.user_guidance_activity);
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        slide_view_pager=findViewById(R.id.guideance_pager);
        dot_layout=findViewById(R.id.guidance_dots);
        finish_btn=findViewById(R.id.guide_finish);
        slide_giudance =new slideAdapter(this);
        slide_view_pager.setAdapter(slide_giudance);
        addDotsIndicator(0);
        slide_view_pager.addOnPageChangeListener(viewListner);

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(user_guidance_activity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void addDotsIndicator(int position)
    {
        dots=new TextView[4];
        dot_layout.removeAllViews();
        for (int i=0;i<dots.length;i++)
        {
            dots[i]=new TextView(this);
            dots[i].setText(".");
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.Grey));
            dot_layout.addView(dots[i]);
        }
        if(dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.Black));
        }
    }
    ViewPager.OnPageChangeListener viewListner=new ViewPager.OnPageChangeListener()
    {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            if(position==3)
            {
                finish_btn.setVisibility(View.VISIBLE);
            }
            else if(position==dots.length){finish_btn.setVisibility(View.INVISIBLE);}
            else finish_btn.setVisibility(View.INVISIBLE);


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private class logo extends Thread{

        public void run()
        {
            try {
                sleep(3000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }

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
