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

    private MutableLiveData<List<Meeting>> meetings;

    MeetingRepo(int type)
    {
        meetings = new MutableLiveData<>();
//        updateMeetingList();
    }

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
                                String name = temp.getString("name");
                                int id = temp.getInt("conference_id");
//                                String image_urls = temp.getString("image_urls");
//                                String image_urls = "https://tjy.iterator-traits.com/static/default.jpg";
                                String image_urls = "http://123.56.88.4:1234/media/conference/default.jpg";
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

        CommonInterface.sendOkHttpPostRequest(URL, cb, map);
    }
}
