package com.deeefoo.myappl;

public class User {
    private static String mobile_number;
    private static String Name;
    private static int Points;

    public User(){

    }
    public  User(String Mob_Number,String Name,int Points)
    {
        this.mobile_number=Mob_Number;
        this.Name=Name;
        this.Points=Points;
    }

    public String getMobile_number() {
        return this.mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }
    public int getPoints() {
        return this.Points;
    }

    public void setPoints(int Points) {
        this.Points = Points;
    }
}
