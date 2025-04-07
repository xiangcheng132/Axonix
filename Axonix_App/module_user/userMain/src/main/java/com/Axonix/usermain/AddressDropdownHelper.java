package com.Axonix.usermain;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;

public class AddressDropdownHelper {
    private final Context context;
    private final AutoCompleteTextView provinceView;
    private final AutoCompleteTextView cityView;
    private final AutoCompleteTextView districtView;

    private final Map<String, Map<String, List<String>>> addressData = new LinkedHashMap<>();
    private final List<String> provinceList = new ArrayList<>();
    private final Map<String, List<String>> cityMap = new HashMap<>();
    private final Map<String, List<String>> districtMap = new HashMap<>();

    public AddressDropdownHelper(Context context,
                                 AutoCompleteTextView provinceView,
                                 AutoCompleteTextView cityView,
                                 AutoCompleteTextView districtView) {
        this.context = context;
        this.provinceView = provinceView;
        this.cityView = cityView;
        this.districtView = districtView;
        loadAddressData();
    }

    private void loadAddressData() {
        try (InputStream is = context.getAssets().open("china_address.json")) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Map<String, List<String>>>>() {}.getType();
            Map<String, Map<String, List<String>>> jsonData = gson.fromJson(new InputStreamReader(is), type);

            addressData.clear();
            addressData.putAll(jsonData);

            provinceList.clear();
            provinceList.addAll(addressData.keySet());

            // 设置省适配器
            ArrayAdapter<String> provinceAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_list_item_1, provinceList);
            provinceView.setAdapter(provinceAdapter);

            // 省份选择监听，填充城市
            provinceView.setOnItemClickListener((parent, view, position, id) -> {
                String selectedProvince = provinceList.get(position);
                List<String> cities = new ArrayList<>(addressData.get(selectedProvince).keySet());
                cityMap.put(selectedProvince, cities);

                ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_list_item_1, cities);
                cityView.setAdapter(cityAdapter);
                cityView.setText("", false); // 清空城市
                districtView.setText("", false); // 清空区域
            });

            // 城市选择监听，填充分区
            cityView.setOnItemClickListener((parent, view, position, id) -> {
                String selectedProvince = provinceView.getText().toString();
                String selectedCity = cityMap.get(selectedProvince).get(position);
                List<String> districts = addressData.get(selectedProvince).get(selectedCity);
                districtMap.put(selectedCity, districts);

                ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(context,
                        android.R.layout.simple_list_item_1, districts);
                districtView.setAdapter(districtAdapter);
                districtView.setText("", false); // 清空区域
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getProvinceList() {
        return provinceList;
    }

    public List<String> getCityList(String province) {
        return new ArrayList<>(addressData.getOrDefault(province, Collections.emptyMap()).keySet());
    }

    public List<String> getDistrictList(String province, String city) {
        return addressData.getOrDefault(province, Collections.emptyMap()).getOrDefault(city, Collections.emptyList());
    }

    public void setSelection(String province, String city, String district) {
        provinceView.setText(province, false);

        List<String> cities = getCityList(province);
        cityView.setAdapter(new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, cities));
        cityView.setText(city, false);

        List<String> districts = getDistrictList(province, city);
        districtView.setAdapter(new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, districts));
        districtView.setText(district, false);
    }

    public String getSelectedProvince() {
        return provinceView.getText().toString();
    }

    public String getSelectedCity() {
        return cityView.getText().toString();
    }

    public String getSelectedDistrict() {
        return districtView.getText().toString();
    }
}
