package com.example.myapplication;

import com.example.processbutton.iml.ActionProcessButton;
import com.example.utils.CommonInterface;
import com.example.utils.ui.*;
import com.example.processbutton.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;


public class SignInActivity extends Activity implements ProgressGenerator.OnCompleteListener {

    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";
    private boolean asAdmin = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_sign_in);

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


                progressGenerator.start(btnSignIn);
                btnSignIn.setEnabled(false);
                editEmail.setEnabled(false);
                editPassword.setEnabled(false);

                //TODO
                //发送登录http请求
                if(!asAdmin){
                    String email = editEmail.getText().toString();
                    String password = editPassword.getText().toString();
                    String login_url = "login";
                    String TAG = "SignIn";

                    HashMap<String, String> map = new HashMap<>();
                    map.put("email", email);
                    map.put("password", password);

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
