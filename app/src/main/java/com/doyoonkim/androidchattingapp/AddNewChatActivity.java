package com.doyoonkim.androidchattingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.doyoonkim.androidchattingapp.adapter.UserListRecyclerViewAdapter;

public class AddNewChatActivity extends AppCompatActivity {
    Intent goChatListActivityIntent;
    EditText newChatNameEditText;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_chat);

        mRecyclerView = findViewById(R.id.userListRecyclerView);
        UserListRecyclerViewAdapter adapter = new UserListRecyclerViewAdapter(
                ChatSession.getInstance(getApplicationContext()).requestAllUser()
        );
        mRecyclerView.setAdapter(adapter);


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