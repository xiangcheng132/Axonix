package com.Axonix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;

public class AccessibilityCommunicationFragment extends Fragment {

    private EditText etInput;
    private TextView tvOutput;
    private Button btnVoiceToText;
    private Button btnTextToVoice;
    private Button btnTranslate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_communicate, container, false);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        // 加载 fragment 布局
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化控件
        etInput = view.findViewById(R.id.et_input);
        tvOutput = view.findViewById(R.id.tv_output);
        btnVoiceToText = view.findViewById(R.id.btn_voice_to_text);
        btnTextToVoice = view.findViewById(R.id.btn_text_to_voice);
        btnTranslate = view.findViewById(R.id.btn_translate);

        // 模拟语音转文字
        btnVoiceToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvOutput.setText("【语音转文字】模拟结果：这是语音识别的文字。");
            }
        });

        // 模拟文字转语音
        btnTextToVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvOutput.setText("【文字转语音】模拟结果：朗读中……");
            }
        });

        // 模拟翻译功能
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvOutput.setText("【翻译】模拟结果：这是翻译后的文本。");
            }
        });
    }
}
