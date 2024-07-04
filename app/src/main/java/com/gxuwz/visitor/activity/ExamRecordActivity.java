package com.gxuwz.visitor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gxuwz.visitor.MyApplication;
import com.gxuwz.visitor.R;
import com.gxuwz.visitor.adapter.ExamRecordAdapter;
import com.gxuwz.visitor.model.bean.ExamRecord;
import com.gxuwz.visitor.model.bean.User;
import com.gxuwz.visitor.util.AppDatabase;

import java.util.List;

public class ExamRecordActivity extends BaseActivity implements View.OnClickListener{

    private AppDatabase db;
    private ListView RecordeListView;
    private List<ExamRecord> examRecordList;
    private LinearLayout score_layout;
    private Handler handler = new Handler();
    private TextView score_text,if_pass_text,recordSize_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examrecord);
        db = MyApplication.database;
        database();
        initView();
    }

    private void database() {
        MyApplication.appExecutors.diskIO().execute(() -> {
            // TODO: 2024/6/28  查询数据库
            examRecordList = db.examRecordDao().getAllRecords();
            Log.d("考试记录数据量", "database: " + examRecordList);
            handler.post(this::method);
        });
    }

    private void method() {
        if(examRecordList != null){
            RecordeListView = findViewById(R.id.RecordeListView);
            ExamRecordAdapter adapter = new ExamRecordAdapter(this,R.layout.record_item,examRecordList);
            RecordeListView.setAdapter(adapter);
        }
        recordSize_text.setText("考试记录数量：" + examRecordList.size());
        Intent intent = getIntent();
        String score = intent.getStringExtra("score");
        if(score != null) {
            score_text.setText(score);
            score_layout.setVisibility(View.VISIBLE);
            int score1 = Integer.parseInt(score);
            if (score1 >= 60) {
                if_pass_text.setText("通过");
            } else {
                if_pass_text.setText("未通过");
            }
        }
    }

    private void initView() {
        RecordeListView  = findViewById(R.id.RecordeListView);
        score_layout = findViewById(R.id.score_layout);
        score_text = findViewById(R.id.score_text);
        if_pass_text = findViewById(R.id.if_pass_text);
        recordSize_text = findViewById(R.id.recordSize_text);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }



}