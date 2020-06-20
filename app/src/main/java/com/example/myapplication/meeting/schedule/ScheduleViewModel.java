package com.example.myapplication.meeting.schedule;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ScheduleViewModel extends ViewModel {
    private MutableLiveData<List<Schedule>> schedules;
    private ScheduleRepo mRepository;

    public ScheduleViewModel()
    {
        super();
        mRepository = new ScheduleRepo(0);
        schedules = mRepository.getSchedule();
    }

    public MutableLiveData<List<Schedule>> getSchedule() { return schedules; }

    public void update() {
        mRepository.updateScheduleList();
    }

    public void setConferenceId(int id) { mRepository.setConference_id(id); }
}
