package com.doyoonkim.androidchattingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    private Thread dataThread;

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
        adapter = new ChatRecyclerViewAdapter();
        adapter.notifyChanges(session.getChatByChatRoom());

        mChatRecyclerView.setAdapter(adapter);


        backBtn.setOnClickListener((view) -> {
            session.setCurrentChatRoom(null);
            startActivity(new Intent(this, ChatListActivity.class));
        });

        sendBtn.setOnClickListener((view) -> {
            String msg = msgEditText.getText().toString();
            if (!msg.equals("")) {
                if (dataThread != null) {
                    try {
                        dataThread.sleep(100);
                        session.addChat(msg);
                    } catch (InterruptedException e) {
                        Log.d("Thread", e.getMessage());
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (dataThread == null) {
            dataThread = new Thread(() -> {
                while (session.getCurrentChatRoom() != null) {
                    ArrayList<Chat>  chats = session.getChatByChatRoom();
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Log.d("Handler", "Size: " + chats.size());
                        adapter.notifyChanges(chats);
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // DO NOTHING.
                    }
                }
            });
            dataThread.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataThread.interrupt();
        dataThread = null;
    }
}