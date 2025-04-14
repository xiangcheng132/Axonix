package com.Axonix.usermain;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Axonix.index.config.NetworkClient;
import com.Axonix.index.config.NetworkTimeClient;
import com.Axonix.index.session.DeviceTokenListener;
import com.Axonix.index.session.UserSessionManager;
import com.Axonix.index.controller.UserUpdateManager;
import com.Axonix.index.model.User;
import com.Axonix.index.BaseActivity;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.huawei.hms.aaid.HmsInstanceId;

@Route(path = "/user/login")
public class LoginFragment extends Fragment {

    private TextInputEditText etUsername, etPassword;
    private OkHttpClient httpClient;
    private Gson gson;
    private static final String LOGIN_URL = "https://192.168.43.87:8080/api/users/login";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);

        //安全证书
        httpClient = NetworkClient.INSTANCE.getClient();
        gson = NetworkTimeClient.getGson();

        Button buLogin = view.findViewById(R.id.btn_login);
        buLogin.setOnClickListener(v -> attemptLogin());

        TextView tvRegister = view.findViewById(R.id.tv_register);
        tvRegister.setOnClickListener(v -> navigateToRegister());

        return view;
    }

    private void navigateToRegister() {
        Fragment registerFragment = (Fragment) ARouter.getInstance()
                .build("/user/register")
                .navigation();
        if (registerFragment != null && getActivity() != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            );
            transaction.replace(com.Axonix.index.R.id.content_frame, registerFragment);
            transaction.addToBackStack("register");
            transaction.commit();
        } else {
            Toast.makeText(getContext(), "页面跳转失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Log.d("LoginFragment", "用户名：" + username);
        Log.d("LoginFragment", "密码：" + password);

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 构造登录请求 JSON 数据
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);
        String jsonBody = jsonObject.toString();

        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            final Handler mainHandler = new Handler(Looper.getMainLooper());

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("LoginFragment", "登录请求失败", e);
                mainHandler.post(() ->
                        Toast.makeText(getContext(), "登录失败，请检查网络连接", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("LoginFragment", "登录错误，状态码：" + response.code());
                    mainHandler.post(() ->
                            Toast.makeText(getContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                String responseBody = response.body().string();
                Log.d("LoginFragment", "登录接口响应：" + responseBody);

                try {
                    // 解析返回的 User 对象
                    User user = gson.fromJson(responseBody, User.class);

                    // 在主线程中保存初步的用户信息
                    mainHandler.post(() -> {
                        UserSessionManager.getInstance(getContext()).saveUser(user);

                        // 异步获取设备 token
                        getDeviceToken(token -> {
                            if (token == null) {
                                Toast.makeText(getContext(),
                                        "获取设备 Token 失败，部分功能可能受限",
                                        Toast.LENGTH_SHORT
                                ).show();
                                // 即使 Token 失败也继续登录流程
                                handleLoginSuccess(user);
                                return;
                            }

                            Log.d("LoginFragment", "获取到 HMS Token：" + token);

                            // 更新 user 对象并保存 token 到 session
                            user.setDeviceToken(token); // 假设 User 类中有 setDeviceToken 方法
                            UserSessionManager.getInstance(getContext()).saveUser(user); // 覆盖保存


                            // 使用获取到的 Token 更新服务器上的用户信息
                            UserUpdateManager.getInstance().updateUser(
                                    getContext(),
                                    user,
                                    new UserUpdateManager.UpdateCallback() {
                                        @Override
                                        public void onUpdateSuccess() {
                                            // 更新成功的回调
                                            Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                                            handleLoginSuccess(user);
                                        }

                                        @Override
                                        public void onUpdateFailure() {
                                            // 更新失败的回调
                                            Toast.makeText(getContext(), "保存失败，请重试", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );
                        });
                    });

                } catch (Exception e) {
                    Log.e("LoginFragment", "解析登录响应错误", e);
                    mainHandler.post(() ->
                            Toast.makeText(getContext(), "登录失败，请稍后重试", Toast.LENGTH_SHORT).show()
                    );
                }
            }

        });
    }

    private void getDeviceToken(DeviceTokenListener listener) {
        Executors.newSingleThreadExecutor().execute(() -> {
            String token = HmsInstanceId.getInstance(requireContext()).getToken();
            new Handler(Looper.getMainLooper()).post(() -> listener.onTokenReceived(token));
        });
    }

    private void handleLoginSuccess(User user) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (getActivity() instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) getActivity();
            activity.setNavigationVisibility(true);
            activity.bottomNavigation.setSelectedItemId(com.Axonix.index.R.id.nav_home);
            activity.switchFragment(com.Axonix.index.R.id.nav_home);
        }
        Toast.makeText(getContext(), "登录成功，欢迎 " + user.getUsername(), Toast.LENGTH_SHORT).show();
    }
}
