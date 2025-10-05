package com.example.hompage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hompage.databinding.ItemBinding;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<ListItem> list;
    private Context context;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    public MyAdapter(List<ListItem> list, Context context, FragmentManager fragmentManager, Fragment currentFragment) {
        this.list = list;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBinding binding = ItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ListItem item = list.get(position);
        holder.binding.imageView.setImageResource(item.getImageResId());
        holder.binding.textView.setText(item.getText());
        holder.binding.likeButton.setImageResource(item.getHeartImageResId());

        // Like button click listener
        holder.binding.likeButton.setOnClickListener(v -> {
            item.toggleHeart(context);
            holder.binding.likeButton.setImageResource(item.getHeartImageResId());
        });

        holder.binding.imageView.setOnClickListener(v -> {
            InformationTinipingFragment fragment = new InformationTinipingFragment();
            Bundle bundle = new Bundle();
            bundle.putString("clickedItem", holder.binding.textView.getText().toString());
            fragment.setArguments(bundle);

            // 현재 그룹에 Fragment 추가
            ((MainActivity) context).pushFragment(fragment, "InformationTinipingFragment", 0);
        });

    }

    public void updateList(List<ListItem> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemBinding binding;

        public MyViewHolder(ItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

