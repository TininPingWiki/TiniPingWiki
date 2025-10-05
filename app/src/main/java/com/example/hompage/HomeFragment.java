package com.example.hompage;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class HomeFragment extends Fragment {

    private FrameLayout miniHomeView;
    private FrameLayout deleteZone;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private ImageView ivProfileImage;
    private TextView tvNickname, tvStatusMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvFriends = view.findViewById(R.id.tvFriends);
        tvFriends.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).pushFragment(new FriendListFragment(), "FriendListFragment", 0);
        });

        TextView textView = view.findViewById(R.id.changeprofile);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ProfileFragment로 전환
                ((MainActivity) requireActivity()).pushFragment(new ProfileFragment(), "ProfileFragment", 0);
            }
        });

        TextView logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SharedPreferences에서 사용자 데이터 제거
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();  // 모든 데이터를 지움
                editor.apply();  // 변경사항 적용


                // LoginFragment로 전환
                ((MainActivity) requireActivity()).pushFragment(new LoginFragment(), "LoginFragment", 1);

                // HomeFragment만 백스택에서 제거
                Deque<String> group4Deque = ((MainActivity) requireActivity()).getBackStack("group4");
                if (group4Deque != null && !group4Deque.isEmpty()) {
                    String topFragmentTag = group4Deque.peekFirst();
                    if ("HomeFragment".equals(topFragmentTag)) {
                        group4Deque.pollFirst(); // HomeFragment만 제거
                        Fragment homeFragment = getParentFragmentManager().findFragmentByTag(topFragmentTag);
                        if (homeFragment != null) {
                            getParentFragmentManager().beginTransaction()
                                    .remove(homeFragment)
                                    .commit();
                        }
                    }
                }

            }

        });

        // Bottom Sheet를 여는 버튼
        Button showBottomSheetButton = view.findViewById(R.id.show_bottom_sheet_button);
        showBottomSheetButton.setOnClickListener(v -> {
            ScrollableBottomSheet bottomSheet = new ScrollableBottomSheet();
            bottomSheet.show(getParentFragmentManager(), "ScrollableBottomSheet");
        });


        // MiniHome 뷰와 삭제 영역 초기화
        miniHomeView = view.findViewById(R.id.minihome_view);
        deleteZone = view.findViewById(R.id.delete_zone);

        // Firebase 초기화
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // MiniHome 뷰의 드래그 앤 드롭 이벤트 처리
        miniHomeView.setOnDragListener(this::handleMiniHomeDrag);

        // Delete Zone의 드래그 앤 드롭 이벤트 처리
        deleteZone.setOnDragListener(this::handleDeleteZoneDrag);

        // Firebase에서 MiniHome 상태 복원
        restoreMiniHomeState();

        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvNickname = view.findViewById(R.id.tvNickname);
        tvStatusMessage = view.findViewById(R.id.tvStatusMessage);
        loadUserProfile();
    }

    private boolean handleMiniHomeDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                return event.getClipDescription() == null
                        || event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);

            case DragEvent.ACTION_DRAG_ENTERED:
                miniHomeView.setBackgroundResource(R.drawable.homehome); // 강조 효과
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                miniHomeView.setBackgroundResource(R.drawable.homehome); // 기본 색상 복구
                return true;

            case DragEvent.ACTION_DROP:
                View draggedView = (View) event.getLocalState();
                if (draggedView != null && draggedView.getParent() == miniHomeView) {
                    // MiniHome 내부 이미지 이동
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            draggedView.getWidth(), draggedView.getHeight());
                    params.leftMargin = (int) event.getX() - (draggedView.getWidth() / 2);
                    params.topMargin = (int) event.getY() - (draggedView.getHeight() / 2);
                    draggedView.setLayoutParams(params);
                    miniHomeView.invalidate();
                    saveMiniHomeStateToFirestore();
                } else {
                    // Bottom Sheet에서 드래그된 이미지 추가
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    String imageUrl = item.getText().toString();
                    addImageToMiniHome(imageUrl, event.getX(), event.getY());
                }
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                miniHomeView.setBackgroundResource(R.drawable.homehome);
                return true;

            default:
                return false;
        }
    }

    private boolean handleDeleteZoneDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundColor(0xFFD32F2F); // Delete Zone 강조 색상
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                deleteZone.setBackgroundResource(R.drawable.garbege); // 기본 색상 복구
                return true;

            case DragEvent.ACTION_DROP:
                View draggedView = (View) event.getLocalState();
                if (draggedView != null && draggedView.getParent() == miniHomeView) {
                    // 드래그된 뷰 삭제
                    miniHomeView.removeView(draggedView);
                    saveMiniHomeStateToFirestore();
                    Log.d("DeleteZone", "View deleted");
                }
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                deleteZone.setBackgroundResource(R.drawable.garbege); // 기본 색상 복구
                return true;

            default:
                return false;
        }
    }

    private void addImageToMiniHome(String imageUrl, float x, float y) {
        ImageView imageView = new ImageView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(300, 200);
        params.leftMargin = (int) x - 150;
        params.topMargin = (int) y - 100;
        imageView.setLayoutParams(params);

        // Glide로 이미지 로드
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);

        imageView.setTag(imageUrl); // Firebase 저장용 태그 설정
        enableDragAndDropForImage(imageView);
        miniHomeView.addView(imageView);

        saveMiniHomeStateToFirestore();
    }

    private void enableDragAndDropForImage(ImageView imageView) {
        imageView.setOnLongClickListener(v -> {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(imageView);
            v.startDragAndDrop(null, shadowBuilder, imageView, 0);
            return true;
        });
    }

    private void saveMiniHomeStateToFirestore() {
        try {
            JSONArray stateArray = new JSONArray();
            for (int i = 0; i < miniHomeView.getChildCount(); i++) {
                View child = miniHomeView.getChildAt(i);
                if (child instanceof ImageView) {
                    ImageView imageView = (ImageView) child;
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();

                    JSONObject imageObject = new JSONObject();
                    imageObject.put("imageUrl", (String) imageView.getTag());
                    imageObject.put("x", params.leftMargin);
                    imageObject.put("y", params.topMargin);
                    stateArray.put(imageObject);
                }
            }

            Map<String, Object> miniHomeData = new HashMap<>();
            miniHomeData.put("miniHome", stateArray.toString());

            db.collection("users").document(currentUser.getUid())
                    .update(miniHomeData)
                    .addOnSuccessListener(aVoid -> Log.d("MiniHome", "State saved to Firebase"))
                    .addOnFailureListener(e -> Log.e("MiniHome", "Failed to save MiniHome: " + e.getMessage()));

        } catch (Exception e) {
            Log.e("MiniHome", "Error saving MiniHome state: " + e.getMessage());
        }
    }

    private void restoreMiniHomeState() {
        db.collection("users").document(currentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String miniHomeState = documentSnapshot.getString("miniHome");
                        try {
                            JSONArray stateArray = new JSONArray(miniHomeState);
                            for (int i = 0; i < stateArray.length(); i++) {
                                JSONObject imageObject = stateArray.getJSONObject(i);
                                String imageUrl = imageObject.getString("imageUrl");
                                int x = imageObject.getInt("x");
                                int y = imageObject.getInt("y");

                                addImageToMiniHome(imageUrl, x, y);
                            }
                        } catch (Exception e) {
                            Log.e("MiniHome", "Failed to parse MiniHome state: " + e.getMessage());
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("MiniHome", "Failed to restore MiniHome: " + e.getMessage()));
    }
    private void loadUserProfile() {
        if (currentUser == null) return;

        db.collection("users").document(currentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");
                        String nickname = documentSnapshot.getString("nickname");
                        String statusMessage = documentSnapshot.getString("statusMessage");

                        // UI에 프로필 데이터 표시
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(this).load(profileImageUrl).into(ivProfileImage);
                        } else {
                            ivProfileImage.setImageResource(R.drawable.defaultprofile); // 기본 이미지
                        }
                        tvNickname.setText(nickname != null ? nickname : "No nickname");
                        tvStatusMessage.setText(statusMessage != null ? statusMessage : "No status message");
                    }
                });
    }
}

