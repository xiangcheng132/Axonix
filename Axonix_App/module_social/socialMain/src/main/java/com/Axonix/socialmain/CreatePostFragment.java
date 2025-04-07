package com.Axonix.socialmain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.alibaba.android.arouter.facade.annotation.Route;
import jp.wasabeef.richeditor.RichEditor;

@Route(path = "/social/create")
public class CreatePostFragment extends Fragment {
    private RichEditor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        initRichEditor(view);
        setupButtons(view);

//        返回按钮
        view.findViewById(R.id.btn_create_back).setOnClickListener(v -> {
            if (getParentFragment() != null) {
                getParentFragment().getChildFragmentManager().popBackStack();
            }
        });
        return view;
    }

    private void initRichEditor(View view) {
        editor = view.findViewById(R.id.editor);
        editor.setPlaceholder("输入帖子内容...");
    }

    private void setupButtons(View view) {
//        Button btnBold = view.findViewById(R.id.btn_bold);
//        Button btnItalic = view.findViewById(R.id.btn_italic);
//        Button btnImage = view.findViewById(R.id.btn_image);
        Button btnSubmit = view.findViewById(R.id.btn_submit);

//        btnBold.setOnClickListener(v -> editor.setBold());
//        btnItalic.setOnClickListener(v -> editor.setItalic());
//        btnImage.setOnClickListener(v -> showImagePicker());
        btnSubmit.setOnClickListener(v -> handleSubmit());
    }

    private void showImagePicker() {
        Toast.makeText(requireContext(), "图片功能待实现", Toast.LENGTH_SHORT).show();
    }

    private void handleSubmit() {
        String content = editor.getHtml();
        if (content.isEmpty()) {
            Toast.makeText(requireContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
        } else {
            submitPost(content);
        }
    }

    private void submitPost(String htmlContent) {
        // 实际开发中调用API提交数据
        Toast.makeText(requireContext(), "发布成功", Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().popBackStack("social_main", 0);
    }
}