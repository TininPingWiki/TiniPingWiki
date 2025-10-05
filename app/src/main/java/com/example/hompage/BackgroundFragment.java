package com.example.hompage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hompage.databinding.FragmentBackgroundBinding;

public class BackgroundFragment extends Fragment {

    private FragmentBackgroundBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBackgroundBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // 각 이미지 클릭 리스너 수정
        binding.imagebakery.setOnClickListener(v -> {
            BakeryFragment fragment = new BakeryFragment();
            ((MainActivity) requireContext()).pushFragment(fragment, "BakeryFragment", 0);
        });

        binding.imagekingdom.setOnClickListener(v -> {
            BakeryFragment fragment = new BakeryFragment();
            ((MainActivity) requireContext()).pushFragment(fragment, "BakeryFragment", 0);
        });

        binding.imageschool.setOnClickListener(v -> {
            BakeryFragment fragment = new BakeryFragment();
            ((MainActivity) requireContext()).pushFragment(fragment, "BakeryFragment", 0);
        });

        binding.imagevillage.setOnClickListener(v -> {
            BakeryFragment fragment = new BakeryFragment();
            ((MainActivity) requireContext()).pushFragment(fragment, "BakeryFragment", 0);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

