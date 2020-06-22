package com.example.myapplication.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.utils.CommonInterface;
import com.example.utils.Global;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class AddArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        String conference_id = getIntent().getStringExtra("conference_id");
        String program_id = getIntent().getStringExtra("program_id");

        EditText editTile = (EditText) findViewById(R.id.ArticleTitle);
        EditText editAuthor = (EditText) findViewById(R.id.ArticleAuthor);
        EditText editAbstract = (EditText) findViewById(R.id.ArticleAbstract);
        EditText editLink = (EditText) findViewById(R.id.ArticleLink);

        TextView finish = (TextView) findViewById(R.id.ArticleFinish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTile.getText().toString();
                String author = editAuthor.getText().toString();
                String abst = editAbstract.getText().toString();
                String link = editLink.getText().toString();

                try{
                    JSONObject j = new JSONObject();
                    j.put("title", title);
                    j.put("authors", author);
                    j.put("abstract", abst);
                    j.put("link", link);
                    j.put("conference_id", Global.getConference_id());
                    if(!program_id.equals("null")){
                        j.put("program_id", program_id);
                        System.out.println("add article to conference " + Global.getConference_id()+ "  program " + j.getString("program_id"));
                    }


                    String add_article_url = "add_paper";

                    okhttp3.Callback cb = new okhttp3.Callback(){
                        @Override
                        public void onFailure(Call call, IOException e){
                            AddArticleActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddArticleActivity.this);
                                    builder.setTitle("添加论文失败");
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
                                AddArticleActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddArticleActivity.this);
                                        builder.setTitle("添加论文成功");
                                        builder.setMessage("是否继续(为该议程)添加论文");
                                        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                editTile.setText("");
                                                editAbstract.setText("");
                                                editAuthor.setText("");
                                                editLink.setText("");
                                            }
                                        });
                                        builder.setNegativeButton("否，返回", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(AddArticleActivity.this, AddChooseActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                        builder.show();
                                    }
                                });
                            }
                            catch (Exception e){ e.printStackTrace();}

                        }
                    };
                    CommonInterface.sendOkHttpJsonPostRequest(add_article_url, cb, j);

                }
                catch (Exception e){e.printStackTrace();}
            }
        });
    }
}
