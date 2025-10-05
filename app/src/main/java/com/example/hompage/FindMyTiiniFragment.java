package com.example.hompage;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FindMyTiiniFragment extends Fragment {

    private TextView animatedText;
    private ImageView mbtiImageView;
    private TextView mbtiNameTextView;
    private String fullText;
    private int index = 0;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_mytini, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // View 초기화
        animatedText = view.findViewById(R.id.animatedText2);
        mbtiImageView = view.findViewById(R.id.mbtiImageView);
        mbtiNameTextView = view.findViewById(R.id.mbtiNameTextView);

        // Bundle에서 MBTI_RESULT 가져오기
        Bundle args = getArguments();
        if (args != null) {
            String mbtiResult = args.getString("MBTI_RESULT", "Unknown");
            startTextAnimation(mbtiResult);
        } else {
            Toast.makeText(getContext(), "MBTI 결과를 받을 수 없습니다.", Toast.LENGTH_SHORT).show();
        }

        mbtiImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InformationTinipingFragment fragment = new InformationTinipingFragment();
                Bundle bundle = new Bundle();

                // Bundle에 데이터 추가
                bundle.putString("clickedItem", mbtiNameTextView.getText().toString());
                fragment.setArguments(bundle);

                // MainActivity의 pushFragment() 메서드 호출
                ((MainActivity) requireActivity()).pushFragment(fragment, "InformationTinipingFragment", 0);
            }
        });

    }

    public void onResume() {
        super.onResume();
        if (fullText != null && !fullText.isEmpty()) {
            index = 0;
            animatedText.setText(""); // 이전 텍스트 초기화
            startTextAnimation(getArguments().getString("MBTI_RESULT", "Unknown"));
        }
    }

    private void startTextAnimation(String mbtiResult) {
        String name = "기본";
        String name2 = "기본2";
        int imageResId = R.drawable.heartsping; // 기본 이미지

        if (mbtiResult != null) {
            name = getNameByMBTI(mbtiResult);
            name2 = getNameByMBTI2(mbtiResult);
            imageResId = getImageByMBTI(mbtiResult);
            fullText = "친구의 티니핑은~~ " + name + "이야!!";
        } else {
            fullText = "알 수 없습니다.";
        }

        // 이미지 설정
        mbtiImageView.setImageResource(imageResId);
        mbtiImageView.setVisibility(View.VISIBLE);

        // 텍스트 애니메이션
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (index < fullText.length()) {
                    animatedText.setText(fullText.substring(0, index + 1));
                    index++;
                    handler.postDelayed(this, 100);
                }
            }
        }, 1000);

        String finalName = name2;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mbtiNameTextView.setText(finalName);
                mbtiNameTextView.setVisibility(View.VISIBLE);
                mbtiNameTextView.setOnClickListener(v -> {
                    //navigateToMBTIActivity(mbtiResult);
                });
            }
        }, 2000);
    }


    private String getNameByMBTI(String mbti) {
        switch (mbti) {
            case "ISTJ":
                return "실용적인 티니핑";
            case "ISTP":
                return "모험적인 티니핑";
            case "ISFJ":
                return "배려 깊은 티니핑";
            case "ISFP":
                return "예술적인 티니핑";
            case "INFJ":
                return "통찰력 있는 티니핑";
            case "INFP":
                return "이상적인 티니핑";
            case "INTJ":
                return "전략적인 티니핑";
            case "INTP":
                return "논리적인 티니핑";
            case "ESTP":
                return "활동적인 티니핑";
            case "ESTJ":
                return "결단력 있는 티니핑";
            case "ESFP":
                return "사교적인 티니핑";
            case "ESFJ":
                return "친절한 티니핑";
            case "ENFP":
                return "열정적인 티니핑";
            case "ENFJ":
                return "영감을 주는 티니핑";
            case "ENTP":
                return "창의적인 티니핑";
            case "ENTJ":
                return "리더십 있는 티니핑";
            default:
                return "우산핑";
        }
    }

    private String getNameByMBTI2(String mbti) {
        switch (mbti) {
            case "ISTJ":
                return "빛나핑";
            case "ISTP":
                return "스타 하츄핑";
            case "ISFJ":
                return "말랑핑";
            case "ISFP":
                return "행운핑";
            case "INFJ":
                return "새콤핑";
            case "INFP":
                return "달콤핑";
            case "INTJ":
                return "아자핑";
            case "INTP":
                return "바로핑";
            case "ESTP":
                return "하츄핑";
            case "ESTJ":
                return "차차핑";
            case "ESFP":
                return "라라핑";
            case "ESFJ":
                return "해핑";
            case "ENFP":
                return "키키핑";
            case "ENFJ":
                return "나나핑";
            case "ENTP":
                return "방글핑";
            case "ENTJ":
                return "믿어핑";
            default:
                return "";
        }
    }

    /**
     * MBTI 결과에 따라 이미지를 반환합니다.
     *
     * @param mbti MBTI 결과 문자열
     * @return MBTI 유형에 따른 이미지 리소스 ID
     */
    private int getImageByMBTI(String mbti) {
        switch (mbti) {
            case "ISTJ":
                return R.drawable.shimmerping;
            case "ISTP":
                return R.drawable.heartsping;
            case "ISFJ":
                return R.drawable.jellyping;
            case "ISFP":
                return R.drawable.luckyping;
            case "INFJ":
                return R.drawable.tangyping;
            case "INFP":
                return R.drawable.sweetping;
            case "INTJ":
                return R.drawable.gogoping;
            case "INTP":
                return R.drawable.dadaping;
            case "ESTP":
                return R.drawable.heartsping;
            case "ESTJ":
                return R.drawable.chachaping;
            case "ESFP":
                return R.drawable.lalaping;
            case "ESFJ":
                return R.drawable.happying;
            case "ENFP":
                return R.drawable.kikiping;
            case "ENFJ":
                return R.drawable.nanaping;
            case "ENTP":
                return R.drawable.teeheeping;
            case "ENTJ":
                return R.drawable.trustping;
            default:
                return R.drawable.heartsping; // 기본 이미지
        }
    }
}

