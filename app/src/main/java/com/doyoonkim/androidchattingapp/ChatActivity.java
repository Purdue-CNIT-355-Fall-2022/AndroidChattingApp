package com.doyoonkim.androidchattingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.doyoonkim.androidchattingapp.adapter.ChatRecyclerViewAdapter;

import java.util.ArrayList;

import data.Chat;

public class ChatActivity extends AppCompatActivity {

    private TextView chatTitle;
    private ImageButton backBtn, sendBtn;
    private EditText msgEditText;

    private RecyclerView mChatRecyclerView;
    private ChatRecyclerViewAdapter adapter;

    private ChatSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        session = ChatSession.getInstance(getApplicationContext());

        // Bind UI Elements.
        chatTitle = findViewById(R.id.chatTitleTextView);
        msgEditText = findViewById(R.id.msgEditText);
        backBtn = findViewById(R.id.backBtn);
        sendBtn = findViewById(R.id.sendBtn);

        chatTitle.setText(session.getCurrentChatRoom().getChatroomName());

        mChatRecyclerView = findViewById(R.id.mChatRecyclerView);
        adapter = new ChatRecyclerViewAdapter(session.getChatByChatRoom());
        mChatRecyclerView.setAdapter(adapter);


        backBtn.setOnClickListener((view) -> {
            session.setCurrentChatRoom(null);
            finish();
        });

        sendBtn.setOnClickListener((view) -> {
            String msg = msgEditText.getText().toString();
            if (!msg.equals("")) {
                session.addChat(msg);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Thread temp = new Thread(() -> {
            while (session.getCurrentChatRoom() != null) {
                ArrayList<Chat>  chats = session.getChatByChatRoom();
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Log.d("Handler", "Size: " + chats.size());
                    adapter.notifyChanges(chats);
                }, 500);
            }
        });
        temp.start();
    }
}