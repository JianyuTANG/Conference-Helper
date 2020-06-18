package com.example.myapplication.home.meeting;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

public class MeetingRepo {
    private LiveData<List<Meeting>> meetings;

    MeetingRepo(int type)
    {
        meetings = new MutableLiveData<>();
        updateMeetingList();
    }

    LiveData<List<Meeting>> getMeetings() {return meetings;}

    public void updateMeetingList()
    {

    }
}
