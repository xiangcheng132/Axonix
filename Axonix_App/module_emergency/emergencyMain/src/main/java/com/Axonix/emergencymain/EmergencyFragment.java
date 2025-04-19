package com.Axonix.emergencymain;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.Axonix.index.config.NetworkClient;
import com.Axonix.index.config.NetworkTimeClient;
import com.Axonix.index.model.HelpRequest;
import com.Axonix.index.session.UserSessionManager;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.Axonix.emergencymain.R;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.graphics.drawable.GradientDrawable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Route(path = "/emergency/main")
public class EmergencyFragment extends Fragment {
    private View circleView;
    private Button btnSeekHelp, btnProvideHelp,btnHistory;
    private Animation pulseAnimation;
    private boolean isSeekingHelp = false;
    private AMapLocationClient locationClient;
    private OkHttpClient httpClient;
    private String HELP_URL;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emergency, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        circleView = view.findViewById(R.id.circleView);
        btnSeekHelp = view.findViewById(R.id.btnSeekHelp);
        btnProvideHelp = view.findViewById(R.id.btnProvideHelp);

        httpClient = NetworkClient.INSTANCE.getClient();
        HELP_URL = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/help-request/insert/selective";
        // 点击寻求帮助
        btnSeekHelp.setOnClickListener(v -> showConfirmDialog(true));

        // 点击提供帮助
        btnProvideHelp.setOnClickListener(v -> showConfirmDialog(false));

        // 历史记录
        btnHistory = view.findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(v -> {
            new HistoryDialogFragment().show(
                    getParentFragmentManager(),
                    "history_dialog"
            );
        });
    }

    private void showConfirmDialog(boolean isSeeking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(isSeeking ? "寻求帮助" : "提供帮助")
                .setMessage("确定要" + (isSeeking ? "寻找帮助吗？" : "提供帮助吗？"))
                .setPositiveButton("是", (dialog, which) -> startHelpProcess(isSeeking))
                .setNegativeButton("否", null)
                .show();
    }

    private void startHelpProcess(boolean isSeeking) {
        isSeekingHelp = isSeeking;

        // **设置动态颜色（寻求帮助红色，提供帮助蓝色）**
        int colorRes = isSeeking ? R.color.pulse_red_end : R.color.pulse_blue_end;
        animateCircleColor(colorRes);

        // **加载不同的脉冲动画**
        int animRes = isSeeking ? R.anim.pulse_red : R.anim.pulse_blue;
        pulseAnimation = AnimationUtils.loadAnimation(getContext(), animRes);
        circleView.startAnimation(pulseAnimation);

        // **确保隐私合规**
        showPrivacyDialog();
        initLocation(requireContext());
    }

    private void animateCircleColor(int targetColorRes) {
        GradientDrawable drawable = (GradientDrawable) circleView.getBackground();
        if (drawable != null) {
            int startColor = ((GradientDrawable) circleView.getBackground()).getColor().getDefaultColor();
            int endColor = ContextCompat.getColor(requireContext(), targetColorRes);

            ValueAnimator colorAnimator = ValueAnimator.ofObject(
                    new ArgbEvaluator(),
                    startColor,
                    endColor
            );
            colorAnimator.setDuration(1000);
            colorAnimator.addUpdateListener(animator -> {
                drawable.setColor((int) animator.getAnimatedValue());
            });
            colorAnimator.start();
        }
    }


    private void showPrivacyDialog() {
        AMapLocationClient.updatePrivacyShow(getContext(), true, true);
        AMapLocationClient.updatePrivacyAgree(getContext(), true);
    }

    private void initLocation(Context context) {
        try {
            locationClient = new AMapLocationClient(context);
            AMapLocationClientOption option = new AMapLocationClientOption();
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            option.setOnceLocation(true);
            locationClient.setLocationOption(option);
            locationClient.setLocationListener(this::onLocationReceived);
            locationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onLocationReceived(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            double latitude = aMapLocation.getLatitude();
            double longitude = aMapLocation.getLongitude();

            // 向服务器发起匹配请求
            sendMatchRequest(latitude, longitude, isSeekingHelp);
        } else {
            Toast.makeText(getContext(), "定位失败！请检查 GPS 设置", Toast.LENGTH_LONG).show();
        }
    }

    private void sendMatchRequest(double latitude, double longitude, boolean isSeekingHelp) {
        HelpRequest helpRequest = new HelpRequest();
        if (isSeekingHelp){
            helpRequest.setRequesterId(UserSessionManager.getInstance(requireContext()).getUser().getId());
            helpRequest.setRequesterLat(BigDecimal.valueOf(latitude));
            helpRequest.setRequesterLng(BigDecimal.valueOf(longitude));
            helpRequest.setCreatedAt(new Date());
            helpRequest.setStatus(1);
        }else {
            helpRequest.setHelperId(UserSessionManager.getInstance(requireContext()).getUser().getId());
            helpRequest.setHelperLat(BigDecimal.valueOf(latitude));
            helpRequest.setHelperLng(BigDecimal.valueOf(longitude));
            helpRequest.setCreatedAt(new Date());
            helpRequest.setStatus(1);
        }
        Gson gson = NetworkTimeClient.getGson();
        String json = gson.toJson(helpRequest);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(HELP_URL)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("匹配情况", "匹配失败", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("匹配情况", "匹配失败，状态码：" + response.code());
                    return;
                }
            }
        });
    }


}
