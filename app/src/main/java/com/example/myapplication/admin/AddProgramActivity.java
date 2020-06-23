package com.example.myapplication.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.myapplication.R;
import com.example.utils.CommonInterface;
import com.example.utils.Global;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class AddProgramActivity extends AppCompatActivity {
    private EditText editName, editOrg, editHost, editReporter, editPlace;
    private TextView startDate, startTime, endDate, endTime, finish;
    private int conference_id;
    private String typeSelected;
    private Calendar calendar;
    private RadioGroup radiogroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_program);

        String conference_id = getIntent().getStringExtra("conference_id");
        int type = getIntent().getIntExtra("type", 0);

        editName = (EditText) findViewById(R.id.ProgramName);
        editOrg = (EditText) findViewById(R.id.ProgramOrg);
        editHost = (EditText) findViewById(R.id.ProgramHost);
        editReporter = (EditText) findViewById(R.id.ProgramReporter);
        editPlace = (EditText) findViewById(R.id.ProgramPlace);

        startDate = (TextView) findViewById(R.id.ProgramStartDate);
        startTime = (TextView) findViewById(R.id.ProgramStartTime);
        endDate = (TextView) findViewById(R.id.ProgramEndDate);
        endTime = (TextView) findViewById(R.id.ProgramEndTime);
        finish = (TextView) findViewById(R.id.ProgramFinsh);
        radiogroup = (RadioGroup) findViewById(R.id.ProgramType);

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton)findViewById(radiogroup.getCheckedRadioButtonId());
                typeSelected = rb.getText().toString();
            }
        });

        calendar = Calendar.getInstance();
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddProgramActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day) {

                                startDate.setText(new StringBuilder()
                                        .append(year)
                                        .append("-")
                                        .append((month + 1) < 10 ? "0"
                                                + (month + 1) : (month + 1))
                                        .append("-")
                                        .append((day < 10) ? "0" + day : day));
                            }
                        }, calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddProgramActivity.this,new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (minute < 10){
                            startTime.setText(hourOfDay + ":" + "0" + minute);
                        }else {
                            startTime.setText(hourOfDay + ":" + minute);
                        }
                    }
                }      , calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE)
                        , true).show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddProgramActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day) {

                                // 更新EditText控件日期 小于10加0
                                endDate.setText(new StringBuilder()
                                        .append(year)
                                        .append("-")
                                        .append((month + 1) < 10 ? "0"
                                                + (month + 1) : (month + 1))
                                        .append("-")
                                        .append((day < 10) ? "0" + day : day));
                            }
                        }, calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddProgramActivity.this,new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (minute < 10){
                            endTime.setText(hourOfDay + ":" + "0" + minute);
                        }else {
                            endTime.setText(hourOfDay + ":" + minute);
                        }
                    }
                }      , calendar.get(Calendar.HOUR_OF_DAY)
                        , calendar.get(Calendar.MINUTE)
                        , true).show();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String host = editHost.getText().toString();
                String reporter = editReporter.getText().toString();
                String org = editOrg.getText().toString();
                String place = editPlace.getText().toString();
                String sd = startDate.getText().toString();
                String st = startTime.getText().toString();
                String ed = endDate.getText().toString();
                String et = endTime.getText().toString();

                if (name.length() <= 0 || host.length() <= 0 || reporter.length() <= 0 ||
                        org.length() <= 0 || place.length() <= 0 || st.length() <= 0 || sd.length() <= 0 || ed.length() <= 0 || et.length() <= 0) {
                    AddProgramActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddProgramActivity.this);
                            builder.setTitle("添加议程失败");
                            builder.setMessage("请输入每一项内容");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            builder.show();
                        }
                    });
                } else {
                    String add_program_url = "add_program";
                    HashMap<String, String> map = new HashMap<>();
                    map.put("conference_id", Global.getConference_id());
                    map.put("title", name);
                    map.put("program_type", typeSelected);
                    map.put("organization", org);
                    map.put("start_time", sd + "-" + st);
                    map.put("end_time", ed + "-" + et);
                    map.put("host", host);
                    map.put("reporter", reporter);
                    map.put("place", place);

                    okhttp3.Callback cb = new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            AddProgramActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddProgramActivity.this);
                                    builder.setTitle("添加议程失败");
                                    builder.setMessage("请检查您的网络连接");
                                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    builder.show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //JSONObject str = new JSONObject(response.body().toString());
                            String str = response.body().string();
                            System.out.println(str);
                            try {
                                JSONObject j = new JSONObject(str);
                                String program_id = j.getString("program_id");
                                AddProgramActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddProgramActivity.this);
                                        builder.setTitle("添加议程成功");
                                        if (type == 1)
                                        {
                                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            });
                                            builder.show();
                                        }
                                        else{
                                            builder.setMessage("是否为该议程添加论文");
                                            builder.setMessage("是否为该议程添加论文");
                                            builder.setCancelable(false);
                                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(AddProgramActivity.this, AddArticleActivity.class);
                                                    intent.putExtra("conference_id", conference_id);
                                                    intent.putExtra("program_id", program_id);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                            builder.setNegativeButton("不添加，返回", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(AddProgramActivity.this, AddChooseActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                            builder.show();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    CommonInterface.sendOkHttpPostRequest(add_program_url, cb, map);
                }
            }
        });
    }
}
