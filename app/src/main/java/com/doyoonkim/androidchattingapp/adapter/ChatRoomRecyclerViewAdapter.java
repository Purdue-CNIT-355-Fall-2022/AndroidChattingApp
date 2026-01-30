package com.doyoonkim.androidchattingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doyoonkim.androidchattingapp.R;

import java.util.ArrayList;

import data.ChatRoom;

public class ChatRoomRecyclerViewAdapter extends RecyclerView.Adapter<ChatRoomViewHolder> {
    private ArrayList<ChatRoom> dataset;
    private ChatRoomClickListener listener;

    public interface ChatRoomClickListener {
        void onClick(ChatRoom cr);
    }

    public ChatRoomRecyclerViewAdapter(ArrayList<ChatRoom> dataset, ChatRoomClickListener listener) {
        this.dataset = dataset;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chatroom_element, parent, false);
        return new ChatRoomViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        holder.onBind(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

}

class ChatRoomViewHolder extends RecyclerView.ViewHolder {
    private ChatRoomRecyclerViewAdapter.ChatRoomClickListener listener;

    public ChatRoomViewHolder(@NonNull View itemView, ChatRoomRecyclerViewAdapter.ChatRoomClickListener listener) {
        super(itemView);
        this.listener = listener;
    }

    public void onBind(ChatRoom data) {
        LinearLayout mLinearLayout = itemView.findViewById(R.id.mLinearLayout);
        mLinearLayout.setOnClickListener((view) -> {
            if (listener != null) {
                listener.onClick(data);
            }
        });
        TextView chatRoomTitle = itemView.findViewById(R.id.chatRoomNameTextView);
        chatRoomTitle.setText(data.getChatroomName());
    }
}