package com.example.myapplication.meeting.paper;

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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeetingPaper#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetingPaper extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    PaperListAdapter mAdapter;

    public MeetingPaper() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeetingPaper.
     */
    // TODO: Rename and change types and number of parameters
    public static MeetingPaper newInstance(String param1, String param2) {
        MeetingPaper fragment = new MeetingPaper();
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

        System.out.println("777");

        Paper m = new Paper("Deep Learning", "Zhang, San. et.al.",
                1);
        ArrayList<Paper> papers = new ArrayList<>();
        papers.add(m);
        papers.add(new Paper("Shadow Learning", "Li, Si. et.al.",
                2));
        mAdapter.setPapers(papers);

        return view;
    }
}