package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.utils.CommonInterface;
import com.example.utils.Global;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class InviteActivity extends AppCompatActivity {
    private TextView finish;
    private EditText inputemail;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        finish = (TextView) findViewById(R.id.tv_finsh);
        back = (ImageView) findViewById(R.id.iv_backward);
        inputemail = (EditText) findViewById(R.id.InviteEmail);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputemail.getText().toString();
                if(!Global.judge_email(email)){
                    InviteActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(InviteActivity.this);
                            builder.setTitle("邀请失败");
                            builder.setMessage("请输入合法的邮箱地址");
                            builder.show();
                        }
                    });
                }
                else{
                    HashMap<String, String> map = new HashMap<>();
                    map.put("email", email);
                    String url = "create_admin_invite";

                    okhttp3.Callback cb = new okhttp3.Callback(){
                        @Override
                        public void onFailure(Call call, IOException e){
                            InviteActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(InviteActivity.this);
                                    builder.setTitle("邀请失败");
                                    builder.setMessage("请检查您的网络连接");
                                    builder.show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String str = response.body().string();
                            System.out.println(str);
                            try {
                                JSONObject j = new JSONObject(str);
                                if(j.has("error")){
                                    InviteActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(InviteActivity.this);
                                            builder.setTitle("邀请失败");
                                            builder.setMessage("无法向该邮箱地址发送邀请码");
                                            builder.show();
                                        }
                                    });
                                }
                                else{
                                    InviteActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(InviteActivity.this);
                                            builder.setTitle("邀请成功");
                                            builder.setMessage("已向该邮箱发送邀请码");
                                            builder.setPositiveButton("返回主界面", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            });
                                            builder.show();
                                        }
                                    });
                                }

                            }
                            catch (Exception e){}
                        }
                    };
                    CommonInterface.sendOkHttpPostRequest(url, cb, map);
                }
            }
        });
    }
}
