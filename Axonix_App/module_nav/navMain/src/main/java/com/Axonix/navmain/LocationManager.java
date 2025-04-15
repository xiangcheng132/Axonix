package com.Axonix.navmain;

import android.content.Context;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;

public class LocationManager implements AMapLocationListener {
    private AMapLocationClient locationClient;
    private LocationListener listener;
    private AMap aMap;
    private boolean isLocationUpdated = false;
    private double currentLatitude;
    private double currentLongitude;

    public interface LocationListener {
        void onLocationReceived(double lat, double lng);
    }

    public void startLocation(Context context, AMap aMap) {
        try {
            this.aMap = aMap;
            locationClient = new AMapLocationClient(context);
            AMapLocationClientOption option = new AMapLocationClientOption();
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            option.setInterval(1000);
            locationClient.setLocationOption(option);
            locationClient.setLocationListener(this);
            locationClient.startLocation();

            setupLocationButtonStyle();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setupLocationButtonStyle() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.interval(1000);

        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true); // 显示定位按钮
        aMap.setMyLocationEnabled(true); // 启用定位图层

    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null && location.getErrorCode() == 0) {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            if (listener != null) {
                listener.onLocationReceived(currentLatitude, currentLongitude);
            }
            if (!isLocationUpdated) {
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(currentLatitude, currentLongitude), 16
                ));
                isLocationUpdated = true;
            }
        }
    }

    // 获取当前位置
    public void getLocation(LocationListener listener, Context context, AMap aMap) {
        this.listener = listener;
        if (currentLatitude != 0 && currentLongitude != 0) {
            // 如果当前位置已知，直接回调
            listener.onLocationReceived(currentLatitude, currentLongitude);
        } else {
            // 如果当前位置未知，开始定位
            startLocation(context, aMap);
        }
    }

    // 停止定位
    public void stopLocation() {
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
    }

    // 设置位置监听
    public void setLocationListener(LocationListener listener) {
        this.listener = listener;
    }
}
