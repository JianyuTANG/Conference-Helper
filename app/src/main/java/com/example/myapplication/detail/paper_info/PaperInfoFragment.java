package com.example.myapplication.detail.paper_info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaperInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaperInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "authors";
    private static final String ARG_PARAM2 = "abs";
    private static final String ARG_PARAM3 = "authors_num";

    // TODO: Rename and change types of parameters
    private ArrayList<String> authors;
    private ArrayList<Integer> author_ids;
    private String abs;
    private int authors_num;

    public PaperInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PaperInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaperInfoFragment newInstance(
            String abs, ArrayList<String> authors, int authors_num) {
        PaperInfoFragment fragment = new PaperInfoFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, authors);
        args.putString(ARG_PARAM2, abs);
        args.putInt(ARG_PARAM3, authors_num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            authors = getArguments().getStringArrayList(ARG_PARAM1);
            abs = getArguments().getString(ARG_PARAM2);
            authors_num = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paper_info, container, false);

        // abstract
        TextView absView = view.findViewById(R.id.paper_info_abstract);
        absView.setText(abs);

        // authors
        final Context context = getContext();
        String res = authors.get(0);
        for (int i = 1; i < authors_num; i++) {
            res = res + ", " + authors.get(i);
        }
        SpannableString ss = new SpannableString(res);
        int cur = 0;
        for (int i = 1; i < authors_num; i++) {
            String s = authors.get(i);
            final int finalI = i;
            ClickableSpan myActivityLauncher = new ClickableSpan() {
                public void onClick(View view) {
                    context.startActivity(getIntentForActivityToStart(author_ids.get(finalI)));
                }

                private Intent getIntentForActivityToStart(int id) {
                    Intent intent = new Intent();
                    return null;
                }
            };

            int len = s.length();
            ss.setSpan(myActivityLauncher, cur, cur + len,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            cur += 2 + len;
        }
        TextView authorsView = view.findViewById(R.id.paper_info_authors);
        authorsView.setText(ss);
        authorsView.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }
}