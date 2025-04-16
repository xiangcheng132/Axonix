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
        // 构造通知 builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(com.Axonix.usermain.R.drawable.ic_avatar)  // 请将 ic_notification 替换为你项目中的通知图标
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(body);
        builder.setStyle(bigTextStyle);

        // 点击通知时打开 MainActivity（可根据需求修改为其他 Activity）
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );
        builder.setContentIntent(pendingIntent);

        // 获取 NotificationManager 并创建通知通道
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Android 8.0 (API 26) 及以上需要创建通知通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "Default Channel";
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        // 发出通知，id 可根据需求定义
        notificationManager.notify(0, builder.build());
    }
}
