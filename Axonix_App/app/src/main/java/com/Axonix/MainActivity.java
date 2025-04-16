package com.Axonix;

import android.os.Bundle;
import com.Axonix.index.BaseActivity;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocationClient;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.push.HmsMessaging;

import android.util.Log;


public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("MainActivity", "MainActivity 启动");
        HmsMessaging.getInstance(this).setAutoInitEnabled(true);
        Log.d("消息推送MyActivity", HmsMessaging.getInstance(this).isAutoInitEnabled()+"");
        new Thread(() -> {
            try {
                String token = HmsInstanceId.getInstance(this)
                        .getToken("你的appid", "HCM");  // HCM = Huawei Cloud Messaging
                Log.d("HMS Token", "获取token成功：" + token);
            } catch (Exception e) {
                Log.e("HMS Token", "获取token失败：" + e.getMessage());
            }
        }).start();

    }
}
