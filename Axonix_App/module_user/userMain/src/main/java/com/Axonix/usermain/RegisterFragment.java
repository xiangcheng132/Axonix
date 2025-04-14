package com.Axonix.usermain;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.Axonix.index.config.NetworkClient;
import com.Axonix.index.config.NetworkTimeClient;
import com.Axonix.index.model.User;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.widget.AutoCompleteTextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Route(path = "/user/register")
public class RegisterFragment extends Fragment {

    private AddressSpinnerHelper addressHelper;
    private CircleImageView ivAvatar;
    private Uri avatarUri;

    private Gson gson;

    // 文本输入
    private TextInputEditText etUsername, etPassword, etAge,
            etDetailAddress, etPhone, etEmail;
    // 下拉选择
    private TextInputLayout tilDisability, tilProvince, tilCity, tilDistrict, tilGender;

    private OkHttpClient httpClient;

    private static final String REGISTER_URL = "https://192.168.43.87:8080/api/users/registerWithAvatar";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        httpClient = NetworkClient.INSTANCE.getClient();
        gson = NetworkTimeClient.getGson();

        // Toolbar 返回
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) getActivity().onBackPressed();
        });

        // 头像 & 按钮
        ivAvatar = view.findViewById(R.id.iv_avatar);
        Button btnRegister = view.findViewById(R.id.btn_register);

        // 文本输入框绑定
        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        etAge = view.findViewById(R.id.et_age);
        etDetailAddress = view.findViewById(R.id.et_detail_address);
        etPhone = view.findViewById(R.id.et_phone);
        etEmail = view.findViewById(R.id.et_email);

        // 下拉框绑定
        tilDisability = view.findViewById(R.id.til_disability);
        tilProvince = view.findViewById(R.id.til_province);
        tilCity = view.findViewById(R.id.til_city);
        tilDistrict = view.findViewById(R.id.til_district);
        tilGender = view.findViewById(R.id.til_gender);

        // 残障类型下拉
        ArrayAdapter<CharSequence> disabilityAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.disability_types,
                android.R.layout.simple_list_item_1);
        ((android.widget.AutoCompleteTextView) tilDisability.getEditText())
                .setAdapter(disabilityAdapter);

//        性别下拉
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.gender_options,
                android.R.layout.simple_list_item_1);
        ((android.widget.AutoCompleteTextView) tilGender.getEditText())
                .setAdapter(genderAdapter);

        AutoCompleteTextView actProvince = (AutoCompleteTextView) tilProvince.getEditText();
        AutoCompleteTextView actCity = (AutoCompleteTextView) tilCity.getEditText();
        AutoCompleteTextView actDistrict = (AutoCompleteTextView) tilDistrict.getEditText();

        AddressDropdownHelper addressHelper = new AddressDropdownHelper(
                requireContext(), actProvince, actCity, actDistrict
        );

        // 头像选择
        ivAvatar.setOnClickListener(v -> ImagePicker.with(this)
                .crop().compress(1024).maxResultSize(1080, 1080).start());

        // 注册提交
        btnRegister.setOnClickListener(v -> submitForm());

        return view;
    }

    private void submitForm() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String gender = ((AutoCompleteTextView) tilGender.getEditText()).getText().toString();
        String ageStr = etAge.getText().toString().trim();
        String detail = etDetailAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String province = ((AutoCompleteTextView) tilProvince.getEditText()).getText().toString();
        String city = ((AutoCompleteTextView) tilCity.getEditText()).getText().toString();
        String district = ((AutoCompleteTextView) tilDistrict.getEditText()).getText().toString();
        String disability = ((AutoCompleteTextView) tilDisability.getEditText()).getText().toString();
        String avatar = avatarUri != null ? avatarUri.toString() : "";

        if (username.isEmpty() || password.isEmpty() || gender.isEmpty() ||
                ageStr.isEmpty() || disability.isEmpty() ||
                province.isEmpty() || city.isEmpty() || district.isEmpty() ||
                detail.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(getContext(), "请填写所有必填项", Toast.LENGTH_SHORT).show();
            return;
        }
        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (Exception e) {
            Toast.makeText(getContext(), "年龄请输入数字", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setGender(convertGender(gender));
        user.setAge(age);
        user.setProvince(province);
        user.setCity(city);
        user.setCounty(district);
        user.setAddress(detail);
        user.setPhone(phone);
        user.setEmail(email);
        user.setDisabilityType(convertDisability(disability));
        user.setAvatar(avatarUri != null ? avatarUri.toString() : "");
        user.setUpdatedAt(new Date());

        String jsonUser = gson.toJson(user);

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user", jsonUser);

        if (avatarUri != null) {
            try {
                ContentResolver contentResolver = requireContext().getContentResolver();
                String fileName = getFileNameFromUri(contentResolver, avatarUri);
                InputStream inputStream = contentResolver.openInputStream(avatarUri);
                if (inputStream != null) {
                    byte[] fileBytes = getBytesFromInputStream(inputStream);
                    RequestBody fileBody = RequestBody.create(fileBytes, MediaType.parse("image/*"));
                    multipartBuilder.addFormDataPart("avatar", fileName, fileBody);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        RequestBody requestBody = multipartBuilder.build();

        Request request = new Request.Builder()
                .url(REGISTER_URL)
                .post(requestBody)
                .build();

        // 发起异步请求
        httpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "网络请求失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call,Response response) throws IOException {
                String responseStr = response.body() != null ? response.body().string() : "";
                requireActivity().runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "注册成功", Toast.LENGTH_SHORT).show();
                        if (getActivity() != null) getActivity().onBackPressed();
                    } else {
                        Toast.makeText(getContext(), "注册失败: " + responseStr, Toast.LENGTH_SHORT).show();
                        Log.d("注册失败", responseStr);
                    }
                });
            }
        });
    }

    private int disabilityAdapterPosition(String sel) {
        String[] arr = getResources().getStringArray(R.array.disability_types);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(sel)) return i;
        }
        return 0;
    }

    @Override
    public void onActivityResult(int req, int res, @Nullable Intent data) {
        super.onActivityResult(req, res, data);
        if (data != null) {
            avatarUri = data.getData();
            ivAvatar.setImageURI(avatarUri);
        }
    }

    private int convertGender(String genderStr) {
        switch (genderStr) {
            case "男":
                return 1;
            case "女":
                return 2;
            default:
                return 0; // 未知
        }
    }

    private int convertDisability(String disabilityStr) {
        switch (disabilityStr) {
            case "视障":
                return 1;
            case "听障":
                return 2;
            case "其他障碍":
                return 3;
            default:
                return 0;
        }
    }

    private String getFileNameFromUri(ContentResolver contentResolver, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}
