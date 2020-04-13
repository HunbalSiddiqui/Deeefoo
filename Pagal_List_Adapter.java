package com.deeefoo.myappl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Pagal_List_Adapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> adap_item_List;
    private ArrayList<Integer> adap_price_lsit;

    public Pagal_List_Adapter(Context context, ArrayList<String> item_List,ArrayList<Integer> price_List) {
        this.context = context;
        this.adap_item_List = item_List;
        this.adap_price_lsit=price_List;
    }

    @Override
    public int getCount() {
        return adap_item_List.size();
    }


    @Override
    public Object getItem(int i) {
        return adap_item_List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    //Class For View //
    private class ViewHolder
    {
        public TextView item_name;
        public TextView item_price;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view ==null){
            holder =new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.pagal_list_layout,null);
            holder.item_name= (TextView) view.findViewById(R.id.item_name);
            holder.item_price=(TextView)view.findViewById(R.id.item_price);
            view.setTag(holder);
        }
        else {
            holder= (ViewHolder) view.getTag();
        }
        holder.item_name.setText(adap_item_List.get(i));
        holder.item_price.setText(adap_price_lsit.get(i).toString());
        return view;
    }
}
