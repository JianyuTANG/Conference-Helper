package com.example.myapplication.home;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.home.meeting.Meeting;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private ChatFragment cf;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
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
                return MeetingFragment.newInstance("", "");
            case 1:
                return PaperFragment.newInstance("", "");
            case 2:
                cf = ChatFragment.newInstance("", "");
                return cf;
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

    public ChatFragment getChatFragment(){return cf;}
}