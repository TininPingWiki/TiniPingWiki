package com.example.hompage;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hompage.R;
import com.example.hompage.model.Friend;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private final List<Friend> friendList;
    private final OnFriendActionListener onFriendActionListener;

    public FriendAdapter(List<Friend> friendList, OnFriendActionListener onFriendActionListener) {
        this.friendList = friendList;
        this.onFriendActionListener = onFriendActionListener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friendList.get(position);
        holder.tvFriendName.setText(friend.getNickname());
        holder.tvFriendStatusMessage.setText(friend.getStatusMessage());

        if (friend.getProfileImageUrl() != null && !friend.getProfileImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(friend.getProfileImageUrl())
                    .into(holder.ivFriendProfile);
        } else {
            holder.ivFriendProfile.setImageResource(R.drawable.defaultprofile);
        }

        holder.btnDeleteFriend.setOnClickListener(v -> {
            if (onFriendActionListener != null) {
                onFriendActionListener.onDelete(friend, position);
            }
        });

        holder.btnGoToHome.setOnClickListener(v -> {
            if (onFriendActionListener != null) {
                Log.d("FriendHomeFragment", "Friend nickname: " + friend.getNickname());
                Log.d("FriendHomeFragment", "Friend ProgileImageUrl: " + friend.getProfileImageUrl());
                Log.d("FriendHomeFragment", "Friend statusMessage: " + friend.getStatusMessage());
                Log.d("FriendHomeFragment", "Friend id: " + friend.getFriendId());
                onFriendActionListener.onGoToHome(friend);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFriendProfile;
        TextView tvFriendName, tvFriendStatusMessage;
        Button btnDeleteFriend, btnGoToHome;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFriendProfile = itemView.findViewById(R.id.ivFriendProfile);
            tvFriendName = itemView.findViewById(R.id.tvFriendName);
            tvFriendStatusMessage = itemView.findViewById(R.id.tvFriendStatusMessage);
            btnDeleteFriend = itemView.findViewById(R.id.btnDeleteFriend);
            btnGoToHome = itemView.findViewById(R.id.btnGoToHome);
        }
    }

    public interface OnFriendActionListener {
        void onDelete(Friend friend, int position);
        void onGoToHome(Friend friend);
    }
}

