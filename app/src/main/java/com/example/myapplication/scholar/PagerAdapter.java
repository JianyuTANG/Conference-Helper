package com.example.myapplication.scholar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myapplication.detail.schedule_info.ScheduleInfoFragment;
import com.example.myapplication.scholar.info.ScholarInfoFragment;
import com.example.myapplication.scholar.paper.ScholarPaperFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private int id;
    private String name;

    public PagerAdapter(FragmentManager fm, int NumOfTabs, int id, String name) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.id = id;
        this.name = name;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ScholarInfoFragment.newInstance(name, id);
            case 1:
                return ScholarPaperFragment.newInstance(id, name);
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
