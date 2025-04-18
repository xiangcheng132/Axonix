package com.Axonix.socialmain;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.Axonix.index.config.NetworkClient;
import com.Axonix.index.config.NetworkTimeClient;
import com.Axonix.index.dto.EmergencyContactDto;
import com.Axonix.index.enumClass.ForumStatus;
import com.Axonix.index.model.ForumComment;
import com.Axonix.index.model.ForumPost;
import com.Axonix.index.model.ForumPostExample;
import com.Axonix.index.model.User;
import com.Axonix.index.model.UserExample;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostDetailFragment extends Fragment {

    private ForumPost post;
    private List<ForumComment> comments;
    private MaterialToolbar btnBack;

    private String USER_URL, UPDATAF_URL, COMMENT_UPDATA_URL;

    private OkHttpClient httpClient;

    public PostDetailFragment(ForumPost post, List<ForumComment> comments) {
        this.post = post;
        this.comments = comments;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        USER_URL = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/users/list";
        UPDATAF_URL = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/forum-post/update";
        COMMENT_UPDATA_URL = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/forum-comment/update";

        httpClient = NetworkClient.INSTANCE.getClient();
        bindPostData(view);
        addComments(view, comments);
        FragmentManager fragmentManager = getParentFragmentManager();
        btnBack = view.findViewById(R.id.btn_back);
        btnBack.setNavigationOnClickListener(v -> {
            fragmentManager.popBackStack();
        });
        return view;
    }

    private void bindPostData(View view) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        int id = post.getUserId();
        criteria.andIdEqualTo(id);

        Gson gson = NetworkTimeClient.getGson();
        String json = gson.toJson(userExample);

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(USER_URL)  // 你的后端插入接口地址
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("PostDetail", "加载用户数据失败", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("PostDetail", "加载用户数据失败，状态码：" + response.code());
                    return;
                }
                Type listType = new TypeToken<List<User>>() {
                }.getType();
                List<User> user = gson.fromJson(response.body().string(), listType);
                requireActivity().runOnUiThread(() -> {
                    ((TextView) view.findViewById(R.id.tv_username)).setText(user.get(0).getUsername());
                    ((TextView) view.findViewById(R.id.tv_title)).setText(post.getTitle());
                    ((TextView) view.findViewById(R.id.tv_content)).setText(post.getContent());
                    ((TextView) view.findViewById(R.id.tv_status)).setText("状态：" + ForumStatus.fromCode(post.getStatus()).getDescription());
                    ((TextView) view.findViewById(R.id.tv_publish_time)).setText("发布时间：" + post.getCreatedAt());

                    TextView like = (TextView) view.findViewById(R.id.tv_likes);
                    like.setText("👍 " + post.getLikes());
                    like.setOnClickListener(v -> {
                        post.setLikes(post.getLikes() + 1);
                        like.setText("👍 " + post.getLikes());
                        post.setUpdatedAt(new Date());
                        String jsonPost = gson.toJson(post);
                        RequestBody body2 = RequestBody.create(jsonPost, MediaType.get("application/json; charset=utf-8"));
                        Request request2 = new Request.Builder()
                                .url(UPDATAF_URL)  // 你的后端插入接口地址
                                .put(body2)
                                .build();

                        httpClient.newCall(request2).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("PostDetail", "修改喜欢数失败", e);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (!response.isSuccessful()) {
                                    Log.e("PostDetail", "修改喜欢数失败，状态码：" + response.code());
                                    return;
                                }
                                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "点赞成功", Toast.LENGTH_SHORT).show());
                            }
                        });
                        like.setOnClickListener(null);
                    });

                    TextView dislike = (TextView) view.findViewById(R.id.tv_dislikes);
                    dislike.setText("👎 " + post.getDislikes());
                    dislike.setOnClickListener(v -> {
                        post.setDislikes(post.getDislikes() + 1);
                        dislike.setText("👎 " + post.getDislikes());
                        post.setUpdatedAt(new Date());
                        String jsonPost = gson.toJson(post);
                        RequestBody body2 = RequestBody.create(jsonPost, MediaType.get("application/json; charset=utf-8"));
                        Request request2 = new Request.Builder()
                                .url(UPDATAF_URL)  // 你的后端插入接口地址
                                .put(body2)
                                .build();

                        httpClient.newCall(request2).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("PostDetail", "修改不喜欢数失败", e);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (!response.isSuccessful()) {
                                    Log.e("PostDetail", "修改不喜欢数失败，状态码：" + response.code());
                                    return;
                                }
                                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "踩成功", Toast.LENGTH_SHORT).show());
                            }
                        });
                        dislike.setOnClickListener(null);
                    });
                });
            }
        });


    }

    private void addComments(View view, List<ForumComment> commentList) {
        LinearLayout commentContainer = view.findViewById(R.id.comment_container);
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        commentContainer.removeAllViews();

        for (ForumComment comment : commentList) {
            View itemView = inflater.inflate(R.layout.item_comment, commentContainer, false);
            UserExample userExample = new UserExample();
            UserExample.Criteria criteria = userExample.createCriteria();
            int id = comment.getUserId();
            criteria.andIdEqualTo(id);

            Gson gson = NetworkTimeClient.getGson();
            String json = gson.toJson(userExample);

            RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

            Request request = new Request.Builder()
                    .url(USER_URL)  // 你的后端插入接口地址
                    .post(body)
                    .build();

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("PostDetail", "加载用户数据失败", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e("PostDetail", "加载用户数据失败，状态码：" + response.code());
                        return;
                    }
                    Type listType = new TypeToken<List<User>>() {
                    }.getType();
                    List<User> user = gson.fromJson(response.body().string(), listType);
                    requireActivity().runOnUiThread(() -> {
                        ((TextView) itemView.findViewById(R.id.tv_comment_username)).setText(user.get(0).getUsername());
                        ((TextView) itemView.findViewById(R.id.tv_comment_time)).setText(comment.getCreatedAt());
                        ((TextView) itemView.findViewById(R.id.tv_comment_status)).setText("状态：" + ForumStatus.fromCode(comment.getStatus()).getDescription());
                        ((TextView) itemView.findViewById(R.id.tv_comment_content)).setText(comment.getContent());

                        TextView like = (TextView) itemView.findViewById(R.id.tv_comment_likes);
                        like.setText("👍 " + comment.getLikes());
                        like.setOnClickListener(v -> {
                            comment.setLikes(comment.getLikes() + 1);
                            like.setText("👍 " + comment.getLikes());
                            String jsonPost = gson.toJson(comment);
                            RequestBody body2 = RequestBody.create(jsonPost, MediaType.get("application/json; charset=utf-8"));
                            Request request2 = new Request.Builder()
                                    .url(COMMENT_UPDATA_URL)  // 你的后端插入接口地址
                                    .put(body2)
                                    .build();

                            httpClient.newCall(request2).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.e("PostDetail", "修改喜欢数失败", e);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (!response.isSuccessful()) {
                                        Log.e("PostDetail", "修改喜欢数失败，状态码：" + response.code());
                                        return;
                                    }
                                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "点赞成功", Toast.LENGTH_SHORT).show());
                                }
                            });
                            like.setOnClickListener(null);
                        });

                        TextView dislike = (TextView) itemView.findViewById(R.id.tv_comment_dislikes);
                        dislike.setText("👎 " + comment.getDislikes());
                        dislike.setOnClickListener(v -> {
                            comment.setDislikes(comment.getDislikes() + 1);
                            dislike.setText("👎 " + comment.getDislikes());
                            String jsonPost = gson.toJson(comment);
                            RequestBody body2 = RequestBody.create(jsonPost, MediaType.get("application/json; charset=utf-8"));
                            Request request2 = new Request.Builder()
                                    .url(COMMENT_UPDATA_URL)  // 你的后端插入接口地址
                                    .put(body2)
                                    .build();

                            httpClient.newCall(request2).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.e("PostDetail", "修改不喜欢数失败", e);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (!response.isSuccessful()) {
                                        Log.e("PostDetail", "修改不喜欢数失败，状态码：" + response.code());
                                        return;
                                    }
                                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "踩成功", Toast.LENGTH_SHORT).show());
                                }
                            });
                            dislike.setOnClickListener(null);
                        });
                        commentContainer.addView(itemView);
                    });
                }
            });

        }
    }
}
