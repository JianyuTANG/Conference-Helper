package com.example.myapplication.home.meeting;

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
import com.example.myapplication.meeting.MeetingActivity;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.meeting.MeetingActivity.EXTRA_MEETING_ID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PastMeetingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PastMeetingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MeetingListAdapter mAdapter;

    private MeetingViewModel mMeetingViewModel;

    public PastMeetingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PastMeetingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PastMeetingFragment newInstance(String param1, String param2) {
        PastMeetingFragment fragment = new PastMeetingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cur_meeting, container, false);

        RecyclerView mRecyclerView = view.findViewById(R.id.cur_meeting_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getContext(), DividerItemDecoration.VERTICAL));

        mAdapter = new MeetingListAdapter(getContext());
        mAdapter.setOnItemClickListener(new MeetingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Meeting m = mAdapter.getMeetingAtPosition(position);
                int id = m.getId();

                Intent intent = new Intent(getContext(), MeetingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_MEETING_ID, id);
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
        mMeetingViewModel.setType(1);
        mMeetingViewModel.update();

//        System.out.println("777");
//
//        ArrayList<Meeting> meetings = new ArrayList<>();
//        meetings.add(new Meeting(7, "CVPR2020", "Seattle, WA",
//                "https://tjy.iterator-traits.com/static/default.jpg"));
//        meetings.add(new Meeting(8, "ICML", "Vienna, Austria",
//                "https://tjy.iterator-traits.com/static/default.jpg"));
//        mAdapter.setMeetings(meetings);

        return view;
    }

    @Override
    public void onResume() {
        if (mMeetingViewModel != null)
            mMeetingViewModel.update();
        super.onResume();
    }
}
