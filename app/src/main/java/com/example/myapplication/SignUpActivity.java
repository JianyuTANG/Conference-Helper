package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.processbutton.SmoothCheckBox;
import com.example.processbutton.iml.ActionProcessButton;
import com.example.utils.CommonInterface;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class SignUpActivity extends Activity {
    private boolean asAdmin = false;

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

                //TODO
                //发送登录http请求
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                String nickname = editNickname.getText().toString();
                String login_url = "logon";
                String TAG = "SignUp";

                HashMap<String, String> map = new HashMap<>();
                map.put("email", email);
                map.put("password", password);
                map.put("nickname", nickname);
                map.put("admin", String.valueOf(asAdmin));
                System.out.println("admin:" + asAdmin);
                okhttp3.Callback cb = new okhttp3.Callback(){
                    @Override
                    public void onFailure(Call call, IOException e){

                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string();
                        System.out.println(str);
                    }
                };
                CommonInterface.sendOkHttpPostRequest(login_url, cb, map);
            }
        });
    }
}
