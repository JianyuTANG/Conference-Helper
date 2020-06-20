package com.example.myapplication.detail.paper_info;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.myapplication.detail.DetailActivity;
import com.example.myapplication.home.HomeActivity;
import com.example.utils.CommonInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaperInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaperInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "param1";
    private static final String URL = "view_paper";

//    private static final String ARG_PARAM2 = "abs";
//    private static final String ARG_PARAM3 = "authors_num";

    // TODO: Rename and change types of parameters
    private ArrayList<String> authors;
    private ArrayList<Integer> author_ids;
    private int authors_num;
    private int paper_id;

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
//    public static PaperInfoFragment newInstance(
//            String abs, ArrayList<String> authors, int authors_num) {
//        PaperInfoFragment fragment = new PaperInfoFragment();
//        Bundle args = new Bundle();
//        args.putStringArrayList(ARG_PARAM1, authors);
//        args.putString(ARG_PARAM2, abs);
//        args.putInt(ARG_PARAM3, authors_num);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static PaperInfoFragment newInstance(Bundle args) {
        PaperInfoFragment fragment = new PaperInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(int id) {
        PaperInfoFragment fragment = new PaperInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            authors = getArguments().getStringArrayList(ARG_PARAM1);
//            abs = getArguments().getString(ARG_PARAM2);
//            authors_num = getArguments().getInt(ARG_PARAM3);
            paper_id = getArguments().getInt(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paper_info, container, false);

        // authors
//        final Context context = getContext();
//        String res = authors.get(0);
//        for (int i = 1; i < authors_num; i++) {
//            res = res + ", " + authors.get(i);
//        }
//        SpannableString ss = new SpannableString(res);
//        int cur = 0;
//        for (int i = 1; i < authors_num; i++) {
//            String s = authors.get(i);
//            final int finalI = i;
//            ClickableSpan myActivityLauncher = new ClickableSpan() {
//                public void onClick(View view) {
//                    context.startActivity(getIntentForActivityToStart(author_ids.get(finalI)));
//                }
//
//                private Intent getIntentForActivityToStart(int id) {
//                    Intent intent = new Intent();
//                    return null;
//                }
//            };
//
//            int len = s.length();
//            ss.setSpan(myActivityLauncher, cur, cur + len,
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            cur += 2 + len;
//        }
//        TextView authorsView = view.findViewById(R.id.paper_info_authors);
//        authorsView.setText(ss);
//        authorsView.setMovementMethod(LinkMovementMethod.getInstance());

        // network request
        JSONObject json = new JSONObject();
        try {
            json.put("paper_id", paper_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        okhttp3.Callback cb = new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                String str = response.body().string();
                System.out.println(str);

                try {
                    JSONObject j = new JSONObject(str);

                    if (j.has("error")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("连接失败");
                                builder.setMessage("网络有问题请重试");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getContext(), HomeActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                builder.show();
                            }
                        });
                    }
                    else {
                        String abs = j.getString("abstract");
                        String title = j.getString("title");
                        String link = j.getString("link");
                        String conference_name = j.getString("conference_name");
                        String like_num = j.getString("like_num");
                        JSONArray j_a = j.getJSONArray("authors");

                        final Context context = getContext();
                        SpannableString ss = new SpannableString("");
                        authors_num = j_a.length();
                        if (authors_num > 0) {
                            String res = j_a.getString(0);
                            for (int i = 1; i < authors_num; i++) {
                                res = res + ", " + j_a.getString(i);
                            }
                            ss = new SpannableString(res);
                            int cur = 0;
                            for (int i = 1; i < authors_num; i++) {
                                String s = j_a.getString(i);
                                final int finalI = i;
                                ClickableSpan myActivityLauncher = new ClickableSpan() {
                                    public void onClick(View view) {
                                        context.startActivity(
                                                getIntentForActivityToStart(s));
                                    }

                                    private Intent getIntentForActivityToStart(String name) {
                                        Intent intent = new Intent(context, DetailActivity.class);
                                        return null;
                                    }
                                };

                                int len = s.length();
                                ss.setSpan(myActivityLauncher, cur, cur + len,
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                cur += 2 + len;
                            }
                        }


//                        String img_urls = j.getString("img_urls");

                        SpannableString finalSs = ss;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView absView = view.findViewById(R.id.paper_info_abstract);
                                absView.setText(abs);

                                TextView authorsView = view.findViewById(R.id.paper_info_authors);
                                authorsView.setText(finalSs);
                                authorsView.setMovementMethod(LinkMovementMethod.getInstance());
                            }
                        });
                    }
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
        };

        CommonInterface.sendOkHttpJsonPostRequest(URL, cb, json);

        return view;
    }
}