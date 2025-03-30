package com.Axonix.emergencymain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.view.Gravity;

public class HistoryListFragment extends Fragment {
    private static final String ARG_TYPE = "type";
    private static final String EXAMPLE_API = "https://exampleAPI/records"; // 接口占位符

    public static HistoryListFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        HistoryListFragment fragment = new HistoryListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // 初始化RecyclerView
        RecyclerView recyclerView = new RecyclerView(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        );

        // 模拟数据（后续替换为接口调用）
        List<String> mockData = generateMockData();
        HistoryAdapter adapter = new HistoryAdapter(mockData);
        recyclerView.setAdapter(adapter);

        // 处理空数据
        if (mockData.isEmpty()) {
            TextView emptyView = new TextView(requireContext());
            emptyView.setText("暂无记录");
            emptyView.setTextSize(16);
            emptyView.setGravity(Gravity.CENTER);
            return emptyView;
        }

        return recyclerView;
    }

    /**
     * 生成模拟数据（后续替换为网络请求）
     */
    private List<String> generateMockData() {
        List<String> data = new ArrayList<>();
        // 示例数据
        data.add("[求助] 2023-10-01 14:30");
        data.add("[帮助] 2023-10-02 09:15");
        data.add("[求助] 2023-10-03 16:45");
        return data;
    }

    // ====================== RecyclerView Adapter ======================
    private static class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {
        private final List<String> items;

        public HistoryAdapter(List<String> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_history, parent, false);
            return new HistoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
            holder.tvRecord.setText(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    // ====================== ViewHolder ======================
    private static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecord;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecord = itemView.findViewById(R.id.tvRecord);
        }
    }
}