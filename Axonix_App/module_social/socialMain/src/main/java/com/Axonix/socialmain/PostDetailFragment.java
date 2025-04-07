package com.Axonix.socialmain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/social/detail")
public class PostDetailFragment extends Fragment {
    private static final String ARG_POST_ID = "post_id";
    private String postId;

    // 添加 newInstance 方法
    public static PostDetailFragment newInstance(String postId) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POST_ID, postId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            postId = getArguments().getString(ARG_POST_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        TextView tvContent = view.findViewById(R.id.tv_content);
        TextView tvLikes = view.findViewById(R.id.tv_likes);
        TextView tvDislikes = view.findViewById(R.id.tv_dislikes);

        // 模拟数据加载
        tvContent.setText("帖子ID：" + postId + "\n详细内容...");
        tvLikes.setText("👍 15");
        tvDislikes.setText("👎 2");

        // 返回按钮
        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getParentFragment() != null) {
                getParentFragment().getChildFragmentManager().popBackStack();
            }
        });

        return view;
    }
}
