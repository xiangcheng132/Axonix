package com.Axonix.demo.model;

import java.util.Date;

public class SignLanguageTranslationLog {
    private Integer id;

    private Integer userId;

    private String originalVideoUrl;

    private Date createdtime;

    private String translatedText;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOriginalVideoUrl() {
        return originalVideoUrl;
    }

    public void setOriginalVideoUrl(String originalVideoUrl) {
        this.originalVideoUrl = originalVideoUrl == null ? null : originalVideoUrl.trim();
    }

    public Date getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Date createdtime) {
        this.createdtime = createdtime;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText == null ? null : translatedText.trim();
    }
}