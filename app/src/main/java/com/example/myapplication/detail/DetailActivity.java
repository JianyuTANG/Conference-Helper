package com.example.myapplication.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabItem;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_TYPE = "com.example.myapplication.detail.extra_type";

    public static final String EXTRA_AUTHORS = "com.example.myapplication.detail.extra_authors";
    public static final String EXTRA_ABSTRACT = "com.example.myapplication.detail.extra_abstract";

    private int type;
    private String title;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // set adapter
        adapter = new PagerAdapter(getSupportFragmentManager(), 2, type);
        Intent intent = getIntent();
        Bundle bundle;
        if (intent != null)
        {
            bundle = intent.getExtras();
            type = bundle.getInt(EXTRA_TYPE);
            if (type == 0) {

            }
            else if (type == 1) {
                adapter.setPaper(
                        bundle.getStringArrayList(EXTRA_AUTHORS),
                        bundle.getString(EXTRA_ABSTRACT)
                );
            }
        }

        initUI();
    }

    private void initUI() {
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_detail_activity);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ViewPager viewPager = findViewById(R.id.detail_activity_pager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.detail_activity_tab_layout);

        TextView mTextView = (TextView) findViewById(R.id.detail_activity_title);
        mTextView.setText(title);

        if (type == 0) {
            // keynote信息
            setTitle(R.string.detail_activity_header_schedule);
            mTabLayout.getTabAt(1).setText(R.string.tab_label_detail_paper);
        }
        else if (type == 1) {
            // 论文信息
            setTitle(R.string.detail_activity_header_paper);
            mTabLayout.getTabAt(1).setText(R.string.tab_label_detail_comment);
        }
    }
}
