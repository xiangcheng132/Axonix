package com.Axonix.navmain;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.amap.api.location.AMapLocationClient;
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

    private RecyclerView searchResultsRecyclerView;
    private SearchResultsAdapter searchResultsAdapter;

    private LatLng currentLocation,endLocation;

    private boolean isSearching = false; // 用来标记是否正在搜索

    private LinearLayout stopNavigationContainer;
    private ImageButton stopNavButton;
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
            permissionLauncher.launch(MapPermissions.PERMISSIONS);
        }

        stopNavInit();//停止导航按钮初始化
        searchInit();//搜索功能初始化
        parameterNav();//参数导航，即其他页面进入该页面时附带导航参数时使用

        return rootView;
    }

    private void parameterNav(){
        if (getArguments() != null) {
            double latitude = getArguments().getDouble("latitude", Double.NaN);
            double longitude = getArguments().getDouble("longitude", Double.NaN);

            if (!Double.isNaN(latitude) && !Double.isNaN(longitude)) {
                endLocation = new LatLng(latitude, longitude);
                if (currentLocation != null) {
                    navigationManager.stopNavi();
                    navigationManager.calculateRoute(currentLocation, endLocation, RouteType.DRIVING);
                    Toast.makeText(getContext(), "正在导航...", Toast.LENGTH_SHORT).show();
                    mapDisplay.getAMap().moveCamera(com.amap.api.maps.CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
                    stopNavigationContainer.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    private void stopNavInit(){
        stopNavigationContainer = rootView.findViewById(R.id.stopNavigationContainer);
        stopNavButton = rootView.findViewById(R.id.stopNavigationButton);
        stopNavigationContainer.setVisibility(View.GONE);
        stopNavigationContainer.setOnClickListener(v->{
            navigationManager.stopNavi();
            stopNavigationContainer.setVisibility(View.GONE);
        });
        stopNavButton.setOnClickListener(v->{
            navigationManager.stopNavi();
            stopNavigationContainer.setVisibility(View.GONE);
        });
    }

    private void searchInit(){
        searchResultsRecyclerView = rootView.findViewById(R.id.searchResultsRecyclerView);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultsAdapter = new SearchResultsAdapter(null, new SearchResultsAdapter.OnItemClickListener() {
            //            点击地点，地图位置移动
            @Override
            public void onItemClick(Tip tip) {
                if (tip.getPoint() != null) {
                    LatLng targetLatLng = new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
                    mapDisplay.getAMap().moveCamera(com.amap.api.maps.CameraUpdateFactory.newLatLngZoom(targetLatLng, 16));
                    mapDisplay.getAMap().addMarker(new MarkerOptions().position(targetLatLng).title(tip.getName()));
                    searchResultsRecyclerView.setVisibility(View.GONE);
                    searchView.clearFocus();
                }
            }
            //             出发按钮功能
            @Override
            public void onDepartClick(Tip tip) {
                if (currentLocation != null && tip.getPoint() != null) {
                    navigationManager.stopNavi();
                    LatLng destination = new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
                    navigationManager.calculateRoute(currentLocation, destination, RouteType.DRIVING);
                    Toast.makeText(getContext(), "正在导航...", Toast.LENGTH_SHORT).show();
                    mapDisplay.getAMap().moveCamera(com.amap.api.maps.CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
                    stopNavigationContainer.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "当前位置尚未确定", Toast.LENGTH_SHORT).show();
                }
                searchView.clearFocus();
                searchResultsRecyclerView.setVisibility(View.GONE);
            }
        });
        searchResultsRecyclerView.setAdapter(searchResultsAdapter);
        searchView = rootView.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(true);

        setSearchViewInitialWidth();
        setUpSearchView();
    }

    private void initMapAndLocation() {
        AMapLocationClient.updatePrivacyShow(requireContext(), true, true);
        AMapLocationClient.updatePrivacyAgree(requireContext(), true);

        // 初始化地图
        mapView = rootView.findViewById(R.id.map);
        mapDisplay = new MapDisplayManager();
        mapDisplay.initMap(mapView, null);
        mapDisplay.getAMap().setOnMapClickListener(latLng -> {
            searchResultsRecyclerView.setVisibility(View.GONE);
            searchView.clearFocus();
        });

        // 初始化定位
        locationManager = new LocationManager();
        locationManager.setLocationListener(this);
        locationManager.startLocation(requireContext(), mapDisplay.getAMap());

        // 初始化导航
        navigationManager = new NavigationManager(requireContext(),mapDisplay.getAMap());

        // 初始化搜索管理器
        searchManager = new SearchManager(requireContext());
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
                searchPOI(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty()) {
                    searchResultsRecyclerView.setVisibility(View.GONE);
                }
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            // 当搜索框获取焦点且文字非空时，显示搜索结果列表
            if (hasFocus && !searchView.getQuery().toString().trim().isEmpty()) {
                searchResultsRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        searchView.setOnSearchClickListener(v -> {
            expandSearchView();
        });

        // 当搜索框关闭时，如果当前没有内容，则收缩搜索框，并隐藏结果列表
        searchView.setOnCloseListener(() -> {
            searchView.setQuery("", false); // 清空搜索框文本
            shrinkSearchView();
            searchResultsRecyclerView.setVisibility(View.GONE);
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
        Log.d("搜索函数", "触发搜索函数1");

        if (keyword.trim().isEmpty()) {
            searchResultsRecyclerView.setVisibility(View.GONE);
            return;
        }

        if (isSearching) {
            // 如果正在搜索，直接返回，不再重复搜索
            return;
        }

        isSearching = true; // 设置标志，表示正在搜索

        // 获取当前位置，直接执行查询
        if (currentLocation != null) {
            LatLonPoint centerPoint = new LatLonPoint(currentLocation.latitude, currentLocation.longitude);
            Log.d("searchPOI", "当前位置: " + currentLocation.latitude + ", " + currentLocation.longitude);

            searchManager.searchPOI(keyword, centerPoint, new SearchManager.SearchResultListener() {
                @Override
                public void onSearchResult(List<Tip> tips) {
                    isSearching = false; // 搜索完成，重置标志

                    if (tips != null && !tips.isEmpty()) {
                        // 清除旧标记
                        mapDisplay.getAMap().clear();

                        // 更新 RecyclerView 数据
                        searchResultsAdapter.updateData(tips);
                        searchResultsRecyclerView.setVisibility(View.VISIBLE);

                        // 同时在地图上添加所有标记
                        for (Tip tip : tips) {
                            LatLng searchLatLng = new LatLng(tip.getPoint().getLatitude(), tip.getPoint().getLongitude());
                            mapDisplay.getAMap().addMarker(new MarkerOptions().position(searchLatLng).title(tip.getName()));
                        }

                        // 移动地图至第一个搜索结果
                        LatLng firstResultLatLng = new LatLng(tips.get(0).getPoint().getLatitude(), tips.get(0).getPoint().getLongitude());
                        mapDisplay.getAMap().moveCamera(com.amap.api.maps.CameraUpdateFactory.newLatLngZoom(firstResultLatLng, 16));
                    } else {
                        Toast.makeText(requireContext(), "没有找到相关地点", Toast.LENGTH_SHORT).show();
                        searchResultsRecyclerView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(AMapException e) {
                    isSearching = false; // 搜索失败，重置标志
                    Toast.makeText(requireContext(), "搜索失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    searchResultsRecyclerView.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(requireContext(), "当前位置尚未确定", Toast.LENGTH_SHORT).show();
            isSearching = false; // 搜索结束，重置标志
        }
    }


    @Override
    public void onLocationReceived(double lat, double lng) {
        currentLocation = new LatLng(lat, lng);

    }


    @Override
    public void onDestroyView() {
        if (mapDisplay != null) mapDisplay.onDestroy();
        if (locationManager != null) locationManager.stopLocation();
        if (navigationManager != null) navigationManager.destroy();
        super.onDestroyView();
    }
}
