package com.example.myapplication.manage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int numTab;
    private int conference_id;

    public PagerAdapter(@NonNull FragmentManager fm, int numTab, int conference_id) {
        super(fm);
        this.numTab = numTab;
        this.conference_id = conference_id;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ProgramListFragment.newInstance(conference_id, "");
            case 1:
                return PaperListFragment.newInstance(conference_id, "");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTab;
    }
}
