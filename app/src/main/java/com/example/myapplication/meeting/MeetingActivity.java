package com.example.myapplication.meeting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.myapplication.R;


public class MeetingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        initUI();
    }

    private void initUI() {
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("会议信息");

        final ViewPager viewPager = findViewById(R.id.meeting_activity_pager);
        viewPager.setOffscreenPageLimit(3);
        final PagerAdapter adapter = new PagerAdapter (getSupportFragmentManager(), 3);
        viewPager.setAdapter(adapter);
    }
}
