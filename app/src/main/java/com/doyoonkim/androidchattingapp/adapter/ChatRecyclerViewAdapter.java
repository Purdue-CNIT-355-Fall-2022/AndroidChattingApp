package com.doyoonkim.androidchattingapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doyoonkim.androidchattingapp.ChatSession;
import com.doyoonkim.androidchattingapp.R;
import com.doyoonkim.androidchattingapp.TransApi;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import data.Chat;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private ArrayList<Chat> dataset = new ArrayList<Chat>();
    private static final String APP_ID = "20221206001486416";
    private static final String SECURITY_KEY = "wcH8q9RlUx3N6DaNooeI";
    private TransApi api;

    public ChatRecyclerViewAdapter() {
        this.api = new TransApi(APP_ID, SECURITY_KEY);
    }


    public void notifyChanges(ArrayList<Chat> dataset) {
        Log.d("Same?", "" + this.dataset.containsAll(dataset));
        if (!(this.dataset.containsAll(dataset))) {
            Thread t = new Thread(() -> {
                for (Chat s : dataset){
                    if(s.getReceiver().equals(ChatSession.instance.getSessionUser())) {
                        System.out.println("Original Message: " + s.getMsg());
                        String message = s.getMsg();
                        String translatedJson = api.getTransResult(message, "auto", "en");
                        Result jsonObject = new Gson().fromJson(translatedJson, Result.class);

                        try {
                            String translated = jsonObject.getTrans_result().get(0).getDst();
                            System.out.println(translatedJson);
                            System.out.println(translated);
                            s.setMsg(translated);
                        } catch (NullPointerException npe) {
                            Log.d("ChatRecyclerViewAdapter", npe.getMessage());
                            System.out.println(translatedJson);
                        }
                    }
                }
                this.dataset = dataset;
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            notifyDataSetChanged();
            Log.d("ChatRecyclerViewAdapter", "Notified.");
        }
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_chat_element, parent, false
        );
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.onBind(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}

class ChatViewHolder extends RecyclerView.ViewHolder {
    private ChatSession session;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        session = ChatSession.instance;
    }

    public void onBind(Chat data) {
        TextView sender = itemView.findViewById(R.id.senderNameTextView);
        TextView msg = itemView.findViewById(R.id.msgTextView);
        TextView timestamp = itemView.findViewById(R.id.timeStampTextView);

        sender.setText(data.getSender().getUserName());
        msg.setText(data.getMsg());
        timestamp.setText(
                new SimpleDateFormat("MM.dd.HH.mm").format(new Date(data.getSendDate().getTime()))
        );

        if (data.getSender().equals(session.getSessionUser())) {
            sender.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            timestamp.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        } else {
            sender.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            timestamp.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }
}
