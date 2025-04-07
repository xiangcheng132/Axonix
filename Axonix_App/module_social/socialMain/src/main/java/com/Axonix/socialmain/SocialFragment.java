package com.Axonix.socialmain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Route(path = "/social/main")
public class SocialFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<HashMap<String, Object>> data = new ArrayList<>();
    private SocialEventHandler eventHandler;
    private FloatingActionButton fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(false); // 确保不保留实例
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_social, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_posts);
        fab = view.findViewById(R.id.fab_create_post);
        eventHandler = new SocialEventHandler(requireContext(), getChildFragmentManager());

        initMockData();
        initRecyclerView();

        fab.setOnClickListener(v -> eventHandler.handleCreatePost());
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new PostAdapter());
    }

    private void initMockData() {
        data.clear();
        HashMap<String, Object> item1 = new HashMap<>();
        item1.put("id", "p001");
        item1.put("username", "TechMaster");
        item1.put("content", "多行内容示例\n".repeat(10));
        item1.put("time", "2024-03-21 09:30");
        data.add(item1);

        HashMap<String, Object> item2 = new HashMap<>();
        item2.put("id", "p002");
        item2.put("username", "CodeArtisan");
        item2.put("content", "高效编程技巧分享：\n".repeat(8));
        item2.put("time", "2024-03-21 10:15");
        data.add(item2);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fab != null && !isHidden()) {
            fab.show(); // 恢复显示FAB
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fab != null) {
            fab.hide(); // 离开页面隐藏FAB
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (fab == null) return;
        if (hidden) {
            fab.hide();
        } else {
            fab.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fab = null; // 释放FAB引用
        recyclerView.setAdapter(null); // 释放RecyclerView资源
    }

    // RecyclerView适配器
    class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post, parent, false);
            return new PostViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
            HashMap<String, Object> item = data.get(position);
            holder.tvUsername.setText(item.get("username").toString());
            holder.tvContent.setText(item.get("content").toString());
            holder.tvTime.setText(item.get("time").toString());

            holder.itemView.setOnClickListener(v ->
                    eventHandler.handlePostClick(item.get("id").toString())
            );
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvContent, tvTime;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}