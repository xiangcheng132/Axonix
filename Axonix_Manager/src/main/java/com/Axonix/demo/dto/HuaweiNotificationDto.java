package com.Axonix.demo.dto;

import com.Axonix.service.HuaweiPushService;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class HuaweiNotificationDto {
    private String token;
    private String title;
    private String content;

    public HuaweiNotificationDto() {
    }

    public HuaweiNotificationDto(String token, String title, String content) {
        this.token = token;
        this.title = title;
        this.content = content;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
