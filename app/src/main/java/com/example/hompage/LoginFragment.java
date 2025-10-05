package com.example.hompage;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class LoginFragment extends Fragment {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private CheckBox cbAutoLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize SharedPreferences
        sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);
        cbAutoLogin = view.findViewById(R.id.autoLogin);

        btnRegister.setOnClickListener(v -> registerUser());
        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void registerUser() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // 회원가입 성공 시 사용자 프로필 생성
                        FirebaseUser user = mAuth.getCurrentUser();
                        createUserProfile(user);
                    } else {
                        Toast.makeText(getContext(), "회원가입 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createUserProfile(FirebaseUser user) {
        // 기본 사용자 프로필 생성
        Map<String, Object> profile = new HashMap<>();
        profile.put("userId", user.getUid());
        profile.put("email", user.getEmail());
        profile.put("nickname", ""); // 기본 닉네임은 빈 값
        profile.put("statusMessage", ""); // 기본 상태 메시지

        db.collection("users").document(user.getUid())
                .set(profile)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show();

                    if (cbAutoLogin.isChecked()) {
                        // CheckBox가 체크된 경우에만 SharedPreferences에 저장
                        saveUserToPreferences(user.getUid(), user.getEmail());
                    }

                    // ProfileFragment로 이동
                    ProfileFragment profileFragment = new ProfileFragment();
                    ((MainActivity) requireActivity()).pushFragment(profileFragment, "ProfileFragment", 1);

                    // LoginFragment만 큐에서 제거
                    Deque<String> group4Queue = ((MainActivity) requireActivity()).getBackStack("group4");
                    if (group4Queue != null && !group4Queue.isEmpty()) {
                        String firstFragmentTag = group4Queue.peekFirst(); // 큐의 첫 번째 원소 확인
                        if ("LoginFragment".equals(firstFragmentTag)) {
                            group4Queue.pollFirst(); // 큐에서 LoginFragment만 제거
                            Fragment loginFragment = getParentFragmentManager().findFragmentByTag(firstFragmentTag);
                            if (loginFragment != null) {
                                getParentFragmentManager().beginTransaction()
                                        .remove(loginFragment)
                                        .commit();
                            }
                        }
                    }
                });
    }


    private void loginUser() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getContext(), "로그인 성공!", Toast.LENGTH_SHORT).show();

                        if (cbAutoLogin.isChecked()) {
                            // CheckBox가 체크된 경우에만 SharedPreferences에 저장
                            saveUserToPreferences(user.getUid(), user.getEmail());
                        }

                        // HomeFragment로 전환
                        HomeFragment homeFragment = new HomeFragment();
                        ((MainActivity) requireActivity()).pushFragment(homeFragment, "HomeFragment", 1);

                        // LoginFragment만 큐에서 제거
                        Deque<String> group4Queue = ((MainActivity) requireActivity()).getBackStack("group4");
                        if (group4Queue != null && !group4Queue.isEmpty()) {
                            String firstFragmentTag = group4Queue.peekFirst(); // 큐의 첫 번째 원소 확인
                            if ("LoginFragment".equals(firstFragmentTag)) {
                                group4Queue.pollFirst(); // 큐에서 LoginFragment만 제거
                                Fragment loginFragment = getParentFragmentManager().findFragmentByTag(firstFragmentTag);
                                if (loginFragment != null) {
                                    getParentFragmentManager().beginTransaction()
                                            .remove(loginFragment)
                                            .commit();
                                }
                            }
                        }


                    } else {
                        Toast.makeText(getContext(), "로그인 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToPreferences(String userId, String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.putString("email", email);
        editor.apply(); // 변경 사항 저장
    }
}