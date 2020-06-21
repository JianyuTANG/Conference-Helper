package com.example.myapplication.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SymbolTable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.InfoActivity;
import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.utils.CommonInterface;
import com.example.utils.Global;
import com.example.widget.RoundImageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class AddConferenceActivity extends AppCompatActivity {
    private TextView ig_start, ig_end;
    private EditText edit_name, edit_org, edit_description, edit_jian, edit_place;
    private LinearLayout portrait;
    private SimpleDraweeView portraint_img;
    private Calendar calendar;
    private static int TAKE_PHOTO = 1;
    private Uri conference_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_conference);

//        Fresco.initialize(this);
        ig_start = (TextView) findViewById(R.id.startDate);
        ig_end = (TextView) findViewById(R.id.endDate);
        portrait = findViewById(R.id.ll_portrait);
        portraint_img = findViewById(R.id.meeting_item_image_add);
        edit_name = (EditText) findViewById(R.id.editName);
        edit_org = (EditText) findViewById(R.id.editOrg);
        edit_description = (EditText) findViewById(R.id.editDescription);
        edit_jian = (EditText) findViewById(R.id.editJian);
        edit_place = (EditText) findViewById(R.id.editPlace);

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
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, TAKE_PHOTO);

            }
        });

        TextView finish_change = (TextView) findViewById(R.id.tv_finsh);
        finish_change.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 String name = edit_name.getText().toString();
                 String org = edit_org.getText().toString();
                 String description = edit_description.getText().toString();
                 String date_start = ig_start.getText().toString();
                 String date_end = ig_end.getText().toString();
                 String jian = edit_jian.getText().toString();
                 String place = edit_place.getText().toString();

                 String url = "add_conference";

                 HashMap<String, String> map = new HashMap<>();
                 map.put("admin_id", Global.getID());
                 map.put("name", name);
                 map.put("organization", org);
                 map.put("description", description);
                 map.put("start_date", date_start);
                 map.put("end_date", date_end);
                 map.put("short_name", jian);
                 map.put("place", place);

                okhttp3.Callback cb = new okhttp3.Callback(){
                    @Override
                    public void onFailure(Call call, IOException e){
                        AddConferenceActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddConferenceActivity.this);
                                builder.setTitle("添加会议失败");
                                builder.setMessage("请检查您的网络连接");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
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
                        try{
                            JSONObject j = new JSONObject(str);
                            JSONObject upload_img = new JSONObject();
                            String conference_id = j.getString("conference_id");
                            String add_img_url = "upload_conference_imgs";
                            File f = getFileByUri(conference_img);
                            upload_img.put("conference_id", conference_id);
                            Global.setConference_id(conference_id);

                            okhttp3.Callback cb2 = new okhttp3.Callback(){
                                @Override
                                public void onFailure(Call call, IOException e){
                                    e.printStackTrace();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    System.out.println(response.body().string());
                                    AddConferenceActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AddConferenceActivity.this);
                                            builder.setTitle("添加会议成功！");
                                            builder.setMessage("继续添加议程和论文");
                                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(AddConferenceActivity.this, AddChooseActivity.class);
                                                    intent.putExtra("conference_id", conference_id);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });
                                            builder.show();
                                        }
                                    });
                                }
                            };
                            CommonInterface.sendOkHttpJsonFile(add_img_url, cb2, upload_img, f);
                        }
                        catch (Exception e){ e.printStackTrace();}

                    }
                };
                CommonInterface.sendOkHttpPostRequest(url, cb, map);

           }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == TAKE_PHOTO){
            if(data.getData() != null) {
                conference_img = data.getData();
                portraint_img.setImageURI(conference_img);
            }
        }
    }

    public File getFileByUri(Uri uri) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append( MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] {  MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex( MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex( MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = this.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
//            Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }
}
