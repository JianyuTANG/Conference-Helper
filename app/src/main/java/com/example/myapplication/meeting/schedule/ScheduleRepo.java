package com.example.myapplication.meeting.schedule;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ScheduleRepo {
    private MutableLiveData<List<Schedule>> schedules;
    private int tp;

    ScheduleRepo(int type)
    {
        schedules = new MutableLiveData<>();
        tp = type;
        updateScheduleList();
    }

    MutableLiveData<List<Schedule>> getSchedule() {return schedules;}

    public void updateScheduleList()
    {

    }
}
