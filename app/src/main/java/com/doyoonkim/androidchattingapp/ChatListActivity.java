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

        if(receivedIntent.getStringExtra("newChatName") != null){
            addChatRoom(receivedIntent.getStringExtra("newChatName"));
        }//when return from addNewChatActivity, call addChatRoom method

    }

    /**
     *
     * @param name new Chat name
     */
    public void addChatRoom(String name){
        LinearLayout chatListLinearLayout = findViewById(R.id.chatListLinearLayout);
        LinearLayout newChatRoom = new LinearLayout(ChatListActivity.this);
        newChatRoom.setOrientation(LinearLayout.HORIZONTAL);
        ImageView newImage = new ImageView(ChatListActivity.this);
        TextView newName = new TextView(ChatListActivity.this);
        chatListLinearLayout.addView(newChatRoom);
        newChatRoom.addView(newImage);
        newChatRoom.addView(newName);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.weight = 8;
        newImage.setLayoutParams(lp);
        lp.weight = 1;
        newName.setLayoutParams(lp);
        newName.setText(name);
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