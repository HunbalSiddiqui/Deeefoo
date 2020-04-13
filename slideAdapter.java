package com.deeefoo.myappl;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class slideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layout_inflater;

    public slideAdapter(Context context)
    {
        this.context=context;
    }

    public int[] slide_images={
            R.drawable.screen_1,
            R.drawable.screen_2,
            R.drawable.screen_3,
            R.drawable.screen_4
    };

    public String[] slide_headings={
            "SELECT CANTEENS ON MAP",
            "SELECT FOOD TYPE",
            "SELECT CANTEEN",
            "TRACK YOUR FOOD"
    };
    public String [] slide_descriptions={
            "Markers point to the canteens. You can zoom in the map for the canteens of your choice. Visible markers on your screen will only be used in processing.",
            "Tap the search bar, select your food and search. It shows you a ranking order wise list of canteens where your desired food is BEST available.",
            "Ranking is based on your rating and our professionals' choice after tasting and checking it in many measures.",
            "Click on the canteen from the list on your screen. Click on the menu and order the food. Confirm and track your order in real time :)  Rate your food which will help that canteen to earn points."
    };
    @Override
    public int getCount() {
        return slide_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(ViewGroup container,int position){
        layout_inflater =(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view =layout_inflater.inflate(R.layout.slide_layout,container,false);
        TextView slide_heading=view.findViewById(R.id.slide_heading);
        ImageView slide_image=view.findViewById(R.id.slide_image);
        TextView slide_description=view.findViewById(R.id.slide_desc);

        slide_heading.setText(slide_headings[position]);
        slide_image.setImageResource(slide_images[position]);
        slide_description.setText(slide_descriptions[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object)
    {
        container.removeView((ConstraintLayout)object);
    }
}
