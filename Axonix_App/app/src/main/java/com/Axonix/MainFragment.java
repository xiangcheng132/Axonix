package com.Axonix;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.Axonix.adapter.ContactAdapter;
import com.Axonix.index.dto.EmergencyContactDto;
import com.Axonix.index.config.NetworkClient;
import com.Axonix.index.config.NetworkTimeClient;
import com.Axonix.index.model.User;
import com.Axonix.index.session.UserSessionManager;
import com.Axonix.index.model.EmergencyContact;
import com.Axonix.index.model.EmergencyContactExample;
import com.Axonix.index.model.EmergencyContactTable;
import com.Axonix.usermain.LoginFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.reflect.TypeToken;



@Route(path = "/index/main")
public class MainFragment extends Fragment {

    private RecyclerView rvContacts;
    private View view;
    private OkHttpClient httpClient;
    private FloatingActionButton addContacter;
    private String EMERGENCY_CONTACT_URL,ALL_USER_URL,ADD_EMERGENCY_CONTANCT_URL,DELETE_EMERGENCY;
    private MaterialButton addBtu;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化 URL 和网络客户端
        EMERGENCY_CONTACT_URL = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/emergency-contact/select_By_userId";
        ALL_USER_URL = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/users/list";
        ADD_EMERGENCY_CONTANCT_URL = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/emergency-contact/insert";
        DELETE_EMERGENCY = requireContext().getResources().getString(com.Axonix.index.R.string.Base_url) + "/api/emergency-contact/delete/by-example";

        httpClient = NetworkClient.INSTANCE.getClient();

        butInit();               // 按钮功能初始化
        setupContacts();         // 初始化联系人列表
        addEmergencyContact();   // 添加联系人功能
    }

    private void butInit(){
        view.findViewById(R.id.btn_ai).setOnClickListener(v -> {
            Fragment aiFragment = new AIAssistantFragment();
            navigateToFragment(aiFragment, "ai");
        });

        view.findViewById(R.id.btn_traffic).setOnClickListener(v -> {
            Fragment trafficFragment = new TrafficRecognitionFragment();
            navigateToFragment(trafficFragment, "traffic");
        });

        view.findViewById(R.id.btn_communicate).setOnClickListener(v -> {
            Fragment communicateFragment = new AccessibilityCommunicationFragment();
            navigateToFragment(communicateFragment, "communicate");
        });
    }


    private void setupContacts() {
        rvContacts = view.findViewById(R.id.rv_contacts);
        List<EmergencyContact> contacts = new ArrayList<>();
        User currentUser = UserSessionManager.getInstance(requireContext()).getUser();
        if (currentUser == null){
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(R.id.container, new LoginFragment());
            transaction.addToBackStack(null); // 可选
            transaction.commit();
            return;
        }
        Integer userId = UserSessionManager.getInstance(requireContext()).getUser().getId();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userId);
        String jsonUser = jsonObject.toString();
        RequestBody body = RequestBody.create(jsonUser, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(EMERGENCY_CONTACT_URL)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MainFragment", "获取紧急联系人失败", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e("MainFragment", "获取紧急联系人失败，状态码：" + response.code());
                    return;
                }
                Gson gson = new Gson();
                Type listType = new TypeToken<List<EmergencyContactDto>>() {}.getType();
                List<EmergencyContactDto> responseList = gson.fromJson(response.body().string(), listType);

                for (EmergencyContactDto item : responseList) {
                    contacts.add(new EmergencyContact(item.getContactUsername(), item.getContactPhone(), item.getRelationship()));
                }
                if (isAdded()) {
                    getActivity().runOnUiThread(() -> {
                        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
                        ContactAdapter adapter = new ContactAdapter(contacts, new ContactAdapter.OnContactClickListener() {
                            @Override
                            public void onDeleteClick(EmergencyContact contact) {

                                EmergencyContactExample example = new EmergencyContactExample();
                                EmergencyContactExample.Criteria criteria = example.createCriteria();

                                int userId = UserSessionManager.getInstance(requireContext()).getUser().getId();
                                String phone = contact.getPhone().replaceAll("[^0-9]", "");
                                String relationship = contact.getRelationship().trim();

                                criteria.andUserIdEqualTo(userId);
                                criteria.andContactPhoneEqualTo(phone);
                                criteria.andRelationshipEqualTo(relationship);

                                Gson gson3 = NetworkTimeClient.getGson();
                                String json3 = gson3.toJson(example);
                                RequestBody body3 = RequestBody.create(json3, MediaType.get("application/json; charset=utf-8"));

                                Request request3 = new Request.Builder()
                                        .url(DELETE_EMERGENCY)  // 你的后端插入接口地址
                                        .delete(body3)
                                        .build();

                                httpClient.newCall(request3).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.e("删除紧急联系人", "失败", e);
                                        requireActivity().runOnUiThread(() ->
                                                Toast.makeText(getContext(), "删除失败，请检查网络", Toast.LENGTH_SHORT).show());
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        if (response.isSuccessful()) {
                                            requireActivity().runOnUiThread(() -> {
                                                Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                                setupContacts();
                                            });
                                        } else {
                                            requireActivity().runOnUiThread(() ->
                                                    Toast.makeText(getContext(), "删除失败，状态码：" + response.code(), Toast.LENGTH_SHORT).show());
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCallClick(EmergencyContact contact) {

                                String rawNumber = contact.getPhone().replaceAll("[^0-9]", "");
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + rawNumber));
                                startActivity(intent);
                            }
                        });
                        rvContacts.setAdapter(adapter);
                    });
                } else {
                    Log.w("MainFragment", "Fragment 已经被销毁，跳过 UI 更新");
                }

            }
        });

    }

    private void navigateToFragment(Fragment targetFragment, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
        );
        transaction.replace(com.Axonix.index.R.id.content_frame, targetFragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    private void addEmergencyContact() {
        addContacter = view.findViewById(R.id.fab_add_contact);
        addContacter.setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_contact, null);
            AutoCompleteTextView actContact = dialogView.findViewById(R.id.act_contact);
            EditText etRelation = dialogView.findViewById(R.id.et_relation);
            MaterialButton btnAdd = dialogView.findViewById(R.id.btn_add_contact);

            List<String> contactDisplayList = new ArrayList<>();
            Map<String, Map<String, Object>> contactDataMap = new HashMap<>();

            RequestBody body = RequestBody.create("{}", MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(ALL_USER_URL)
                    .post(body)
                    .build();

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("MainFragment", "加载联系人数据失败", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e("MainFragment", "加载联系人数据失败，状态码：" + response.code());
                        return;
                    }
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<User>>() {}.getType();
                    List<User> responseList = gson.fromJson(response.body().string(), listType);

                    for (User item : responseList) {
                        String displayText = item.getUsername() + " - " + item.getPhone();
                        contactDisplayList.add(displayText);

                        Map<String, Object> data = new HashMap<>();
                        data.put("userId", item.getId());
                        data.put("phone", item.getPhone());
                        contactDataMap.put(displayText, data);
                    }

                    requireActivity().runOnUiThread(() -> {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                                android.R.layout.simple_dropdown_item_1line, contactDisplayList);
                        actContact.setAdapter(adapter);
                        actContact.setThreshold(1);
                    });
                }
            });

            final int[] selectedUserId = {-1};
            final String[] selectedPhone = {""};

            actContact.setOnItemClickListener((parent, view1, position, id) -> {
                String selectedText = (String) parent.getItemAtPosition(position);
                Map<String, Object> data = contactDataMap.get(selectedText);
                if (data != null) {
                    selectedUserId[0] = (int) data.get("userId");
                    selectedPhone[0] = (String) data.get("phone");
                    Log.d("选中联系人", "ID: " + selectedUserId[0] + ", Phone: " + selectedPhone[0]);
                }
            });

            AlertDialog dialog = new AlertDialog.Builder(requireContext())
                    .setView(dialogView)
                    .setCancelable(true)
                    .create();

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

            btnAdd.setOnClickListener(v1 -> {
                if (selectedUserId[0] == -1) {
                    Toast.makeText(getContext(), "请先选择一个联系人", Toast.LENGTH_SHORT).show();
                    return;
                }

                String relation = etRelation.getText().toString().trim();
                if (relation.isEmpty()) {
                    Toast.makeText(getContext(), "请输入关系", Toast.LENGTH_SHORT).show();
                    return;
                }

                EmergencyContactTable emergencyContactTable = new EmergencyContactTable(selectedUserId[0],relation,selectedPhone[0],UserSessionManager.getInstance(requireContext()).getUser().getId());

                Gson gson = NetworkTimeClient.getGson();
                String json = gson.toJson(emergencyContactTable);
                RequestBody body2 = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

                Request request2 = new Request.Builder()
                        .url(ADD_EMERGENCY_CONTANCT_URL)  // 你的后端插入接口地址
                        .post(body2)
                        .build();

                httpClient.newCall(request2).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("添加紧急联系人", "失败", e);
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "添加失败，请检查网络", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "添加成功", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                setupContacts();
                            });
                        } else {
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(getContext(), "添加失败，状态码：" + response.code(), Toast.LENGTH_SHORT).show());
                        }
                    }
                });

            });
            actContact.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!contactDataMap.containsKey(s.toString())) {
                        selectedUserId[0] = -1;
                        selectedPhone[0] = "";
                        Log.d("联系人输入框", "内容被修改或清空，已重置选择");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

        });
    }


}