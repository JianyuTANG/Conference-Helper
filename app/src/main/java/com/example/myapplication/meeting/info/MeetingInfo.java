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
import com.example.utils.CommonInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
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

    private static final String URL = "view_conference";

    // TODO: Rename and change types of parameters
    private String id;
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
    public static MeetingInfo newInstance(String param1, String param2) {
        MeetingInfo fragment = new MeetingInfo();
        Bundle args = new Bundle();
        args.putString(ARG_ID, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
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
        HashMap<String, String> map = new HashMap<>();
        map.put("conference_id", id);

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
                        String name = j.getString("name");
                        String organization = j.getString("organization");
                        String description = j.getString("description");
                        String start_date = j.getString("start_date");
                        String end_date = j.getString("end_date");
                        String img_urls = j.getString("img_urls");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView nameView = view.findViewById(R.id.meeting_info_name);
                                nameView.setText(name);
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

        CommonInterface.sendOkHttpPostRequest(URL, cb, map);

        return view;
    }
}
