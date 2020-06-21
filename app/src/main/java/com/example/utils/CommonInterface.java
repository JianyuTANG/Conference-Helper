package com.example.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    private static final String server_url = "http://123.56.88.4:1234/";


    private static Request request;

    private static final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    // 请求的Cookie处理
    private static CookieJar cookieJar= new CookieJar() {
        @Override
        public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
            //cookieStore.put(httpUrl.host(), list);
            cookieStore.put(server_url, list);
        }

        @NotNull
        @Override
        public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
            List<Cookie> cookies = cookieStore.get(server_url);
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
        System.out.println(server_url + url);
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
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        for(String key:params.keySet())
        {
            try{
                if(params.get(key).equals("false"))
                    json.put(key, false);
                else if(params.get(key).equals("true"))
                    json.put(key, true);
                else
                    json.put(key, params.get(key));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

        //Log.e("post", server_url + url);
        request = new Request.Builder()
                .url(server_url + url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpFile(String url, okhttp3.Callback callback, File f) throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("avatar", f.getName(), fileBody)
                .build();
        request = new Request.Builder()
                .post(body)
                .url(server_url + url)
                .build();

        okHttpClient.newCall(request).enqueue(callback);

    }

    public static Bitmap getImage(String path){
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(server_url + path).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if(conn.getResponseCode() == 200){
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发起异步post请求 参数直接为json对象JSONObject
     * @param url
     * @param callback
     * @param params
     */
    public static void sendOkHttpJsonPostRequest(
            String url, okhttp3.Callback callback, JSONObject params)
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(params));

        //Log.e("post", server_url + url);
        request = new Request.Builder()
                .url(server_url + url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpJsonFile(
            String url, okhttp3.Callback callback, JSONObject params, File f)
    {
        try{
            System.out.println(params.getString("conference_id"));
            OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
            MediaType mediaType = MediaType.parse("application/json");

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file1", "img", RequestBody.create(MediaType.parse("application/octet-stream"), f))
                    .addFormDataPart("conference_id", params.getString("conference_id"))
                    .build();

            //Log.e("post", server_url + url);
            request = new Request.Builder()
                    .url(server_url + url)
                    .post(requestBody)
                    .addHeader("Content-Type", "application/json")
                    .build();
            okHttpClient.newCall(request).enqueue(callback);
        }
        catch (Exception e){e.printStackTrace();}
    }

}
