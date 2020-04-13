package com.deeefoo.myappl;

import android.content.Intent;

import java.util.ArrayList;

public class Chart_Static_Variable {
    static    int chart_count=0;
    static     ArrayList<String> selected_menu_list=new ArrayList<String>();
    static     ArrayList<Integer> total_price_list=new ArrayList<Integer>();
    static     ArrayList<String> canteen_list=new ArrayList<String>();
    static     ArrayList<Integer> total_discount_list=new ArrayList<>();
    static int total=0;
    static int discount=0;

    public static void add_discount_func(Integer item)
    {
        total_discount_list.add(item);
    }

}
