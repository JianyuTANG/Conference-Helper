package com.example.myapplication.meeting;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.meeting.info.MeetingInfo;
import com.example.myapplication.meeting.paper.MeetingPaper;
import com.example.myapplication.meeting.schedule.MeetingSchedule;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private int conference_id;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, int id) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.conference_id = id;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        //To do
        //return the corresponded fragment according to position
        //remember that the position can not be out of [0, 2]

        switch (position) {
            case 0:
                return MeetingInfo.newInstance(conference_id, "");
            case 1:
                return MeetingSchedule.newInstance(conference_id, "");
            case 2:
                return MeetingPaper.newInstance(conference_id, "");
            default:
                return null;
        }

        //To do closed
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}