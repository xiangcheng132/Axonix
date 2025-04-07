package com.Axonix.index;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.Axonix.index.R;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {
    public BottomNavigationView bottomNavigation;
    public FloatingActionButton fabSos;
    private final Map<Integer, String> routeMap = new HashMap<>();
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initRoutes();
        setupNavigation();
        setupSOSButton(); // 初始化 SOS 按钮

        // 默认加载首页
        if (savedInstanceState == null) {
            switchFragment(R.id.nav_home);
        }
    }



    private void initRoutes() {
        routeMap.put(R.id.nav_home, "/index/main");
        routeMap.put(R.id.nav_nav, "/nav/main");
        routeMap.put(R.id.nav_emergency, "/emergency/main");
        routeMap.put(R.id.nav_social, "/social/main");
        routeMap.put(R.id.nav_user, "/user/main");
    }

    private void setupNavigation() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this::onNavItemSelected);

        // 设置默认选中首页
        bottomNavigation.setSelectedItemId(R.id.nav_home);
    }

    private void setupSOSButton() {
        fabSos = findViewById(R.id.fab_sos);
        fabSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到拨号界面并输入 110
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:110"));
                startActivity(intent);
            }
        });
    }

    private boolean onNavItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return switchFragment(id);
    }

    public boolean switchFragment(int itemId) {
        String routePath = routeMap.get(itemId);
        if (routePath != null) {
            Fragment fragment = (Fragment) ARouter.getInstance().build(routePath).navigation();
            if (fragment != null) {
                Log.d("BaseActivity", "成功获取 Fragment: " + routePath);
                replaceFragment(fragment);
                return true;
            } else {
                Log.e("BaseActivity", "获取 Fragment 失败: " + routePath);
            }
        }
        return false;
    }

    private void replaceFragment(Fragment newFragment) {
        if (newFragment == null || newFragment == currentFragment) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, newFragment); // 直接替换当前Fragment
        transaction.commit();
        currentFragment = newFragment;
    }

    public void setNavigationVisibility(boolean show) {
        runOnUiThread(() -> {
            int visibility = show ? View.VISIBLE : View.GONE;
            // 控制底部导航栏
            if (bottomNavigation != null) {
                bottomNavigation.setVisibility(visibility);
                bottomNavigation.getMenu().setGroupEnabled(0, show); // 禁用/启用交互
            }
            // 控制悬浮按钮
            if (fabSos != null) {
                fabSos.setVisibility(visibility);
                fabSos.setClickable(show); // 控制点击状态
            }
        });
    }
}
