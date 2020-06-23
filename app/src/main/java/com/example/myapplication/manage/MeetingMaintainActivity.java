package com.example.myapplication.manage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.home.meeting.Meeting;
import com.example.myapplication.home.meeting.MeetingListAdapter;
import com.example.myapplication.home.meeting.MeetingViewModel;
import com.example.myapplication.meeting.MeetingActivity;
import com.example.utils.CommonInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.myapplication.meeting.MeetingActivity.EXTRA_MEETING_ID;


public class MeetingMaintainActivity extends AppCompatActivity {
    private static final String URL = "delete_conference";

    private MeetingListAdapter mAdapter;
    private MeetingViewModel mMeetingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_maintain);

        final Toolbar myToolbar = findViewById(R.id.my_toolbar_meeting_maintain_activity);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("会议管理");

        RecyclerView mRecyclerView = findViewById(R.id.cur_meeting_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(
//                getContext(), DividerItemDecoration.VERTICAL));

        mAdapter = new MeetingListAdapter(this);
        mAdapter.setOnItemClickListener(new MeetingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Meeting m = mAdapter.getMeetingAtPosition(position);
                int id = m.getId();

                Intent intent = new Intent(MeetingMaintainActivity.this, MeetingDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);


        mMeetingViewModel = new ViewModelProvider(this).get(MeetingViewModel.class);
        mMeetingViewModel.getMeetings().observe(this, new Observer<List<Meeting>>() {
            @Override
            public void onChanged(List<Meeting> meetings) {
                mAdapter.setMeetings(meetings);
            }
        });
        mMeetingViewModel.setType(2);
        mMeetingViewModel.update();

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getLayoutPosition();
                Meeting m = mAdapter.getMeetingAtPosition(pos);

                int id = m.getId();

                // network request
                JSONObject json = new JSONObject();
                try {
                    json.put("conference_id", id);
                } catch (Exception e) {
                    System.out.println(e);
                }

                okhttp3.Callback cb = new okhttp3.Callback() {
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response)
                            throws IOException {
                        String str = response.body().string();
                        System.out.println(str);
                        try {
                            JSONObject j = new JSONObject(str);
                            if (j.has("conference_id")) {
                                mMeetingViewModel.update();
                                showMsg("删除成功");
                            }
                            else {
                                showMsg("网络错误，删除失败");
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                            showMsg("网络错误，删除失败");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        showMsg("网络错误，删除失败");
                    }

                    private void showMsg(String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MeetingMaintainActivity.this,
                                        msg,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                };

                CommonInterface.sendOkHttpJsonPostRequest(URL, cb, json);
            }
        });
        helper.attachToRecyclerView(mRecyclerView);
    }

    public void onResume() {
        super.onResume();
        if (mMeetingViewModel != null)
            mMeetingViewModel.update();
    }
}