package com.Axonix.socialmain;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Axonix.index.config.NetworkClient;
import com.Axonix.index.config.NetworkTimeClient;
import com.Axonix.index.enumClass.ForumStatus;
import com.Axonix.index.model.ForumComment;
import com.Axonix.index.model.ForumPost;
import com.Axonix.index.model.ForumPostExample;
import com.Axonix.index.model.User;
import com.Axonix.index.session.UserSessionManager;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Route(path = "/social/main")
public class SocialFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<ForumPost> data = new ArrayList<>();
    private SocialEventHandler eventHandler;
    private FloatingActionButton fab;
    private String SOCIAL_URL, DELETE_URL, ADDCOMMENT_URL;
    private OkHttpClient httpClient;
    private MaterialButtonToggleGroup toggleGroup;
    private boolean isMe = false;
    ForumPost item;

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SOCIAL_URL = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/forum-post/select/by-example";
        DELETE_URL = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/forum-post/delete/";
        ADDCOMMENT_URL = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/forum-comment/insert";
        httpClient = NetworkClient.INSTANCE.getClient();
        setRetainInstance(false); // 确保不保留实例

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_social, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view_posts);
        fab = view.findViewById(R.id.fab_create_post);
        eventHandler = new SocialEventHandler(requireContext(), getChildFragmentManager());

        fab.setOnClickListener(v -> {
            if (getChildFragmentManager().getBackStackEntryCount() > 0) {
                popAddComment();
            } else {
                eventHandler.handleCreatePost();
                getChildFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        if (getChildFragmentManager().getBackStackEntryCount() == 0) {
                            initMockData();
                            getChildFragmentManager().removeOnBackStackChangedListener(this);
                        }
                    }
                });
            }
        });
        initMockData();
        toggleGroup = view.findViewById(R.id.toggle_group);
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    if (checkedId == R.id.btn_all_posts) {
                        isMe = false;
                    } else if (checkedId == R.id.btn_my_posts) {
                        isMe = true;
                    }
                    initMockData();
                }
            }
        });

        getParentFragmentManager().setFragmentResultListener("post_publish_result", getViewLifecycleOwner(), (requestKey, result) -> {
            boolean refreshData = result.getBoolean("refresh_data", false);
            if (refreshData) {
                // 刷新数据
                initMockData();
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new PostAdapter(DELETE_URL, httpClient));
    }

    private void popAddComment() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_comment, null);
        MaterialButton btnAdd = dialogView.findViewById(R.id.btn_add_contact);
        EditText etComment = dialogView.findViewById(R.id.et_comment);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setCancelable(true)
                .create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        btnAdd.setOnClickListener(v1 -> {
            ForumComment comment = new ForumComment();
            comment.setContent(etComment.getText().toString());
            comment.setUserId(UserSessionManager.getInstance(requireContext()).getUser().getId());
            comment.setPostId(item.getId());
            comment.setStatus(3);

            Gson gson = NetworkTimeClient.getGson();
            String commentJson = gson.toJson(comment);
            RequestBody body = RequestBody.create(commentJson, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(ADDCOMMENT_URL)  // 你的后端插入接口地址
                    .post(body)
                    .build();

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("COMMENT_ADD", "添加评论失败", e);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e("COMMENT_ADD", "添加评论失败，状态码：" + response.code());
                        return;
                    }
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "添加评论成功", Toast.LENGTH_SHORT).show());
                    getChildFragmentManager().popBackStack();
                    getChildFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                        @Override
                        public void onBackStackChanged() {
                            if (getChildFragmentManager().getBackStackEntryCount() == 0) {
                                eventHandler.handlePostClick(item);
                                getChildFragmentManager().removeOnBackStackChangedListener(this);
                            }
                        }
                    });
                };
            });
            dialog.dismiss();
        });
    }

    private void initMockData() {
        data.clear();
        ForumPostExample forumPostExample = new ForumPostExample();
        ForumPostExample.Criteria criteria = forumPostExample.createCriteria();
        criteria.andStatusEqualTo(3);
        if (isMe)
            criteria.andUserIdEqualTo(UserSessionManager.getInstance(requireContext()).getUser().getId());
        Gson gson = NetworkTimeClient.getGson();
        String json = gson.toJson(forumPostExample);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(SOCIAL_URL)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("SocialFragment", "加载帖子数据失败", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("SocialFragment", "加载帖子数据失败，状态码：" + response.code());
                    return;
                }
                Gson gson = NetworkTimeClient.getGson();
                Type listType = new TypeToken<List<ForumPost>>() {
                }.getType();
                data = gson.fromJson(response.body().string(), listType);
                requireActivity().runOnUiThread(() -> {
                    initRecyclerView();
                });
            }
        });
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
        private String deleteUrl;
        private final OkHttpClient httpClient;

        public PostAdapter(String deleteUrl, OkHttpClient httpClient) {
            this.deleteUrl = deleteUrl;
            this.httpClient = httpClient;
        }

        @NonNull
        @Override
        public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post, parent, false);
            return new PostViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
            item = data.get(position);
            holder.tvUsername.setText(item.getTitle());
            holder.tvContent.setText(item.getContent());
            holder.tvTime.setText(item.getCreatedAt());

            holder.itemView.setOnClickListener(v -> {
                        eventHandler.handlePostClick(item);
                    }
            );
            // 记录触摸位置，用于弹窗定位
            final int[] touchPosition = new int[2];
            holder.itemView.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    item = data.get(position);
                    touchPosition[0] = (int) event.getRawX();
                    touchPosition[1] = (int) event.getRawY();
                }
                return false;
            });

            holder.itemView.setOnLongClickListener(v -> {
                if (isMe) {
                    View popupView = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.popup_delete, null);
                    PopupWindow popupWindow = new PopupWindow(
                            popupView,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            true
                    );
                    TextView tvDelete = popupView.findViewById(R.id.tv_delete);
                    tvDelete.setOnClickListener(delView -> {
                        popupWindow.dismiss();
                        deleteUrl = deleteUrl + item.getId();
                        Request request = new Request.Builder()
                                .url(deleteUrl)  // 你的后端插入接口地址
                                .delete()
                                .build();

                        httpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("删除帖子", "失败", e);
                                requireActivity().runOnUiThread(() ->
                                        Toast.makeText(getContext(), "删除失败，请检查网络", Toast.LENGTH_SHORT).show());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    requireActivity().runOnUiThread(() -> {
                                        Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                        initMockData();
                                    });
                                } else {
                                    requireActivity().runOnUiThread(() ->
                                            Toast.makeText(getContext(), "删除失败，状态码：" + response.code(), Toast.LENGTH_SHORT).show());
                                }
                            }
                        });
                    });
                    popupWindow.setBackgroundDrawable(new ColorDrawable());
                    popupWindow.setOutsideTouchable(true);

                    int offsetY = (int) (holder.itemView.getContext().getResources().getDisplayMetrics().density * 10);
                    popupWindow.showAtLocation(holder.itemView, Gravity.NO_GRAVITY, touchPosition[0], touchPosition[1] + offsetY);

                    return true;
                }
                return false;
            });
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