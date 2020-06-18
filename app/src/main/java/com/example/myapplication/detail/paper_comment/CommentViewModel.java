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
        mRepository = new CommentRepo(0);
        comments = mRepository.getComments();
    }

    public MutableLiveData<List<Comment>> getComment() { return comments; }

    public void update() {
        mRepository.updateCommentList();
    }
}
