package com.example.myapplication.meeting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;


public class MeetingActivity extends AppCompatActivity {
    public static final String EXTRA_MEETING_ID = "com.example.myapplication.meeting.id";

    private int conference_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            conference_id = bundle.getInt(EXTRA_MEETING_ID);
        }

        initUI();
    }

    private void initUI() {
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_meeting_activity);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("会议信息");

        final ViewPager viewPager = findViewById(R.id.meeting_activity_pager);
        viewPager.setOffscreenPageLimit(3);
        final PagerAdapter adapter = new PagerAdapter(
                getSupportFragmentManager(), 3, conference_id);
        viewPager.setAdapter(adapter);

        final TabLayout tabLayout = findViewById(R.id.meeting_activity_tab_layout);
        viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(
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
