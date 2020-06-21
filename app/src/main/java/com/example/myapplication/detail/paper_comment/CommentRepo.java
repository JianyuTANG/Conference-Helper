package com.example.myapplication.detail.paper_comment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.home.meeting.Meeting;
import com.example.utils.CommonInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class CommentRepo {
    private static final String URL = "query_comment";

    private MutableLiveData<List<Comment>> comments;
    private int paper_id;

    CommentRepo()
    {
        comments = new MutableLiveData<>();
    }

    public MutableLiveData<List<Comment>> getComments() { return comments; }

    public void setPaper_id(int id) { paper_id = id; }

    public void updateCommentList()
    {
        // network request
        JSONObject json = new JSONObject();
        try {
            json.put("paper_id", paper_id);
        } catch (Exception e) {
            System.out.println(e);
        }

        okhttp3.Callback cb = new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                String str = response.body().string();
                System.out.println(str);

                try {
                    JSONObject j = new JSONObject(str);

                    if (!j.has("error")) {
                        ArrayList<Comment> m = new ArrayList<>();

                        JSONArray commentList = j.getJSONArray("comments_list");
                        if (commentList.length() > 0) {
                            for (int i = 0; i < commentList.length(); i++) {
                                JSONObject temp = commentList.getJSONObject(i);
                                m.add(new Comment(
                                        temp.getString("user_name"),
                                        temp.getString("content"),
                                        temp.getInt("comment_id")));
                            }
                        }

                        comments.postValue(m);
                    }
                    else {
                        System.out.println("error");
                    }
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
        };

        CommonInterface.sendOkHttpJsonPostRequest(URL, cb, json);
    }
}
