package com.example.hompage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hompage.databinding.CharacterItemBinding;

import java.util.List;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder> {
    private List<CharactersListItem> list;
    private Context context;
    private FragmentManager fragmentManager;

    public CharactersAdapter(List<CharactersListItem> list, Context context, FragmentManager fragmentManager){
        this.list = list;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public CharactersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CharacterItemBinding binding = CharacterItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CharactersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CharactersViewHolder holder, int position) {
        CharactersListItem item = list.get(position);
        holder.binding.imageView.setImageResource(item.getImageResId());
        holder.binding.textView.setText(item.getText());
        holder.binding.likeButton.setImageResource(item.getHeartImageResId()); // 하트 이미지 설정

        // 하트 버튼 클릭 리스너 설정
        holder.binding.likeButton.setOnClickListener(view -> {
            item.toggleHeart(context); // 하트 상태 반전 및 SharedPreferences에 저장
            holder.binding.likeButton.setImageResource(item.getHeartImageResId()); // 변경된 하트 이미지 업데이트
        });

        // 이미지 클릭 리스너 설정
        holder.binding.imageView.setOnClickListener(view -> {
            // InformationTinipingFragment 생성
            InformationCharacterFragment fragment = new InformationCharacterFragment();
            Bundle bundle = new Bundle();
            bundle.putString("clickedItem", holder.binding.textView.getText().toString());
            fragment.setArguments(bundle);
            ((MainActivity) context).pushFragment(fragment, "InformationCharacterFragment", 0);

        });
    }

    public void updateList(List<CharactersListItem> newList) {
        list.clear();
        list.addAll(newList); // 새 리스트를 추가
        notifyDataSetChanged(); // 어댑터 갱신
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class CharactersViewHolder extends RecyclerView.ViewHolder {
        private final CharacterItemBinding binding;

        public CharactersViewHolder(CharacterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

