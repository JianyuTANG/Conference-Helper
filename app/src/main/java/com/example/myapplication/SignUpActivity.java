package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.processbutton.SmoothCheckBox;
import com.example.processbutton.iml.ActionProcessButton;
import com.example.utils.CommonInterface;
import com.hyphenate.chat.EMClient;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class SignUpActivity extends Activity {
    private boolean asAdmin = false;
    private String username;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final ActionProcessButton btnSignUp = (ActionProcessButton) findViewById(R.id.btnSignUp);
        final SmoothCheckBox scb = (SmoothCheckBox) findViewById(R.id.checkbox);
        scb.setChecked(false);
        scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                Log.d("SmoothCheckBox", String.valueOf(isChecked));
                asAdmin = isChecked;
            }
        });


        final EditText editEmail = (EditText) findViewById(R.id.editEmail);
        final EditText editPassword = (EditText) findViewById(R.id.editPassword);
        final EditText editNickname = (EditText) findViewById(R.id.editNickname);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressGenerator.start(btnSignUp);
                btnSignUp.setEnabled(false);
                editEmail.setEnabled(false);
                editPassword.setEnabled(false);
                editNickname.setEnabled(false);

                //TODO
                //发送登录http请求
                username = editEmail.getText().toString();
                pwd = editPassword.getText().toString();
                String nickname = editNickname.getText().toString();
                String login_url = "logon";
                String TAG = "SignUp";

                HashMap<String, String> map = new HashMap<>();
                map.put("email", username);
                map.put("password", pwd);
                map.put("nickname", nickname);
                map.put("admin", String.valueOf(asAdmin));

                okhttp3.Callback cb = new okhttp3.Callback(){
                    @Override
                    public void onFailure(Call call, IOException e){
                        SignUpActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setTitle("注册失败");
                                builder.setMessage("请检查您的网络连接");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(SignUpActivity.this, SignUpActivity.class);
                                        startActivity(intent);
                                    }
                                });
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
                            if(j.has("user_id")){
                                SignUpActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String imname = username.replace("@", "s").replace(".","s");
                                        try{
                                            EMClient.getInstance().createAccount(imname, pwd);

                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                            builder.setTitle("注册成功");
                                            builder.setMessage("开始体验吧！");
                                            builder.setPositiveButton("返回登录", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                            builder.show();
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            else if(j.has("error")){
                                SignUpActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                        builder.setTitle("注册失败");
                                        builder.setMessage("该邮箱已被注册");
                                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(SignUpActivity.this, SignUpActivity.class);
                                                startActivity(intent);
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
                CommonInterface.sendOkHttpPostRequest(login_url, cb, map);
            }
        });

    }

}
