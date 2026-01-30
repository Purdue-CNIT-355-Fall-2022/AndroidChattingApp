package com.doyoonkim.androidchattingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.doyoonkim.androidchattingapp.adapter.ChatRoomRecyclerViewAdapter;

import data.ChatRoom;

public class ChatListActivity extends AppCompatActivity implements ChatRoomRecyclerViewAdapter.ChatRoomClickListener {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Intent receivedIntent = getIntent();

        mRecyclerView = findViewById(R.id.mRecyclerView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ChatRoomRecyclerViewAdapter adapter = new ChatRoomRecyclerViewAdapter(
                ChatSession.getInstance(getApplicationContext()).getChatRoomByUser(), this
        );
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * ADD button onClick method
     * @param view
     */
    public void onAdd(View view) {
        Intent addIntent = new Intent(this, AddNewChatActivity.class);
        startActivity(addIntent);

    }

    // RecyclerView Item Click Listener.
    @Override
    public void onClick(ChatRoom cr) {
        ChatSession.getInstance(getApplicationContext()).setCurrentChatRoom(cr);
        startActivity(new Intent(this, ChatActivity.class));
    }
}