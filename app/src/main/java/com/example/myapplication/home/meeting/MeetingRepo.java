package com.example.myapplication.home.meeting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.utils.CommonInterface;
import com.example.utils.Global;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class MeetingRepo {
    private static final String URL = "query_conference";
    private static final String URL_PAST = "query_past_conference";
    private static final String URL_ADMIN = "query_my_conference";
    private static final String URL_PREFIX = "http://123.56.88.4:1234";

    private int type;
    private MutableLiveData<List<Meeting>> meetings;

    MeetingRepo(int type)
    {
        meetings = new MutableLiveData<>();
        this.type = type;
    }

    public void setType(int t) { type = t; }

    MutableLiveData<List<Meeting>> getMeetings() {return meetings;}

    public void updateMeetingList()
    {
        // network request
        HashMap<String, String> map = new HashMap<>();

        okhttp3.Callback cb = new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                String str = response.body().string();
                System.out.println("conference " + type);
                System.out.println(str);

                try {
                    JSONObject j = new JSONObject(str);

                    if (!j.has("error")) {
                        ArrayList<Meeting> m = new ArrayList<>();

                        JSONArray conferenceList = j.getJSONArray("conference_list");
                        System.out.println(999);
                        if (conferenceList.length() > 0) {
                            for (int i = 0; i < conferenceList.length(); i++) {
                                JSONObject temp = conferenceList.getJSONObject(i);
                                String name = temp.getString("short_name");
                                int id = temp.getInt("conference_id");
                                String image_urls = URL_PREFIX + temp.getString("img_urls");
                                String description = temp.getString("description");
                                m.add(new Meeting(id, name, description, image_urls));
                                System.out.println(id);
                            }
                        }

                        meetings.postValue(m);
                    }
                    else {
                        System.out.println("error");
                    }
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
        };

        String url;
        if (type == 0)
            url = URL;
        else if (type == 1)
            url = URL_PAST;
        else {
            map.put("admin_id", Global.getID());
            url = URL_ADMIN;
        }

        CommonInterface.sendOkHttpPostRequest(url, cb, map);
    }
}
