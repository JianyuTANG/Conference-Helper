package com.example.myapplication.home.paper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

public class PaperRepo {
    private static final String URL_AUTHORSHIP = "query_paper_by_author";
    private static final String URL_CONFERENCE = "query_paper_by_conference";

    private MutableLiveData<List<Paper>> papers;
    private int type;

    private int conferenceId;
    private int programId;
    private String authorName;

    PaperRepo(int type)
    {
        papers = new MutableLiveData<>();
        this.type = type;
    }

    MutableLiveData<List<Paper>> getPapers() {return papers;}

    public void setType(int t) { type = t; }

    public void setConferenceId(int id) { conferenceId = id; }

    public void setAuthorName(String name) { authorName = name; }

    public void setProgramId(int id) { programId = id; }

    public void updatePaperList()
    {
        // network request
        okhttp3.Callback cb = new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                String str = response.body().string();
                System.out.println(str);

                try {
                    JSONObject j = new JSONObject(str);

                    if (!j.has("error")) {
                        ArrayList<Paper> m = new ArrayList<>();

                        JSONArray paperList = j.getJSONArray("paper_list");
                        if (paperList.length() > 0) {
                            for (int i = 0; i < paperList.length(); i++) {
                                JSONObject temp = paperList.getJSONObject(i);
                                String name = temp.getString("title");
                                int id = temp.getInt("paper_id");
                                String author = temp.getString("first_author");
                                m.add(new Paper(name, author, id));
//                                System.out.println(id);
                            }
                        }

                        papers.postValue(m);
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

        JSONObject json = new JSONObject();
        String url;
        switch (type) {
            case 0:
                // 收藏的
                url = URL_CONFERENCE;
                break;
            case 1:
                // 我写的
                url = URL_CONFERENCE;
                break;
            case 2:
                // 会议论文
                url = URL_CONFERENCE;
                try {
                    json.put("conference_id", conferenceId);
                } catch (Exception e) {
                    System.out.println(e);
                }

                break;
            case 3:
                // keynote论文
                url = URL_CONFERENCE;
                break;
            default:
                url = URL_CONFERENCE;
        }
        CommonInterface.sendOkHttpJsonPostRequest(url, cb, json);
    }
}
