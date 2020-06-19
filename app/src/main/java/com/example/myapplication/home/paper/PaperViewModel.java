package com.example.myapplication.home.paper;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class PaperViewModel extends ViewModel {
    private MutableLiveData<List<Paper>> papers;
    private PaperRepo mRepository;

    private int conferenceId;
    private int programId;
    private String authorName;

    public PaperViewModel()
    {
        super();
        mRepository = new PaperRepo(0);
        papers = mRepository.getPapers();
    }

    public MutableLiveData<List<Paper>> getPapers() {return papers;}

    public void update() {
        mRepository.updatePaperList();
    }

    public void setType(int t) { mRepository.setType(t); }

    public void setConferenceId(int id) {
        conferenceId = id;
        mRepository.setConferenceId(id);
    }

    public void setAuthorName(String name) {
        authorName = name;
        mRepository.setAuthorName(name);
    }

    public void setProgramId(int id) {
        programId = id;
        mRepository.setProgramId(id);
    }
}
