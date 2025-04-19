package com.Axonix.emergencymain;

import android.os.Bundle;
import android.util.Log;
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

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import android.view.Gravity;
import android.widget.Toast;

import com.Axonix.index.config.NetworkClient;
import com.Axonix.index.config.NetworkTimeClient;
import com.Axonix.index.controller.FunctionStatController;
import com.Axonix.index.model.ForumPost;
import com.Axonix.index.model.HelpRequest;
import com.Axonix.index.model.HelpRequestExample;
import com.Axonix.index.session.UserSessionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HistoryListFragment extends Fragment {
    private static final String ARG_TYPE = "type";
    private String HELP_LOG_URL; // 接口占位符
    private OkHttpClient httpClient;
    private String helpname = "";
    private List<String> mockData = new ArrayList<>();
    private HistoryAdapter adapter;


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
        httpClient = NetworkClient.INSTANCE.getClient();
        HELP_LOG_URL = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/help-request/select/by-example";

        adapter = new HistoryAdapter(mockData);
        recyclerView.setAdapter(adapter);

        generateMockData();
        return recyclerView;
    }

    /**
     * 生成模拟数据（后续替换为网络请求）
     */
    private void generateMockData() {
        String type = getArguments() != null ? getArguments().getString(ARG_TYPE) : "";

        HelpRequestExample helpRequestExample = new HelpRequestExample();
        HelpRequestExample.Criteria criteria = helpRequestExample.createCriteria();

        if ("seek".equals(type)) {
            criteria.andRequesterIdEqualTo(UserSessionManager.getInstance(requireContext()).getUser().getId());
            helpname = "[求助]";
        } else if ("provide".equals(type)) {
            criteria.andHelperIdEqualTo(UserSessionManager.getInstance(requireContext()).getUser().getId());
            helpname = "[帮助]";
        }

        Gson gson = NetworkTimeClient.getGson();
        String json = gson.toJson(helpRequestExample);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(HELP_LOG_URL)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HELP_LOG", "获取记录失败", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("HELP_LOG", "获取记录失败，状态码：" + response.code());
                    return;
                }
                Type listType = new TypeToken<List<HelpRequest>>() {}.getType();
                List<HelpRequest> helpRequestList = gson.fromJson(response.body().string(), listType);
                requireActivity().runOnUiThread(() -> {
                    mockData.clear(); // 清空旧数据
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                    for (HelpRequest item : helpRequestList) {
                        Log.d("helper",item.getHelperLat() + "  " + item.getHelperLng());
                        mockData.add(helpname + " 经度：" + item.getRequesterLng() + " 纬度：" + item.getRequesterLat() + "\n时间：" + sdf.format(item.getCreatedAt()));
                    }

                    if (mockData.isEmpty()) {
                        Toast.makeText(requireContext(), "暂无记录", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged(); // 刷新 RecyclerView
                });
            }
        });
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