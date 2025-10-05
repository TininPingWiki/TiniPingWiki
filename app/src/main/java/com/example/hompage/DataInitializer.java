package com.example.hompage;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataInitializer {
    public static List<ListItem> initializeData(Context context) {
        List<ListItem> fullList = new ArrayList<>();
        fullList.add(new ListItem(R.drawable.heartsping, "하츄핑", "royal", context));//
        fullList.add(new ListItem(R.drawable.luckyping, "행운핑", "legend", context));//
        fullList.add(new ListItem(R.drawable.dadaping, "바로핑", "royal", context));//
        fullList.add(new ListItem(R.drawable.kikiping, "키키핑", "normal", context));//
        fullList.add(new ListItem(R.drawable.gogoping, "아자핑", "royal", context));//
        fullList.add(new ListItem(R.drawable.giggleping, "악동핑", "villan", context));
        fullList.add(new ListItem(R.drawable.chachaping, "차차핑", "royal", context));//
        fullList.add(new ListItem(R.drawable.charmping, "아잉핑", "normal", context));
        fullList.add(new ListItem(R.drawable.lalaping, "라라핑", "royal", context));//
        fullList.add(new ListItem(R.drawable.egoping, "앙대핑", "villan", context));
        fullList.add(new ListItem(R.drawable.happying, "해핑", "royal", context));//
        fullList.add(new ListItem(R.drawable.shyping, "부끄핑", "normal", context));
        fullList.add(new ListItem(R.drawable.tangyping, "새콤핑", "legend", context));//
        fullList.add(new ListItem(R.drawable.teeheeping, "방글핑", "royal", context));//
        fullList.add(new ListItem(R.drawable.trustping, "믿어핑", "royal", context));//
        fullList.add(new ListItem(R.drawable.dareping, "부투핑", "normal", context));
        fullList.add(new ListItem(R.drawable.maskping, "가면핑", "villan", context));
        fullList.add(new ListItem(R.drawable.shimmerping, "빛나핑", "royal", context));//
        fullList.add(new ListItem(R.drawable.sweetping, "달콤핑", "legend", context));//
        fullList.add(new ListItem(R.drawable.blankping, "깜빡핑", "normal", context));
        fullList.add(new ListItem(R.drawable.onlyping, "다해핑", "villan", context));
        fullList.add(new ListItem(R.drawable.nanaping, "나나핑", "royal", context));//
        fullList.add(new ListItem(R.drawable.auroraping, "오로라핑", "legend", context));
        fullList.add(new ListItem(R.drawable.dreamping, "띠용핑", "normal", context));
        fullList.add(new ListItem(R.drawable.jellyping, "말랑핑", "royal", context));//

        return fullList;
    }

    public static Map<String, List<ListItem>> filterDataByType(List<ListItem> fullList) {
        Map<String, List<ListItem>> filteredLists = new HashMap<>();
        filteredLists.put("royal", new ArrayList<>());
        filteredLists.put("legend", new ArrayList<>());
        filteredLists.put("normal", new ArrayList<>());
        filteredLists.put("villan", new ArrayList<>());

        for (ListItem item : fullList) {
            filteredLists.get(item.getType()).add(item);
        }
        return filteredLists;
    }
}