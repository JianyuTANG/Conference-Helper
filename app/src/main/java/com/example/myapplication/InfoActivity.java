package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utils.CommonInterface;
import com.example.widget.ItemGroup;
import com.example.widget.RoundImageView;
import com.example.widget.TitleLayout;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

public class InfoActivity extends AppCompatActivity {
    private ItemGroup ig_id, ig_nickname, ig_institution, ig_position, ig_website, ig_signature, ig_direction;
    private RoundImageView ri_avatar;
    private SharedPreferences sharedPreferences;
    private String info_save = "com.example.myapplication.InfoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ig_id = (ItemGroup) findViewById(R.id.ig_id);
        ig_nickname = (ItemGroup) findViewById(R.id.ig_nickname);
        ig_institution = (ItemGroup)findViewById(R.id.ig_institution);
        ig_position = (ItemGroup)findViewById(R.id.ig_postiion);
        ig_direction = (ItemGroup) findViewById(R.id.ig_direction);
        ig_website = (ItemGroup)findViewById(R.id.ig_website);
        ig_signature = (ItemGroup)findViewById(R.id.ig_signature);

        ri_avatar = (RoundImageView)findViewById(R.id.ri_portrait);

        sharedPreferences = getSharedPreferences(info_save, MODE_PRIVATE);


        TextView finish_change = (TextView) findViewById(R.id.tv_finsh);
        finish_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = ig_nickname.contentEdt.getText().toString();
                String institution = ig_institution.contentEdt.getText().toString();
                String position = ig_position.contentEdt.getText().toString();
                String direction = ig_direction.contentEdt.getText().toString();
                String website = ig_website.contentEdt.getText().toString();
                String signature = ig_signature.contentEdt.getText().toString();

                String url = "set_userinfo";
                String TAG = "UploadInfo";

                HashMap<String, String> map = new HashMap<>();
                map.put("nickname", nickname);
                map.put("institution", institution);
                map.put("position", position);
                map.put("research_topic", direction);
                map.put("website", website);
                map.put("signature", signature);

                okhttp3.Callback cb = new okhttp3.Callback(){
                    @Override
                    public void onFailure(Call call, IOException e){

                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try{
                            JSONObject str = new JSONObject(response.body().toString());
                            System.out.println(str);
                            Toast.makeText(InfoActivity.this, "保存成功!", Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e){}
                    }
                };
                CommonInterface.sendOkHttpPostRequest(url, cb, map);
            }
        });
    }

    private void initInfo(){

    }
}
