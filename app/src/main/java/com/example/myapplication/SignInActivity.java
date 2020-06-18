package com.example.myapplication;

import com.example.myapplication.home.HomeActivity;
import com.example.processbutton.iml.ActionProcessButton;
import com.example.utils.CommonInterface;
import com.example.utils.ui.*;
import com.example.processbutton.*;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import okhttp3.Call;
import okhttp3.Response;


public class SignInActivity extends Activity implements ProgressGenerator.OnCompleteListener {

    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";
    private boolean asAdmin = false;
    private String username, pwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_sign_in);

        /*
        final SmoothCheckBox scb = (SmoothCheckBox) findViewById(R.id.checkbox);
        scb.setChecked(false);
        scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                Log.d("SmoothCheckBox", String.valueOf(isChecked));
                asAdmin = isChecked;
            }
        });
        */

        final EditText editEmail = (EditText) findViewById(R.id.editEmail);
        final EditText editPassword = (EditText) findViewById(R.id.editPassword);

        final ProgressGenerator progressGenerator = new ProgressGenerator(this);
        final ActionProcessButton btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        final ActionProcessButton btnSignUp = (ActionProcessButton) findViewById(R.id.btnSignUp);
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.getBoolean(EXTRAS_ENDLESS_MODE)) {
            btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        } else {
            btnSignIn.setMode(ActionProcessButton.Mode.PROGRESS);
        }
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //progressGenerator.start(btnSignIn);
                btnSignIn.setEnabled(false);
                editEmail.setEnabled(false);
                editPassword.setEnabled(false);

                //TODO
                //发送登录http请求
                if(!asAdmin){
                    username = editEmail.getText().toString();
                    pwd = editPassword.getText().toString();
                    String login_url = "login";
                    String TAG = "SignIn";

                    HashMap<String, String> map = new HashMap<>();
                    map.put("email", username);
                    map.put("password", pwd);

                    okhttp3.Callback cb = new okhttp3.Callback(){
                        @Override
                        public void onFailure(Call call, IOException e){
                            SignInActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                                    builder.setTitle("登陆失败");
                                    builder.setMessage("请检查您的网络连接");
                                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(SignInActivity.this, SignInActivity.class);
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
                                if(j.has("error")){
                                    SignInActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                                            builder.setTitle("登陆失败");
                                            builder.setMessage("请检查您的邮箱或密码");
                                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(SignInActivity.this, SignInActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                            builder.show();
                                        }
                                    });
                                }
                                else{
                                    SignInActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String imname = username.replace("@", "s").replace(".","s");
                                            EMClient.getInstance().login(imname, pwd, new EMCallBack() {
                                                @Override
                                                public void onSuccess() {
                                                    EMClient.getInstance().groupManager().loadAllGroups();
                                                    EMClient.getInstance().chatManager().loadAllConversations();
                                                    Log.d("main", "登录聊天服务器成功！");
                                                }

                                                @Override
                                                public void onError(int i, String s) {

                                                }

                                                @Override
                                                public void onProgress(int i, String s) {
                                                    Log.d("main", "登录聊天服务器失败！");
                                                }
                                            });
                                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                            catch (Exception e){

                            }
                            //EMClient.getInstance().createAccount();
                        }
                    };
                    CommonInterface.sendOkHttpPostRequest(login_url, cb, map);
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignUp = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(toSignUp);
                //overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
            }
        });
    }

    @Override
    public void onComplete() {
        Toast.makeText(this, R.string.Loading_Complete, Toast.LENGTH_LONG).show();
    }

}
