package com.Axonix.index.session;

import android.content.Context;
import android.content.SharedPreferences;
import com.Axonix.index.model.User;
import com.google.gson.Gson;

public class UserSessionManager {
    private static final String PREF_NAME = "user_session";
    private static final String KEY_USER_JSON = "user_json";

    private static UserSessionManager instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private final Gson gson;

    private UserSessionManager(Context context) {
        // 使用ApplicationContext以避免内存泄漏
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public static synchronized UserSessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserSessionManager(context);
        }
        return instance;
    }

    /**
     * 保存整个User对象
     */
    public void saveUser(User user) {
        if (user != null) {
            String userJson = gson.toJson(user);
            editor.putString(KEY_USER_JSON, userJson);
            editor.apply();
        }
    }

    /**
     * 获取保存的User对象，若无则返回null
     */
    public User getUser() {
        String userJson = sharedPreferences.getString(KEY_USER_JSON, null);
        return userJson != null ? gson.fromJson(userJson, User.class) : null;
    }

    /**
     * 清除保存的用户登录信息
     */
    public void clearSession() {
        editor.remove(KEY_USER_JSON).apply();
    }

    /**
     * 判断用户是否已登录
     */
    public boolean isLoggedIn() {
        return sharedPreferences.contains(KEY_USER_JSON);
    }
}
