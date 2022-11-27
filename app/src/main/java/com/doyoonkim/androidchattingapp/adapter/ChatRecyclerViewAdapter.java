package com.doyoonkim.androidchattingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.doyoonkim.androidchattingapp.ChatSession;
import com.doyoonkim.androidchattingapp.R;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

import data.Chat;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private ArrayList<Chat> dataset = new ArrayList<Chat>();

    public ChatRecyclerViewAdapter(ArrayList<Chat> dataset) {
        this.dataset = dataset;
    }

    public void notifyChanges(ArrayList<Chat> dataset) {
        this.dataset = dataset;
        notifyDataSetChanged();
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
