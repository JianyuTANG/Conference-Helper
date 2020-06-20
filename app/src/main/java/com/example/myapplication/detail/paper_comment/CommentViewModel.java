package com.example.myapplication.detail.paper_comment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CommentViewModel extends ViewModel{
    private MutableLiveData<List<Comment>> comments;
    private CommentRepo mRepository;

    public CommentViewModel()
    {
        super();
        mRepository = new CommentRepo();
        comments = mRepository.getComments();
    }

    public MutableLiveData<List<Comment>> getComment() { return comments; }

    public void setPaperId(int id) { mRepository.setPaper_id(id); }

    public void update() {
        mRepository.updateCommentList();
    }
}
