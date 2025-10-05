package com.example.hompage;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.hompage.R;
import com.example.hompage.model.MiniHomeItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendHomeFragment extends Fragment {

    private ImageView ivFriendProfile;
    private TextView tvFriendNickname, tvFriendStatusMessage;
    private FrameLayout miniHomeView;

    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_friend_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivFriendProfile = view.findViewById(R.id.ivFriendProfile);
        tvFriendNickname = view.findViewById(R.id.tvFriendNickname);
        tvFriendStatusMessage = view.findViewById(R.id.tvFriendStatusMessage);
        miniHomeView = view.findViewById(R.id.minihome_view);

        db = FirebaseFirestore.getInstance();
        Log.d("FriendHomeFragment", "Friend id: " + getArguments().getString("friendId"));
        String friendUid = getArguments() != null ? getArguments().getString("friendId") : null; //여기서 nullpoingexception이 뜸

        if (friendUid != null) {
            loadFriendProfile(friendUid);
            loadMiniHome(friendUid);
        } else {
            Log.e("FriendHomeFragment", "No friend UID provided");
        }
    }

    private void loadFriendProfile(String friendUid) {
        db.collection("users").document(friendUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nickname = documentSnapshot.getString("nickname");
                        String statusMessage = documentSnapshot.getString("statusMessage");
                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");

                        tvFriendNickname.setText(nickname != null ? nickname : "Unknown");
                        tvFriendStatusMessage.setText(statusMessage != null ? statusMessage : "No status message");

                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(getContext()).load(profileImageUrl).into(ivFriendProfile);
                        } else {
                            ivFriendProfile.setImageResource(R.drawable.defaultprofile);
                        }
                    } else {
                        Log.e("FriendHomeFragment", "No profile found for UID: " + friendUid);
                    }
                })
                .addOnFailureListener(e -> Log.e("FriendHomeFragment", "Failed to load profile", e));
    }

    private void loadMiniHome(String friendUid) {
        db.collection("users").document(friendUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String miniHomeJson = documentSnapshot.getString("miniHome");
                        if (miniHomeJson != null && !miniHomeJson.isEmpty()) {
                            parseAndRenderMiniHome(miniHomeJson);
                        } else {
                            Log.e("FriendHomeFragment", "MiniHome data is empty for UID: " + friendUid);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("FriendHomeFragment", "Failed to load MiniHome", e));
    }

    private void parseAndRenderMiniHome(String miniHomeJson) {
        try {
            // JSON 배열 파싱
            JSONArray jsonArray = new JSONArray(miniHomeJson);
            List<MiniHomeItem> miniHomeItems = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String imageUrl = jsonObject.getString("imageUrl");
                float x = (float) jsonObject.getDouble("x");
                float y = (float) jsonObject.getDouble("y");

                miniHomeItems.add(new MiniHomeItem(imageUrl, x, y));
            }

            // MiniHome 이미지 렌더링
            for (MiniHomeItem item : miniHomeItems) {
                addImageToMiniHome(item);
            }
        } catch (JSONException e) {
            Log.e("FriendHomeFragment", "Failed to parse MiniHome JSON", e);
        }
    }

    private void addImageToMiniHome(MiniHomeItem item) {
        ImageView imageView = new ImageView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(300, 200);
        params.leftMargin = (int) item.getX();
        params.topMargin = (int) item.getY();
        imageView.setLayoutParams(params);

        Glide.with(getContext()).load(item.getImageUrl()).into(imageView);
        miniHomeView.addView(imageView);
    }
}