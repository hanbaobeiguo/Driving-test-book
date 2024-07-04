package com.gxuwz.visitor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gxuwz.visitor.MyApplication;
import com.gxuwz.visitor.R;
import com.gxuwz.visitor.model.bean.Question;
import com.gxuwz.visitor.model.bean.User;
import com.gxuwz.visitor.util.AppDatabase;

import java.util.List;

public class ErrorQuestionFrontActivity extends BaseActivity implements View.OnClickListener {

    private AppDatabase db;

    private Button btn_wrong_questions;
    private TextView tv_wrong_questions, clearButton, tv_error_rate, tv_advise;

    private MyApplication application;
    private User user;
    private List<Question> ReviewQuestions, errorQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (MyApplication) getApplication();
        user = application.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errorfront);
        db = MyApplication.database;
        initView();
        loadData();
    }

    private void initView() {
        btn_wrong_questions = findViewById(R.id.btn_wrong_questions);
        tv_wrong_questions = findViewById(R.id.tv_wrong_questions);
        clearButton = findViewById(R.id.clearButton);
        tv_error_rate = findViewById(R.id.tv_error_rate);
        tv_advise = findViewById(R.id.tv_advise);

        btn_wrong_questions.setOnClickListener(this);
        clearButton.setOnClickListener(this);
    }

    private void loadData() {
        MyApplication.appExecutors.diskIO().execute(() -> {
            // TODO: 2024/6/28  查询数据库
            ReviewQuestions = db.questionDao().getReviewQuestions();
            errorQuestion = db.questionDao().getAllErrorQuestions();
            Log.d("数据库查询", "错误题目数量：" + errorQuestion.size() + "题");
            Log.d("数据库查询", "回答题目数量：" + ReviewQuestions.size() + "题");

            new Handler(Looper.getMainLooper()).post(() -> updateUI());
        });
    }

    private void updateUI() {
        if (ReviewQuestions != null && errorQuestion != null) {
            Integer rate = (errorQuestion.size() * 100) / ReviewQuestions.size();
            tv_wrong_questions.setText(errorQuestion.size() + "题");
            tv_error_rate.setText("错题率：" + rate + "%");
            tv_advise.setText("建议：请多做题，提高自己的能力！");
        } else {
            tv_wrong_questions.setText("暂无错题");
            tv_error_rate.setText("暂无错题");
            tv_advise.setText("暂无建议");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_wrong_questions:
                Intent intent = new Intent(this, ErrorQuestionActivity.class);
                startActivity(intent);
                break;
            case R.id.clearButton:
                db.errorQuestionDao().deleteAll();
                Toast.makeText(getApplicationContext(), "清除成功", Toast.LENGTH_SHORT).show();
                db.questionDao().updateStatusAll();
                break;
            default:
                break;
        }
    }
}
