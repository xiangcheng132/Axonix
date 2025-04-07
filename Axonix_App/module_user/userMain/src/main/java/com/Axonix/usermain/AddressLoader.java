package com.Axonix.usermain;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.List;

public class AddressLoader {
    public static Map<String, Map<String, List<String>>> loadAddressData(Context context) {
        try {
            InputStream is = context.getAssets().open("china_address.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Type type = new TypeToken<Map<String, Map<String, List<String>>>>() {}.getType();
            return new Gson().fromJson(sb.toString(), type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
