package com.example.hompage;

import android.content.Context;
import android.content.SharedPreferences;

public class CharactersListItem {
    private int imageResId;  // 이미지 리소스 ID
    private String text; //티니핑 이름
    private boolean isHeartFilled;

    public CharactersListItem(int imageResId, String text, Context context){
        this.imageResId = imageResId;
        this.text = text;
        this.isHeartFilled = getHeartStateFromPrefs(context, text);
    }
    public int getImageResId() {
        return imageResId;
    }

    public String getText() {
        return text;
    }

    public boolean isHeartFilled() {
        return isHeartFilled;
    }
    public void toggleHeart(Context context){
        isHeartFilled=!isHeartFilled;
        // 상태 변경 후 SharedPreferences에 저장
        saveHeartStateToPrefs(context, text, isHeartFilled);
    }
    public int getHeartImageResId() {
        return isHeartFilled ? R.drawable.like_filter_button : R.drawable.empty_heart;
    }
    // SharedPreferences에 하트 상태 저장
    private void saveHeartStateToPrefs(Context context, String itemName, boolean isFilled) {
        SharedPreferences prefs = context.getSharedPreferences("heart_state_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(itemName, isFilled);
        editor.apply();
    }

    // SharedPreferences에서 하트 상태 불러오기
    private boolean getHeartStateFromPrefs(Context context, String itemName) {
        SharedPreferences prefs = context.getSharedPreferences("heart_state_prefs", Context.MODE_PRIVATE);
        return prefs.getBoolean(itemName, false); // 기본값은 false
    }

}
