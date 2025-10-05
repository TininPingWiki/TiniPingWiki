package com.example.hompage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.hompage.databinding.FragmentTinipingDictionaryBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DictionaryFragment extends Fragment {

    private MyAdapter adapter;
    private List<ListItem> fullList;
    private List<ListItem> heartFilteredList;
    private List<ListItem> searchFilteredList;
    private List<ListItem> royalTinipingFilteredList;
    private List<ListItem> legendTinipingFilteredList;
    private List<ListItem> normalTinipingFilteredList;
    private List<ListItem> villanTinipingFilteredList;
    private FragmentTinipingDictionaryBinding binding;

    private String activeFilter = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTinipingDictionaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 데이터 초기화
        fullList = DataInitializer.initializeData(requireContext());
        Map<String, List<ListItem>> filteredData = DataInitializer.filterDataByType(fullList);

        royalTinipingFilteredList = filteredData.get("royal");
        legendTinipingFilteredList = filteredData.get("legend");
        normalTinipingFilteredList = filteredData.get("normal");
        villanTinipingFilteredList = filteredData.get("villan");
        heartFilteredList = new ArrayList<>(fullList);
        searchFilteredList = new ArrayList<>(fullList);


        // RecyclerView 설정
        binding.recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2)); // 2열 설정
        adapter = new MyAdapter(heartFilteredList, requireContext(), requireActivity().getSupportFragmentManager(), this);
        binding.recyclerView.setAdapter(adapter);


        // 필터링 버튼 리스너 설정
        setupFilters();

        // 검색 기능 설정
        setupSearch();


    }

    private void setupFilters() {
        View.OnClickListener filterClickListener = view -> {
            String selectedFilter = "";
            List<ListItem> filteredList = fullList;

            if (view == binding.likeFilterButton) {
                selectedFilter = "heart";
                filteredList = filterItemsByHeartState(true);
            } else if (view == binding.royalButton) {
                selectedFilter = "royal";
                filteredList = royalTinipingFilteredList;
            } else if (view == binding.legendButton) {
                selectedFilter = "legend";
                filteredList = legendTinipingFilteredList;
            } else if (view == binding.normalButton) {
                selectedFilter = "normal";
                filteredList = normalTinipingFilteredList;
            } else if (view == binding.villanButton) {
                selectedFilter = "villan";
                filteredList = villanTinipingFilteredList;
            }

            if (selectedFilter.equals(activeFilter)) {
                activeFilter = ""; // 모든 필터 비활성화
                adapter.updateList(fullList);
            } else {
                activeFilter = selectedFilter;
                adapter.updateList(filteredList);
            }

            binding.recyclerView.scrollToPosition(0);
        };

        // 각 버튼에 공통 리스너 적용
        binding.likeFilterButton.setOnClickListener(filterClickListener);
        binding.royalButton.setOnClickListener(filterClickListener);
        binding.legendButton.setOnClickListener(filterClickListener);
        binding.normalButton.setOnClickListener(filterClickListener);
        binding.villanButton.setOnClickListener(filterClickListener);
    }

    private List<ListItem> filterItemsByHeartState(boolean isFilled) {
        List<ListItem> filteredList = new ArrayList<>();
        for (ListItem item : fullList) {
            if (item.isHeartFilled() == isFilled) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    private void setupSearch() {
        binding.searchView.setOnSearchClickListener(v -> {
            ViewGroup.LayoutParams params = binding.searchView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            binding.searchView.setLayoutParams(params);
        });

        binding.searchView.setOnCloseListener(() -> {
            ViewGroup.LayoutParams params = binding.searchView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            binding.searchView.setLayoutParams(params);
            return false;
        });

        binding.searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String query) {
        searchFilteredList.clear();
        if (query.isEmpty()) {
            searchFilteredList.addAll(fullList);
        } else {
            for (ListItem item : fullList) {
                if (item.getText().toLowerCase(Locale.KOREAN).contains(query.toLowerCase(Locale.KOREAN))) {
                    searchFilteredList.add(item);
                }
            }
        }
        adapter.updateList(searchFilteredList);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;  // 메모리 누수를 방지하기 위해 뷰 바인딩 해제
    }
}


