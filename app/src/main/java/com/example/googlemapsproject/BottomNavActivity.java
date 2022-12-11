package com.example.googlemapsproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class BottomNavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_nav);

        init();

    }

    private void init(){
        //Setup RecyclerView
    }
}
