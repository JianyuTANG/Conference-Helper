package com.example.myapplication.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabItem;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_TYPE = "com.example.myapplication.detail.extra_type";
    public static final String EXTRA_TITLE = "com.example.myapplication.detail.extra_title";
    public static final String EXTRA_ID = "com.example.myapplication.detail.extra_id";
    public static final String EXTRA_P_TYPE = "com.example.myapplication.detail.extra_p_type";

    private int type;
    private int id;
    private String title;
    private String program_type;

    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Bundle bundle;
        if (intent != null)
        {
            bundle = intent.getExtras();
            type = bundle.getInt(EXTRA_TYPE);
            id = bundle.getInt(EXTRA_ID);
            title = bundle.getString(EXTRA_TITLE);
            if (type == 0)
                program_type = bundle.getString(EXTRA_P_TYPE);
        }
        adapter = new PagerAdapter(getSupportFragmentManager(), 2, type, id);
        initUI();
    }

    private void initUI() {
        final Toolbar myToolbar = findViewById(R.id.my_toolbar_detail_activity);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.detail_activity_tab_layout);
        final ViewPager viewPager = findViewById(R.id.detail_activity_pager);
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

        TextView mTextView = findViewById(R.id.detail_activity_title);
        mTextView.setText(title);

        if (type == 0) {
            // keynote信息
            setTitle(program_type);
            mTabLayout.getTabAt(1).setText(R.string.tab_label_detail_paper);
        }
        else if (type == 1) {
            // 论文信息
            setTitle(R.string.detail_activity_header_paper);
            mTabLayout.getTabAt(1).setText(R.string.tab_label_detail_comment);
        }
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
}
