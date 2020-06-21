package com.example.myapplication.meeting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.tabs.TabLayout;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.List;


public class MeetingActivity extends AppCompatActivity {
    public static final String EXTRA_MEETING_ID = "com.example.myapplication.meeting.id";

    private int conference_id;

    private MZBannerView mMZBannerView;

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

        mMZBannerView = findViewById(R.id.meeting_banner);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    public void setBanner(List<String> urls){
        mMZBannerView.addPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mMZBannerView.setPages(urls, new MZHolderCreator<MeetingBannerViewHolder>() {
            @Override
            public MeetingBannerViewHolder createViewHolder() {
                return new MeetingBannerViewHolder();
            }
        });

        mMZBannerView.start();

    }

    public static class MeetingBannerViewHolder implements MZViewHolder<String> {
        private SimpleDraweeView mImageView;
        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.component_banner_item,null);
            mImageView = view.findViewById(R.id.meeting_banner_item);
            return view;
        }

        @Override
        public void onBind(Context context, int position, String data) {
            Uri uri = Uri.parse(data);
            mImageView.setImageURI(uri);
        }
    }
}
