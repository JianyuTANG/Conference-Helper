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

import com.example.myapplication.home.HomeActivity;
import com.example.utils.CommonInterface;
import com.example.utils.Global;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class ModifyPwdActivity extends AppCompatActivity {
    private ImageView back;
    private TextView finish;
    private EditText opwd, npwd, npwda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);

        opwd = (EditText)findViewById(R.id.editOldPwd);
        npwd = (EditText)findViewById(R.id.editNewPwd);
        npwda = (EditText)findViewById(R.id.editNewPwdAgain);

        back = (ImageView)findViewById(R.id.iv_backward);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModifyPwdActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        finish = (TextView)findViewById(R.id.tv_finsh);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpwd = opwd.getText().toString();
                String newpwd = npwd.getText().toString();
                String newpwd2 = npwda.getText().toString();

                if(!newpwd.equals(newpwd2)){
                    ModifyPwdActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ModifyPwdActivity.this);
                            builder.setTitle("设置新密码失败");
                            builder.setMessage("两次输入新密码不一致");
                            builder.setPositiveButton("重新输入", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    npwd.setText("");
                                    npwda.setText("");
                                }
                            });
                            builder.show();
                        }
                    });
                }
                else{
                    String mpwd_url = "alter_pwd";
                    HashMap<String, String> map = new HashMap<>();
                    map.put("cur_password", oldpwd);
                    map.put("new_password", newpwd);

                    okhttp3.Callback cb = new okhttp3.Callback(){
                        @Override
                        public void onFailure(Call call, IOException e){
                            ModifyPwdActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ModifyPwdActivity.this);
                                    builder.setTitle("设置新密码失败");
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
                                boolean success = Boolean.parseBoolean(j.getString("success"));
                                if(success){
                                    ModifyPwdActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ModifyPwdActivity.this);
                                            builder.setTitle("设置新密码成功");
                                            builder.setMessage("请牢记您的新密码");
                                            builder.setPositiveButton("返回主界面", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(ModifyPwdActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            });
                                            builder.show();
                                        }
                                    });
                                }
                                else{
                                    ModifyPwdActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ModifyPwdActivity.this);
                                            builder.setTitle("设置新密码失败");
                                            builder.setMessage("请检查您的输入");
                                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });
                                            builder.show();
                                        }
                                    });
                                }
                            }
                            catch (Exception e){

                            }
                        }
                    };
                    CommonInterface.sendOkHttpPostRequest(mpwd_url, cb, map);
                }

            }
        });

    }
}
