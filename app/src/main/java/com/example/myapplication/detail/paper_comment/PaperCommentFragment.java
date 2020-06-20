package com.example.myapplication.detail.paper_comment;

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
import com.example.myapplication.meeting.schedule.Schedule;
import com.example.myapplication.meeting.schedule.ScheduleListAdapter;
import com.example.myapplication.meeting.schedule.ScheduleViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaperCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaperCommentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int paper_id;
    private String mParam2;

    CommentListAdapter mAdapter;

    public PaperCommentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaperCommentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaperCommentFragment newInstance(int param1, String param2) {
        PaperCommentFragment fragment = new PaperCommentFragment();
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
            paper_id = getArguments().getInt(ARG_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_paper_comment,
                container, false);
        final RecyclerView mRecyclerView = view.findViewById(R.id.paper_comment_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getContext(), DividerItemDecoration.VERTICAL));

        mAdapter = new CommentListAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        CommentViewModel mScheduleViewModel = new
                ViewModelProvider(this).get(CommentViewModel.class);
        mScheduleViewModel.getComment().observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> comments) {
                mAdapter.setComments(comments);
            }
        });
        mScheduleViewModel.setPaperId(paper_id);
        mScheduleViewModel.update();

//        ArrayList<Comment> comments = new ArrayList<>();
//        comments.add(new Comment("zhang", "good job!", 0));
//        comments.add(new Comment("liang", "fantastic work of Turing Award level", 1));
//        mAdapter.setComments(comments);

        return view;
    }
}