package com.doyoonkim.androidchattingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.doyoonkim.androidchattingapp.adapter.UserListRecyclerViewAdapter;

public class AddNewChatActivity extends AppCompatActivity {
    Intent goChatListActivityIntent;
    EditText newChatNameEditText;

    private RecyclerView mRecyclerView;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_chat);

        mRecyclerView = findViewById(R.id.userListRecyclerView);
        UserListRecyclerViewAdapter adapter = new UserListRecyclerViewAdapter(
                ChatSession.getInstance(getApplicationContext()).requestAllUser(),
                getApplicationContext()
        );
        mRecyclerView.setAdapter(adapter);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener((view) -> {
            finish();
        });


    }
}