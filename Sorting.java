package com.deeefoo.myappl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Sorting {
    ArrayList<Integer> list1=new ArrayList<>();
    ArrayList<String> list2=new ArrayList<>();

    Sorting(ArrayList<Integer> raw,ArrayList<String> canlist){
        this.list1=raw;
        this.list2=canlist;
        for(int i=0;i<this.list1.size();i++)
        {
            for(int j=i+1;j<this.list1.size();j++)
            {
                if(this.list1.get(i)<this.list1.get(j))
                {
                    Collections.swap(this.list1,i,j);
                    Collections.swap(this.list2,i,j);
                }
            }
        }
    }

    public ArrayList<Integer> getpoints(){
        return this.list1;
    }
    public ArrayList<String> getCanlist(){
        return this.list2;
    }
}
