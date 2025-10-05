package com.example.hompage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hompage.databinding.FragmentSeriesOneBinding;

public class SeriesOneFragment extends Fragment {
    private FragmentSeriesOneBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSeriesOneBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // imageseriesone 클릭 이벤트 설정
        binding.episodeListButton.setOnClickListener(v -> {
            SeriesOneListFragment fragment = new SeriesOneListFragment();
            // MainActivity의 pushFragment 메서드 사용
            ((MainActivity) requireContext()).pushFragment(fragment, "SeriesOneListFragment", 0);
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

