package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.IDNA;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.chat.ChatActivity;
import com.example.myapplication.home.HomeActivity;
import com.example.utils.CommonInterface;
import com.example.utils.Global;
import com.example.widget.ItemGroup;
import com.example.widget.RoundImageView;
import com.example.widget.TitleLayout;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class InfoActivity extends AppCompatActivity {
    private ItemGroup ig_id, ig_nickname, ig_institution, ig_position, ig_website, ig_signature, ig_direction;
    private SimpleDraweeView ri_avatar;
    private ImageView back;
    private SharedPreferences sharedPreferences;
    private String info_save = "com.example.myapplication.InfoActivity";
    private static int TAKE_PHOTO = 1;
    private static final String base_path = "data/user/0/com.example.myapplication/files/";
    private static final String server_path = "http://123.56.88.4:1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ig_id = (ItemGroup) findViewById(R.id.ig_id);
        ig_id.contentEdt.setEnabled(false);
        ig_nickname = (ItemGroup) findViewById(R.id.ig_nickname);
        ig_institution = (ItemGroup)findViewById(R.id.ig_institution);
        ig_position = (ItemGroup)findViewById(R.id.ig_postiion);
        ig_direction = (ItemGroup) findViewById(R.id.ig_direction);
        ig_website = (ItemGroup)findViewById(R.id.ig_website);
        ig_signature = (ItemGroup)findViewById(R.id.ig_signature);

        ri_avatar = (SimpleDraweeView) findViewById(R.id.ri_portrait);

        //sharedPreferences = getSharedPreferences(info_save, MODE_PRIVATE);
        initInfo();

        back = (ImageView)findViewById(R.id.iv_backward);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ri_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });




        TextView finish_change = (TextView) findViewById(R.id.tv_finsh);
        finish_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = ig_id.contentEdt.getText().toString();
                String nickname = ig_nickname.contentEdt.getText().toString();
                String institution = ig_institution.contentEdt.getText().toString();
                String position = ig_position.contentEdt.getText().toString();
                String direction = ig_direction.contentEdt.getText().toString();
                String website = ig_website.contentEdt.getText().toString();
                String signature = ig_signature.contentEdt.getText().toString();

                String url = "set_userinfo";

                HashMap<String, String> map = new HashMap<>();

                map.put("user_id", Global.getID());
                map.put("nickname", nickname);
                map.put("institution", institution);
                map.put("position", position);
                map.put("research_topic", direction);
                map.put("website", website);
                map.put("signature", signature);

                okhttp3.Callback cb = new okhttp3.Callback(){
                    @Override
                    public void onFailure(Call call, IOException e){
                        InfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
                                builder.setTitle("保存失败");
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
                        Global.setNickname(nickname);
                        InfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
                                builder.setTitle("保存成功！");
                                builder.setMessage("返回主界面");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                                builder.show();
                            }
                        });
                    }
                };
                CommonInterface.sendOkHttpPostRequest(url, cb, map);
            }
        });
    }

    private void initInfo(){
        HashMap<String, String> view_map = new HashMap<>();
        view_map.put("user_id", Global.getID());
        String view_url = "view_user";

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String avatar = base_path + Global.getID() + "_avatar.jpg";
//                File f = new File(avatar);
//                if(!f.exists()){
//                    try {
//                        Bitmap bm = CommonInterface.getImage("media/user_avatar/" + Global.getID());
//                        f.createNewFile();
//                        FileOutputStream save = new FileOutputStream(f);
//                        bm.compress(Bitmap.CompressFormat.JPEG, 80, save);
//                        save.flush();
//                        save.close();
//                        InfoActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ri_avatar.setImageBitmap(bm);
//                            }
//                        });
//                    }
//                    catch (Exception e){e.printStackTrace();}
//                }
//                else{
//                    InfoActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ri_avatar.setImageURI(Uri.parse(avatar));
//                        }
//                    });
//                }
//            }
//        }).start();


        okhttp3.Callback cb = new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e){

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //JSONObject str = new JSONObject(response.body().toString());
                String str = response.body().string();
                System.out.println(str);

                InfoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            JSONObject j = new JSONObject(str);
                            ig_id.contentEdt.setText(Global.getID());
                            if(!j.has("error")){
                                ig_institution.contentEdt.setText(j.getString("institution"));
                                ig_nickname.contentEdt.setText(j.getString("nickname"));
                                ig_signature.contentEdt.setText(j.getString("signature"));
                                ig_direction.contentEdt.setText(j.getString("research_topic"));
                                ig_position.contentEdt.setText(j.getString("position"));
                                ig_website.contentEdt.setText(j.getString("website"));
                                ri_avatar.setImageURI(Uri.parse(server_path + j.getString("avatar_url")));
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

                //if(str.get("success") == "true")
                //Toast.makeText(InfoActivity.this, "保存成功!", Toast.LENGTH_LONG).show();
            }
        };
        CommonInterface.sendOkHttpPostRequest(view_url, cb, view_map);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_PHOTO){
            if(data.getData() != null) {
                Uri uri = data.getData();
                String upload_url = "upload_user_avatar";
                try{
                    File f = getFileByUri(uri);
                    okhttp3.Callback cb = new okhttp3.Callback(){
                        @Override
                        public void onFailure(Call call, IOException e){
                            e.printStackTrace();
                            InfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(InfoActivity.this, "上传头像失败！", Toast.LENGTH_SHORT).show();
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
                                String aurl = j.getString("avatar_url");
                                if(aurl != null){
                                    Global.setAvatar(aurl);
                                    System.out.println("update avatar url: " + aurl);
                                }
                            }
                            catch (Exception e){e.printStackTrace();}

                            InfoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(InfoActivity.this, "上传头像成功！", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    };
                    CommonInterface.sendOkHttpFile(upload_url, cb, f);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "上传头像失败！", Toast.LENGTH_SHORT).show();
                }
                ri_avatar.setImageURI(uri);
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
