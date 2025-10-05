package com.example.hompage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hompage.databinding.FragmentStoryBinding;

public class StoryFragment extends Fragment {

    private FragmentStoryBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // imageseriesone 클릭 이벤트 설정
        binding.imageseriesone.setOnClickListener(v -> {
            SeriesOneFragment fragment = new SeriesOneFragment();
            ((MainActivity) requireContext()).pushFragment(fragment, "SeriesOneFragment",0);
        });

        binding.imageseriestwo.setOnClickListener(v -> {
            SeriesOneFragment fragment = new SeriesOneFragment();
            ((MainActivity) requireContext()).pushFragment(fragment, "SeriesOneFragment", 0);
        });

        binding.imageseriesthree.setOnClickListener(v -> {
            SeriesOneFragment fragment = new SeriesOneFragment();
            ((MainActivity) requireContext()).pushFragment(fragment, "SeriesOneFragment", 0);
        });

        binding.imageseriesfour.setOnClickListener(v -> {
            SeriesOneFragment fragment = new SeriesOneFragment();
            ((MainActivity) requireContext()).pushFragment(fragment, "SeriesOneFragment", 0);
        });

        binding.imageseriesfive.setOnClickListener(v -> {
            SeriesOneFragment fragment = new SeriesOneFragment();
            ((MainActivity) requireContext()).pushFragment(fragment, "SeriesOneFragment", 0);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
