package com.Axonix.index.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class NetworkTimeClient {
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            .create();

    public static Gson getGson() {
        return gson;
    }
}