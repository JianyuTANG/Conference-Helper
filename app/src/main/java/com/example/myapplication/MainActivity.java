package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.utils.CommonInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Callback;
import okhttp3.Call;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HashMap<String, String> map = new HashMap<>();
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("nickname", "王世杰");
        map2.put("password", "751178946");
        map2.put("email", "wang98thu@gmail.com");

        map.put("nickname", "robin");
        map.put("password", "123456");
        map.put("email", "1@163.com");
        //map.put("admin", "false");
        String TAG = "LoginActivity";
        Log.e(TAG, "start!");

        String url = "login";
        String urlout = "logout";
        String urlon = "logon";
        okhttp3.Callback cb = new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e){

                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "Success");
                String str = response.body().string();
                System.out.println(str);
            }
        };

        CommonInterface.sendOkHttpPostRequest(urlon, cb, map);
        try{
            Thread.currentThread().sleep(2000);
        }
        catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        CommonInterface.sendOkHttpPostRequest(urlon, cb, map);


        //CommonInterface.sendOkHttpPostRequest(urlout, cb, map);
        //CommonInterface.sendOkHttpPostRequest(url, cb, map2);
    }
}
