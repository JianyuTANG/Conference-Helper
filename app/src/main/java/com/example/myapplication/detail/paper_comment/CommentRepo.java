package com.example.myapplication.detail.paper_comment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class CommentRepo {
    private MutableLiveData<List<Comment>> comments;

    CommentRepo(int type)
    {
        comments = new MutableLiveData<>();
        updateCommentList();
    }

    MutableLiveData<List<Comment>> getComments() { return comments; }

    public void updateCommentList()
    {

    }
}
