package com.Axonix.socialmain;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Axonix.index.config.NetworkClient;
import com.Axonix.index.config.NetworkTimeClient;
import com.Axonix.index.controller.FunctionStatController;
import com.Axonix.index.enumClass.ForumStatus;
import com.Axonix.index.model.ForumPost;
import com.Axonix.index.model.User;
import com.Axonix.index.session.UserSessionManager;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Route(path = "/social/create")
public class CreatePostFragment extends Fragment {
    private TextInputEditText editor;
    private TextInputEditText title;
    private Button btnSubmit;
    private OkHttpClient httpClient;
    private String ADDFORUM_URL;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);
        btnSubmit = view.findViewById(R.id.btn_submit);
        initRichEditor(view);
        httpClient = NetworkClient.INSTANCE.getClient();
        ADDFORUM_URL = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/forum-post/insert";
//        返回按钮
        view.findViewById(R.id.btn_create_back).setOnClickListener(v -> {
            if (getParentFragment() != null) {
                getParentFragment().getChildFragmentManager().popBackStack();
            }
        });
        btnSubmit.setOnClickListener(v->handleSubmit());
        return view;
    }

    private void initRichEditor(View view) {
        title = view.findViewById(R.id.et_title);
        editor = view.findViewById(R.id.et_content);
    }

    private void handleSubmit() {
        String content = editor.getText().toString().trim();
        String titlecontent = title.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(requireContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
        } else if (titlecontent.isEmpty()) {
            Toast.makeText(requireContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
        } else {
            ForumPost forumPost = new ForumPost();
            forumPost.setUserId(UserSessionManager.getInstance(requireContext()).getUser().getId());
            forumPost.setTitle(titlecontent);
            forumPost.setContent(content);
            forumPost.setStatus(3);
            forumPost.setLikes(0);
            forumPost.setDislikes(0);

            Gson gson = NetworkTimeClient.getGson();
            String json = gson.toJson(forumPost);
            RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(ADDFORUM_URL)  // 你的后端插入接口地址
                    .post(body)
                    .build();

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("CreatePost", "添加帖子失败", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e("CreatePost", "添加帖子失败，状态码：" + response.code());
                        return;
                    }
                    FunctionStatController.incrementField(requireContext(), UserSessionManager.getInstance(requireContext()).getUser().getId(),"post_publish");
                    requireActivity().runOnUiThread(()->Toast.makeText(requireContext(), "发布成功", Toast.LENGTH_SHORT).show());
                    if (getParentFragment() != null) {
                        Bundle result = new Bundle();
                        result.putBoolean("refresh_data", true); // 发送刷新信号
                        getParentFragmentManager().setFragmentResult("post_publish_result", result);
                        getParentFragment().getChildFragmentManager().popBackStack();
                    }
                }
            });
        }
    }
}