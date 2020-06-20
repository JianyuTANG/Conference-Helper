package com.example.myapplication.home.paper;

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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TYPE = "param1";
    private static final String ARG_PARAM2 = "param2";
    PaperListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    private int mtype;
    private String mParam2;

    public CollectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CollectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CollectionFragment newInstance(int param1, String param2) {
        CollectionFragment fragment = new CollectionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mtype = getArguments().getInt(ARG_TYPE, 0);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        RecyclerView mRecyclerView = view.findViewById(R.id.collection_paper_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getContext(), DividerItemDecoration.VERTICAL));

        mAdapter = new PaperListAdapter(getContext());
        mAdapter.setOnItemClickListener(new PaperListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Paper m = mAdapter.getPaperAtPosition(position);
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
        mPaperViewModel.setType(mtype);
        mPaperViewModel.update();

//        ArrayList<Paper> papers = new ArrayList<>();
//        papers.add(new Paper("Deep Learning", "Zhang, San. et.al.", 1));
//        papers.add(new Paper("Shadow Learning", "Li, Si. et.al.", 2));
//        mAdapter.setPapers(papers);

        return view;
    }
}
