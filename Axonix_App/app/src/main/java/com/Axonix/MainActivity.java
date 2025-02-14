package com.Axonix;

import android.os.Bundle;
import com.Axonix.index.BaseActivity;
import com.alibaba.android.arouter.facade.annotation.Route;
import android.util.Log;


public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "MainActivity 启动");
    }
}
