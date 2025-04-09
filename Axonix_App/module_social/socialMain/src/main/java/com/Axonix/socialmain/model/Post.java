package com.Axonix.socialmain.model;

public class Post {
    private String username;
    private String title;
    private String content;
    private String status;
    private String publishTime;
    private int likes;
    private int dislikes;

    public Post(String username,String title,String content,String status,String publishTime,int likes ,int dislikes){
        this.username = username;
        this.title = title;
        this.content = content;
        this.status = status;
        this.publishTime = publishTime;
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

    public String getPublishTime() {
        return this.publishTime;
    }

    public void setPublishTime(final String publishTime) {
        this.publishTime = publishTime;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }


}
