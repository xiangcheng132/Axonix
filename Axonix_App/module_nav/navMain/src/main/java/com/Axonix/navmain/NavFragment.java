package com.Axonix.navmain;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Tip;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Inputtips;

import java.util.List;

@Route(path = "/nav/main")
public class NavFragment extends Fragment implements LocationManager.LocationListener {
    private MapView mapView;
    private MapDisplayManager mapDisplay;
    private LocationManager locationManager;
    private NavigationManager navigationManager;
    private SearchManager searchManager;

    private View rootView;
    private SearchView searchView;

    // 注册权限请求器
    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean allGranted = true;
                for (Boolean granted : result.values()) {
                    if (!granted) {
                        allGranted = false;
                        break;
                    }
                }
                if (allGranted) {
                    initMapAndLocation();  // 权限请求成功，初始化地图和定位
                } else {
                    MapPermissions.showPermissionDeniedToast(requireActivity());  // 显示权限拒绝的提示
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_nav, container, false);

        // 检查并请求权限
        if (MapPermissions.hasAllPermissions(requireActivity())) {
            initMapAndLocation();  // 权限已授予，直接初始化地图和定位
        } else {
            permissionLauncher.launch(MapPermissions.PERMISSIONS);  // 否则请求权限
        }

        searchView = rootView.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(true); // 初始时只显示图标

        // 设置初始宽度
        setSearchViewInitialWidth();

        // 初始化搜索框
        setUpSearchView();

        return rootView;
    }

    private void setSearchViewInitialWidth() {
        // 设置搜索框初始宽度为 40dp
        int initialWidth = (int) getResources().getDimension(R.dimen.searchView_initial_width); // 40dp
        ViewGroup.LayoutParams layoutParams = searchView.getLayoutParams();
        layoutParams.width = initialWidth;
        searchView.setLayoutParams(layoutParams);
    }

    private void setUpSearchView() {
        // 搜索框点击回调
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPOI(query);  // 提交搜索
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;  // 输入变化时无需处理
            }
        });

        // 搜索框点击图标时触发展开
        searchView.setOnSearchClickListener(v -> expandSearchView());

        // 监听输入框内容变化，结束输入后如果为空则收缩搜索框
        searchView.setOnCloseListener(() -> {
            if (searchView.getQuery().length() == 0) {
                shrinkSearchView();  // 如果没有输入内容，收缩搜索框
            }
            return false;
        });
    }

    private void expandSearchView() {
        // 使用 ViewTreeObserver 来监听视图的布局完成
        searchView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int startWidth = searchView.getWidth();
                int parentWidth = ((View) searchView.getParent()).getMeasuredWidth() - 200;

                searchView.getViewTreeObserver().removeOnPreDrawListener(this);

                ValueAnimator animator = ValueAnimator.ofInt(startWidth, parentWidth);
                animator.addUpdateListener(valueAnimator -> {
                    int width = (int) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = searchView.getLayoutParams();
                    layoutParams.width = width;
                    searchView.setLayoutParams(layoutParams);
                });

                animator.setDuration(300);
                animator.start();

                return true;
            }
        });
    }

    private void shrinkSearchView() {
        int startWidth = searchView.getWidth();
        int endWidth = (int) getResources().getDimension(R.dimen.searchView_initial_width);

        ValueAnimator animator = ValueAnimator.ofInt(startWidth, endWidth);
        animator.addUpdateListener(valueAnimator -> {
            int width = (int) valueAnimator.getAnimatedValue();

            ViewGroup.LayoutParams layoutParams = searchView.getLayoutParams();
            layoutParams.width = width;
            searchView.setLayoutParams(layoutParams);
        });

        animator.setDuration(300);
        animator.start();
    }


    private void searchPOI(String keyword) {
        // 延迟处理，用户点击搜索时才执行查询
        locationManager.getLocation(new LocationManager.LocationListener() {
            @Override
            public void onLocationReceived(double lat, double lng) {
                if (lat == 0 && lng == 0) {
                    Toast.makeText(requireContext(), "定位失败", Toast.LENGTH_SHORT).show();
                    return;
                }

                LatLonPoint centerPoint = new LatLonPoint(lat, lng);
                Log.d("searchPOI", "定位点: " + lat + ", " + lng);

                searchManager.searchPOI(keyword, centerPoint, new SearchManager.SearchResultListener() {
                    @Override
                    public void onSearchResult(List<Tip> tips) {
                        if (tips != null && !tips.isEmpty()) {
                            // 清除旧标记
                            mapDisplay.getAMap().clear();
                            Log.d("searchPOI", "搜索结果数量: " + tips.size());

                            // 在地图上添加新的标记
                            for (Tip tip : tips) {
                                LatLng searchLatLng = new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
                                mapDisplay.getAMap().addMarker(new MarkerOptions().position(searchLatLng).title(tip.getName()));
                            }
                            // 移动地图至搜索结果的第一个地点
                            LatLng firstResultLatLng = new LatLng(tips.get(0).getPoint().getLatitude(), tips.get(0).getPoint().getLongitude());
                            mapDisplay.getAMap().moveCamera(com.amap.api.maps.CameraUpdateFactory.newLatLngZoom(firstResultLatLng, 16));
                        } else {
                            Toast.makeText(requireContext(), "没有找到相关地点", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(AMapException e) {
                        Toast.makeText(requireContext(), "搜索失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, requireContext(), mapDisplay.getAMap());
    }


    private void initMapAndLocation() {
        // 初始化地图
        mapView = rootView.findViewById(R.id.map);
        mapDisplay = new MapDisplayManager();
        mapDisplay.initMap(mapView, null);

        // 初始化定位
        locationManager = new LocationManager();
        locationManager.setLocationListener(this);
        locationManager.startLocation(requireContext(), mapDisplay.getAMap());

        // 初始化导航
        navigationManager = new NavigationManager(requireContext(),mapDisplay.getAMap());

        // 初始化搜索管理器
        searchManager = new SearchManager(requireContext());
    }

    @Override
    public void onLocationReceived(double lat, double lng) {
        LatLng currentLocation = new LatLng(lat, lng);
        LatLng destination = new LatLng(39.908691, 116.397499);

        if (navigationManager != null) {
//            navigationManager.calculateRoute(currentLocation, destination, NavigationManager.RouteType.DRIVING);
            navigationManager.calculateRoute(currentLocation, destination, RouteType.DRIVING);

        }
    }


    @Override
    public void onDestroyView() {
        if (mapDisplay != null) mapDisplay.onDestroy();
        if (locationManager != null) locationManager.stopLocation();
        if (navigationManager != null) navigationManager.destroy();
        super.onDestroyView();
    }
}
