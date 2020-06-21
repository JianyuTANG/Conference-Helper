package com.example.myapplication.detail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.detail.paper_comment.PaperCommentFragment;
import com.example.myapplication.detail.paper_info.PaperInfoFragment;
import com.example.myapplication.detail.schedule_info.ScheduleInfoFragment;
import com.example.myapplication.detail.schedule_paper_list.SchedulePaperListFragment;
import com.example.myapplication.meeting.paper.MeetingPaper;
import com.example.utils.Global;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int type;
    private int mNumOfTabs;
    private int id;

    // paper
    private ArrayList<String> authors = null;
    private String abs = null;

    // schedule
    private String lecturer = null;
    private String time = null;
    private String place = null;
    private String detail = null;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, int type, int id) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.type = type;
        this.id = id;
    }

//    public void setPaper(ArrayList<String> authors, String abs) {
//        this.authors = authors;
//        this.abs = abs;
//    }
//
//    public void setSchedule(String lecturer, String time, String place, String detail) {
//        this.detail = detail;
//        this.time = time;
//        this.place = place;
//    }
//
//    public void setType(int type) { this.type = type; }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (type == 0) {
            switch (position) {
                case 0:
                    return ScheduleInfoFragment.newInstance(id, "");
                case 1:
                    return SchedulePaperListFragment.newInstance(id, "");
            }
        }
        else if (type == 1) {
            switch (position) {
                case 0:
                    return PaperInfoFragment.newInstance(id, Integer.parseInt(Global.getID()));
                case 1:
                    return PaperCommentFragment.newInstance(id, "");
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
