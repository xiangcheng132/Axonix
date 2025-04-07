package com.Axonix.usermain;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Axonix.usermain.api.AuthApi;
import com.Axonix.usermain.model.ApiResponse;
import com.Axonix.usermain.model.RegisterRequest;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.appbar.MaterialToolbar;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Route(path = "/user/register")
public class RegisterFragment extends Fragment {

    private AddressSpinnerHelper addressHelper;
    private Spinner provinceSpinner, citySpinner, districtSpinner, disabilitySpinner;
    private CircleImageView ivAvatar;
    private Uri avatarUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Toolbar 返回按钮设置为调用系统返回（弹出回退栈），而不是新建登录页面
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        // 初始化其余视图
        initViews(view);
        initAddressSpinners(view);

        return view;
    }

    private void initViews(View view) {
        ivAvatar = view.findViewById(R.id.iv_avatar);
        Button btnRegister = view.findViewById(R.id.btn_register);
        provinceSpinner = view.findViewById(R.id.spinner_province);
        citySpinner = view.findViewById(R.id.spinner_city);
        districtSpinner = view.findViewById(R.id.spinner_district);
        disabilitySpinner = view.findViewById(R.id.spinner_disability);

        // 残障类型适配器
        ArrayAdapter<CharSequence> disabilityAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.disability_types,
                android.R.layout.simple_spinner_item
        );
        disabilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        disabilitySpinner.setAdapter(disabilityAdapter);

        // 头像选择
        ivAvatar.setOnClickListener(v -> ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start());

        // 注册提交
        btnRegister.setOnClickListener(v -> submitForm());
    }

    private void initAddressSpinners(View view) {
        provinceSpinner = view.findViewById(R.id.spinner_province);
        citySpinner = view.findViewById(R.id.spinner_city);
        districtSpinner = view.findViewById(R.id.spinner_district);

        addressHelper = new AddressSpinnerHelper(
                requireContext(),
                provinceSpinner,
                citySpinner,
                districtSpinner,
                null
        );
        addressHelper.initAddressSpinners();
    }

    private void submitForm() {
        String username = ((EditText) getView().findViewById(R.id.et_username)).getText().toString().trim();
        String password = ((EditText) getView().findViewById(R.id.et_password)).getText().toString().trim();
        String avatarUrl = avatarUri != null ? avatarUri.toString() : "";
        String gender = ((InfoItem) getView().findViewById(R.id.item_gender)).getEditText();
        int age = Integer.parseInt(((InfoItem) getView().findViewById(R.id.item_age)).getEditText());
        String address = ((InfoItem) getView().findViewById(R.id.item_detail_address)).getEditText();
        String email = ((InfoItem) getView().findViewById(R.id.item_email)).getEditText();
        String phone = ((InfoItem) getView().findViewById(R.id.item_phone)).getEditText();
        int disabilityType = disabilitySpinner.getSelectedItemPosition();

        String province = addressHelper.getSelectedProvince();
        String city = addressHelper.getSelectedCity();
        String district = addressHelper.getSelectedDistrict();

        if (username.isEmpty() || password.isEmpty() || gender.isEmpty() ||
                province.isEmpty() || city.isEmpty() || district.isEmpty() ||
                address.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getContext(), "请填写所有必填项", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://your.api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthApi api = retrofit.create(AuthApi.class);
        api.register(new RegisterRequest(
                username, password, avatarUrl, gender, age,
                province, city, district, address, email, phone, disabilityType
        )).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null &&
                        response.body().getCode() == 200) {
                    Toast.makeText(getContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    // 注册成功后也可以直接弹出回退栈返回登录页
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                } else {
                    Toast.makeText(getContext(), "注册失败: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            avatarUri = data.getData();
            ivAvatar.setImageURI(avatarUri);
        }
    }
}
