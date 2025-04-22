package com.Axonix.index.model;
import java.util.List;

public class BlindPathDetectionResult {

    private Integer code; // 状态码，0 表示成功
    private String message; // 状态描述
    private String status; // 盲道状态：no detection/warning:left!/warning:right!/stop/GO
    private String tts_text; // 语音播报内容
    private Boolean vibrate; // 是否需要震动提示
    private List<BoundingBox> bounding_boxes; // 检测到的盲道边界框信息
    private String image_annotated; // Base64 编码的标注后图像
    private String timestamp; // 时间戳

    // Getter 和 Setter 方法
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTts_text() {
        return tts_text;
    }

    public void setTts_text(String tts_text) {
        this.tts_text = tts_text;
    }

    public Boolean getVibrate() {
        return vibrate;
    }

    public void setVibrate(Boolean vibrate) {
        this.vibrate = vibrate;
    }

    public List<BoundingBox> getBounding_boxes() {
        return bounding_boxes;
    }

    public void setBounding_boxes(List<BoundingBox> bounding_boxes) {
        this.bounding_boxes = bounding_boxes;
    }

    public String getImage_annotated() {
        return image_annotated;
    }

    public void setImage_annotated(String image_annotated) {
        this.image_annotated = image_annotated;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // 内部类：用于表示边界框信息
    public static class BoundingBox {
        private int x;
        private int y;
        private int width;
        private int height;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}

