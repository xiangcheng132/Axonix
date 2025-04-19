package com.Axonix;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Axonix.index.config.NetworkClient;
import com.Axonix.index.model.Message;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Route(path = "/feature/ai")
public class AIAssistantFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messageList = new ArrayList<>();
    private EditText editTextMessage;
    private String AI_URL;
    private OkHttpClient httpClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 加载新的布局文件
        View view = inflater.inflate(R.layout.fragment_ai, container, false);
        httpClient = NetworkClient.INSTANCE.getClient();
        AI_URL = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/ai-log/ai_assistant";
        // 设置 Toolbar
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // 设置 RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new ChatAdapter(messageList);
        recyclerView.setAdapter(chatAdapter);

        // 底部输入框与按钮
        editTextMessage = view.findViewById(R.id.edit_text_message);
        Button buttonSend = view.findViewById(R.id.button_send);
        buttonSend.setOnClickListener(v -> sendMessage());

        return view;
    }

    // 模拟发送消息
    private void sendMessage() {
        String text = editTextMessage.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;

        Message sentMessage = new Message(Message.TYPE_SENT, text);
        messageList.add(sentMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
        editTextMessage.setText("");

        RequestBody body = RequestBody.create(text, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(AI_URL)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("AIAssistant", "获取失败", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("PostDetail", "修改不喜欢数失败，状态码：" + response.code());
                    return;
                }
                String aiReply = "AI 回复：" + response.body().string();
                requireActivity().runOnUiThread(()->{
                    Message replyMessage = new Message(Message.TYPE_RECEIVED, aiReply);
                    messageList.add(replyMessage);
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                });
            }
        });

    }
}
