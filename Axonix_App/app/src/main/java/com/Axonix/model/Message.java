package com.Axonix.model;

public class Message {
    public static final int TYPE_SENT = 1;     // 用户发送消息
    public static final int TYPE_RECEIVED = 2; // AI 回复消息

    private int type;
    private String content;

    public Message(int type, String content) {
        this.type = type;
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}