package com.Axonix.index.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.Axonix.index.config.NetworkClient;
import com.Axonix.index.config.NetworkTimeClient;
import com.Axonix.index.model.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserUpdateManager {

    private static final String UPDATE_URL = "https://192.168.43.87:8080/api/users/update";//ip地址：win+R -> 输入cmd -> ipconfig
    private static UserUpdateManager instance;
    private final OkHttpClient httpClient;
    private final Gson gson;
    private final Handler mainHandler;

    private UserUpdateManager() {
        httpClient = NetworkClient.INSTANCE.getClient();
        gson = NetworkTimeClient.getGson();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static synchronized UserUpdateManager getInstance() {
        if (instance == null) {
            instance = new UserUpdateManager();
        }
        return instance;
    }

    /**
     * 更新用户信息，将 deviceToken 与 lastLoginTime 上传。
     * @param context 用于显示错误提示
     * @param user 完整的User对象，此对象必须已包含有效的 id
     * @param deviceToken 当前设备的DeviceToken
     * @param callback 更新成功后的回调动作
     */
    public void updateUser(final Context context, final User user,  final UpdateCallback callback) {

        String jsonUser = gson.toJson(user);
        RequestBody body = RequestBody.create(jsonUser, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(UPDATE_URL)
                .put(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("UserUpdateManager", "更新用户信息失败", e);
                mainHandler.post(callback::onUpdateFailure);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("UserUpdateManager", "更新用户信息错误，状态码：" + response.code());
                    mainHandler.post(callback::onUpdateFailure);
                    return;
                }

                String responseBody = response.body().string();
                Log.d("UserUpdateManager", "更新用户信息成功：" + responseBody);
                // 成功更新后调用回调，通知调用者进行页面跳转
                mainHandler.post(callback::onUpdateSuccess);
            }
        });
    }
    private Date parseDate(Date date, SimpleDateFormat sdf) {
        try {
            return sdf.parse(sdf.format(date)); // 格式化并解析日期
        } catch (Exception e) {
            // 处理日期解析错误
            return null;
        }
    }
    public interface UpdateCallback {
        void onUpdateSuccess();
        void onUpdateFailure();
    }
}
