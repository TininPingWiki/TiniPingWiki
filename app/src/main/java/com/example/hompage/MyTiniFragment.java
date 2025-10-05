package com.example.hompage;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hompage.databinding.FragmentMyTiniBinding;

public class MyTiniFragment extends Fragment {

    private FragmentMyTiniBinding binding;
    private TextView animatedText;
    private String[] texts;
    private int textIndex = 0;   // 현재 텍스트 인덱스
    private int charIndex = 0;   // 현재 글자 인덱스
    private Handler handler = new Handler();

    // MBTI 점수 변수
    private int extraversionPoints = 0;
    private int introversionPoints = 0;
    private int sensingPoints = 0;
    private int intuitionPoints = 0;
    private int thinkingPoints = 0;
    private int feelingPoints = 0;
    private int judgingPoints = 0;
    private int perceivingPoints = 0;

    // 버튼 배열
    private Button[] buttons;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyTiniBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 애니메이션 텍스트 초기화
        animatedText = binding.animatedText;
        texts = getResources().getStringArray(R.array.animated_texts);
        startTextAnimation();

    }

    private void startTextAnimation() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (charIndex < texts[textIndex].length()) {
                    // 현재 인덱스까지의 텍스트를 설정
                    animatedText.setText(texts[textIndex].substring(0, charIndex + 1));
                    charIndex++;
                    handler.postDelayed(this, 100);  // 다시 실행하여 다음 글자를 추가
                } else {
                    // 모든 글자가 표시되었을 때 다음 텍스트로 이동
                    charIndex = 0;
                    if (textIndex < texts.length - 1) { // 마지막 텍스트가 아닐 경우에만 인덱스 증가
                        textIndex++;
                        handler.postDelayed(this, 1000); // 1초 후에 다음 텍스트로 전환
                    } else {
                        // 모든 텍스트가 표시된 후 버튼들을 초기화하고 표시
                        initializeQuizButtons();
                    }
                }
            }
        }, 100);
    }

    void initializeQuizButtons() {
        // 버튼 배열 초기화
        buttons = new Button[20];
        buttons[0] = binding.button1;
        buttons[1] = binding.button2;
        buttons[2] = binding.button3;
        buttons[3] = binding.button4;
        buttons[4] = binding.button5;
        buttons[5] = binding.button6;
        buttons[6] = binding.button7;
        buttons[7] = binding.button8;
        buttons[8] = binding.button9;
        buttons[9] = binding.button10;
        buttons[10] = binding.button11;
        buttons[11] = binding.button12;
        buttons[12] = binding.button13;
        buttons[13] = binding.button14;
        buttons[14] = binding.button15;
        buttons[15] = binding.button16;
        buttons[16] = binding.button17;
        buttons[17] = binding.button18;
        buttons[18] = binding.button19;
        buttons[19] = binding.button20;

        // 초기에는 모든 버튼을 숨김
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] != null) {
                buttons[i].setVisibility(View.GONE);
            } else {
                Log.e("MyTiniFragment", "Button with index " + i + " is null. Check the layout file.");
            }
        }

        // 첫 번째 질문 버튼들만 표시
        if (buttons[0] != null) buttons[0].setVisibility(View.VISIBLE);
        if (buttons[1] != null) buttons[1].setVisibility(View.VISIBLE);

        // 버튼 클릭 리스너 설정
        setupButtonListeners();
    }

    /**
     * 모든 버튼의 클릭 리스너를 설정합니다.
     */
    private void setupButtonListeners() {
        String[] buttonTypes = {
                "P", "J",  // button1, button2
                "E", "I",  // button3, button4
                "T", "F",  // button5, button6
                "E", "I",  // button7, button8
                "T", "F",  // button9, button10
                "I", "E",  // button11, button12
                "S", "N",  // button13, button14
                "F", "T",  // button15, button16
                "N", "S",  // button17, button18
                "S", "N"   // button19, button20
        };

        for (int i = 0; i < buttons.length; i += 2) { // i는 짝수 인덱스로만 순회
            final int index = i;

            // 현재 쌍의 버튼 가져오기
            final Button currentButton1 = buttons[index];
            final Button currentButton2 = buttons[index + 1];
            final String type1 = buttonTypes[index];
            final String type2 = buttonTypes[index + 1];

            if (currentButton1 != null && currentButton2 != null) {
                // 첫 번째 버튼 클릭 리스너
                currentButton1.setOnClickListener(v -> {
                    updateScore(type1); // 점수 갱신
                    hideCurrentPairAndShowNext(index); // 현재 쌍 숨기고 다음 쌍 표시
                });

                // 두 번째 버튼 클릭 리스너
                currentButton2.setOnClickListener(v -> {
                    updateScore(type2); // 점수 갱신
                    hideCurrentPairAndShowNext(index); // 현재 쌍 숨기고 다음 쌍 표시
                });
            } else {
                Log.e("MyTiniFragment", "Buttons with indices " + index + " and " + (index + 1) + " are null. Check the layout file.");
            }
        }
    }

    private void hideCurrentPairAndShowNext(int index) {
        // 현재 쌍의 버튼 숨기기
        if (buttons[index] != null) buttons[index].setVisibility(View.GONE);
        if (buttons[index + 1] != null) buttons[index + 1].setVisibility(View.GONE);

        // 다음 쌍의 버튼 표시
        if (index + 2 < buttons.length && buttons[index + 2] != null) {
            buttons[index + 2].setVisibility(View.VISIBLE);
        }
        if (index + 3 < buttons.length && buttons[index + 3] != null) {
            buttons[index + 3].setVisibility(View.VISIBLE);
        }

        // 마지막 버튼 클릭 후 결과 계산
        if (index + 2 >= buttons.length) {
            calculateMBTIResult();
        }
    }


    private void updateScore(String type) {
        switch (type) {
            case "E": extraversionPoints++; break;
            case "I": introversionPoints++; break;
            case "S": sensingPoints++; break;
            case "N": intuitionPoints++; break;
            case "T": thinkingPoints++; break;
            case "F": feelingPoints++; break;
            case "J": judgingPoints++; break;
            case "P": perceivingPoints++; break;
        }
    }

    private void calculateMBTIResult() {

        StringBuilder mbti = new StringBuilder();

        mbti.append(extraversionPoints >= introversionPoints ? "E" : "I");
        mbti.append(sensingPoints >= intuitionPoints ? "S" : "N");
        mbti.append(thinkingPoints >= feelingPoints ? "T" : "F");
        mbti.append(judgingPoints >= perceivingPoints ? "J" : "P");

        // MBTI 결과를 번들로 전달
        Bundle bundle = new Bundle();
        bundle.putString("MBTI_RESULT", mbti.toString());

        // FindMytiniFragment로 전환
        FindMyTiiniFragment findMytiniFragment = new FindMyTiiniFragment();
        findMytiniFragment.setArguments(bundle);

        // 그룹 2 백스택에 추가하여 그룹 2에 대한 프래그먼트 처리
        ((MainActivity) requireActivity()).pushFragment(findMytiniFragment, "FindMyTiiniFragment",0);

        // 추가된 프래그먼트가 표시될 때 자동으로 그룹 2의 백스택을 갱신
    }



    @Override
    public void onResume() {
        super.onResume();
        // 문항 버튼들이 보이도록 상태 초기화
        initializeQuizButtons();
    }

}
