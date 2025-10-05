package com.example.hompage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ProfileFragment extends Fragment {

    private ImageView ivProfileImage;
    private EditText etNickname, etStatusMessage;
    private View btnSaveProfile;
    private RecyclerView rvImageList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseUser currentUser;

    private String selectedImageUrl; // 선택된 이미지의 다운로드 URL
    private List<String> imageUrlList;
    private ImageAdapter imageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Fragment의 레이아웃을 Inflate하여 반환
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        etNickname = view.findViewById(R.id.etNickname);
        etStatusMessage = view.findViewById(R.id.etStatusMessage);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
        rvImageList = view.findViewById(R.id.rvImageList);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        currentUser = mAuth.getCurrentUser();

        // "프로필 저장" 버튼 (닉네임, 상태메시지 변경 시 사용)
        btnSaveProfile.setOnClickListener(v -> saveUserProfile());

        // RecyclerView에 이미지 리스트 표시 준비
        imageUrlList = new ArrayList<>();
        imageAdapter = new ImageAdapter(imageUrlList, imageUrl -> {
            selectedImageUrl = imageUrl;
            Glide.with(getContext()).load(imageUrl).into(ivProfileImage);
            Toast.makeText(getContext(), "이미지가 선택되었습니다.", Toast.LENGTH_SHORT).show();
        });


        // 가로 스크롤 레이아웃 매니저
        rvImageList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvImageList.setAdapter(imageAdapter);

        // 프로필 정보 및 이미지 리스트 로드
        loadUserProfile();
        loadImageList();
    }

    private void loadImageList() {
        // Firestore의 profile_images 컬렉션에서 모든 이미지 문서를 불러옴
        db.collection("profile_images")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    imageUrlList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String imageUrl = doc.getString("imageUrl");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            imageUrlList.add(imageUrl);
                        }
                    }
                    // 이미지 리스트 갱신
                    imageAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "이미지 리스트 로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateProfileImage(String imageUrl) {
        selectedImageUrl = imageUrl;
    }


    private void saveUserProfile() {
        String nickname = etNickname.getText().toString();
        String statusMessage = etStatusMessage.getText().toString();

        Map<String, Object> profileUpdates = new HashMap<>();
        profileUpdates.put("nickname", nickname);
        profileUpdates.put("statusMessage", statusMessage);

        if (selectedImageUrl != null) {
            profileUpdates.put("profileImageUrl", selectedImageUrl);
        }

        updateUserProfile(profileUpdates);
    }


    private void updateUserProfile(Map<String, Object> profileUpdates) {
        db.collection("users").document(currentUser.getUid())
                .update(profileUpdates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "프로필 업데이트 완료", Toast.LENGTH_SHORT).show();

                    if (profileUpdates.containsKey("nickname") || profileUpdates.containsKey("statusMessage")) {


                        HomeFragment homeFragment = new HomeFragment();
                        ((MainActivity) requireActivity()).pushFragment(homeFragment, "HomeFragment", 1);

                        // ProfileFragment만 백스택에서 제거
                        Deque<String> group4Deque = ((MainActivity) requireActivity()).getBackStack("group4");
                        if (group4Deque != null && !group4Deque.isEmpty()) {
                            String topFragmentTag = group4Deque.peekFirst();
                            if ("ProfileFragment".equals(topFragmentTag)) {
                                group4Deque.pollFirst(); // ProfileFragment만 제거
                                Fragment profileFragment = getParentFragmentManager().findFragmentByTag(topFragmentTag);
                                if (profileFragment != null) {
                                    getParentFragmentManager().beginTransaction()
                                            .remove(profileFragment)
                                            .commit();
                                }
                            }
                        }
                        if (group4Deque.size() == 1)
                            return;
                        if (group4Deque != null && !group4Deque.isEmpty()) {
                            String topFragmentTag = group4Deque.peekFirst();
                            if ("HomeFragment".equals(topFragmentTag)) {
                                group4Deque.pollFirst(); // 기존의 home fragment 제거
                                Fragment HomeFragment = getParentFragmentManager().findFragmentByTag(topFragmentTag);
                                if (HomeFragment != null) {
                                    getParentFragmentManager().beginTransaction()
                                            .remove(HomeFragment)
                                            .commit();
                                }
                            }
                        }

                    }
                });
    }


    private void loadUserProfile() {
        if (currentUser == null) {
            Toast.makeText(getContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users").document(currentUser.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nickname = documentSnapshot.getString("nickname");
                        String statusMessage = documentSnapshot.getString("statusMessage");
                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");

                        etNickname.setText(nickname);
                        etStatusMessage.setText(statusMessage);

                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(getContext()).load(profileImageUrl).into(ivProfileImage);
                        } else {
                            ivProfileImage.setImageResource(R.drawable.defaultprofile);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "프로필 로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

