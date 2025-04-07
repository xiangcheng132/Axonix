package com.Axonix.usermain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Axonix.index.BaseActivity;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;


@Route(path = "/user/login")
public class LoginFragment extends Fragment {

    private EditText etUsername, etPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

// LoginFragment.java
        TextView tvRegister = view.findViewById(R.id.tv_register);
        tvRegister.setOnClickListener(v -> {
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


                // 替换Fragment容器内容
                transaction.replace(com.Axonix.index.R.id.content_frame, registerFragment);

                // 可选：添加到返回栈
                transaction.addToBackStack("register");

                transaction.commit();
            } else {
                Toast.makeText(getContext(), "页面跳转失败", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if ("admin".equals(username) && "123456".equals(password)) {
            // 清除所有Fragment回退栈
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            // 显示导航栏并切换到首页
            if (getActivity() instanceof BaseActivity) {
                BaseActivity activity = (BaseActivity) getActivity();
                activity.setNavigationVisibility(true);
                activity.bottomNavigation.setSelectedItemId(com.Axonix.index.R.id.nav_home);

                activity.switchFragment(com.Axonix.index.R.id.nav_home);
            }
        } else {
            Toast.makeText(getContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }
    }
}