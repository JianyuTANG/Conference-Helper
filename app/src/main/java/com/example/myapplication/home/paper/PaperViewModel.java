package com.example.myapplication.home.paper;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class PaperViewModel extends ViewModel {
    private MutableLiveData<List<Paper>> papers;
    private PaperRepo mRepository;

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
}
