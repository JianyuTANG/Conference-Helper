package com.example.myapplication.meeting.paper;

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
import com.example.myapplication.home.paper.Paper;
import com.example.myapplication.home.paper.PaperListAdapter;
import com.example.myapplication.home.paper.PaperViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.detail.DetailActivity.EXTRA_ID;
import static com.example.myapplication.detail.DetailActivity.EXTRA_TITLE;
import static com.example.myapplication.detail.DetailActivity.EXTRA_TYPE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeetingPaper#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetingPaper extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CONFERENCE_ID = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int TYPE = 2;

    // TODO: Rename and change types of parameters
    private int conference_id;
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
    public static MeetingPaper newInstance(int param1, String param2) {
        MeetingPaper fragment = new MeetingPaper();
        Bundle args = new Bundle();
        args.putInt(ARG_CONFERENCE_ID, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            conference_id = getArguments().getInt(ARG_CONFERENCE_ID);
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
        mAdapter.setOnItemClickListener(new PaperListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Paper m = mAdapter.getPaperAtPosition(position);

                Intent intent = new Intent(getContext(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_TYPE, 1);
                bundle.putInt(EXTRA_ID, m.getID());
                bundle.putString(EXTRA_TITLE, m.getTitle());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
        mPaperViewModel.setConferenceId(conference_id);
        mPaperViewModel.update();

//        ArrayList<Paper> papers = new ArrayList<>();
//        papers.add(new Paper("Deep Learning", "Zhang, San. et.al.", 1));
//        papers.add(new Paper("Shadow Learning", "Li, Si. et.al.", 2));
//        mAdapter.setPapers(papers);

        return view;
    }
}
