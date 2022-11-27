package com.doyoonkim.androidchattingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import data.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {

    // UI variables.
    private EditText userNameEditText, serverAddressEditText;
    private Button connectBtn;

    private ChatSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Bind UI elements to corresponding variables.
        userNameEditText = findViewById(R.id.loginNameEditText);
        serverAddressEditText = findViewById(R.id.loginIPAddressEditText);
        connectBtn = findViewById(R.id.loginButton);

        session = ChatSession.getInstance(getApplicationContext());
    }

    public void onLogin(View view) {
        String address = serverAddressEditText.getText().toString();
        String userName = userNameEditText.getText().toString();

        if (!address.equals("") || !userName.equals("")) {
            session.startConnection(address);

            if (session.isConnected()) {
                User sessionUser = ChatSession.getInstance(getApplicationContext()).connectToServer(userName);
                if (sessionUser != null) {
                    Toast.makeText(getApplicationContext(),
                            "Login as " + sessionUser.getUserName(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, ChatListActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Login in failed", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Cannot connect to the Server", Toast.LENGTH_LONG).show();
            }
        }
    }
}


