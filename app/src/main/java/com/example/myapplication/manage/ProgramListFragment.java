package com.example.myapplication.manage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.admin.AddArticleActivity;
import com.example.myapplication.admin.AddProgramActivity;
import com.example.myapplication.detail.DetailActivity;
import com.example.myapplication.home.paper.Paper;
import com.example.myapplication.meeting.schedule.Schedule;
import com.example.myapplication.meeting.schedule.ScheduleListAdapter;
import com.example.myapplication.meeting.schedule.ScheduleViewModel;
import com.example.utils.CommonInterface;
import com.example.utils.Global;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.myapplication.manage.ProgramMaintainActivity.EXTRA_CONFID;
import static com.example.myapplication.manage.ProgramMaintainActivity.EXTRA_ID;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgramListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgramListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int conference_id;
    private String mParam2;

    private ScheduleViewModel mScheduleViewModel;
    private ScheduleListAdapter mAdapter;

    public ProgramListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgramListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgramListFragment newInstance(int param1, String param2) {
        ProgramListFragment fragment = new ProgramListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            conference_id = getArguments().getInt(ARG_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_program_list, container, false);

        final RecyclerView mRecyclerView = view.findViewById(R.id.program_list_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getContext(), DividerItemDecoration.VERTICAL));

        mAdapter = new ScheduleListAdapter(getContext());
        mAdapter.setOnItemClickListener(new ScheduleListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Schedule m = mAdapter.getScheduleAtPosition(position);
                int id = m.getId();

                Intent intent = new Intent(getContext(), ProgramMaintainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_CONFID, conference_id);
                bundle.putInt(EXTRA_ID, id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mScheduleViewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);
        mScheduleViewModel.getSchedule().observe(this, new Observer<List<Schedule>>() {
            @Override
            public void onChanged(List<Schedule> schedules) {
                mAdapter.setSchedules(schedules);
            }
        });
        mScheduleViewModel.setConferenceId(conference_id);
        mScheduleViewModel.update();

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
                Schedule m = mAdapter.getScheduleAtPosition(pos);

                int id = m.getId();

                // network request
                JSONObject json = new JSONObject();
                try {
                    json.put("program_id", id);
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
                            if (j.has("program_id")) {
                                mScheduleViewModel.update();
                                showMsg("删除成功");
                            }
                            else {
                                mScheduleViewModel.update();
                                showMsg("网络错误，删除失败");
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                            mScheduleViewModel.update();
                            showMsg("网络错误，删除失败");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        mScheduleViewModel.update();
                        showMsg("网络错误，删除失败");
                    }

                    private void showMsg(String msg) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),
                                        msg,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                };

                CommonInterface.sendOkHttpJsonPostRequest("delete_program", cb, json);
            }
        });
        helper.attachToRecyclerView(mRecyclerView);

        FloatingActionButton button = view.findViewById(R.id.program_add_float_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getContext(),
                        AddProgramActivity.class
                );
                intent.putExtra("conference_id", String.valueOf(conference_id));
                intent.putExtra("type", 1);
                Global.setConference_id(String.valueOf(conference_id));
                startActivity(intent);
            }
        });

        return view;
    }

    public void onResume() {
        super.onResume();
        if (mScheduleViewModel != null)
            mScheduleViewModel.update();
    }
}