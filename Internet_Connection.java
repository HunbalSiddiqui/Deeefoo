package com.deeefoo.myappl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Internet_Connection  {
private  static Internet_Connection ic=new Internet_Connection();
static  Context context;
ConnectivityManager cm;
NetworkInfo wifiinfo,mobileinfo;
boolean connected=false;
public static Internet_Connection instance(Context cont)
{
    context=cont.getApplicationContext();
    return  ic;
}
public  boolean isonline(){
    try{
        cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo=cm.getActiveNetworkInfo();
        connected=netinfo!=null && netinfo.isAvailable()&&netinfo.isConnected();
        return connected;
    }
    catch (Exception e)
    {
        Toast.makeText(context, "Exception", Toast.LENGTH_SHORT).show();
    }
    return connected;
}
}
