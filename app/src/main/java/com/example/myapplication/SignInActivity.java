package com.example.myapplication;

import com.example.myapplication.home.HomeActivity;
import com.example.processbutton.iml.ActionProcessButton;
import com.example.utils.CommonInterface;
import com.example.utils.Global;
import com.example.utils.ui.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;


public class SignInActivity extends Activity implements ProgressGenerator.OnCompleteListener {

    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";
    private boolean asAdmin = false;
    private String username, pwd;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String save_account = "save";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_sign_in);
        verifyStoragePermissions(this);

        sharedPreferences = getSharedPreferences(save_account, MODE_PRIVATE);
        if(sharedPreferences.getString("username", null)!=null){
            Autologin();
        }
        editor = sharedPreferences.edit();
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
                                builder.setTitle("登录失败");
                                builder.setMessage("请检查您的网络连接");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        btnSignIn.setEnabled(true);
                                        editEmail.setEnabled(true);
                                        editPassword.setEnabled(true);
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
                                        builder.setTitle("登录失败");
                                        builder.setMessage("请检查您的邮箱或密码");
                                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                btnSignIn.setEnabled(true);
                                                editEmail.setEnabled(true);
                                                editPassword.setEnabled(true);
                                            }
                                        });
                                        builder.show();
                                    }
                                });
                            }
                            else{
                                try{
                                    editor.putString("username", username);
                                    editor.putString("password", pwd);
                                    editor.commit();
                                    Global.setID(j.getString("user_id"));
                                    Global.setNickname(j.getString("user_name"));
                                    Global.setIfadmin(Boolean.parseBoolean(j.getString("admin")));
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Global.init();
                                            //Global.initWebSocket();
                                        }
                                    }).start();

                                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }


                            }
                        }
                        catch (Exception e){

                        }
                    }
                };
                CommonInterface.sendOkHttpPostRequest(login_url, cb, map);
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

    private void Autologin(){
        username = sharedPreferences.getString("username", null);
        pwd = sharedPreferences.getString("password", null);
        String login_url = "login";

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
                        builder.setTitle("登录失败");
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
                String str = response.body().string();
                System.out.println(str);
                try {

                    JSONObject j = new JSONObject(str);
                    if(j.has("error")){
                        SignInActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                                builder.setTitle("登录失败");
                                builder.setMessage("请检查您的邮箱或密码");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                builder.show();
                            }
                        });
                    }
                    else{
                        try{
                            editor.putString("username", username);
                            editor.putString("password", pwd);
                            editor.commit();
                            Global.setID(j.getString("user_id"));
                            Global.setNickname(j.getString("user_name"));
                            Global.setIfadmin(Boolean.parseBoolean(j.getString("admin")));
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Global.init();
                                }
                            }).start();

                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                catch (Exception e){
                }
            }
        };
        CommonInterface.sendOkHttpPostRequest(login_url, cb, map);
    }

    @Override
    public void onComplete() {
        Toast.makeText(this, R.string.Loading_Complete, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    // 权限申请
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };


    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
