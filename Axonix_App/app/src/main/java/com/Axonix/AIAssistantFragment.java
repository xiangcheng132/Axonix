package com.Axonix;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Axonix.model.Message;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/feature/ai")
public class AIAssistantFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> messageList = new ArrayList<>();
    private EditText editTextMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // 加载新的布局文件
        View view = inflater.inflate(R.layout.fragment_ai, container, false);

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

        // 添加用户发送的消息
        Message sentMessage = new Message(Message.TYPE_SENT, text);
        messageList.add(sentMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
        editTextMessage.setText("");

        // 模拟 AI 回复（实际应用中应调用后端服务或 AI SDK）
        String aiReply = "AI 回复：" + text;
        Message replyMessage = new Message(Message.TYPE_RECEIVED, aiReply);
        messageList.add(replyMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }
}
