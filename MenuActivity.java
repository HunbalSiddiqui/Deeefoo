package com.deeefoo.myappl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileReader;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements
        BurgerFragment.OnFragmentInteractionListener
        ,JuiceFragment.OnFragmentInteractionListener
        ,RollFragment.OnFragmentInteractionListener
        ,FriesFragment.OnFragmentInteractionListener
        ,DesiFragment.OnFragmentInteractionListener
        ,ChineseFragment.OnFragmentInteractionListener
        ,BiryaniFragment.OnFragmentInteractionListener
        ,BreadFragment.OnFragmentInteractionListener
        ,CurriesFragment.OnFragmentInteractionListener
        ,DaalFragment.OnFragmentInteractionListener
        ,DrinkFragment.OnFragmentInteractionListener
        ,ObsFragment.OnFragmentInteractionListener
        ,OtherFragment.OnFragmentInteractionListener
        ,RiceFragment.OnFragmentInteractionListener
        ,SandwichFragment.OnFragmentInteractionListener
        ,ShakeFragment.OnFragmentInteractionListener
        , GestureDetector.OnGestureListener
{
    public static final int SWIPE_TRESHOLD = 100;
    public static final int SWIPE_VELOCITY_TRESHOLD = 100;
    private DatabaseReference number_ping= FirebaseDatabase.getInstance().getReference("Placed Orders");
    private FirebaseUser fbuser;
    static String Actionbar_Canteen_Name="null";
    static ArrayList<Fragment> FragmentList;
    static TabLayout tbl;
    private TextView canteenname;
    private ImageView action_bar_image;
    protected static Button menu;
    private Button back_menu;
    private GestureDetector gestureDetector;
    private ViewGroup.LayoutParams params;
    private int height;
    private ConstraintLayout canteen_image_name;
    protected static int price_check=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        getSupportActionBar().hide();
        fbuser= FirebaseAuth.getInstance().getCurrentUser();
        String s=getIntent().getStringExtra("CanName");
            back_menu=findViewById(R.id.back_menu);
            menu=findViewById(R.id.menu_chart);
            menu.setText(String.valueOf("No of Selected Items"+"  "+Chart_Static_Variable.chart_count));
        canteenname = findViewById(R.id.canteen_name);
        canteen_image_name = findViewById(R.id.canteen_image_name);
        action_bar_image = findViewById(R.id.action_bar_image);
        canteenname.setText(searc_frag.Clicked_Button_TextHolder);
        Actionbar_Canteen_Name=searc_frag.Clicked_Button_TextHolder;
        params=action_bar_image.getLayoutParams();
        height =params.height;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //  getWindow().setStatusBarColor(this.getResources().getColor(R.color.Main));
        }
        tbl = (TabLayout) findViewById(R.id.tablayout);
        FragmentList=new ArrayList<>();
        if(searc_frag.Clicked_Button_TextHolder.contains("Nafees"))
        {
            action_bar_image.setImageResource(R.drawable.nafees);
            tbl.addTab(tbl.newTab().setText("BURGER"));
            BurgerFragment bf=new BurgerFragment();
            FragmentList.add(bf);
            tbl.addTab(tbl.newTab().setText("ROLL"));
            RollFragment rf =new RollFragment();
            FragmentList.add(rf);
            tbl.addTab(tbl.newTab().setText("BIRYANI"));
            BiryaniFragment brf =new BiryaniFragment();
            FragmentList.add(brf);
            tbl.addTab(tbl.newTab().setText("SHAKE"));
            ShakeFragment sk=new ShakeFragment();
            FragmentList.add(sk);
            tbl.addTab(tbl.newTab().setText("OTHER"));
            OtherFragment of =new OtherFragment();
            FragmentList.add(of);
        }

        if(searc_frag.Clicked_Button_TextHolder.contains("Shaheen Fast Food"))
        {
            action_bar_image.setImageResource(R.drawable.shaheen);
            tbl.addTab(tbl.newTab().setText("BURGER"));
            BurgerFragment bf=new BurgerFragment();
            FragmentList.add(bf);
            tbl.addTab(tbl.newTab().setText("ROLL"));
            RollFragment rf =new RollFragment();
            FragmentList.add(rf);
            tbl.addTab(tbl.newTab().setText("SANDWICH"));
            SandwichFragment sf=new SandwichFragment();
            FragmentList.add(sf);
            tbl.addTab(tbl.newTab().setText("CHINESE"));
            ChineseFragment cf=new ChineseFragment();
            FragmentList.add(cf);
            tbl.addTab(tbl.newTab().setText("DESI"));
            DesiFragment df=new DesiFragment();
            FragmentList.add(df);
            tbl.addTab(tbl.newTab().setText("SHAKE"));
            ShakeFragment sk =new ShakeFragment();
            FragmentList.add(sk);
            tbl.addTab(tbl.newTab().setText("OTHER"));
            OtherFragment of =new OtherFragment();
            FragmentList.add(of);
        }
        if(searc_frag.Clicked_Button_TextHolder.equalsIgnoreCase("Student Star Food"))
        {
            action_bar_image.setImageResource(R.drawable.student);
            tbl.addTab(tbl.newTab().setText("BURGER"));
            BurgerFragment bf=new BurgerFragment();
            FragmentList.add(bf);
            tbl.addTab(tbl.newTab().setText("ROLL"));
            RollFragment rf =new RollFragment();
            FragmentList.add(rf);
            tbl.addTab(tbl.newTab().setText("SANDWICH"));
            SandwichFragment sf=new SandwichFragment();
            FragmentList.add(sf);
            tbl.addTab(tbl.newTab().setText("BIRYANI"));
            BiryaniFragment brf=new BiryaniFragment();
            FragmentList.add(brf);
            tbl.addTab(tbl.newTab().setText("OTHER"));
            OtherFragment of =new OtherFragment();
            FragmentList.add(of);
        }
        if(searc_frag.Clicked_Button_TextHolder.equalsIgnoreCase("Sufi Canteen"))
        {
            action_bar_image.setImageResource(R.drawable.sufi);
            tbl.addTab(tbl.newTab().setText("CURRIES"));
            CurriesFragment cf =new CurriesFragment();
            FragmentList.add(cf);
            tbl.addTab(tbl.newTab().setText("DAAL"));
            DaalFragment df =new DaalFragment();
            FragmentList.add(df);
            tbl.addTab(tbl.newTab().setText("RICE"));
            RiceFragment rf =new RiceFragment();
            FragmentList.add(rf);
            tbl.addTab(tbl.newTab().setText("BREAD"));
            BreadFragment bf =new BreadFragment();
            FragmentList.add(bf);
            tbl.addTab(tbl.newTab().setText("BEVERAGES"));
            DrinkFragment drf =new DrinkFragment();
            FragmentList.add(drf);
            tbl.addTab(tbl.newTab().setText("OTHER"));
            OtherFragment of =new OtherFragment();
            FragmentList.add(of);
        }
        if(searc_frag.Clicked_Button_TextHolder.equalsIgnoreCase("Karachi Food Center"))
        {
            action_bar_image.setImageResource(R.drawable.student);
            tbl.addTab(tbl.newTab().setText("BURGER"));
            BurgerFragment bf=new BurgerFragment();
            FragmentList.add(bf);
            tbl.addTab(tbl.newTab().setText("JUICE"));
            JuiceFragment jf =new JuiceFragment();
            FragmentList.add(jf);
            tbl.addTab(tbl.newTab().setText("ROLL"));
            RollFragment rf =new RollFragment();
            FragmentList.add(rf);
            tbl.addTab(tbl.newTab().setText("CHINESE"));
            ChineseFragment cf=new ChineseFragment();
            FragmentList.add(cf);
            tbl.addTab(tbl.newTab().setText("SANDWICH"));
            SandwichFragment sf=new SandwichFragment();
            FragmentList.add(sf);
            tbl.addTab(tbl.newTab().setText("BIRYANI"));
            DesiFragment df=new DesiFragment();
            FragmentList.add(df);
            tbl.addTab(tbl.newTab().setText("BEVERAGES"));
            DrinkFragment drf =new DrinkFragment();
            FragmentList.add(drf);
            tbl.addTab(tbl.newTab().setText("OTHER"));
            OtherFragment of =new OtherFragment();
            FragmentList.add(of);
        }
        if(searc_frag.Clicked_Button_TextHolder.contains("Nazir"))
        {
            action_bar_image.setImageResource(R.drawable.mass);
            tbl.addTab(tbl.newTab().setText("BURGER"));
            BurgerFragment bf=new BurgerFragment();
            FragmentList.add(bf);
            tbl.addTab(tbl.newTab().setText("ROLL"));
            RollFragment rf =new RollFragment();
            FragmentList.add(rf);
            tbl.addTab(tbl.newTab().setText("FRIES"));
            FriesFragment ff =new FriesFragment();
            FragmentList.add(ff);
            tbl.addTab(tbl.newTab().setText("OTHER"));
            OtherFragment of =new OtherFragment();
            FragmentList.add(of);
        }
        if(searc_frag.Clicked_Button_TextHolder.contains("Mass"))
        {
            action_bar_image.setImageResource(R.drawable.mass);
            tbl.addTab(tbl.newTab().setText("ROLL"));
            RollFragment rf =new RollFragment();
            FragmentList.add(rf);
            tbl.addTab(tbl.newTab().setText("FRIES"));
            FriesFragment ff =new FriesFragment();
            FragmentList.add(ff);
            tbl.addTab(tbl.newTab().setText("JUICE"));
            JuiceFragment jf=new JuiceFragment();
            FragmentList.add(jf);
            tbl.addTab(tbl.newTab().setText("OTHER"));
            OtherFragment of =new OtherFragment();
            FragmentList.add(of);
        }
        if(searc_frag.Clicked_Button_TextHolder.contains("Tahir"))
        {
            action_bar_image.setImageResource(R.drawable.tjc);
            tbl.addTab(tbl.newTab().setText("JUICE"));
            JuiceFragment jf=new JuiceFragment();
            FragmentList.add(jf);
        }
        if(searc_frag.Clicked_Button_TextHolder.contains("Qasim"))
        {
            action_bar_image.setImageResource(R.drawable.obs);
            tbl.addTab(tbl.newTab().setText("One Bite Samosa"));
            ObsFragment of=new ObsFragment();
            FragmentList.add(of);
            tbl.addTab(tbl.newTab().setText("OTHER"));
            OtherFragment otf =new OtherFragment();
            FragmentList.add(otf);
        }

        if(tbl.getTabCount()>4){
        tbl.setTabMode(TabLayout.MODE_SCROLLABLE);
        tbl.setTabGravity(TabLayout.GRAVITY_CENTER);}
        else tbl.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager=(ViewPager)findViewById(R.id.pager);
        final PagerAdapter pagerAdapter=new PagerAdapter(getSupportFragmentManager(),tbl.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tbl));
        tbl.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_ping.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            if((dataSnapshot.child(fbuser.getPhoneNumber()+" ping").child("delievered").getValue().toString()).equals("1"))
                            {
                                if(Chart_Static_Variable.chart_count==0)
                                {
                                    Toast.makeText(MenuActivity.this, "You have not selected any item.First select any item to proceed further.", Toast.LENGTH_SHORT).show();
                                }
                                else if(price_check<100)
                                {
                                    Toast.makeText(MenuActivity.this, "The minimum limit for order is 100 Rs.Please select more items.", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {Intent intent = new Intent(MenuActivity.this,Final_Activity.class);
                                    startActivity(intent);}
                            }
                            else {Toast.makeText(MenuActivity.this, "You can't place a new order untill your previous order has been completed", Toast.LENGTH_SHORT).show(); }

                        }catch (Exception e)
                        {
                            if(Chart_Static_Variable.chart_count==0)
                            {
                                Toast.makeText(MenuActivity.this, "You have not selected any item.First select any item to proceed further.", Toast.LENGTH_SHORT).show();
                            }
                            else if(price_check<100)
                            {
                                Toast.makeText(MenuActivity.this, "The minimum limit for order is 100 Rs.Please select more items.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {Intent intent = new Intent(MenuActivity.this,Final_Activity.class);
                                startActivity(intent);}
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        back_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chart_Static_Variable.selected_menu_list.clear();
                Chart_Static_Variable.canteen_list.clear();
                Chart_Static_Variable.chart_count=0;
                Chart_Static_Variable.total=0;
                Chart_Static_Variable.total_price_list.clear();
                Chart_Static_Variable.total_discount_list.clear();
                Intent intent = new Intent(MenuActivity.this,MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("Exit",true);
                startActivity(intent);
                finish();
            }
        });
        gestureDetector = new GestureDetector(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    public void onBackPressed() {
        Chart_Static_Variable.selected_menu_list.clear();
        Chart_Static_Variable.canteen_list.clear();
        Chart_Static_Variable.chart_count=0;
        Chart_Static_Variable.total=0;
        price_check=0;
        Chart_Static_Variable.total_price_list.clear();
        Chart_Static_Variable.total_discount_list.clear();
        Intent intent = new Intent(MenuActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        boolean result =false;
        float dif_y=moveEvent.getY()-downEvent.getY();
        float dif_x=moveEvent.getX()-downEvent.getX();
        //towards which axix movement was greater
        if(Math.abs(dif_x)>Math.abs(dif_y)){
            //right or left swipe
            if(Math.abs(dif_x)> SWIPE_TRESHOLD && Math.abs(velocityX)> SWIPE_VELOCITY_TRESHOLD){
                if(dif_x>0){
                    onSwipeRight();
                }
                else
                    onSwipeLeft();
                result=true;
            }
        }
        else
            //up or down swipe
            if(Math.abs(dif_y)> SWIPE_TRESHOLD && Math.abs(velocityY)> SWIPE_VELOCITY_TRESHOLD){
                if(dif_y>0){
                    onSwipeDown();
                }
                else
                    onSwipeUp();
                result=true;
            }
        return result;
    }

    private void onSwipeUp() {
//        collapse(canteen_image_name);
//        action_bar_image.setImageResource(R.color.colorPrimary);
//        action_bar_image.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.No_colour)));

    }

    private void onSwipeDown() {
//        expand(canteen_image_name);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(searc_frag.Clicked_Button_TextHolder.contains("Nafees"))
//                {
//                    action_bar_image.setImageResource(R.drawable.nafees);
//                }
//                else if(searc_frag.Clicked_Button_TextHolder.contains("Shaheen Fast Food"))
//                {
//                    action_bar_image.setImageResource(R.drawable.shaheen);
//                }
//                else if(searc_frag.Clicked_Button_TextHolder.equalsIgnoreCase("Student Star Food"))
//                {
//                    action_bar_image.setImageResource(R.drawable.student);
//                }
//                else if(searc_frag.Clicked_Button_TextHolder.equalsIgnoreCase("Sufi Canteen"))
//                {
//                    action_bar_image.setImageResource(R.drawable.sufi);
//                }
//                else if(searc_frag.Clicked_Button_TextHolder.equalsIgnoreCase("Karachi Food Center"))
//                {
//                    action_bar_image.setImageResource(R.drawable.student);
//                }
//                else if(searc_frag.Clicked_Button_TextHolder.contains("Nazir"))
//                {
//                    action_bar_image.setImageResource(R.drawable.mass);
//                }
//                else if(searc_frag.Clicked_Button_TextHolder.contains("Mass"))
//                {
//                    action_bar_image.setImageResource(R.drawable.mass);
//                }
//                else if(searc_frag.Clicked_Button_TextHolder.contains("Tahir"))
//                {
//                    action_bar_image.setImageResource(R.drawable.tjc);
//                }
//                else if(searc_frag.Clicked_Button_TextHolder.contains("Qasim"))
//                {
//                    action_bar_image.setImageResource(R.drawable.obs);
//                }
//                action_bar_image.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.Tint)));
//
//            }
//
//                },285);


    }

    private void onSwipeLeft() {
//        Toast.makeText(this,"SWIPE LEFT",Toast.LENGTH_SHORT).show();
    }

    private void onSwipeRight() {
//        Toast.makeText(this,"SWIPE RIGHT",Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public static void expand(final View view) {
//        view.setVisibility(View.VISIBLE);

//        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        view.measure(widthSpec, heightSpec);
        int finalHeight = view.getHeight();
        ValueAnimator mAnimator = slideAnimator(view,finalHeight, 400);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        mAnimator.start();
    }

    public static void collapse(final View view) {
        int finalHeight = view.getHeight();

        ValueAnimator mAnimator = slideAnimator(view, finalHeight, 150);

        mAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationEnd(Animator animator) {
//                view.setVisibility(View.VISIBLE);

            }


            @Override
            public void onAnimationStart(Animator animation) {
            }


            @Override
            public void onAnimationCancel(Animator animation) {

            }


            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    private static ValueAnimator slideAnimator(final View v, int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {

        int value = (Integer) valueAnimator.getAnimatedValue();
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.height = value;
        v.setLayoutParams(layoutParams);
    }
});
    return animator;
}
}


