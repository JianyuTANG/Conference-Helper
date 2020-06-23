package com.example.myapplication.home;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.InfoActivity;
import com.example.myapplication.InviteActivity;
import com.example.myapplication.ModifyPwdActivity;
import com.example.myapplication.R;
import com.example.myapplication.SignInActivity;
import com.example.myapplication.SignUpActivity;
import com.example.myapplication.admin.AddConferenceActivity;
import com.example.myapplication.detail.DetailActivity;
import com.example.myapplication.home.search.SearchListAdapter;
import com.example.myapplication.home.search.SearchResult;
import com.example.myapplication.manage.MeetingMaintainActivity;
import com.example.myapplication.meeting.MeetingActivity;
import com.example.myapplication.scholar.ScholarActivity;
import com.example.utils.CommonInterface;
import com.example.utils.Global;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import devlight.io.library.ntb.NavigationTabBar;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static com.example.myapplication.detail.DetailActivity.EXTRA_ID;
import static com.example.myapplication.detail.DetailActivity.EXTRA_TITLE;
import static com.example.myapplication.detail.DetailActivity.EXTRA_TYPE;
import static com.example.myapplication.meeting.MeetingActivity.EXTRA_MEETING_ID;
import static com.example.myapplication.scholar.ScholarActivity.EXTRA_NAME;


public class HomeActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener{
    private static final String URL_SEARCH_MEETING = "search_conference";
    private static final String URL_SEARCH_PAPER = "search_paper";
    private static final String URL_SEARCH_PERSON = "search_user";
    private static final String URL_PREFIX = "http://123.56.88.4:1234";


    private NavigationTabBar navigationTabBar;
    private ArrayAdapter arrayAdapter;
    private SearchView mSearchView;
    private RecyclerView mLv;
    private SearchListAdapter mSearchListAdapter;

    private int mCurTag;
    private boolean isExit = false;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            isExit=false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mCurTag = 0;
        mLv = findViewById(R.id.lv);
        mLv.setLayoutManager(new LinearLayoutManager(this));
        mLv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mSearchListAdapter = new SearchListAdapter(this);
        mSearchListAdapter.setOnItemClickListener(new SearchListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SearchResult sr = mSearchListAdapter.getResByPos(position);
                int s_id = sr.id;
                String s_name = sr.name;

                Intent it;
                Bundle bundle = new Bundle();
                switch (mCurTag) {
                    case 0:
                        it = new Intent(HomeActivity.this, MeetingActivity.class);
                        bundle.putInt(EXTRA_MEETING_ID, s_id);
                        it.putExtras(bundle);
                        break;
                    case 1:
                        it = new Intent(HomeActivity.this, DetailActivity.class);
                        bundle.putInt(EXTRA_TYPE, 1);
                        bundle.putInt(EXTRA_ID, s_id);
                        bundle.putString(EXTRA_TITLE, s_name);
                        it.putExtras(bundle);
                        break;
                    case 2:
                        it = new Intent(HomeActivity.this, ScholarActivity.class);
                        bundle.putInt(ScholarActivity.EXTRA_ID, s_id);
                        bundle.putString(EXTRA_NAME, s_name);
                        it.putExtras(bundle);
                        break;
                    default:
                        it =new Intent();
                }
                startActivity(it);
            }
        });
        mLv.setAdapter(mSearchListAdapter);

        initUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);

        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    private void initUI()
    {
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, myToolbar,
                R.string.toggle_open, R.string.toggle_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if(Global.getNickname().equals("admin")){
            String[] lvs = {"我的信息", "修改密码", "新增会议","管理我发布的会议" , "邀请管理员", "退出登录"};
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        }
        else if(Global.getIfadmin()){
            String[] lvs = {"我的信息", "修改密码", "新增会议","管理我发布的会议" , "", "退出登录"};
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        }
        else{
            String[] lvs = {"我的信息", "修改密码", "", "", "", "退出登录"};
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        }
        ListView leftMenu = findViewById(R.id.lv_left_menu);
        leftMenu.setAdapter(arrayAdapter);
        leftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
                    startActivity(intent);
                }
                else if(position == 1){
                    Intent intent = new Intent(HomeActivity.this, ModifyPwdActivity.class);
                    startActivity(intent);
                }
                else if(position == 2){
                    if(Global.getIfadmin()){
                        Intent intent = new Intent(HomeActivity.this, AddConferenceActivity.class);
                        startActivity(intent);
                    }
                }
                else if(position == 3){
                    if(Global.getIfadmin()){
                        Intent intent = new Intent(HomeActivity.this, MeetingMaintainActivity.class);
                        startActivity(intent);
                    }
                }
                else if(position == 4 && Global.getNickname().equals("admin")){
                    Intent intent = new Intent(HomeActivity.this, InviteActivity.class);
                    startActivity(intent);
                }
                else if(position == 5){
                    String logout_url = "logout";
                    HomeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            builder.setTitle("退出当前账号");
                            builder.setMessage("确定要退出么");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    okhttp3.Callback cb = new okhttp3.Callback(){
                                        @Override
                                        public void onFailure(Call call, IOException e){
                                            HomeActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                                                    builder.setTitle("退出失败");
                                                    builder.setMessage("请检查您的网络连接");
                                                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                        }
                                                    });
                                                    builder.show();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            String str = response.body().string();
                                            System.out.println(str);
                                            Global.setID(null);
                                            Global.setNickname(null);
                                            Global.setConference_name(null);
                                            Global.setConference_id(null);
                                            Global.setAvatar(null);
                                            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                                            intent.putExtra("logout", "true");
                                            startActivity(intent);
                                            finish();
                                        }
                                    };
                                    CommonInterface.sendOkHttpPostRequest(logout_url, cb, new HashMap<>());
                                }
                            });
                            builder.show();
                        }
                    });
                }
            }
        });
        ((TextView) findViewById(R.id.home_drawer_name)).setText(Global.getNickname());
        Uri avatar_uri = Uri.parse(URL_PREFIX + Global.getAvatar_url());
        System.out.println(Global.getAvatar_url());
        ((SimpleDraweeView) findViewById((R.id.home_drawer_image))).setImageURI(avatar_uri);

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
                        mCurTag = 0;
                        break;
                    case 1:
                        setTitle("论文");
                        mCurTag = 1;
                        break;
                    case 2:
                        setTitle("私信");
                        mCurTag = 2;
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
//                        getDrawable(R.drawable.ic_meeting),
                        getDrawable(R.drawable.ic_meeting_gray),
//                        Color.BLUE
                        Color.parseColor("#B0C4DE")
                ).build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
//                        getDrawable(R.drawable.ic_paper),
                        getDrawable(R.drawable.ic_paper_gray),
//                        Color.BLUE
                        Color.parseColor("#B0C4DE")
                ).build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
//                        getDrawable(R.drawable.ic_chat),
                        getDrawable(R.drawable.ic_chat_gray),
//                        Color.BLUE
                        Color.parseColor("#B0C4DE")
                ).build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, mCurTag);

        navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
        navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
        navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        //navigationTabBar.setTypeface("fonts/custom_font.ttf");
        navigationTabBar.setIsBadged(false);
        navigationTabBar.setIsTitled(false);
        navigationTabBar.setIsTinted(false);
        navigationTabBar.setIsBadgeUseTypeface(false);
        navigationTabBar.setBadgeBgColor(Color.RED);
        navigationTabBar.setBadgeTitleColor(Color.LTGRAY);
        navigationTabBar.setIsSwiped(true);
        navigationTabBar.setBgColor(Color.WHITE);
        navigationTabBar.setBadgeSize(10);
        navigationTabBar.setTitleSize(10);
        navigationTabBar.setIconSizeFraction((float) 0.5);
        //navigationTabBar.setBehaviorEnabled(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchNames(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() == 0)
            mSearchListAdapter.setResults(null);
        return false;
    }

    private void searchNames(String query) {
        JSONObject json = new JSONObject();

        String url;
        String search_key;
        String anskey_id = "";
        String anskey_name = "";
        String list_name = "";
        switch (mCurTag) {
            case 0:
                url = URL_SEARCH_MEETING;
                search_key = "query";
                anskey_id = "conference_id";
                anskey_name = "name";
                list_name = "conference_list";
                break;
            case 1:
                url = URL_SEARCH_PAPER;
                search_key = "query";
                anskey_id = "paper_id";
                anskey_name = "title";
                list_name = "paper_list";
                break;
            case 2:
                url = URL_SEARCH_PERSON;
                search_key = "nickname";
                anskey_id = "user_id";
                anskey_name = "nickname";
                list_name = "list";
                try {
                    json.put("institution", "");
                    json.put("research_topic", "");
                } catch (Exception e) {
                    System.out.println(e);
                    return;
                }
                break;
            default:
                url = "";
                search_key = "";
        }

        try {
            json.put(search_key, query);
        } catch (Exception e) {
            System.out.println(e);
            return;
        }

        String finalAnskey_id = anskey_id;
        String finalAnskey_name = anskey_name;
        String finalList_name = list_name;
        okhttp3.Callback cb = new okhttp3.Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String str = response.body().string();
                System.out.println(str);

                try {
                    JSONObject j = new JSONObject(str);

                    if (!j.has("error")) {
                        JSONArray j_in = j.getJSONArray(finalList_name);
                        ArrayList<SearchResult> asr = new ArrayList<>();
                        // TODO
                        for (int i = 0; i < j_in.length(); i++) {
                            JSONObject obj = j_in.getJSONObject(i);
                            asr.add(new SearchResult(obj.getInt(finalAnskey_id),
                                    obj.getString(finalAnskey_name)));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSearchListAdapter.setResults(asr);
                            }
                        });
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        };

        CommonInterface.sendOkHttpJsonPostRequest(url, cb, json);
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void exit(){
        if(!isExit){
            isExit=true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(0,2000);
        }
        else{
            Global.save_contact();
            //Global.WebClose();
            finish();
            System.exit(0);
        }
    }

    public void onResume() {
        ((TextView) findViewById(R.id.home_drawer_name)).setText(Global.getNickname());
        Uri avatar_uri = Uri.parse(URL_PREFIX + Global.getAvatar_url());
        System.out.println(Global.getAvatar_url());
        ((SimpleDraweeView) findViewById((R.id.home_drawer_image))).setImageURI(avatar_uri);
        super.onResume();
    }
}
