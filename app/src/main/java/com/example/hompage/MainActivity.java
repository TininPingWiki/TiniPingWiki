package com.example.hompage;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Map<String, Deque<String>> backStackMap; // 그룹별 양방향 큐 저장
    private String currentGroupTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        backStackMap = new HashMap<>();
        backStackMap.put("group1", new LinkedList<>());
        backStackMap.put("group2", new LinkedList<>());
        backStackMap.put("group3", new LinkedList<>());
        backStackMap.put("group4", new LinkedList<>());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 그룹 1 초기화
        switchToGroup("group1", new DictionaryFragment(), "DictionaryFragment");

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.page_1) {
                switchToGroup("group1", new DictionaryFragment(), "DictionaryFragment");
            } else if (item.getItemId() == R.id.page_2) {
                switchToGroup("group2", new MyTiniFragment(), "MyTiniFragment");
            } else if (item.getItemId() == R.id.page_3) {
                switchToGroup("group3", new FriendBackgroundFragment(), "FriendBackgroundFragment");
            } else if (item.getItemId() == R.id.page_4) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String userId = sharedPreferences.getString("userId", null);
                String email = sharedPreferences.getString("email", null);

                if (userId != null && email != null) {
                    // 사용자 정보가 저장되어 있으면 HomeFragment로 이동
                    switchToGroup("group4", new HomeFragment(), "HomeFragment");
                } else {
                    // 저장된 정보가 없으면 LoginFragment로 이동
                    switchToGroup("group4", new LoginFragment(), "LoginFragment");
                }
            }

            return true;
        });

        // 뒤로 가기 버튼 처리
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Deque<String> groupQueue = backStackMap.get(currentGroupTag);
                if (groupQueue != null && groupQueue.size() > 1) {
                    // 큐에 여러 개의 프래그먼트가 있으면 popFragment() 호출
                    popFragment();
                } else {
                    // 큐에 프래그먼트가 하나만 남아있으면 앱 종료 대화상자 표시
                    showExitDialog();
                }
            }
        });
    }

    public void switchToGroup(String groupTag, Fragment initialFragment, String initialFragmentTag) {
        // 그룹 전환 시 현재 그룹 숨기기
        if (currentGroupTag != null && !currentGroupTag.equals(groupTag)) {
            hideCurrentGroup();
        }

        currentGroupTag = groupTag;
        Deque<String> groupQueue = backStackMap.get(groupTag);

        if (groupQueue.isEmpty()) {
            // 그룹이 처음 선택된 경우, 초기 Fragment 추가
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, initialFragment, initialFragmentTag)
                    .commit();
            groupQueue.offerLast(initialFragmentTag); // 큐에 마지막으로 추가
        } else {
            // 기존 그룹의 최상위 Fragment 표시
            String firstFragmentTag = groupQueue.peekFirst(); // 큐의 첫 번째 원소를 가져옴
            Fragment firstFragment = fragmentManager.findFragmentByTag(firstFragmentTag);
            if (firstFragment != null) {
                fragmentManager.beginTransaction()
                        .show(firstFragment)
                        .commit();
            }
        }
    }

    private void hideCurrentGroup() {
        Deque<String> currentQueue = backStackMap.get(currentGroupTag);
        if (currentQueue != null && !currentQueue.isEmpty()) {
            String firstFragmentTag = currentQueue.peekFirst();
            Fragment firstFragment = fragmentManager.findFragmentByTag(firstFragmentTag);
            if (firstFragment != null) {
                fragmentManager.beginTransaction()
                        .hide(firstFragment)
                        .commitNow();
            }
        }
    }

    public void pushFragment(Fragment fragment, String tag, int position) {
        Deque<String> groupQueue = backStackMap.get(currentGroupTag);
        if (groupQueue != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            String firstFragmentTag = groupQueue.peekFirst();
            Fragment firstFragment = fragmentManager.findFragmentByTag(firstFragmentTag);

            // 현재 Fragment 숨기고 새로운 Fragment 추가
            if (firstFragment != null) {
                transaction.hide(firstFragment);
            }
            transaction.add(R.id.fragment_container, fragment, tag)
                    .commit();

            // position이 0이면 큐의 앞에 추가, 1이면 큐의 뒤에 추가
            if (position == 0) {
                groupQueue.offerFirst(tag); // 큐의 앞에 새 프래그먼트 태그 추가
            } else if (position == 1) {
                groupQueue.offerLast(tag); // 큐의 뒤에 새 프래그먼트 태그 추가
            }
        }
    }

    public Deque<String> getBackStack(String groupTag) {
        return backStackMap.get(groupTag);
    }

    public void popFragment() {
        Deque<String> groupQueue = backStackMap.get(currentGroupTag);
        if (groupQueue != null && groupQueue.size() > 1) {
            String firstFragmentTag = groupQueue.pollFirst();
            Fragment firstFragment = fragmentManager.findFragmentByTag(firstFragmentTag);
            if (firstFragment != null) {
                fragmentManager.beginTransaction()
                        .remove(firstFragment)
                        .commit();
            }

            // 새로운 최상위 Fragment 표시
            String newFirstFragmentTag = groupQueue.peekFirst();
            Fragment newFirstFragment = fragmentManager.findFragmentByTag(newFirstFragmentTag);
            if (newFirstFragment != null) {
                fragmentManager.beginTransaction()
                        .show(newFirstFragment)
                        .commit();
            }
            // MyTiniFragment로 돌아왔을 때 문항을 다시 표시
            if (newFirstFragment instanceof MyTiniFragment) {
                ((MyTiniFragment) newFirstFragment).initializeQuizButtons();
            }
        }
    }

    private void showExitDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("앱 종료")
                .setMessage("앱을 종료하시겠습니까?")
                .setPositiveButton("종료", (dialog, which) -> {
                    // 종료 버튼 클릭 시 앱 종료
                    finish(); // 현재 액티비티 종료
                })
                .setNegativeButton("취소", (dialog, which) -> {
                    // 취소 버튼 클릭 시 대화상자 닫기
                    dialog.dismiss();
                })
                .show();
    }
}