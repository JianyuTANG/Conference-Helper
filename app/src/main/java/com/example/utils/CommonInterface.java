package com.example.utils;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CommonInterface {

    public static void addViewsListener(Activity activity, int viewId, View.OnClickListener onClickListener) {
        View view = activity.findViewById(viewId);
        view.setOnClickListener(onClickListener);
    }

    public static void addViewsListener(Activity activity, int[] viewIds, View.OnClickListener onClickListener) {
        for (int viewId : viewIds) {
            View view = activity.findViewById(viewId);
            view.setOnClickListener(onClickListener);
        }
    }

    // 以下为网络请求的封装
    private static final String server_url = "http://34.238.156.247:8080";

    private static Request request;

    private static final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    // 请求的Cookie处理
    private static CookieJar cookieJar= new CookieJar() {
        @Override
        public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
            cookieStore.put(httpUrl.host(), list);
        }

        @NotNull
        @Override
        public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
            List<Cookie> cookies = cookieStore.get(httpUrl.host());
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    };

    /**
     * 发起异步get请求
     * @param url
     * @param callback
     */
    public static void sendOkHttpGetRequest(String url, okhttp3.Callback callback)
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();

        request = new Request.Builder().url(server_url + url).build();

        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 发起异步post请求
     * @param url
     * @param callback
     * @param params
     */
    public static void sendOkHttpPostRequest(String url, okhttp3.Callback callback, HashMap<String,String> params)
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
        FormBody.Builder builder = new FormBody.Builder();
        for(String key:params.keySet())
        {
            builder.add(key, params.get(key));
        }
        RequestBody requestBody=builder.build();

        request = new Request.Builder().url(server_url + url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
