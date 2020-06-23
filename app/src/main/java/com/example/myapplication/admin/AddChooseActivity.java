package com.example.myapplication.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.home.HomeActivity;
import com.example.processbutton.iml.ActionProcessButton;

public class AddChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_choose);

        final ActionProcessButton add_program = (ActionProcessButton) findViewById(R.id.btnProgram);
        final ActionProcessButton add_article = (ActionProcessButton) findViewById(R.id.btnArticle);
        String conference_id = getIntent().getStringExtra("conference_id");
        ImageView back = (ImageView) findViewById(R.id.iv_backward);

        add_program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddChooseActivity.this, AddProgramActivity.class);
                intent.putExtra("conference_id", conference_id);
                startActivity(intent);
                finish();
            }
        });

        add_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddChooseActivity.this, AddArticleActivity.class);
                intent.putExtra("conference_id", conference_id);
                intent.putExtra("program_id", "null");
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddChooseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddChooseActivity.this);
                        builder.setTitle("退出编辑");
                        builder.setMessage("您完成了本次会议所有议程和论文的添加？");
                        builder.setCancelable(false);
                        builder.setPositiveButton("是的，退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(AddChooseActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                        builder.setNegativeButton("还没，继续编辑", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();
                    }
                });
            }
        });

    }
}
