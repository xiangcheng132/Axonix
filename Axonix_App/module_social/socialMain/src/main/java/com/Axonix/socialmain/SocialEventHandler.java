package com.Axonix.socialmain;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Axonix.index.config.NetworkClient;
import com.Axonix.index.config.NetworkTimeClient;
import com.Axonix.index.dto.EmergencyContactDto;
import com.Axonix.index.model.ForumComment;
import com.Axonix.index.model.ForumCommentExample;
import com.Axonix.index.model.ForumPost;
import com.Axonix.index.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SocialEventHandler {
    private Context context;
    private FragmentManager fragmentManager;
    private String COMMENTDETAIL_URL;

    public SocialEventHandler(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        COMMENTDETAIL_URL = context.getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/forum-comment/select/by-example";
    }

    public void handleCreatePost() {
        Fragment current = fragmentManager.findFragmentById(R.id.container);
        if (current != null) {
            fragmentManager.beginTransaction().remove(current).commitNow();
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,   // 进入动画
                R.anim.slide_out_left,   // 退出动画
                R.anim.slide_in_left,    // 返回进入动画
                R.anim.slide_out_right   // 返回退出动画
        );
        transaction.replace(R.id.container, new CreatePostFragment());
        transaction.addToBackStack("create_post");
        transaction.commit();
    }

    public void handlePostClick(ForumPost forumPost) {
        ForumCommentExample forumCommentExample = new ForumCommentExample();
        ForumCommentExample.Criteria criteria = forumCommentExample.createCriteria();
        int forumPostId = forumPost.getId();

        criteria.andPostIdEqualTo(forumPostId);
        criteria.andStatusEqualTo(3);
        Gson gson = NetworkTimeClient.getGson();
        String json = gson.toJson(forumCommentExample);

        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(COMMENTDETAIL_URL)
                .post(body)
                .build();
        OkHttpClient httpClient = NetworkClient.INSTANCE.getClient();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("socialEvent", "评论数据获取失败", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("socialEvent", "评论数据获取失败，状态码：" + response.code());
                    return;
                }
                Type listType = new TypeToken<List<ForumComment>>() {}.getType();
                List<ForumComment> comments = gson.fromJson(response.body().string(), listType);

                Fragment detailFragment = new PostDetailFragment(forumPost, comments);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                );
                transaction.replace(R.id.container, detailFragment);
                transaction.addToBackStack("post_detail");
                transaction.commit();
            }
        });
    }
}
