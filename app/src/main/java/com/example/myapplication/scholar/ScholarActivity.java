package com.example.myapplication.scholar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.utils.CommonInterface;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;


public class ScholarActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.myapplication.scholar.extra_id";
    public static final String EXTRA_NAME = "com.example.myapplication.scholar.extra_name";
    private static final String URL_PREFIX = "http://123.56.88.4:1234";

    private static final String URL = "view_user";

    private int scholarId;
    private String scholarName;

    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholar);

        Intent intent = getIntent();
        Bundle bundle;
        if (intent != null)
        {
            bundle = intent.getExtras();
            scholarId = bundle.getInt(EXTRA_ID);
            scholarName = bundle.getString(EXTRA_NAME);
            System.out.println("id: " + scholarId);
            System.out.println("name: " + scholarName);
        }

        initUI();
    }

    private void initUI() {
        final Toolbar myToolbar = findViewById(R.id.my_toolbar_scholar_activity);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("学者");

        adapter = new PagerAdapter(getSupportFragmentManager(),
                2, scholarId, scholarName);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.scholar_activity_tab_layout);
        final ViewPager viewPager = findViewById(R.id.scholar_activity_pager);
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

        // network request
        JSONObject json = new JSONObject();
        try {
            json.put("user_id", scholarId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        okhttp3.Callback cb = new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                String str = response.body().string();
                System.out.println(str);

                try {
                    JSONObject j = new JSONObject(str);

                    if (j.has("error")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ScholarActivity.this);
                                builder.setTitle("连接失败");
                                builder.setMessage("网络有问题请重试");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(ScholarActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                builder.show();
                            }
                        });
                    }
                    else {
                        String email = j.getString("email");
                        SpannableString s_email = new SpannableString(email);
                        s_email.setSpan(new URLSpan("mailto:" + email), 0, email.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        String signature = j.getString("signature");
                        String nickname = j.getString("nickname");
                        Uri avatar_url = Uri.parse(URL_PREFIX + j.getString("avatar_url"));
                        String research_topic = j.getString("research_topic");
                        String institution = j.getString("institution");
                        String position = j.getString("position");
                        String website = j.getString("website");
                        SpannableString s_web = new SpannableString(website);
                        s_web.setSpan(new URLSpan("http://" + email), 0, website.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // activity
                                TextView nameView = findViewById(R.id.scholar_item_name);
                                nameView.setText(nickname);
                                TextView positionView = findViewById(R.id.scholar_item_position);
                                positionView.setText(position);
                                SimpleDraweeView avatarView = findViewById(R.id.scholar_item_image);
                                avatarView.setImageURI(avatar_url);

                                // info fragment
                                TextView instituteView = findViewById(R.id.scholar_info_institution);
                                instituteView.setText(institution);
                                TextView topicView = findViewById(R.id.scholar_info_topic);
                                topicView.setText(research_topic);
                                TextView emailView = findViewById(R.id.scholar_info_email);
                                emailView.setText(s_email);
                                emailView.setMovementMethod(LinkMovementMethod.getInstance());
                                TextView websiteView = findViewById(R.id.scholar_info_website);
                                websiteView.setText(s_web);
                                websiteView.setMovementMethod(LinkMovementMethod.getInstance());
                                TextView quoteView = findViewById(R.id.scholar_info_quote);
                                quoteView.setText(signature);
                            }
                        });
                    }
                }
                catch (Exception e) {
                    ;
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
        };

        CommonInterface.sendOkHttpJsonPostRequest(URL, cb, json);
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