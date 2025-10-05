package com.example.hompage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hompage.databinding.FragmentFriendBackgroundBinding;

public class FriendBackgroundFragment extends Fragment {
    private FragmentFriendBackgroundBinding binding;

    private static final String PREFS_NAME = "FriendBackgroundPrefs";
    private static final String KEY_ANIMATION_SHOWN = "AnimationShown";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFriendBackgroundBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // 초기 UI 설정
        binding.stroytext.setVisibility(View.GONE);
        binding.backgroundtext.setVisibility(View.GONE);
        binding.charactertext.setVisibility(View.GONE);

        // SharedPreferences 초기화
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean animationShown = sharedPreferences.getBoolean(KEY_ANIMATION_SHOWN, false);

        if (!animationShown) {
            // 대사 애니메이션 실행
            animateTextWithUIUpdate(
                    binding.myTextView,
                    "안녕! 티니핑의 모험과 친구들에 대해 알려줄게~",
                    new Runnable() {
                        @Override
                        public void run() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    animateTextWithUIUpdate(
                                            binding.myTextView,
                                            "줄거리, 등장인물, 배경, 회차목록 중에 원하는 걸 눌러봐!",
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    // 두 번째 대사가 끝난 후 버튼 표시
                                                    binding.stroytext.setVisibility(View.VISIBLE);
                                                    binding.charactertext.setVisibility(View.VISIBLE);
                                                    binding.backgroundtext.setVisibility(View.VISIBLE);

                                                    // 애니메이션 실행 기록 저장
                                                    sharedPreferences.edit().putBoolean(KEY_ANIMATION_SHOWN, true).apply();
                                                }
                                            }
                                    );
                                }
                            }, 1000); // 1초 지연
                        }
                    }
            );
        } else {
            // 애니메이션을 이미 본 경우 버튼만 표시
            binding.stroytext.setVisibility(View.VISIBLE);
            binding.charactertext.setVisibility(View.VISIBLE);
            binding.backgroundtext.setVisibility(View.VISIBLE);
        }

        // 버튼 클릭 이벤트 설정
        binding.stroytext.setOnClickListener(v -> {
            // MainActivity의 pushFragment()를 통해 StoryFragment로 교체
            ((MainActivity) requireActivity()).pushFragment(new StoryFragment(), "StoryFragment", 0);
        });

        binding.backgroundtext.setOnClickListener(v -> {
            // MainActivity의 pushFragment()를 통해 BackgroundFragment로 교체
            ((MainActivity) requireActivity()).pushFragment(new BackgroundFragment(), "BackgroundFragment", 0);
        });

        binding.charactertext.setOnClickListener(v -> {
            // MainActivity의 pushFragment()를 통해 CharactersFragment로 교체
            ((MainActivity) requireActivity()).pushFragment(new CharactersFragment(), "CharactersFragment", 0);
        });

        return view;
    }

    private void animateTextWithUIUpdate(TextView textView, String fullText, Runnable onComplete) {
        textView.setText(""); // 텍스트 초기화
        Handler handler = new Handler();
        char[] chars = fullText.toCharArray();
        final int[] currentIndex = {0}; // 현재 인덱스를 배열로 선언 (final 처리)

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (currentIndex[0] < chars.length) {
                    textView.append(String.valueOf(chars[currentIndex[0]]));
                    currentIndex[0]++;
                    handler.postDelayed(this, 200); // 200ms 지연
                } else {
                    onComplete.run(); // 텍스트 출력 완료 시 실행
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

