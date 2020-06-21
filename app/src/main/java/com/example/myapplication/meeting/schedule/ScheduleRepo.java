package com.example.myapplication.meeting.schedule;

import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.home.paper.Paper;
import com.example.utils.CommonInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ScheduleRepo {
    private static String URL = "query_program_by_conference";

    private MutableLiveData<List<Schedule>> schedules;
    private int tp;
    private int conference_id;

    ScheduleRepo(int type)
    {
        schedules = new MutableLiveData<>();
        tp = type;
    }

    public void setConference_id(int id) { conference_id = id; }

    MutableLiveData<List<Schedule>> getSchedule() {return schedules;}

    public void updateScheduleList()
    {
        // network request
        okhttp3.Callback cb = new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                String str = response.body().string();
                System.out.println(str);

                try {
                    JSONObject j = new JSONObject(str);

                    if (!j.has("error")) {
                        ArrayList<Schedule> m = new ArrayList<>();

                        JSONArray scheduleList = j.getJSONArray("program_list");
                        if (scheduleList.length() > 0) {
                            for (int i = 0; i < scheduleList.length(); i++) {
                                JSONObject temp = scheduleList.getJSONObject(i);
                                String name = temp.getString("title");
                                String program_type = temp.getString("program_type").
                                        toUpperCase() + " >";
                                int id = temp.getInt("program_id");
                                String organization = temp.getString("organization");
                                String start_date = temp.getString("start_time");
                                String end_date = temp.getString("end_time");

                                m.add(new Schedule(name, organization, program_type,
                                        start_date, end_date, id));
                                System.out.println(id);
                            }
                        }

                        schedules.postValue(m);
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

        JSONObject json = new JSONObject();
        try {
            json.put("conference_id", conference_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CommonInterface.sendOkHttpJsonPostRequest(URL, cb, json);
    }
}
