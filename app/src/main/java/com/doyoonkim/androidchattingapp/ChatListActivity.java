package com.doyoonkim.androidchattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Intent receivedIntent = getIntent();


    }



    /**
     * ADD button onClick method
     * @param view
     */
    public void onAdd(View view) {
        Intent addIntent = new Intent(this, AddNewChatActivity.class);
        startActivity(addIntent);

    }
}