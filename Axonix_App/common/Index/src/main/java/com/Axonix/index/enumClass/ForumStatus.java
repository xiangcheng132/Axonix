package com.Axonix.index.enumClass;

public enum ForumStatus {
    PENDING_REVIEW(0, "审核中"),
    REJECTED(1, "驳回"),
    ABNORMAL(2, "异常"),
    PUBLISHED(3, "已发布"),
    PRIVATE(4, "私密");

    private final int code;
    private final String description;

    ForumStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ForumStatus fromCode(int code) {
        for (ForumStatus status : ForumStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的状态码: " + code);
    }

}
