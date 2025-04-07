package com.Axonix.usermain;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.view.View;


public class AddressSpinnerHelper {
    private final Context context;
    private final Spinner provinceSpinner;
    private final Spinner citySpinner;
    private final Spinner districtSpinner;
    private final AddressLoadListener listener;

    private Map<String, Map<String, List<String>>> addressData = new HashMap<>();
    private ArrayAdapter<String> provinceAdapter;
    private ArrayAdapter<String> cityAdapter;
    private ArrayAdapter<String> districtAdapter;

    public interface AddressLoadListener {
        void onAddressDataLoaded();
    }

    public AddressSpinnerHelper(Context context, Spinner province, Spinner city, Spinner district,
                                AddressLoadListener listener) {
        this.context = context;
        this.provinceSpinner = province;
        this.citySpinner = city;
        this.districtSpinner = district;
        this.listener = listener;
    }

    public void initAddressSpinners() {
        new Thread(this::loadAddressData).start();
    }

    private void loadAddressData() {
        try (InputStream is = context.getAssets().open("china_address.json")) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Map<String, List<String>>>>() {}.getType();
            addressData = gson.fromJson(new InputStreamReader(is), type);

            ((android.app.Activity) context).runOnUiThread(() -> {
                setupProvinceSpinner();
                setupCitySpinner();
                setupDistrictSpinner();
                if (listener != null) {
                    listener.onAddressDataLoaded();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSpinnerAlpha(0.8f);
    }

    public void setupProvinceSpinner() {
        List<String> provinceNames = new ArrayList<>(addressData.keySet());
        provinceAdapter = new ArrayAdapter<>(context, R.layout.custom_spinner_selected_item, provinceNames);
        provinceAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        provinceSpinner.setAdapter(provinceAdapter);

        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateCitySpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void setupCitySpinner() {
        cityAdapter = new ArrayAdapter<>(context, R.layout.custom_spinner_selected_item, new ArrayList<>());
        cityAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        citySpinner.setAdapter(cityAdapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int provincePos = provinceSpinner.getSelectedItemPosition();
                updateDistrictSpinner(provincePos, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void setupDistrictSpinner() {
        districtAdapter = new ArrayAdapter<>(context, R.layout.custom_spinner_selected_item, new ArrayList<>());
        districtAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        districtSpinner.setAdapter(districtAdapter);
    }

    private void updateCitySpinner(int provincePos) {
        String provinceName = provinceAdapter.getItem(provincePos);
        Map<String, List<String>> citiesMap = addressData.get(provinceName);
        List<String> cities = new ArrayList<>(citiesMap.keySet());

        cityAdapter.clear();
        cityAdapter.addAll(cities);
        cityAdapter.notifyDataSetChanged();

        if (!cities.isEmpty()) {
            updateDistrictSpinner(provincePos, 0);
        }
    }

    private void updateDistrictSpinner(int provincePos, int cityPos) {
        String provinceName = provinceAdapter.getItem(provincePos);
        String cityName = cityAdapter.getItem(cityPos);
        List<String> districts = addressData.get(provinceName).get(cityName);

        districtAdapter.clear();
        districtAdapter.addAll(districts);
        districtAdapter.notifyDataSetChanged();
    }

    public void setSpinnerEnabled(boolean enabled) {
        provinceSpinner.setEnabled(enabled);
        citySpinner.setEnabled(enabled);
        districtSpinner.setEnabled(enabled);
    }

    public String getSelectedProvince() {
        return provinceSpinner.getSelectedItem().toString();
    }

    public String getSelectedCity() {
        return citySpinner.getSelectedItem().toString();
    }

    public String getSelectedDistrict() {
        return districtSpinner.getSelectedItem().toString();
    }

    public void setSpinnerAlpha(float alpha) {
        provinceSpinner.setAlpha(alpha);
        citySpinner.setAlpha(alpha);
        districtSpinner.setAlpha(alpha);
    }

    public void setSelection(String province, String city, String district) {
        int provincePos = provinceAdapter.getPosition(province);
        if (provincePos >= 0) {
            provinceSpinner.setSelection(provincePos);
            updateCitySpinner(provincePos);

            provinceSpinner.post(() -> {
                int cityPos = cityAdapter.getPosition(city);
                if (cityPos >= 0) {
                    citySpinner.setSelection(cityPos);
                    updateDistrictSpinner(provincePos, cityPos);

                    citySpinner.post(() -> {
                        int districtPos = districtAdapter.getPosition(district);
                        if (districtPos >= 0) {
                            districtSpinner.setSelection(districtPos);
                        }
                    });
                }
            });
        }
    }
}