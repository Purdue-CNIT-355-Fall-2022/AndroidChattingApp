package com.doyoonkim.androidchattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddNewChatActivity extends AppCompatActivity {
    Intent goChatListActivityIntent;
    EditText newChatNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_chat);
        newChatNameEditText = findViewById(R.id.newChatNameEditText);
    }

    /**
     * ADD button onClick method
     * @param view
     */
    public void onAdd(View view) {
        goChatListActivityIntent = new Intent(this, ChatListActivity.class);
        goChatListActivityIntent.putExtra("newChatName", newChatNameEditText.getText().toString());
        startActivity(goChatListActivityIntent);
    }
}