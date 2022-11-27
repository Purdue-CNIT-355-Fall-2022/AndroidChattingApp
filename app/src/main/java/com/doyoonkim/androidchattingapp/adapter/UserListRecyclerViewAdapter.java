package com.doyoonkim.androidchattingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doyoonkim.androidchattingapp.ChatSession;
import com.doyoonkim.androidchattingapp.R;

import java.util.ArrayList;

import data.User;

public class UserListRecyclerViewAdapter extends RecyclerView.Adapter<UserListViewHolder> {
    private ArrayList<User> dataset;
    private Context context;

    public UserListRecyclerViewAdapter(ArrayList<User> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_element, parent, false);
        return new UserListViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        holder.onBind(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}

class UserListViewHolder extends RecyclerView.ViewHolder {
    private Context context;

    public UserListViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
    }

    public void onBind(User data) {
        TextView userNameTextView = itemView.findViewById(R.id.userNameTextView);
        userNameTextView.setText(data.getUserName());

        // Btn Event listener.
        Button addChatBtn = itemView.findViewById(R.id.createChatBtn);
        addChatBtn.setOnClickListener((itemView) -> {
            ChatSession.getInstance(context).addNewChatRoom(data.getUserName());
        } );
    }
}
