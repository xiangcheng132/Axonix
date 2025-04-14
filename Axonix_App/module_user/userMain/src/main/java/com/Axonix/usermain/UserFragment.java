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

import com.Axonix.index.model.Gender;
import com.Axonix.index.model.User;
import com.Axonix.index.session.UserSessionManager;
import com.Axonix.index.controller.UserUpdateManager;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.Axonix.index.BaseActivity;

@Route(path = "/user/main")
public class UserFragment extends Fragment
        implements AddressSpinnerHelper.AddressLoadListener {

    private User currentUser;
    private boolean isEditMode = false;
    private View rootView;
    private AddressSpinnerHelper addressHelper;

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
        addressHelper.setSpinnerEnabled(false);

        // 获取性别和残障类型的 Spinner
        Spinner spinnerGender = rootView.findViewById(R.id.spinner_gender);
        Spinner spinnerDisability = rootView.findViewById(R.id.spinner_disability);

        // 设置性别 Spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        // 设置残障类型 Spinner
        ArrayAdapter<CharSequence> disabilityAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.disability_options, android.R.layout.simple_spinner_item);
        disabilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDisability.setAdapter(disabilityAdapter);

        btnEdit.setOnClickListener(v -> toggleEditMode());

        Button btnLogout = rootView.findViewById(R.id.btn_logout);
        btnLogout.setVisibility(View.VISIBLE);
        btnLogout.setOnClickListener(v -> performLogout());

        setSpinnerEditable(R.id.spinner_gender, isEditMode);
        setSpinnerEditable(R.id.spinner_disability, isEditMode);
        return rootView;
    }

    // 实现地址加载完成回调
    @Override
    public void onAddressDataLoaded() {
        loadUserFromSessionOrDefault();
    }

    private void loadUserFromSessionOrDefault() {
        User sessionUser = UserSessionManager.getInstance(requireContext()).getUser();
        if (sessionUser != null) {
            currentUser = sessionUser;
        } else {
            currentUser = new User();
        }

        setAddressSpinnersWithUserData();
        updateUserInfoDisplay();
    }

    private void performLogout() {
        // 清除用户 session
        UserSessionManager.getInstance(requireContext()).clearSession();

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
        if (!isEditMode) {
            if (validateInput()) {
                saveUserData();
            } else {
                isEditMode = true;
            }
        }
        updateUIState();
    }

    private void updateUIState() {
        Button btnEdit = rootView.findViewById(R.id.btn_edit);
        btnEdit.setText(isEditMode ? "保存信息" : "编辑信息");

        Button loginOut = rootView.findViewById(R.id.btn_logout);
        loginOut.setVisibility(isEditMode ? View.GONE : View.VISIBLE);

        // 设置地址选择器状态
        addressHelper.setSpinnerEnabled(isEditMode);

        // 强制注册时间不可编辑
        InfoItem createTimeItem = rootView.findViewById(R.id.item_create_time);
        createTimeItem.setEditMode(false);

        // 设置透明度
        float alphaValue = isEditMode ? 1f : 0.8f;
        addressHelper.setSpinnerAlpha(alphaValue);

        // 设置性别和残障类型 Spinner 是否可编辑
        setSpinnerEditable(R.id.spinner_gender, isEditMode);
        setSpinnerEditable(R.id.spinner_disability, isEditMode);

        updateUserInfoDisplay();
    }

    // 控制 Spinner 是否可编辑
    private void setSpinnerEditable(int spinnerId, boolean isEditable) {
        Spinner spinner = rootView.findViewById(spinnerId);
        spinner.setEnabled(isEditable);
    }

    private void updateItemState(int itemId, String value) {
        View itemView = rootView.findViewById(itemId);

        if (itemView instanceof InfoItem) {
            InfoItem item = (InfoItem) itemView;
            if (itemId != R.id.item_create_time) {
                item.setEditMode(isEditMode);
            }
            item.setContent(value);
        } else if (itemView instanceof Spinner) {
            Spinner spinner = (Spinner) itemView;
            // 根据传入的值设置 Spinner 的选中项
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
            Log.d("updateItemState",value);
            int position = adapter.getPosition(value);
            if (position != -1) {
                spinner.setSelection(position);
            }
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
        Spinner spinnerGender = rootView.findViewById(R.id.spinner_gender);
        Spinner spinnerDisability = rootView.findViewById(R.id.spinner_disability);

        // 获取性别和残障类型的选中值
        int selectedGenderPosition = spinnerGender.getSelectedItemPosition();
        int selectedDisabilityPosition = spinnerDisability.getSelectedItemPosition();

        // 设置 gender 和 disability 类型
        currentUser.setGender(getGenderInt(selectedGenderPosition));  // 转换为int类型
        currentUser.setDisabilityType(getDisabilityInt(selectedDisabilityPosition));  // 转换为int类型

        Log.d("gender0",""+spinnerGender.getSelectedItem());
        Log.d("gender1",""+selectedGenderPosition);
        Log.d("disability0",""+spinnerDisability.getSelectedItem());
        Log.d("disability1",""+selectedDisabilityPosition);


        // 其他字段更新
        currentUser.setAge(Integer.parseInt(getItemContent(R.id.item_age)));
        currentUser.setPhone(getItemContent(R.id.item_phone));
        currentUser.setEmail(getItemContent(R.id.item_email));
        currentUser.setAddress(getItemContent(R.id.item_detail_address));

        currentUser.setProvince(addressHelper.getSelectedProvince());
        currentUser.setCity(addressHelper.getSelectedCity());
        currentUser.setCounty(addressHelper.getSelectedDistrict());

        // 保存更新后的数据到会话中
        UserSessionManager.getInstance(requireContext()).saveUser(currentUser);

        // 可以进一步将数据发送到后端进行更新
        UserUpdateManager.getInstance().updateUser(
                requireContext(),
                currentUser,
                new UserUpdateManager.UpdateCallback() {
                    @Override
                    public void onUpdateSuccess() {
                        // 更新成功的回调
                        Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUpdateFailure() {
                        // 更新失败的回调
                        Toast.makeText(getContext(), "保存失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


    private String getItemContent(int itemId) {
        InfoItem item = rootView.findViewById(itemId);
        return item != null ? item.getEditText() : "";
    }

    private void setAddressSpinnersWithUserData() {
        try {
            addressHelper.setSelection(
                    currentUser.getProvince(),
                    currentUser.getCity(),
                    currentUser.getCounty()
            );
        } catch (Exception e) {
            Log.e("UserFragment", "地址设置失败: " + e.getMessage());
        }
    }

    private void updateUserInfoDisplay() {
        updateItemState(R.id.spinner_gender, getGenderString(currentUser.getGender()));
        updateItemState(R.id.item_age, String.valueOf(currentUser.getAge()));
        updateItemState(R.id.item_phone, currentUser.getPhone());
        updateItemState(R.id.item_email, currentUser.getEmail());
        updateItemState(R.id.item_detail_address, currentUser.getAddress());
        updateItemState(R.id.item_create_time, currentUser.getCreatedAt().toLocaleString());
        updateItemState(R.id.spinner_disability, getDisabilityText(currentUser.getDisabilityType()));

        Log.d("updateItemState1",getGenderString(currentUser.getGender()));
        Log.d("updateItemState2",""+currentUser.getGender());
        Log.d("updateItemState3",getDisabilityText(currentUser.getDisabilityType()));
        Log.d("updateItemState4",""+currentUser.getDisabilityType());
        // 设置用户名和 VIP 图标
        TextView tvUsername = rootView.findViewById(R.id.tv_username);
        ImageView ivVip = rootView.findViewById(R.id.iv_vip);
        tvUsername.setText(currentUser.getUsername());
        int vipIconRes = currentUser.getIsVip() != 0 ? R.drawable.ic_vip_yes : R.drawable.ic_vip_no;
        ivVip.setImageResource(vipIconRes);
    }

    private String getGenderString(int gender) {
        switch (gender) {
            case 1: return "男";
            case 2: return "女";
            default: return "未知";
        }
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

    private int getGenderInt(int position) {
        switch (position) {
            case 1: return 1; // 男
            case 2: return 2; // 女
            default: return 0; // 未知
        }
    }

    private int getDisabilityInt(int position) {
        switch (position) {
            case 1: return 1; // 视障
            case 2: return 2; // 听障
            case 3: return 3; // 其他障碍
            default: return 0; // 无
        }
    }
}
