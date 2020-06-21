package com.example.myapplication.meeting.info;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.SignInActivity;
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.meeting.MeetingActivity;
import com.example.utils.CommonInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeetingInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeetingInfo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String URL_PREFIX = "http://123.56.88.4:1234";

    private static final String URL = "view_conference";

    // TODO: Rename and change types of parameters
    private int id;
    private String mParam2;

    public MeetingInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeetingInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static MeetingInfo newInstance(int param1, String param2) {
        MeetingInfo fragment = new MeetingInfo();
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
            id = getArguments().getInt(ARG_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(
                R.layout.fragment_meeting_info, container, false);

        // network request
        JSONObject json = new JSONObject();
        try {
            json.put("conference_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        okhttp3.Callback cb = new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                String str = response.body().string();
                System.out.println("view conference");
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
                        String name = j.getString("name");
                        String short_name = j.getString("short_name");
                        String organization = j.getString("organization");
                        String description = j.getString("description");
                        String start_date = j.getString("start_date");
                        String end_date = j.getString("end_date");
                        String date = start_date + "\n 至 " + end_date;
                        JSONArray img_urls = j.getJSONArray("img_urls");
                        String place = j.getString("place");

                        final ArrayList<String> urls = new ArrayList<>();
                        for (int i = 0; i < img_urls.length(); i++)
                            urls.add(URL_PREFIX + img_urls.getString(i));

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView nameView = view.findViewById(R.id.meeting_info_name);
                                nameView.setText(name);

                                TextView orgView = view.findViewById(R.id.meeting_info_org);
                                orgView.setText(organization);

                                TextView descView = view.findViewById(R.id.meeting_info_desc);
                                descView.setText(description);

                                TextView placeView = view.findViewById(R.id.meeting_info_place);
                                placeView.setText(place);

                                TextView dateView = view.findViewById(R.id.meeting_info_date);
                                dateView.setText(date);

                                ((MeetingActivity)getActivity()).setBanner(urls);
                                ((MeetingActivity)getActivity()).setTitle(short_name);
                            }
                        });
                    }
                }
                catch (Exception e) {
                    ;
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
