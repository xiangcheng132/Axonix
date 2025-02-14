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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavItemSelected);

        // 设置默认选中首页
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private void setupSOSButton() {
        FloatingActionButton sosButton = findViewById(R.id.fab_sos);
        sosButton.setOnClickListener(new View.OnClickListener() {
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

    private boolean switchFragment(int itemId) {
        String routePath = routeMap.get(itemId);
        if (routePath != null) {
            Fragment fragment = (Fragment) ARouter.getInstance().build(routePath).navigation();
            if (fragment != null) {
                Log.d("BaseActivity", "成功获取 Fragment: " + routePath);
                replaceFragment(fragment);
            } else {
                Log.e("BaseActivity", "获取 Fragment 失败: " + routePath);
            }
            return true;
        }
        return false;
    }

    private void replaceFragment(Fragment newFragment) {
        if (newFragment == null || newFragment == currentFragment) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        if (!newFragment.isAdded()) {
            transaction.add(R.id.content_frame, newFragment);
        } else {
            transaction.show(newFragment);
        }
        transaction.commit();
        currentFragment = newFragment;
    }
}
