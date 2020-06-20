package com.example.myapplication.detail.schedule_paper_list;

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
import com.example.myapplication.home.paper.Paper;
import com.example.myapplication.home.paper.PaperListAdapter;
import com.example.myapplication.home.paper.PaperViewModel;
import com.example.myapplication.meeting.schedule.Schedule;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SchedulePaperListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SchedulePaperListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int TYPE = 3;

    // TODO: Rename and change types of parameters
    private int schedule_id;
    private String mParam2;

    private PaperListAdapter mAdapter;

    public SchedulePaperListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchedulePaperListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SchedulePaperListFragment newInstance(int param1, String param2) {
        SchedulePaperListFragment fragment = new SchedulePaperListFragment();
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
            schedule_id = getArguments().getInt(ARG_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_collection, container, false);
        RecyclerView mRecyclerView = view.findViewById(R.id.collection_paper_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getContext(), DividerItemDecoration.VERTICAL));

        mAdapter = new PaperListAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        PaperViewModel mPaperViewModel = new
                ViewModelProvider(this).get(PaperViewModel.class);
        mPaperViewModel.getPapers().observe(this, new Observer<List<Paper>>() {
            @Override
            public void onChanged(List<Paper> papers) {
                mAdapter.setPapers(papers);
            }
        });
        mPaperViewModel.setType(TYPE);
        mPaperViewModel.setProgramId(schedule_id);
        mPaperViewModel.update();

        return view;
    }
}