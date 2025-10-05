package com.example.hompage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hompage.model.Friend;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendListFragment extends Fragment implements FriendAdapter.OnFriendActionListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private RecyclerView rvFriendList;
    private FriendAdapter friendAdapter;
    private List<Friend> friendList;

    private EditText etFriendEmail;
    private Button btnSearchFriend, btnAddFriend;
    private LinearLayout llFriendSearchResult;
    private ImageView ivFriendProfile;
    private TextView tvFriendNickname, tvFriendStatusMessage;

    private String searchedFriendUid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Fragment에서 사용하는 레이아웃을 반환
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        // Firebase 초기화
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // RecyclerView 초기화
        rvFriendList = view.findViewById(R.id.rvFriendList);
        friendList = new ArrayList<>();
        friendAdapter = new FriendAdapter(friendList, this);
        rvFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFriendList.setAdapter(friendAdapter);

        // 검색 UI 초기화
        etFriendEmail = view.findViewById(R.id.etFriendEmail);
        btnSearchFriend = view.findViewById(R.id.btnSearchFriend);
        llFriendSearchResult = view.findViewById(R.id.llFriendSearchResult);
        ivFriendProfile = view.findViewById(R.id.ivFriendProfile);
        tvFriendNickname = view.findViewById(R.id.tvFriendNickname);
        tvFriendStatusMessage = view.findViewById(R.id.tvFriendStatusMessage);
        btnAddFriend = view.findViewById(R.id.btnAddFriend);

        // 검색 버튼
        btnSearchFriend.setOnClickListener(v -> {
            String email = etFriendEmail.getText().toString().trim();
            if (!email.isEmpty()) {
                searchFriendByEmail(email);
            } else {
                Toast.makeText(getContext(), "Enter a valid email", Toast.LENGTH_SHORT).show();
            }
        });

        // 친구 추가 버튼
        btnAddFriend.setOnClickListener(v -> {
            if (searchedFriendUid != null) {
                addFriendToFirestore(searchedFriendUid, tvFriendNickname.getText().toString());
            } else {
                Toast.makeText(getContext(), "No friend selected", Toast.LENGTH_SHORT).show();
            }
        });

        loadFriendList();

        return view;
    }

    private void searchFriendByEmail(String email) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        searchedFriendUid = document.getId();
                        String nickname = document.getString("nickname");
                        String statusMessage = document.getString("statusMessage");
                        String profileImageUrl = document.getString("profileImageUrl");

                        llFriendSearchResult.setVisibility(View.VISIBLE);
                        tvFriendNickname.setText(nickname != null ? nickname : "No nickname");
                        tvFriendStatusMessage.setText(statusMessage != null ? statusMessage : "No status message");
                        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                            Glide.with(getContext()).load(profileImageUrl).into(ivFriendProfile);
                        } else {
                            ivFriendProfile.setImageResource(R.drawable.defaultprofile);
                        }
                    } else {
                        Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                        llFriendSearchResult.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> Log.e("SearchFriend", "Error searching friend: " + e.getMessage()));
    }

    private void addFriendToFirestore(String friendUid, String nickname) {
        db.collection("users").document(friendUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String profileImageUrl = documentSnapshot.getString("profileImageUrl");
                        String statusMessage = documentSnapshot.getString("statusMessage");

                        Map<String, Object> friendData = new HashMap<>();
                        friendData.put("friendId", friendUid);
                        friendData.put("nickname", nickname);
                        friendData.put("profileImageUrl", profileImageUrl);
                        friendData.put("statusMessage", statusMessage);

                        db.collection("users").document(currentUser.getUid())
                                .collection("friends")
                                .document(friendUid)
                                .set(friendData)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getContext(), "Friend added!", Toast.LENGTH_SHORT).show();
                                    llFriendSearchResult.setVisibility(View.GONE);

                                    friendList.add(new Friend(friendUid, profileImageUrl, nickname, statusMessage));
                                    friendAdapter.notifyDataSetChanged();
                                })
                                .addOnFailureListener(e -> Log.e("AddFriend", "Failed to add friend: " + e.getMessage()));
                    }
                })
                .addOnFailureListener(e -> Log.e("AddFriend", "Failed to fetch friend profile: " + e.getMessage()));
    }

    private void loadFriendList() {
        db.collection("users").document(currentUser.getUid()).collection("friends")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    friendList.clear();
                    for (DocumentSnapshot doc : querySnapshot) {
                        String friendId = doc.getString("friendId");
                        String profileImageUrl = doc.getString("profileImageUrl");
                        String nickname = doc.getString("nickname");
                        String statusMessage = doc.getString("statusMessage");

                        friendList.add(new Friend(friendId, profileImageUrl, nickname, statusMessage));
                    }
                    friendAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("FriendList", "Failed to load friend list: " + e.getMessage()));
    }

    @Override
    public void onDelete(Friend friend, int position) {
        // RecyclerView에서만 삭제
        if (position >= 0 && position < friendList.size()) {
            friendList.remove(position);
            friendAdapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), friend.getNickname() + " removed from the list.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onGoToHome(Friend friend) {
        // FriendHomeFragment의 새 인스턴스를 생성하고 Bundle 전달
        FriendHomeFragment friendHomeFragment = new FriendHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("friendId", friend.getFriendId()); // friendUid를 전달
        Log.d("FriendHomeFragment", "Friend id: " + friend.getFriendId());
        friendHomeFragment.setArguments(bundle); // 전달한 Bundle을 fragment에 설정

        // pushFragment 호출 시 제대로 설정된 friendHomeFragment 전달
        ((MainActivity) requireActivity()).pushFragment(friendHomeFragment, "FriendHomeFragment", 0);
    }

}



