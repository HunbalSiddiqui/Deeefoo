package com.deeefoo.myappl;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class side_menu_header extends AppCompatActivity {
    TextView sidemenubarname;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sidemenubarname=(TextView) findViewById(R.id.side_menu_namebar);

    }
}
