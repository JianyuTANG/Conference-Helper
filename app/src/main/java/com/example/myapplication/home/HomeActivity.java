package com.example.myapplication.home;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.myapplication.R;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;


public class HomeActivity extends AppCompatActivity {
    private NavigationTabBar navigationTabBar;
    private ArrayAdapter arrayAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);


        initUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);

        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        return super.onCreateOptionsMenu(menu);
    }

    private void initUI()
    {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, myToolbar,
                R.string.toggle_open, R.string.toggle_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        ListView leftMenu = findViewById(R.id.lv_left_menu);
        leftMenu.setAdapter(arrayAdapter);

        setTitle("会议");

        final ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(3);

        final PagerAdapter adapter = new PagerAdapter (getSupportFragmentManager(), 3);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setTitle("会议");
                        break;
                    case 1:
                        setTitle("论文");
                        break;
                    case 2:
                        setTitle("私信");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb);

        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getDrawable(R.drawable.ic_meeting),
                        Color.BLUE
                        //Color.parseColor("black")
                ).build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getDrawable(R.drawable.ic_paper),
                        Color.BLUE
                        //Color.parseColor("black")
                ).build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getDrawable(R.drawable.ic_chat),
                        Color.BLUE
                        //Color.parseColor("black")
                ).build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);

        navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
        navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
        navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        //navigationTabBar.setTypeface("fonts/custom_font.ttf");
        navigationTabBar.setIsBadged(false);
        navigationTabBar.setIsTitled(false);
        navigationTabBar.setIsTinted(false);
        navigationTabBar.setIsBadgeUseTypeface(false);
        navigationTabBar.setBadgeBgColor(Color.RED);
        navigationTabBar.setBadgeTitleColor(Color.GRAY);
        navigationTabBar.setIsSwiped(true);
        navigationTabBar.setBgColor(Color.GRAY);
        navigationTabBar.setBadgeSize(10);
        navigationTabBar.setTitleSize(10);
        navigationTabBar.setIconSizeFraction((float) 0.5);
        //navigationTabBar.setBehaviorEnabled(true);
    }
}
