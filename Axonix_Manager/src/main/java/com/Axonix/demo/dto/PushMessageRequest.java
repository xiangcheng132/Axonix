package com.Axonix.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PushMessageRequest {
    @JsonProperty("validate_only")
    private boolean validateOnly = false;
    private Message message;

    @Data
    public static class Message {
        private List<String> token;
        private Notification notification;
        private AndroidConfig android;

        // ✅ 添加 data 字段支持透传
        private String data;

        @Data
        public static class Notification {
            private String title;
            private String body;
        }

        @Data
        public static class AndroidConfig {
            private AndroidNotification notification;

            @Data
            public static class AndroidNotification {
                @JsonProperty("click_action")
                private ClickAction clickAction;
            }

            @Data
            public static class ClickAction {
                private int type = 1; // 默认打开应用
            }
        }
    }
}
