package com.Axonix.usermain;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.Axonix.usermain.model.ApiResponse;
import com.Axonix.usermain.model.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import com.Axonix.index.BaseActivity;


@Route(path = "/user/main")
public class UserFragment extends Fragment
        implements AddressSpinnerHelper.AddressLoadListener {

    private UserInfo currentUser = new UserInfo();
    private boolean isEditMode = false;
    private View rootView;
    private AddressSpinnerHelper addressHelper;

    // 定义接口
    public interface UserApi {
        @GET("/api/user")
        Call<UserInfo> getUserInfo();

        @PUT("/api/user")
        Call<ApiResponse> updateUserInfo(@Body UserInfo user);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user, container, false);

        Button btnEdit = rootView.findViewById(R.id.btn_edit);
        Spinner provinceSpinner = rootView.findViewById(R.id.spinner_province);
        Spinner citySpinner = rootView.findViewById(R.id.spinner_city);
        Spinner districtSpinner = rootView.findViewById(R.id.spinner_district);

        // 初始化地址帮助类
        addressHelper = new AddressSpinnerHelper(
                requireContext(),
                provinceSpinner,
                citySpinner,
                districtSpinner,
                this
        );
        addressHelper.initAddressSpinners();

        btnEdit.setOnClickListener(v -> toggleEditMode());

        Button btnLogout = rootView.findViewById(R.id.btn_logout);
        btnLogout.setVisibility(View.VISIBLE);
        btnLogout.setOnClickListener(v -> performLogout());

        return rootView;
    }

    // 实现地址加载完成回调
    @Override
    public void onAddressDataLoaded() {
        loadUserData();
    }

    private void performLogout() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setNavigationVisibility(false);
        }

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        Fragment loginFragment = (Fragment) ARouter.getInstance()
                .build("/user/login")
                .navigation();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left
        );

        transaction.replace(com.Axonix.index.R.id.content_frame, loginFragment);
        transaction.commitNow();
    }

    private void toggleEditMode() {
        isEditMode = !isEditMode;
        updateUIState();
        if (!isEditMode) {
            if (validateInput()) {
                saveUserData();
            } else {
                isEditMode = true;
                updateUIState();
            }
        }
    }

    private void updateUIState() {
        Button btnEdit = rootView.findViewById(R.id.btn_edit);
        btnEdit.setText(isEditMode ? "保存信息" : "编辑信息");

        Button loginOut = rootView.findViewById(R.id.btn_logout);
        loginOut.setVisibility(isEditMode?View.GONE:View.VISIBLE);

        // 设置地址选择器状态
        addressHelper.setSpinnerEnabled(isEditMode);

        // 强制注册时间不可编辑
        InfoItem createTimeItem = rootView.findViewById(R.id.item_create_time);
        createTimeItem.setEditMode(false);

        // 设置透明度
        float alphaValue = isEditMode ? 1f : 0.8f;
        addressHelper.setSpinnerAlpha(alphaValue);

        updateUserInfoDisplay();
    }

    private void updateItemState(int itemId, String value) {
        InfoItem item = rootView.findViewById(itemId);
        if (item != null) {
            if (itemId != R.id.item_create_time)
                item.setEditMode(isEditMode);
            item.setContent(value);
        }
    }

    private boolean validateInput() {
        InfoItem emailItem = rootView.findViewById(R.id.item_email);
        String email = emailItem.getEditText();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getContext(), "邮箱格式不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveUserData() {
        // 收集编辑后的数据
        currentUser.setGender(getItemContent(R.id.item_gender));
        currentUser.setAge(Integer.parseInt(getItemContent(R.id.item_age)));
        currentUser.setPhone(getItemContent(R.id.item_phone));
        currentUser.setEmail(getItemContent(R.id.item_email));
        currentUser.setAddress(getItemContent(R.id.item_detail_address));

        // 更新地址信息
        currentUser.setProvince(addressHelper.getSelectedProvince());
        currentUser.setCity(addressHelper.getSelectedCity());
        currentUser.setDistrict(addressHelper.getSelectedDistrict());

        updateUserInfoDisplay();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://your.api.baseurl/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi api = retrofit.create(UserApi.class);
        api.updateUserInfo(currentUser).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null &&
                        response.body().getCode() == 200) {
                    Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "保存失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getItemContent(int itemId) {
        InfoItem item = rootView.findViewById(itemId);
        return item != null ? item.getEditText() : "";
    }

    private void loadUserData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://your.api.baseurl/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApi api = retrofit.create(UserApi.class);
        Call<UserInfo> call = api.getUserInfo();
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    setAddressSpinnersWithUserData();
                } else {
                    setAddressSpinnersWithUserData();
                    Toast.makeText(getContext(), "加载失败，显示默认数据", Toast.LENGTH_SHORT).show();
                }
                updateUserInfoDisplay();
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                setAddressSpinnersWithUserData();
                Toast.makeText(getContext(), "网络错误，显示默认数据", Toast.LENGTH_SHORT).show();
                updateUserInfoDisplay();
            }
        });
    }

    private void setAddressSpinnersWithUserData() {
        try {
            addressHelper.setSelection(
                    currentUser.getProvince(),
                    currentUser.getCity(),
                    currentUser.getDistrict()
            );
        } catch (Exception e) {
            Log.e("UserFragment", "地址设置失败: " + e.getMessage());
        }
    }

    private void updateUserInfoDisplay() {
        updateItemState(R.id.item_gender, currentUser.getGender());
        updateItemState(R.id.item_age, String.valueOf(currentUser.getAge()));
        updateItemState(R.id.item_phone, currentUser.getPhone());
        updateItemState(R.id.item_email, currentUser.getEmail());
        updateItemState(R.id.item_detail_address, currentUser.getAddress());
        updateItemState(R.id.item_create_time, currentUser.getCreateTime());
        updateItemState(R.id.item_disability, getDisabilityText(currentUser.getDisabilityType()));

        // 设置用户名和 VIP 图标
        TextView tvUsername = rootView.findViewById(R.id.tv_username);
        ImageView ivVip = rootView.findViewById(R.id.iv_vip);
        tvUsername.setText(currentUser.getUsername());
        int vipIconRes = currentUser.isVip() ? R.drawable.ic_vip_yes : R.drawable.ic_vip_no;
        ivVip.setImageResource(vipIconRes);
    }

    private String getDisabilityText(int type) {
        switch (type) {
            case 0: return "无";
            case 1: return "视障";
            case 2: return "听障";
            case 3: return "其他障碍";
            default: return "未知";
        }
    }
}