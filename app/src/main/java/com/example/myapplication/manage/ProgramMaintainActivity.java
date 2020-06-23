package com.example.myapplication.manage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.admin.AddArticleActivity;
import com.example.myapplication.detail.DetailActivity;
import com.example.myapplication.home.paper.Paper;
import com.example.myapplication.home.paper.PaperListAdapter;
import com.example.myapplication.home.paper.PaperViewModel;
import com.example.utils.CommonInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.example.myapplication.detail.DetailActivity.EXTRA_ID;
import static com.example.myapplication.detail.DetailActivity.EXTRA_TITLE;
import static com.example.myapplication.detail.DetailActivity.EXTRA_TYPE;

public class ProgramMaintainActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.myapplication.manage.program_maintain.id";
    public static final String EXTRA_CONFID = "com.myapplication.manage.program_maintain.confid";
    private static final int TYPE = 3;

    private int program_id;
    private int conference_id;

    private PaperListAdapter mAdapter;
    private PaperViewModel mPaperViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_maintain);

        Intent intent = getIntent();
        if (intent != null) {
            program_id = intent.getIntExtra(EXTRA_ID, 0);
            conference_id = intent.getIntExtra(EXTRA_CONFID, 0);
        }

        final Toolbar myToolbar = findViewById(R.id.my_toolbar_program_maintain_activity);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("议程论文管理");

        RecyclerView mRecyclerView = findViewById(R.id.collection_paper_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL));

        mAdapter = new PaperListAdapter(this);
        mAdapter.setOnItemClickListener(new PaperListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Paper m = mAdapter.getPaperAtPosition(position);

                Intent intent = new Intent(ProgramMaintainActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(EXTRA_TYPE, 1);
                bundle.putInt(EXTRA_ID, m.getID());
                bundle.putString(EXTRA_TITLE, m.getTitle());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mPaperViewModel = new ViewModelProvider(this).get(PaperViewModel.class);
        mPaperViewModel.getPapers().observe(this, new Observer<List<Paper>>() {
            @Override
            public void onChanged(List<Paper> papers) {
                mAdapter.setPapers(papers);
            }
        });
        mPaperViewModel.setType(TYPE);
        mPaperViewModel.setProgramId(program_id);
        mPaperViewModel.update();

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getLayoutPosition();
                Paper m = mAdapter.getPaperAtPosition(pos);

                int id = m.getID();

                // network request
                JSONObject json = new JSONObject();
                try {
                    json.put("paper_id", id);
                } catch (Exception e) {
                    System.out.println(e);
                }

                okhttp3.Callback cb = new okhttp3.Callback() {
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response)
                            throws IOException {
                        String str = response.body().string();
                        System.out.println(str);
                        try {
                            JSONObject j = new JSONObject(str);
                            if (j.getBoolean("success")) {
                                mPaperViewModel.update();
                                Toast.makeText(ProgramMaintainActivity.this,
                                        "删除成功",
                                        Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(ProgramMaintainActivity.this,
                                        "网络错误，删除失败",
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                            Toast.makeText(ProgramMaintainActivity.this,
                                    "网络错误，删除失败",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Toast.makeText(ProgramMaintainActivity.this,
                                "网络错误，删除失败",
                                Toast.LENGTH_LONG).show();
                    }
                };

                CommonInterface.sendOkHttpJsonPostRequest("delete_paper", cb, json);
            }
        });
        helper.attachToRecyclerView(mRecyclerView);

        FloatingActionButton button = findViewById(R.id.program_float_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        ProgramMaintainActivity.this,
                        AddArticleActivity.class
                );
                intent.putExtra("conference_id", String.valueOf(conference_id));
                intent.putExtra("program_id", String.valueOf(program_id));
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
    }

    public void onResume() {
        super.onResume();
        if (mPaperViewModel != null)
            mPaperViewModel.update();
    }
}