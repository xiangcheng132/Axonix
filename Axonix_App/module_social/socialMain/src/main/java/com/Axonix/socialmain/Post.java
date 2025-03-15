package com.Axonix.module_social;

public class Post {
    private String username;
    private String content;
    private String time;

    public Post(String username, String content, String time) {
        this.username = username;
        this.content = content;
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
