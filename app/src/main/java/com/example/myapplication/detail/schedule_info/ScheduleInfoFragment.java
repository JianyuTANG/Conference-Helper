package com.example.myapplication.detail.schedule_info;

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
import com.example.myapplication.home.HomeActivity;
import com.example.myapplication.meeting.schedule.Schedule;
import com.example.myapplication.scholar.ScholarActivity;
import com.example.utils.CommonInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.myapplication.scholar.ScholarActivity.EXTRA_ID;
import static com.example.myapplication.scholar.ScholarActivity.EXTRA_NAME;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String URL = "view_program";
    private static final String URL_SEARCH = "search_user";

    // TODO: Rename and change types of parameters
    private int schedule_id;
    private String mParam2;

    public ScheduleInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleInfoFragment newInstance(int param1, String param2) {
        ScheduleInfoFragment fragment = new ScheduleInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ScheduleInfoFragment newInstance(Bundle args) {
        ScheduleInfoFragment fragment = new ScheduleInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_schedule_info, container, false);

        // network request
        JSONObject json = new JSONObject();
        try {
            json.put("program_id", schedule_id);
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
                        final Context context = getContext();

                        String organization = j.getString("organization");

                        String host = j.getString("host");
                        final SpannableString s_host = new SpannableString(host);
                        ClickableSpan myActivityLauncher = new ClickableSpan() {
                            public void onClick(View view) {
                                // network request
                                JSONObject t_json = new JSONObject();
                                try {
                                    t_json.put("nickname", host);
                                    t_json.put("institution", "");
                                    t_json.put("research_topic", "");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                okhttp3.Callback t_cb = new okhttp3.Callback() {

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response)
                                            throws IOException {
                                        String str = response.body().string();
                                        System.out.println(str);

                                        try {
                                            JSONObject j_out = new JSONObject(str);
                                            JSONArray j_in = j_out.getJSONArray("list");
                                            if (j_in.length() == 0) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                        builder.setTitle(R.string.scholar_cannot_find_alert_title);
                                                        builder.setMessage(R.string.scholar_cannot_find_alert_body);
                                                        builder.show();
                                                    }
                                                });
                                            }
                                            else {
                                                JSONObject temp = j_in.getJSONObject(0);
                                                Intent intent = new Intent(context, ScholarActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(EXTRA_ID, temp.getInt("user_id"));
                                                bundle.putString(EXTRA_NAME, host);
                                                intent.putExtras(bundle);
                                                context.startActivity(intent);
                                            }
                                        } catch (Exception e) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                    builder.setTitle(R.string.scholar_cannot_find_alert_title);
                                                    builder.setMessage(R.string.scholar_cannot_find_alert_body);
                                                    builder.show();
                                                }
                                            });

                                            System.out.println(e);
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                                                String str = null;
//                                                try {
//                                                    str = response.body().string();
//                                                    System.out.println(str);
//                                                } catch (IOException ex) {
//                                                    ex.printStackTrace();
//                                                }
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                builder.setTitle(R.string.scholar_cannot_find_alert_title);
                                                builder.setMessage(R.string.scholar_cannot_find_alert_body);
                                                builder.show();
                                            }
                                        });
                                    }
                                };
                                CommonInterface.sendOkHttpJsonPostRequest(URL_SEARCH, t_cb, t_json);
                            }
                        };
                        s_host.setSpan(myActivityLauncher, 0, host.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        String reporter = j.getString("reporter");
                        final SpannableString s_reporter = new SpannableString(reporter);
                        ClickableSpan myActivityLauncher2 = new ClickableSpan() {
                            public void onClick(View view) {
                                // network request
                                JSONObject t_json = new JSONObject();
                                try {
                                    t_json.put("nickname", reporter);
                                    t_json.put("institution", "");
                                    t_json.put("research_topic", "");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                okhttp3.Callback t_cb = new okhttp3.Callback() {

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response)
                                            throws IOException {
                                        String str = response.body().string();
                                        System.out.println(str);

                                        try {
                                            JSONObject j_out = new JSONObject(str);
                                            JSONArray j_in = j_out.getJSONArray("list");
                                            if (j_in.length() == 0) {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                        builder.setTitle(R.string.scholar_cannot_find_alert_title);
                                                        builder.setMessage(R.string.scholar_cannot_find_alert_body);
                                                        builder.show();
                                                    }
                                                });
                                            }
                                            else {
                                                JSONObject temp = j_in.getJSONObject(0);
                                                Intent intent = new Intent(context, ScholarActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(EXTRA_ID, temp.getInt("user_id"));
                                                bundle.putString(EXTRA_NAME, reporter);
                                                intent.putExtras(bundle);
                                                context.startActivity(intent);
                                            }
                                        } catch (Exception e) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                    builder.setTitle(R.string.scholar_cannot_find_alert_title);
                                                    builder.setMessage(R.string.scholar_cannot_find_alert_body);
                                                    builder.show();
                                                }
                                            });

                                            System.out.println(e);
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                                                String str = null;
//                                                try {
//                                                    str = response.body().string();
//                                                    System.out.println(str);
//                                                } catch (IOException ex) {
//                                                    ex.printStackTrace();
//                                                }
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                builder.setTitle(R.string.scholar_cannot_find_alert_title);
                                                builder.setMessage(R.string.scholar_cannot_find_alert_body);
                                                builder.show();
                                            }
                                        });
                                    }
                                };
                                CommonInterface.sendOkHttpJsonPostRequest(URL_SEARCH, t_cb, t_json);
                            }
                        };
                        s_reporter.setSpan(myActivityLauncher2, 0, reporter.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                        String place = j.getString("place");
                        String start_time = j.getString("start_time");
                        String end_time = j.getString("end_time");
                        String dur = start_time + "\n 至 " + end_time;

//                        String img_urls = j.getString("img_urls");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView nameView = view.findViewById(R.id.schedule_info_lecturer);
                                nameView.setText(s_host);
                                nameView.setMovementMethod(LinkMovementMethod.getInstance());

                                TextView reporterView = view.findViewById(R.id.schedule_info_host);
                                reporterView.setText(s_reporter);
                                reporterView.setMovementMethod(LinkMovementMethod.getInstance());

                                TextView organizationView = view.findViewById(R.id.schedule_info_org);
                                organizationView.setText(organization);

                                TextView durView = view.findViewById(R.id.schedule_info_time);
                                durView.setText(dur);

                                TextView placeView = view.findViewById(R.id.schedule_info_place);
                                placeView.setText(place);
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