package com.example.myapplication.home.meeting;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MeetingPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MeetingPagerAdapter(FragmentManager fm, int NumOfTabs) {
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
                return CurMeetingFragment.newInstance("", "");
            case 1:
                return PastMeetingFragment.newInstance("", "");
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
