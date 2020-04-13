package com.deeefoo.myappl;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int no_of_tabs;
    public  PagerAdapter(FragmentManager fm,int no_of_tabs){
        super(fm);
        this.no_of_tabs=no_of_tabs;
    }

    @Override
    public Fragment getItem(int position) {

        return MenuActivity.FragmentList.get(position);
    }

    @Override
    public int getCount() {
        return this.no_of_tabs;
    }
}
