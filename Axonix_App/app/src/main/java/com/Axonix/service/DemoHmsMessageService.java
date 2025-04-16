package com.Axonix.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.Axonix.MainActivity;
import com.Axonix.R;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class DemoHmsMessageService extends HmsMessageService {

    private static final String CHANNEL_ID = "default_channel_id";

    @Override
    public void onMessageReceived(RemoteMessage message) {
        // 打印日志以检查是否接收到消息
        Log.d("HMS", "收到消息：" + message.getData());

        // 如果消息数据不为空
        if (message.getData() != null && !message.getData().isEmpty()) {
            // 获取消息数据（JSON格式的字符串）
            String dataString = message.getData();

            try {
                // 解析JSON数据
                JSONObject dataJson = new JSONObject(dataString);

                // 提取 title 和 body
                String title = dataJson.optString("title", "默认标题");
                String body = dataJson.optString("body", "默认内容");

                Log.d("HMS", "透传消息 - 标题: " + title + ", 内容: " + body);

                // 处理透传消息（根据需求自定义，例如调用特定的方法或更新界面）
                sendNotification(title, body);
            } catch (JSONException e) {
                Log.e("HMS", "解析透传消息时出错", e);
            }
        } else {
            // 如果是通知消息
            RemoteMessage.Notification notification = message.getNotification();
            if (notification != null) {
                String title = notification.getTitle();
                String body = notification.getBody();
                Log.d("HMS", "通知消息 - 标题: " + title + ", 内容: " + body);
                sendNotification(title, body);
            }
        }
    }

    // 用于展示通知
    private void sendNotification(String title, String body) {
        double latitude = Double.NaN;
        double longitude = Double.NaN;

        // 使用正则提取经纬度
        try {
            // 正则匹配“经度：xxx.xxx” 和 “纬度：xxx.xxx”
            java.util.regex.Pattern latPattern = java.util.regex.Pattern.compile("纬度[:：]\\s*([\\d.]+)");
            java.util.regex.Pattern lonPattern = java.util.regex.Pattern.compile("经度[:：]\\s*([\\d.]+)");

            java.util.regex.Matcher latMatcher = latPattern.matcher(body);
            java.util.regex.Matcher lonMatcher = lonPattern.matcher(body);

            if (latMatcher.find()) {
                latitude = Double.parseDouble(latMatcher.group(1));
            }
            if (lonMatcher.find()) {
                longitude = Double.parseDouble(lonMatcher.group(1));
            }
        } catch (Exception e) {
            Log.w("HMS", "正则解析经纬度失败: " + e.getMessage());
        }

        // 创建 Intent，跳转 NavFragment 所在的 Activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // 如果解析到有效经纬度，添加为 extra 参数
        if (!Double.isNaN(latitude) && !Double.isNaN(longitude)) {
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(com.Axonix.usermain.R.drawable.ic_avatar)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, builder.build());
    }

}
