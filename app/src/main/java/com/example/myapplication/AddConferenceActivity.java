package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.widget.ItemGroup;
import com.example.widget.RoundImageView;

public class AddConferenceActivity extends AppCompatActivity {
    private TextView ig_start, ig_end;
    private RoundImageView portrait;
    private Calendar calendar;
    private static int TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_conference);

        ig_start = (TextView) findViewById(R.id.startDate);
        ig_end = (TextView) findViewById(R.id.endDate);
        portrait = (RoundImageView) findViewById(R.id.ri_portrait);

        //ig_start.contentEdt.setFocusable(false);
        //ig_end.contentEdt.setFocusable(false);

        calendar = Calendar.getInstance();

        // 开始日期设置
        ig_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddConferenceActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day) {

                                // 更新EditText控件日期 小于10加0
                                ig_start.setText(new StringBuilder()
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

        // 结束日期设置
        ig_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddConferenceActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day) {

                                // 更新EditText控件日期 小于10加0
                                ig_end.setText(new StringBuilder()
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

        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //申请权限
                System.out.println("click");
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, TAKE_PHOTO);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TAKE_PHOTO){
            if(data.getData() != null) {
                Uri uri = data.getData();
                portrait.setImageURI(uri);
            }
        }
    }
}
