package com.Axonix.usermain;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.widget.AutoCompleteTextView;

@Route(path = "/user/register")
public class RegisterFragment extends Fragment {

    private AddressSpinnerHelper addressHelper;
    private CircleImageView ivAvatar;
    private Uri avatarUri;

    // 文本输入
    private TextInputEditText etUsername, etPassword, etGender, etAge,
            etDetailAddress, etPhone, etEmail;
    // 下拉选择
    private TextInputLayout tilDisability, tilProvince, tilCity, tilDistrict;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Toolbar 返回
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        // 头像 & 按钮
        ivAvatar = view.findViewById(R.id.iv_avatar);
        Button btnRegister = view.findViewById(R.id.btn_register);

        // 文本输入框绑定
        etUsername      = view.findViewById(R.id.et_username);
        etPassword      = view.findViewById(R.id.et_password);
        etGender        = view.findViewById(R.id.et_gender);
        etAge           = view.findViewById(R.id.et_age);
        etDetailAddress = view.findViewById(R.id.et_detail_address);
        etPhone         = view.findViewById(R.id.et_phone);
        etEmail         = view.findViewById(R.id.et_email);

        // 下拉框绑定
        tilDisability = view.findViewById(R.id.til_disability);
        tilProvince   = view.findViewById(R.id.til_province);
        tilCity       = view.findViewById(R.id.til_city);
        tilDistrict   = view.findViewById(R.id.til_district);

        // 残障类型下拉
        ArrayAdapter<CharSequence> disabilityAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.disability_types,
                android.R.layout.simple_list_item_1);
        ((android.widget.AutoCompleteTextView)tilDisability.getEditText())
                .setAdapter(disabilityAdapter);

        AutoCompleteTextView actProvince = (AutoCompleteTextView) tilProvince.getEditText();
        AutoCompleteTextView actCity     = (AutoCompleteTextView) tilCity.getEditText();
        AutoCompleteTextView actDistrict = (AutoCompleteTextView) tilDistrict.getEditText();

        AddressDropdownHelper addressHelper = new AddressDropdownHelper(
                requireContext(), actProvince, actCity, actDistrict
        );

        // 头像选择
        ivAvatar.setOnClickListener(v -> ImagePicker.with(this)
                .crop().compress(1024).maxResultSize(1080,1080).start());

        // 注册提交
        btnRegister.setOnClickListener(v -> submitForm());

        return view;
    }

    private void submitForm() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String gender   = etGender.getText().toString().trim();
        String ageStr   = etAge.getText().toString().trim();
        String detail   = etDetailAddress.getText().toString().trim();
        String phone    = etPhone.getText().toString().trim();
        String email    = etEmail.getText().toString().trim();
        String avatar   = avatarUri!=null?avatarUri.toString():"";
        String disability = ((android.widget.AutoCompleteTextView)tilDisability.getEditText())
                .getText().toString();
        String province = ((android.widget.AutoCompleteTextView)tilProvince.getEditText())
                .getText().toString();
        String city     = ((android.widget.AutoCompleteTextView)tilCity.getEditText())
                .getText().toString();
        String district = ((android.widget.AutoCompleteTextView)tilDistrict.getEditText())
                .getText().toString();

        if (username.isEmpty()||password.isEmpty()||gender.isEmpty()||
                ageStr.isEmpty()||disability.isEmpty()||
                province.isEmpty()||city.isEmpty()||district.isEmpty()||
                detail.isEmpty()||phone.isEmpty()||email.isEmpty()) {
            Toast.makeText(getContext(),"请填写所有必填项",Toast.LENGTH_SHORT).show();
            return;
        }
        int age;
        try { age = Integer.parseInt(ageStr); }
        catch(Exception e){
            Toast.makeText(getContext(),"年龄请输入数字",Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://your.api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AuthApi api = retrofit.create(AuthApi.class);
        api.register(new RegisterRequest(
                username,password,avatar,gender,age,
                province,city,district,detail,email,phone,
                disabilityAdapterPosition(disability)
        )).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> resp) {
                if (resp.isSuccessful()&&resp.body()!=null&&resp.body().getCode()==200) {
                    Toast.makeText(getContext(),"注册成功",Toast.LENGTH_SHORT).show();
                    if (getActivity()!=null) getActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(),"注册失败:"+resp.message(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(),"网络错误:"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int disabilityAdapterPosition(String sel){
        String[] arr = getResources().getStringArray(R.array.disability_types);
        for(int i=0;i<arr.length;i++){
            if(arr[i].equals(sel)) return i;
        }
        return 0;
    }

    @Override
    public void onActivityResult(int req, int res, @Nullable Intent data) {
        super.onActivityResult(req,res,data);
        if(data!=null){
            avatarUri = data.getData();
            ivAvatar.setImageURI(avatarUri);
        }
    }
}
