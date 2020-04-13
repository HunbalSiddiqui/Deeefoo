package com.deeefoo.myappl;

import java.util.ArrayList;

public class place_order_db {
    private ArrayList<String> list=new ArrayList<>();
    private String price;
    private String canteen_name;
    private long already_rated_check;

    place_order_db(){

    }
    place_order_db(ArrayList<String> arrlist,String price,String canteen,long already_rated_check)
    {
        this.list=arrlist;
        this.price=price;
        this.canteen_name=canteen;
        this.already_rated_check=already_rated_check;

    }

    public ArrayList<String> getList() {
        return this.list;
    }

    public String getPrice() {
        return price;
    }

    public String getCanteen_name() {
        return canteen_name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCanteen_name(String canteen_name) {
        this.canteen_name = canteen_name;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public long getAlready_rated_check() {
        return already_rated_check;
    }

    public void setAlready_rated_check(long already_rated_check) { this.already_rated_check=already_rated_check; }
}
