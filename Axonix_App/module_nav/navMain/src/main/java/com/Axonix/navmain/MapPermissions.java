package com.Axonix.navmain;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MapPermissions {
    private static final int REQUEST_CODE_PERMISSIONS = 1001; // 请求码

    /**
     * 检查并申请权限
     *
     * @param activity 当前的 Activity
     */
    public static void requestPermissionsIfNeeded(Activity activity) {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_WIFI_STATE
        };

        boolean needRequest = false;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                needRequest = true;
                break;
            }
        }

        // 动态申请权限
        if (needRequest) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_PERMISSIONS);
        }
    }

    /**
     * 处理权限请求结果
     *
     * @param activity    当前的 Activity
     * @param requestCode 请求码
     * @param permissions 权限数组
     * @param grantResults 授权结果
     */
    public static void handlePermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity, "请授予所有权限以保证应用正常运行", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Toast.makeText(activity, "所有权限已授予", Toast.LENGTH_SHORT).show();
        }
    }
}
