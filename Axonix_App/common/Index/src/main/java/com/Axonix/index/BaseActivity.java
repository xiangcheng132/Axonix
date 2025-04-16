package com.Axonix.index;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.Axonix.index.config.NetworkClient;
import com.Axonix.index.dto.EmergencyContactDto;
import com.Axonix.index.model.SosNotification;
import com.Axonix.index.session.UserSessionManager;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.services.core.LatLonPoint;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

public class BaseActivity extends AppCompatActivity {
    public BottomNavigationView bottomNavigation;
    public FloatingActionButton fabSos;
    private final Map<Integer, String> routeMap = new HashMap<>();
    private Fragment currentFragment;
    private String EMERGENCY_CONTACT_URL, SOS_NOTIFICATION_URL;
    private OkHttpClient httpClient;

    private double latitude = 0.0, longitude = 0.0;
    private AMapLocationClient locationClient;
    private Context that;
    private String address;
    private GeocodeSearch geocodeSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EMERGENCY_CONTACT_URL = getString(R.string.Base_url) + "/api/emergency-contact/select_By_userId";
        SOS_NOTIFICATION_URL = getString(R.string.Base_url) + "/api/sos-notifications/insert";
        setContentView(R.layout.activity_base);

        httpClient = NetworkClient.INSTANCE.getClient();
        that = this;

        initRoutes();
        setupNavigation();
        setupSOSButton();

        initLocation();
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
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this::onNavItemSelected);
        bottomNavigation.setSelectedItemId(R.id.nav_home);
    }

    private void setupSOSButton() {
        fabSos = findViewById(R.id.fab_sos);
        fabSos.setOnClickListener(v -> {
            sendSOSWithLocation();
        });
    }

    private boolean onNavItemSelected(@NonNull MenuItem item) {
        return switchFragment(item.getItemId());
    }

    public boolean switchFragment(int itemId) {
        String routePath = routeMap.get(itemId);
        if (routePath != null) {
            Fragment fragment = (Fragment) ARouter.getInstance().build(routePath).navigation();
            if (fragment != null) {
                replaceFragment(fragment);
                return true;
            }
        }
        return false;
    }

    private void replaceFragment(Fragment newFragment) {
        if (newFragment == null || newFragment == currentFragment) return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, newFragment);
        transaction.commit();
        currentFragment = newFragment;
    }

    public void setNavigationVisibility(boolean show) {
        runOnUiThread(() -> {
            int visibility = show ? View.VISIBLE : View.GONE;
            if (bottomNavigation != null) {
                bottomNavigation.setVisibility(visibility);
                bottomNavigation.getMenu().setGroupEnabled(0, show);
            }
            if (fabSos != null) {
                fabSos.setVisibility(visibility);
                fabSos.setClickable(show);
            }
        });
    }

    private void initLocation() {
        try {
            AMapLocationClient.updatePrivacyShow(this, true, true);
            AMapLocationClient.updatePrivacyAgree(this, true);
            locationClient = new AMapLocationClient(that);
            AMapLocationClientOption option = new AMapLocationClientOption();
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            option.setOnceLocation(true);
            locationClient.setLocationOption(option);
            locationClient.setLocationListener(this::onLocationReceived);
            locationClient.startLocation();

            // 初始化 GeocodeSearch，只需要初始化一次
            geocodeSearch = new GeocodeSearch(this);
            geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                    if (rCode == 1000) {
                        address = result.getRegeocodeAddress().getFormatAddress();
                        Log.d("获取到的地址", address);
                    } else {
                        Log.e("获取地址失败", "Error Code: " + rCode);
                    }
                }

                @Override
                public void onGeocodeSearched(com.amap.api.services.geocoder.GeocodeResult result, int rCode) {
                    // 不需要实现此方法
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onLocationReceived(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            latitude = aMapLocation.getLatitude();
            longitude = aMapLocation.getLongitude();
            Log.d("经纬度获取成功", latitude + ", " + longitude);

            // 确保只有在有效经纬度的情况下才执行地理编码查询
            if (latitude != 0.0 && longitude != 0.0) {
                RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latitude, longitude), 200, GeocodeSearch.AMAP);
                geocodeSearch.getFromLocationAsyn(query);  // 异步获取地址
            } else {
                Log.e("无效经纬度", "获取到无效的经纬度：0.0, 0.0");
            }

        } else {
            Log.e("定位失败", "Error Code: " + aMapLocation.getErrorCode());
            Toast.makeText(that, "定位失败，请检查定位权限或网络", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendSOSWithLocation() {
        Integer userId = UserSessionManager.getInstance(getApplicationContext()).getUser().getId();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userId);
        String jsonUser = jsonObject.toString();

        RequestBody body = RequestBody.create(jsonUser, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(EMERGENCY_CONTACT_URL).post(body).build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("联系人获取失败", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful() || responseBody == null) return;

                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<EmergencyContactDto>>() {}.getType();
                    List<EmergencyContactDto> contactList = gson.fromJson(responseBody.string(), listType);

                    for (EmergencyContactDto item : contactList) {
                        SosNotification sos = new SosNotification();
                        sos.setUserId(item.getUserId());
                        sos.setContactId(item.getContactUserId());
                        sos.setTitle("紧急求助！！救救我！！");
                        sos.setContent( address + "发起紧急求助\n手机号是：" + UserSessionManager.getInstance(getApplicationContext()).getUser().getPhone());
                        sos.setLat(BigDecimal.valueOf(latitude));
                        sos.setLng(BigDecimal.valueOf(longitude));

                        String jsonSos = gson.toJson(sos);
                        RequestBody sosBody = RequestBody.create(jsonSos, MediaType.get("application/json; charset=utf-8"));
                        Request sosRequest = new Request.Builder().url(SOS_NOTIFICATION_URL).post(sosBody).build();

                        httpClient.newCall(sosRequest).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("SOS上传失败", e.getMessage());
                            }

                            @Override
                            public void onResponse(Call call, Response response) {
                                if (response.isSuccessful()) {
                                    Log.d("SOS上传成功", "已通知联系人：" + item.getContactUserId());
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 拨号界面跳转
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:110"));
        startActivity(intent);
    }
}
