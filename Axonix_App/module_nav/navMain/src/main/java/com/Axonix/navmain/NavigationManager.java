package com.Axonix.navmain;

import android.content.Context;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class NavigationManager implements AMapNaviListener {
    private AMapNavi aMapNavi;
    private Context context;
    private AMap aMap;
    private Polyline currentRoutePolyline;
    private RouteType currentRouteType;
    private static final int DRIVING_COLOR = Color.argb(200, 0, 120, 255); // 半透明蓝色
    private static final int WALKING_COLOR = Color.argb(200, 0, 200, 0);   // 半透明绿色
    private static final float POLYLINE_WIDTH = 15.0f;

    public NavigationManager(Context context, AMap aMap) {
        this.context = context;
        this.aMap = aMap;
        try {
            aMapNavi = AMapNavi.getInstance(context);
            aMapNavi.addAMapNaviListener(this);
            aMapNavi.setUseInnerVoice(true);
        } catch (Exception e) {
            showToast("导航初始化失败：" + e.getMessage());
        }
    }

    // 修改策略设置方式（替换原来的AMapNavi.DrivingDefault）
    public void calculateRoute(LatLng start, LatLng end, RouteType routeType) {
        try {
            this.currentRouteType = routeType;
            List<NaviLatLng> startList = new ArrayList<>();
            List<NaviLatLng> endList = new ArrayList<>();
            startList.add(new NaviLatLng(start.latitude, start.longitude));
            endList.add(new NaviLatLng(end.latitude, end.longitude));

            // 修正后的策略设置
            int strategy = 0;
            if (routeType == RouteType.DRIVING) {
                // 驾驶策略：躲避拥堵 & 高速优先
                strategy = aMapNavi.strategyConvert(true, false, false, false, false);
            } else if (routeType == RouteType.WALKING) {
                // 步行策略：多路径 & 躲避拥堵
                strategy = aMapNavi.strategyConvert(false, true, true, false, false);
            }

            aMapNavi.calculateDriveRoute(startList, endList, null, strategy);
        } catch (Exception e) {
            showToast("路径计算异常：" + e.getMessage());
        }
    }

    @Override
    public void onInitNaviFailure() {
        Toast.makeText(context, "导航初始化失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitNaviSuccess() {
        Toast.makeText(context, "导航初始化成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartNavi(int type) {
        Toast.makeText(context, "开始导航，导航类型: " + type, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTrafficStatusUpdate() {
        // 可以在这里更新交通状态信息
    }

    @Override
    public void onLocationChange(AMapNaviLocation location) {
        if (location != null) {
            com.amap.api.navi.model.NaviLatLng coord = location.getCoord();
            if (coord != null) {
                double latitude = coord.getLatitude();
                double longitude = coord.getLongitude();
                // 简单判断经纬度是否为有效数值
                if (latitude != 0 && longitude != 0) {
//                    Toast.makeText(context, "当前位置: " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onGetNavigationText(int code, String text) {
        Toast.makeText(context, "导航提示信息（代码: " + code + "）: " + text, Toast.LENGTH_SHORT).show();
    }

    @Override
    @Deprecated
    public void onGetNavigationText(String text) {
        Toast.makeText(context, "导航提示信息: " + text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEndEmulatorNavi() {
        Toast.makeText(context, "仿真导航结束", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onArriveDestination() {
        Toast.makeText(context, "已到达目的地", Toast.LENGTH_SHORT).show();
    }

    @Override
    @Deprecated
    public void onCalculateRouteFailure(int errorCode) {
        Toast.makeText(context, "路径规划失败，旧错误码: " + errorCode, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReCalculateRouteForYaw() {
        Toast.makeText(context, "偏航，重新规划路线", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        Toast.makeText(context, "前方拥堵，重新规划路线", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onArrivedWayPoint(int wayID) {
        Toast.makeText(context, "到达途经点，途经点 ID: " + wayID, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGpsOpenStatus(boolean isGpsOpen) {
        if (isGpsOpen) {
            Toast.makeText(context, "GPS 已开启", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "GPS 已关闭", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        if (naviInfo != null) {
            String roadName = naviInfo.getCurrentRoadName();  // 获取当前道路
            String nextRoadName = naviInfo.getNextRoadName(); // 获取下一道路
//            showToast("当前道路名称: " + roadName + "\n下一道路名称: " + nextRoadName + "\n导航提示: ");
        }
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] cameraInfos) {
        // 可以在这里更新摄像头信息
    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo cameraInfo, AMapNaviCameraInfo nextCameraInfo, int distance) {
        // 可以在这里更新间隔摄像头信息
    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] serviceAreaInfos) {
        // 可以在这里更新服务区信息
    }

    @Override
    public void showCross(AMapNaviCross cross) {
        // 可以在这里显示路口信息
    }

    @Override
    public void hideCross() {
        // 可以在这里隐藏路口信息
    }

    @Override
    public void showModeCross(AMapModelCross modelCross) {
        // 可以在这里显示模型路口信息
    }

    @Override
    public void hideModeCross() {
        // 可以在这里隐藏模型路口信息
    }

    @Override
    @Deprecated
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {
        // 可以在这里显示车道信息
    }

    @Override
    public void showLaneInfo(AMapLaneInfo laneInfo) {
        // 可以在这里显示车道信息
    }

    @Override
    public void hideLaneInfo() {
        // 可以在这里隐藏车道信息
    }

    @Override
    @Deprecated
    public void onCalculateRouteSuccess(int[] routeIds) {
        Toast.makeText(context, "路径规划成功", Toast.LENGTH_SHORT).show();
        // 这里暂时不处理旧的成功回调
    }

    private int getRouteColor(RouteType type) {
        return type == RouteType.DRIVING ? DRIVING_COLOR : WALKING_COLOR;
    }
    // 主要新增的路线绘制方法
    private void drawRouteOnMap(AMapNaviPath naviPath) {
        if (naviPath == null || aMap == null) return;

        // 清除旧路线
        if (currentRoutePolyline != null) {
            currentRoutePolyline.remove();
        }

        // 获取路径坐标点
        List<NaviLatLng> coordList = naviPath.getCoordList();
        if (coordList == null || coordList.isEmpty()) return;

        // 转换坐标类型
        List<LatLng> pathPoints = new ArrayList<>();
        for (NaviLatLng naviLatLng : coordList) {
            pathPoints.add(new LatLng(naviLatLng.getLatitude(), naviLatLng.getLongitude()));
        }

        // 创建绘制参数
        PolylineOptions options = new PolylineOptions()
                .addAll(pathPoints)
                .width(POLYLINE_WIDTH)
                .color(getRouteColor(currentRouteType))
                .geodesic(true);

        // 添加路线到地图
        currentRoutePolyline = aMap.addPolyline(options);
    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult routeResult) {
        // 获取路径数据
        AMapNaviPath naviPath = aMapNavi.getNaviPath();
        if (naviPath != null) {
            drawRouteOnMap(naviPath);
            aMapNavi.startNavi(NaviType.GPS);
            showToast("路径规划成功");
        } else {
            showToast("获取路径数据失败");
        }
    }


    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult result) {
        Toast.makeText(context, "路径规划失败，错误码：" + result.getErrorCode(), Toast.LENGTH_LONG).show();
    }

    @Override
    @Deprecated
    public void notifyParallelRoad(int msg) {
        // 可以在这里处理并行道路信息
    }

    @Override
    @Deprecated
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] trafficFacilityInfos) {
        // 可以在这里更新交通设施信息
    }

    @Override
    @Deprecated
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo trafficFacilityInfo) {
        // 可以在这里更新交通设施信息
    }

    @Override
    @Deprecated
    public void updateAimlessModeStatistics(AimLessModeStat stat) {
        // 可以在这里更新无目的模式统计信息
    }

    @Override
    @Deprecated
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo congestionInfo) {
        // 可以在这里更新无目的模式拥堵信息
    }

    @Override
    public void onPlayRing(int ringType) {
        // 可以在这里处理播放音效逻辑
    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData notifyData) {
        // 可以在这里处理路线通知信息
    }

    @Override
    public void onGpsSignalWeak(boolean isWeak) {
        if (isWeak) {
            Toast.makeText(context, "GPS 信号弱", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "GPS 信号正常", Toast.LENGTH_SHORT).show();
        }
    }

    public void destroy() {
        if (aMapNavi != null) {
            aMapNavi.stopNavi();
            aMapNavi.destroy();
        }
        if (currentRoutePolyline != null) {
            currentRoutePolyline.remove();
        }
    }

}
