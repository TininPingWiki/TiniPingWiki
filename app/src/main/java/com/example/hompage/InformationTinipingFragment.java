package com.example.hompage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hompage.databinding.InformHeartspingBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


public class InformationTinipingFragment extends Fragment {
    private YouTubePlayerView youTubePlayerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        // 전달받은 데이터 확인
        if (getArguments() != null) {
            String clickedItem = getArguments().getString("clickedItem","");
            // 데이터에 따라 레이아웃을 선택적으로 Inflate
            if ("하츄핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_heartsping, container, false);
            } else if ("행운핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_luckyping, container, false);
            }
            else if ("바로핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_dadaping, container, false);
            }
            else if ("키키핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_kikiping, container, false);
            }
            else if ("아자핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_gogoping, container, false);
            }
            else if ("악동핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_giggleping, container, false);
            }
            else if ("차차핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_chachaping, container, false);
            }
            else if ("아잉핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_charmping, container, false);
            }
            else if ("라라핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_lalaping, container, false);
            }
            else if ("앙대핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_egoping, container, false);
            }
            else if ("해핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_happying, container, false);
            }
            else if ("부끄핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_shyping, container, false);
            }
            else if ("새콤핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_tangyping, container, false);
            }
            else if ("방글핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_teeheeping, container, false);
            }
            else if ("믿어핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_trustping, container, false);
            }
            else if ("부투핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_dareping, container, false);
            }
            else if ("가면핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_maskping, container, false);
            }
            else if ("빛나핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_shimmerping, container, false);
            }
            else if ("달콤핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_sweetping, container, false);
            }
            else if ("깜빡핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_blankping, container, false);
            }
            else if ("다해핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_onlyping, container, false);
            }
            else if ("나나핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_nanaping, container, false);
            }
            else if ("오로라핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_auroraping, container, false);
            }
            else if ("띠용핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_dreamping, container, false);
            }
            else if ("말랑핑".equals(clickedItem)) {
                view = inflater.inflate(R.layout.inform_jellyping, container, false);
            }
        }

        // 기본 레이아웃 설정 (예: 데이터가 없거나 조건에 해당하지 않을 경우)
        if (view == null) {
            view = inflater.inflate(R.layout.inform_heartsping, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // YouTubePlayerView 초기화
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        if (youTubePlayerView != null) {
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    String videoId = "JZnlWeDYd8I"; // 재생할 YouTube 비디오 ID
                    youTubePlayer.loadVideo(videoId, 0);
                }
            });
        }
    }
}