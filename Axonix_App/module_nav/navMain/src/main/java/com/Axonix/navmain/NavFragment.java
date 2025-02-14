package com.Axonix.navmain;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.Axonix.navmain.R;

@Route(path = "/nav/main")
public class NavFragment extends Fragment {
    private MapView mMapView = null;
    private AMap aMap = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav, container, false);

        // 检查并申请权限
        MapPermissions.requestPermissionsIfNeeded(requireActivity());

        // 初始化高德地图
        mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        MapControl mapControl = new MapControl();
        mapControl.init(savedInstanceState);

        aMap = mapControl.AMapInit(mMapView);
        // 开启定位
        aMap = mapControl.location(aMap);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }
}
