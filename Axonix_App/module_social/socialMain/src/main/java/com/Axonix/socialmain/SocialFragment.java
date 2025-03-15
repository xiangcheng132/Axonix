package com.Axonix.module_social;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.Axonix.socialmain.R;
import com.alibaba.android.arouter.facade.annotation.Route;

@Route(path = "/social/main")
public class SocialFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social, container, false);

        // 按钮点击事件
        view.findViewById(R.id.btn_chat).setOnClickListener(v -> openChat());
        view.findViewById(R.id.btn_sign_language).setOnClickListener(v -> openSignLanguage());
        view.findViewById(R.id.btn_community).setOnClickListener(v -> openCommunity());

        // RecyclerView 设置
        recyclerView = view.findViewById(R.id.recycler_view_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 初始化数据
        postList = new ArrayList<>();
        postList.add(new Post("Alice", "今天的天气真好！", "2025-02-22 10:30"));
        postList.add(new Post("Bob", "刚学会了一点手语，感觉很有趣！", "2025-02-22 11:00"));

        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);

        return view;
    }

    private void openChat() {
        Toast.makeText(getContext(), "进入即时通讯", Toast.LENGTH_SHORT).show();
    }

    private void openSignLanguage() {
        Toast.makeText(getContext(), "打开手语转换功能", Toast.LENGTH_SHORT).show();
    }

    private void openCommunity() {
        Toast.makeText(getContext(), "进入社区论坛", Toast.LENGTH_SHORT).show();
    }
}
