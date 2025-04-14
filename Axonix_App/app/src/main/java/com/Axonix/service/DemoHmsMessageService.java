package com.Axonix.service;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.huawei.hms.push.HmsMessageService;

public class DemoHmsMessageService extends HmsMessageService {

    @Override
    public void onNewToken(String token, Bundle bundle) {
        // 获取token
        Log.d("获取Token", "have received refresh token.");

        // 判断token是否为空
        if (!TextUtils.isEmpty(token)) {
            refreshedTokenToServer(token);
        }
    }

    private void refreshedTokenToServer(String token) {
        Log.d("Token", "sending token to server.");
    }
}
