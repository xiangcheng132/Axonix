package com.Axonix.emergencymain;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.Axonix.emergencymain.R;
import com.alibaba.android.arouter.facade.annotation.Route;
import android.graphics.drawable.GradientDrawable;

@Route(path = "/emergency/main")
public class EmergencyFragment extends Fragment {
    private View circleView;
    private Button btnSeekHelp, btnProvideHelp;
    private Animation pulseAnimation;
    private boolean isSeekingHelp = false;
    private AMapLocationClient locationClient;

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

        // 点击寻求帮助
        btnSeekHelp.setOnClickListener(v -> showConfirmDialog(true));

        // 点击提供帮助
        btnProvideHelp.setOnClickListener(v -> showConfirmDialog(false));
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
        int colorRes = isSeeking ? R.color.red_light : R.color.blue_light;
        int targetColor = ContextCompat.getColor(requireContext(), colorRes);
        animateCircleColor(targetColor);

        // **加载不同的脉冲动画**
        int animRes = isSeeking ? R.anim.pulse_red : R.anim.pulse_blue;
        pulseAnimation = AnimationUtils.loadAnimation(getContext(), animRes);
        circleView.startAnimation(pulseAnimation);

        // **确保隐私合规**
        showPrivacyDialog();
    }

    private void animateCircleColor(int targetColor) {
        GradientDrawable drawable = (GradientDrawable) circleView.getBackground();
        if (drawable != null) {
            int startColor = ((GradientDrawable) circleView.getBackground()).getColor().getDefaultColor();

            ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, targetColor);
            colorAnimator.setDuration(1000);
            colorAnimator.addUpdateListener(animator -> drawable.setColor((int) animator.getAnimatedValue()));
            colorAnimator.start();
        }
    }


    private void showPrivacyDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("隐私政策")
                .setMessage("为了提供更好的服务，我们需要获取您的位置信息。请您阅读并同意《隐私政策》")
                .setPositiveButton("同意", (dialog, which) -> {
                    AMapLocationClient.updatePrivacyShow(getContext(), true, true);
                    AMapLocationClient.updatePrivacyAgree(getContext(), true);
                    initLocation(getContext());
                })
                .setNegativeButton("拒绝", (dialog, which) -> {
                    Toast.makeText(getContext(), "您拒绝了权限，无法使用定位功能", Toast.LENGTH_LONG).show();
                })
                .show();
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
            Toast.makeText(getContext(), "定位成功！\n纬度: " + latitude + "\n经度: " + longitude, Toast.LENGTH_SHORT).show();

            circleView.postDelayed(() -> {
                circleView.clearAnimation();
                Toast.makeText(getContext(), "已找到匹配的帮助者！", Toast.LENGTH_LONG).show();
            }, 3000);
        } else {
            Toast.makeText(getContext(), "定位失败！请检查 GPS 设置", Toast.LENGTH_LONG).show();
        }
    }
}
