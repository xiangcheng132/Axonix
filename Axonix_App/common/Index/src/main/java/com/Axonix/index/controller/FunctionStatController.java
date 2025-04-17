package com.Axonix.index.controller;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.Axonix.index.R;

public class FunctionStatController {

    private static final String TAG = "FunctionStatHttpHelper";
    private static final String BASE_URL = R.string.Base_url; // 替换为你的服务器地址
    private static final OkHttpClient client = new OkHttpClient();

    /**
     * 对某字段 +1 请求
     * @param userId 用户 ID
     * @param fieldName 字段名
     */
    public static void incrementField(int userId, String fieldName) {
        String url = BASE_URL + "/api/function-stat/increment/" + userId + "?fieldName=" + fieldName;

        Request request = new Request.Builder()
                .url(url)
                .put(okhttp3.internal.Util.EMPTY_REQUEST) // 空 PUT 请求体
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "请求失败: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "字段 " + fieldName + " +1 成功");
                } else {
                    Log.e(TAG, "字段 +1 失败，状态码: " + response.code());
                }
                response.close();
            }
        });
    }
}
