package com.doyoonkim.androidchattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    Intent goLoginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //this activity is for the loading page


        goLoginActivity = new Intent(this, LoginActivity.class);
        startActivity(goLoginActivity);

    }
}