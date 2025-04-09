package com.Axonix.navmain;

import android.os.Bundle;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;

public class MapDisplayManager {
    private MapView mMapView;
    private AMap aMap;

    // 初始化地图
    public void initMap(MapView mapView, Bundle savedInstanceState) {
        mMapView = mapView;
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        aMap.getUiSettings().setZoomControlsEnabled(true);
    }

    // 返回 AMap 对象，供外部使用
    public AMap getAMap() {
        return aMap;
    }

    public void onResume() {
        if (mMapView != null) mMapView.onResume();
    }

    public void onPause() {
        if (mMapView != null) mMapView.onPause();
    }

    public void onDestroy() {
        if (mMapView != null) mMapView.onDestroy();
    }

    public void onSaveInstanceState(Bundle outState) {
        if (mMapView != null) mMapView.onSaveInstanceState(outState);
    }
}
