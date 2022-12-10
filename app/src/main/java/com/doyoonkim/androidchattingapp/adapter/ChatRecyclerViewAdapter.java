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

import java.util.ArrayList;

import data.Chat;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private ArrayList<Chat> dataset = new ArrayList<Chat>();
    private static final String APP_ID = "20221206001486416";
    private static final String SECURITY_KEY = "wcH8q9RlUx3N6DaNooeI";
    public ChatRecyclerViewAdapter(ArrayList<Chat> dataset) {
        this.dataset = dataset;
    }

    public void notifyChanges(ArrayList<Chat> dataset) {
        Log.d("Same?", "" + this.dataset.containsAll(dataset));
        if (!(this.dataset.containsAll(dataset))) {
            this.dataset = dataset;
            for (Chat s : dataset){
                String message = s.getMsg();
                TransApi api = new TransApi(APP_ID, SECURITY_KEY);
                String translatedJson = api.getTransResult(message, "auto", "en");
                Result jsonObject = new Gson().fromJson(translatedJson, Result.class);
                String translated = jsonObject.getTrans_result().get(0).getDst();
                System.out.println(translatedJson);
                s.setMsg(translated);
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
//        timestamp.setText(data.getSendDate().getTime());

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
