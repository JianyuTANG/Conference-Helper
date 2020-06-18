package com.example.myapplication.home.meeting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MeetingViewModel extends ViewModel {
    private LiveData<List<Meeting>> meetings;
    private MeetingRepo mRepository;

    public MeetingViewModel()
    {
        super();
        mRepository = new MeetingRepo(0);
        meetings = mRepository.getMeetings();
    }

    public LiveData<List<Meeting>> getMeetings() {return meetings;}

    public void update() {
        mRepository.updateMeetingList();
    }
}
