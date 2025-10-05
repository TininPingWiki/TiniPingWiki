package com.example.hompage;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Arrays;
import java.util.List;

public class ScrollableBottomSheet extends BottomSheetDialogFragment {
    private final List<String> imageUrls = Arrays.asList(
            "https://firebasestorage.googleapis.com/v0/b/homepage-ce93e.firebasestorage.app/o/image%2Fimage%201.png?alt=media&token=31fdbfdc-44da-4c0e-8027-22c11bc93ec8",
            "https://firebasestorage.googleapis.com/v0/b/homepage-ce93e.firebasestorage.app/o/image%2Fimage%2011.png?alt=media&token=b3ff7bf0-2cb0-4e4a-b5ab-298e2f82ef54",
            "https://firebasestorage.googleapis.com/v0/b/homepage-ce93e.firebasestorage.app/o/image%2Fimage%207.png?alt=media&token=8e565446-193c-4ab0-bf6c-4858b86f1030",
            "https://firebasestorage.googleapis.com/v0/b/homepage-ce93e.firebasestorage.app/o/image%2Fimage%2071.png?alt=media&token=98bfa991-da65-4b71-a81c-21cce4895d88",
            "https://firebasestorage.googleapis.com/v0/b/homepage-ce93e.firebasestorage.app/o/image%2Fimagep2.png?alt=media&token=f453e6a5-d50e-4063-9534-7bb7fc78be7b"
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 레이아웃 inflate
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        // 이미지 컨테이너 (LinearLayout)
        LinearLayout imageContainer = view.findViewById(R.id.image_container);

        // 서버 이미지 로드
        for (String url : imageUrls) {
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    300, // 이미지 너비 (픽셀)
                    200 // 이미지 높이 (픽셀)
            );
            params.setMargins(16, 16, 16, 16);
            imageView.setLayoutParams(params);

            // Glide로 이미지 로드
            Glide.with(this)
                    .load(url)
                    .into(imageView);

            // 드래그 가능하도록 설정
            imageView.setOnLongClickListener(v -> {
                ClipData.Item item = new ClipData.Item(url);
                ClipData dragData = new ClipData(
                        "image_url",
                        new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item
                );
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(imageView);

                // 드래그 시작
                v.startDragAndDrop(dragData, shadowBuilder, null, View.DRAG_FLAG_GLOBAL);

                // Bottom Sheet 닫기
                dismiss();

                return true;
            });


            // 이미지 추가
            imageContainer.addView(imageView);
        }

        return view;
    }
}
