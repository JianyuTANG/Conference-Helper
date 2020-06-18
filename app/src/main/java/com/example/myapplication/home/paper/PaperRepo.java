package com.example.myapplication.home.paper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class PaperRepo {
    private MutableLiveData<List<Paper>> papers;

    PaperRepo(int type)
    {
        papers = new MutableLiveData<>();
        updatePaperList();
    }

    MutableLiveData<List<Paper>> getPapers() {return papers;}

    public void updatePaperList()
    {

    }
}
