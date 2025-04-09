package com.Axonix.socialmain.model;

public class Comment {

    private String username;
    private String content;
    private String status;
    private String time;
    private int likes;
    private int dislikes;

    public Comment(String username,String content,String status,String time,int likes,int dislikes){
        this.username = username;
        this.content = content;
        this.status =status;
        this.time = time;
        this.likes = likes;
        this.dislikes = dislikes;
    }
    public String getContent() {
        return this.content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public int getDislikes() {
        return this.dislikes;
    }

    public void setDislikes(final int dislikes) {
        this.dislikes = dislikes;
    }

    public int getLikes() {
        return this.likes;
    }

    public void setLikes(final int likes) {
        this.likes = likes;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(final String time) {
        this.time = time;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }


}
