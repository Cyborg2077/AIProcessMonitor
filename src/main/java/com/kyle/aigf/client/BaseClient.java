package com.kyle.aigf.client;

import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class BaseClient {

    private final OkHttpClient okHttpClient;

    public BaseClient() {
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
    }

    public OkHttpClient getClient() {
        return okHttpClient;
    }
}
