package com.Axonix;

import android.app.Application;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class MyApplication extends Application {
    @Override
    public void onCreate() {

        super.onCreate();
        // ARouter 初始化
        if (BuildConfig.DEBUG) {
            ARouter.openLog();     // 打开日志
            ARouter.openDebug();   // 调试模式
        }
        ARouter.init(this);
        try (InputStream is = this.getAssets().open("agconnect-services.json")) {
            int size = is.available();
            Log.d("HMS", "agconnect-services.json 存在, 大小 = " + size);
        } catch (IOException e) {
            Log.e("HMS", "agconnect-services.json 不存在或无法读取");
        }
        new Thread(() -> {
            try {
                InputStream is = getAssets().open("agconnect-services.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();

                String json = new String(buffer, "UTF-8");
                JSONObject jsonObject = new JSONObject(json);

                // 从 client 结构中提取 app_id
                JSONObject client = jsonObject.getJSONObject("client");
                String appId = client.getString("app_id");

                Log.d("HMS", "手动读取 appId 成功: " + appId);

                String token = HmsInstanceId.getInstance(this).getToken(appId, "HCM"); // "HCM" 是华为推送服务名
                if (!token.isEmpty()) {
                    Log.d("获取token成功: " , token);
                    // 可以上传到服务器或者本地保存
                }
            } catch (IOException | JSONException e) {
                Log.e("HMS", "读取 appId 失败: " + e.getMessage());
            }catch (ApiException e) {
                Log.e("获取token失败: ", "" + e);
            }

        }).start();
    }
}

