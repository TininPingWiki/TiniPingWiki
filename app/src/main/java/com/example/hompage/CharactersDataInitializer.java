package com.example.hompage;

import android.content.Context;

import com.example.hompage.R;

import java.util.ArrayList;
import java.util.List;


public class CharactersDataInitializer {
    public static List<CharactersListItem> initializeData(Context context){
        List<CharactersListItem> fullList = new ArrayList<>();
        fullList.add(new CharactersListItem(R.drawable.romi,"로미",context));
        fullList.add(new CharactersListItem(R.drawable.maya,"마야",context));
        fullList.add(new CharactersListItem(R.drawable.marylou,"메리루",context));
        fullList.add(new CharactersListItem(R.drawable.jennie,"제니",context));
        fullList.add(new CharactersListItem(R.drawable.sarah,"사라",context));
        fullList.add(new CharactersListItem(R.drawable.ian,"이안",context));
        fullList.add(new CharactersListItem(R.drawable.kyle,"카일",context));
        fullList.add(new CharactersListItem(R.drawable.jun,"준",context));
        fullList.add(new CharactersListItem(R.drawable.lena,"레나",context));
        fullList.add(new CharactersListItem(R.drawable.cindy,"신디",context));
        fullList.add(new CharactersListItem(R.drawable.hani,"하니",context));
        fullList.add(new CharactersListItem(R.drawable.dylan,"딜런",context));
        fullList.add(new CharactersListItem(R.drawable.eden,"이든",context));
        fullList.add(new CharactersListItem(R.drawable.nyle,"나일",context));
        fullList.add(new CharactersListItem(R.drawable.ellie,"엘리",context));
        fullList.add(new CharactersListItem(R.drawable.raymond,"레이먼",context));
        fullList.add(new CharactersListItem(R.drawable.dua,"두아",context));
        fullList.add(new CharactersListItem(R.drawable.noa,"노아",context));
        fullList.add(new CharactersListItem(R.drawable.dayne,"데인",context));
        fullList.add(new CharactersListItem(R.drawable.olivia,"올리비아",context));

        return fullList;
    }
}
