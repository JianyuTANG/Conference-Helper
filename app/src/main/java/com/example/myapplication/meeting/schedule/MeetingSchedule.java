package com.example.myapplication.meeting.schedule;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.detail.DetailActivity.EXTRA_ID;
import static com.example.myapplication.detail.DetailActivity.EXTRA_P_TYPE;
import static com.example.myapplication.detail.DetailActivity.EXTRA_TITLE;
import static com.example.myapplication.detail.DetailActivity.EXTRA_TYPE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeetingSchedule#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetingSchedule extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int conference_id;
    private String mParam2;

    private ScheduleListAdapter mAdapter;

    public MeetingSchedule() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeetingSchedule.
     */
    // TODO: Rename and change types and number of parameters
    public static MeetingSchedule newInstance(int param1, String param2) {
        MeetingSchedule fragment = new MeetingSchedule();
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
        final View view = inflater.inflate(R.layout.fragment_meeting_schedule,
                container, false);
        final RecyclerView mRecyclerView = view.findViewById(R.id.meeting_schedule_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getContext(), DividerItemDecoration.VERTICAL));

        mAdapter = new ScheduleListAdapter(getContext());
        mAdapter.setOnItemClickListener(new ScheduleListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Schedule m = mAdapter.getScheduleAtPosition(position);
                int id = m.getId();

                Intent intent = new Intent(getContext(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_TYPE, 0);
                bundle.putInt(EXTRA_ID, id);
                bundle.putString(EXTRA_P_TYPE, m.getProgram_type());
                bundle.putString(EXTRA_TITLE, m.getTitle());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        ScheduleViewModel mScheduleViewModel = new
                ViewModelProvider(this).get(ScheduleViewModel.class);
        mScheduleViewModel.getSchedule().observe(this, new Observer<List<Schedule>>() {
            @Override
            public void onChanged(List<Schedule> schedules) {
                mAdapter.setSchedules(schedules);
            }
        });
        mScheduleViewModel.setConferenceId(conference_id);
        mScheduleViewModel.update();

//        ArrayList<Schedule> schedules = new ArrayList<>();
//        schedules.add(new Schedule("1st Agricultural Vision", "Mantic",
//                "6/16 12:00", "Main Hall", 0));
//        schedules.add(new Schedule("1st Low-quality Vision", "Sherry",
//                "6/17 12:00", "Main Hall", 1));
//        mAdapter.setSchedules(schedules);

        return view;
    }
}
