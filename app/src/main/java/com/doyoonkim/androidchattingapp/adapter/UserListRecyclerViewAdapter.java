package com.doyoonkim.androidchattingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doyoonkim.androidchattingapp.R;

import java.util.ArrayList;

import data.User;

public class UserListRecyclerViewAdapter extends RecyclerView.Adapter<UserListViewHolder> {
    private ArrayList<User> dataset;

    public UserListRecyclerViewAdapter(ArrayList<User> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_element, parent, false);
        return new UserListViewHolder(view);
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

    public UserListViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void onBind(User data) {
        TextView userNameTextView = itemView.findViewById(R.id.userNameTextView);
        userNameTextView.setText(data.getUserName());
    }
}
