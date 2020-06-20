package com.example.myapplication.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import java.util.HashMap;

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

                                // 更新EditText控件日期 小于10加0
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


                String start_time = startDate.getText().toString() + "-" + startTime.getText().toString();
                String end_time = endDate.getText().toString() + "-" +endTime.getText().toString();

                HashMap<String, String> map = new HashMap<>();
                map.put("title", name);
                map.put("program_type", typeSelected);
                map.put("organization", org);
                map.put("start_time", start_time);
                map.put("end_time", end_time);
                map.put("host", host);
                map.put("reporter", reporter);
                map.put("place", place);
            }
        });
    }
}
