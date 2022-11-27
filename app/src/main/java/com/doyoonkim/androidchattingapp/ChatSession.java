package com.doyoonkim.androidchattingapp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import data.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ChatSession {
    public static ChatSession instance;

    private User sessionUser;
    private Socket connectedSocket;
    private boolean isConnected;

    private String address;
    private ObjectInputStream inbound;
    private BufferedWriter outbound;
    private Handler mHandler;
    private Context context;

    private String currentRequest = "";

    public ChatSession(Context context) {
        mHandler = new Handler(Looper.getMainLooper());
        this.context = context;
    }


    public static ChatSession getInstance(Context context) {
        if (instance == null) {
            instance = new ChatSession(context);
        }
        return instance;
    }

    public void startConnection(String address) {
        Thread temp = new Thread(() -> {
            try {
                this.address = address;
                connectedSocket = new Socket(this.address, 8189);
                outbound = new BufferedWriter(new OutputStreamWriter(connectedSocket.getOutputStream()));
                inbound = new ObjectInputStream(connectedSocket.getInputStream());
                isConnected = true;
                connectedSocket.close();
                return;
            } catch (IOException e) {
                Log.d("ChatSession", "Unable to make connection");
                e.printStackTrace();
            }
        });
        temp.start();

        try {
            temp.join();
        } catch (InterruptedException e) {
            //DO NOTHING
        }
    }

    public User connectToServer(String userName) {
        Thread temp = new Thread(() -> {
            try {
                connectedSocket = new Socket(this.address, 8189);
                outbound = new BufferedWriter(new OutputStreamWriter(connectedSocket.getOutputStream()));
                inbound = new ObjectInputStream(connectedSocket.getInputStream());

                String req = String.format("%s,%s\n", "REQ_LOGIN", userName);
                outbound.write(req);
                outbound.flush();

                sessionUser = (User) inbound.readObject();
                Log.d("Session", "SessionUser: " + sessionUser.toString());

                connectedSocket.close();
                return;
            } catch (IOException | ClassNotFoundException e) {
                Log.d("ChatSession", "Unable to make connection");
                e.printStackTrace();
            }
        });
        temp.start();

        try {
            temp.join();
        } catch (InterruptedException e) {
            // DO NOTHING;
        }


        return sessionUser;
    }

    public ArrayList<User> requestAllUser() {
        ArrayList<User> user = new ArrayList<>();
        Thread temp = new Thread(() -> {
            try {
                connectedSocket = new Socket(this.address, 8189);
                outbound = new BufferedWriter(new OutputStreamWriter(connectedSocket.getOutputStream()));
                inbound = new ObjectInputStream(connectedSocket.getInputStream());

                String req = String.format("%s\n", "REQ_AU");
                outbound.write(req);
                outbound.flush();

                ArrayList<User> result = (ArrayList<User>) inbound.readObject();
                user.addAll(result);
                Log.d("Session", "Current Size: " + user.size());

                connectedSocket.close();
            } catch (IOException | ClassNotFoundException e) {
                Log.d("ChatSession", "Unable to make connection");
                e.printStackTrace();
            }
        });
        temp.start();

        try {
            temp.join();
        } catch (InterruptedException e) {
            // DO NOTHING;
        }
        return user;
    }

    public boolean addNewChatRoom(String userName) {
        boolean result = false;
        Thread temp = new Thread(() -> {
            try {
                connectedSocket = new Socket(this.address, 8189);
                outbound = new BufferedWriter(new OutputStreamWriter(connectedSocket.getOutputStream()));
                inbound = new ObjectInputStream(connectedSocket.getInputStream());

                String req = String.format("%s,%s\n", "REQ_LOGIN", userName);
                outbound.write(req);
                outbound.flush();

                sessionUser = (User) inbound.readObject();
                Log.d("Session", "SessionUser: " + sessionUser.toString());

                connectedSocket.close();
                return;
            } catch (IOException | ClassNotFoundException e) {
                Log.d("ChatSession", "Unable to make connection");
                e.printStackTrace();
            }
        });
        temp.start();

        try {
            temp.join();
        } catch (InterruptedException e) {
            // DO NOTHING;
        }
        return result;
    }



    public synchronized void request(String req) {
        currentRequest = req;
    }

    public User getSessionUser() {
        return this.sessionUser;
    }

    public boolean isConnected() {
        return this.isConnected;
    }
}
