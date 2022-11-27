package com.doyoonkim.androidchattingapp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import data.Chat;
import data.ChatRoom;
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

    private ChatRoom currentChatRoom;

    private String address;
    private ObjectInputStream inbound;
    private BufferedWriter outbound;
    private Handler mHandler;
    private Context context;


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

    public void addNewChatRoom(String userName1) {
        Thread temp = new Thread(() -> {
            try {
                connectedSocket = new Socket(this.address, 8189);
                outbound = new BufferedWriter(new OutputStreamWriter(connectedSocket.getOutputStream()));
                inbound = new ObjectInputStream(connectedSocket.getInputStream());

                // Chat name set to Default.
                String chatRoomName = String.format("%s and %s 's chatroom", sessionUser.getUserName(), userName1);
                String req = String.format("%s,%s,%s,%s\n", "ADD_CR", chatRoomName, sessionUser.getUserName(), userName1);
                outbound.write(req);
                outbound.flush();

                connectedSocket.close();
            } catch (IOException e) {
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
    }

    public ArrayList<ChatRoom> getChatRoomByUser() {
        ArrayList<ChatRoom> chatRooms = new ArrayList<ChatRoom>();
        Thread temp = new Thread(() -> {
            try {
                connectedSocket = new Socket(this.address, 8189);
                outbound = new BufferedWriter(new OutputStreamWriter(connectedSocket.getOutputStream()));
                inbound = new ObjectInputStream(connectedSocket.getInputStream());

                String req = String.format("%s,%s\n", "REQ_CR", sessionUser.getUserName());
                outbound.write(req);
                outbound.flush();

                ArrayList<ChatRoom> result = (ArrayList<ChatRoom>) inbound.readObject();
                chatRooms.addAll(result);

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
        return chatRooms;
    }

    public ArrayList<Chat> getChatByChatRoom() {
        ArrayList<Chat> chats = new ArrayList<>();
        // Use current ChatRoom Id.
        if (currentChatRoom != null) {
            Thread temp = new Thread(() -> {
                try {
                    connectedSocket = new Socket(this.address, 8189);
                    outbound = new BufferedWriter(new OutputStreamWriter(connectedSocket.getOutputStream()));
                    inbound = new ObjectInputStream(connectedSocket.getInputStream());

                    String req = String.format("%s,%s\n", "REQ_C", currentChatRoom.getChatRoomId());
                    outbound.write(req);
                    outbound.flush();

                    ArrayList<Chat> result = (ArrayList<Chat>) inbound.readObject();
                    chats.addAll(result);

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
        }

        return chats;
    }

    public void addChat(String msg) {
        // ChatRoomId: CurrentChatRoom.getChatRoomId
        // userName1(Sender): SessionUser.

        // Sender2 (Receiver)
        User receiver = null;
        for (User user : currentChatRoom.getUsers()) {
            if (!user.equals(sessionUser)) {
                receiver = user;
            }
        }

        if (receiver != null) {
            User finalReceiver = receiver;
            Thread temp = new Thread(() -> {
                try {
                    connectedSocket = new Socket(this.address, 8189);
                    outbound = new BufferedWriter(new OutputStreamWriter(connectedSocket.getOutputStream()));
                    inbound = new ObjectInputStream(connectedSocket.getInputStream());

                    String req = String.format("%s,%s,%s,%s,%s\n",
                            "ADD_C", currentChatRoom.getChatRoomId(), sessionUser.getUserName(),
                            finalReceiver.getUserName(), msg);
                    outbound.write(req);
                    outbound.flush();

                    connectedSocket.close();
                } catch (IOException e) {
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
        }

    }


    public User getSessionUser() {
        return this.sessionUser;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public void setCurrentChatRoom(ChatRoom cr) {
        currentChatRoom = cr;
    }

    public ChatRoom getCurrentChatRoom() {
        return this.currentChatRoom;
    }
}
