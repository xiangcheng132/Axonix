package com.Axonix.navmain;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

public class MapPermissions {
    public static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_WIFI_STATE
    };

    /**
     * 判断是否已获取所有权限
     */
    public static boolean hasAllPermissions(Activity activity) {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示权限未授予提示
     */
    public static void showPermissionDeniedToast(Activity activity) {
        Toast.makeText(activity, "请授予所有权限以保证导航功能正常使用", Toast.LENGTH_LONG).show();
    }
}
