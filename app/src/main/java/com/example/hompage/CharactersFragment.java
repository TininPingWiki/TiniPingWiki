package com.example.hompage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.hompage.databinding.FragmentCharactersBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CharactersFragment extends Fragment {
    private CharactersAdapter adapter; // RecyclerView 어댑터
    private List<CharactersListItem> fulllist;
    private List<CharactersListItem> heartFilteredList;
    private List<CharactersListItem> searchFilteredList;
    private FragmentCharactersBinding binding;
    private boolean isFilterApplied = false; // 필터 상태를 추적

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCharactersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //
        fulllist = CharactersDataInitializer.initializeData(requireContext());

        heartFilteredList = new ArrayList<>(fulllist);
        searchFilteredList = new ArrayList<>(fulllist);

        //
        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        adapter = new CharactersAdapter(heartFilteredList, requireContext(), requireActivity().getSupportFragmentManager());
        binding.recyclerView.setAdapter(adapter);

        setupFilters();

        setupSearch();
    }

    private void setupFilters() {
        binding.likeFilterButton.setOnClickListener(view -> filterItemsByHeartState(true));
    }

    private void setupSearch() {
        binding.searchView.setOnClickListener(view -> {
            if (binding.searchView.getVisibility() == View.GONE) {
                binding.searchView.setVisibility(View.VISIBLE);
                binding.searchView.setIconified(false);
            } else {
                binding.searchView.setVisibility(View.GONE);
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
    }

    private void filterItemsByHeartState(boolean isFilled) {
        if (isFilterApplied) {
            // 필터 해제: 전체 리스트 복원
            heartFilteredList.clear();
            heartFilteredList.addAll(fulllist);
            isFilterApplied = false;
        } else {
            // 필터 적용: 좋아요 상태로 필터링
            heartFilteredList.clear();
            for (CharactersListItem item : fulllist) {
                if (item.isHeartFilled() == isFilled) {
                    heartFilteredList.add(item);
                }
            }
            isFilterApplied = true;
        }
        adapter.notifyDataSetChanged();
    }

    private void filterList(String query) {
        searchFilteredList.clear();
        if (query.isEmpty()) {
            searchFilteredList.addAll(fulllist);
        } else {
            for (CharactersListItem item : fulllist) {
                if (item.getText().toLowerCase(Locale.KOREAN).contains(query.toLowerCase(Locale.KOREAN))) {
                    searchFilteredList.add(item);
                }
            }
        }
        adapter.updateList(searchFilteredList);
    }
}
