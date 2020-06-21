package com.example.myapplication.home.meeting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MeetingViewModel extends ViewModel {
    private MutableLiveData<List<Meeting>> meetings;
    private MeetingRepo mRepository;

    public MeetingViewModel()
    {
        super();
        mRepository = new MeetingRepo(0);
        meetings = mRepository.getMeetings();
    }

    public void setType(int t) { mRepository.setType(t); }

    public MutableLiveData<List<Meeting>> getMeetings() {return meetings;}

    public void update() {
        mRepository.updateMeetingList();
    }
}
