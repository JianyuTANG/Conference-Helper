package com.example.myapplication.manage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;

public class MeetingDetailActivity extends AppCompatActivity {
    private int conference_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null)
            conference_id = intent.getIntExtra("id", 0);

        PagerAdapter adapter = new PagerAdapter(
                getSupportFragmentManager(),
                2,
                conference_id);

        final Toolbar myToolbar = findViewById(R.id.my_toolbar_manage_detail_activity);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("会议议程与论文管理");

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.detail_manage_activity_tab_layout);
        final ViewPager viewPager = findViewById(R.id.detail_manage_activity_pager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {}

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {}
                });


    }
}