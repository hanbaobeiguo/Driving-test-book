package com.gxuwz.visitor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gxuwz.visitor.MyApplication;
import com.gxuwz.visitor.R;
import com.gxuwz.visitor.api.QuestionApiService;
import com.gxuwz.visitor.model.bean.ErrorQuestion;
import com.gxuwz.visitor.model.bean.ExamRecord;
import com.gxuwz.visitor.model.bean.Question;
import com.gxuwz.visitor.model.bean.User;
import com.gxuwz.visitor.model.vo.QuestionResponse;
import com.gxuwz.visitor.network.RetrofitClient;
import com.gxuwz.visitor.util.AppDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExamActivity extends BaseActivity {
    private LinearLayout answerLayout;
    private RelativeLayout countdown_layout;
    private TextView questionTextView, question_number, question_answer, your_answer, explanationTextView;
    private ImageView questionImageView;
    private RadioGroup optionsRadioGroup;
    //倒计时控件
    private TextView countdown_timer;
    private CountDownTimer timer;
    private long startTime;
    private long endTime;

    private Integer score = 0;
    private Button previousButton, nextButton, submitButton, submit_exam_button;

    private AppDatabase db;
    private MyApplication myApplication;
    private User currentUser;

    private String answerValue;
    private Map<String, String> answerMap;
    private List<Question> questions;

    private int currentQuestionIndex = 0;

    public ExamActivity() {
        this.answerMap = new HashMap<>();
        answerMap.put("1", "A或者正确");
        answerMap.put("2", "B或者错误");
        answerMap.put("3", "C");
        answerMap.put("4", "D");
        answerMap.put("5", "AB");
        answerMap.put("6", "AC");
        answerMap.put("7", "AD");
        answerMap.put("8", "BC");
        answerMap.put("9", "BD");
        answerMap.put("10", "CD");
        answerMap.put("11", "ABC");
        answerMap.put("12", "ABD");
        answerMap.put("13", "ACD");
        answerMap.put("14", "BCD");
        answerMap.put("15", "ABCD");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getApplication();
        currentUser = myApplication.getCurrentUser();
//
//        if (currentUser == null) {
//            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
//            finish();
//        } else {
            setContentView(R.layout.subject_question);
            //获取考试开始时间
            startTime = System.currentTimeMillis();
            // 获取当前用户
            initView();

            //获取intent传参
            Intent intent = getIntent();
            Integer subject = null;
            String model = null;
            String testType = null;
            if (intent != null) {
                subject = intent.getIntExtra("subject", 1);
                model = intent.getStringExtra("model");
                testType = intent.getStringExtra("testType");
            }

            HttpMethod(subject, model, testType);

            displayQuestion(currentQuestionIndex);
            db = MyApplication.database;
            method();

    }

    private void HttpMethod(int subject, String model, String testType) {
        // 网络请求
        Retrofit retrofit = RetrofitClient.getClient("http://v.juhe.cn/");
        QuestionApiService apiService = retrofit.create(QuestionApiService.class);

        // 设置请求参数
        String apiKey = "2503a403bdd2b6b5aaaf841a9ff6e33d";

        Call<QuestionResponse> Questioncall = apiService.getQuestions(apiKey, subject, model, testType);

        Questioncall.enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 处理请求成功时题库数据
                    questions = response.body().getResult();

                    Log.d("请求成功", "请求成功" + response.body().getResult().size());
                } else {
                    // 打印响应错误信息
                    Log.e("请求失败", "Response not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {
                // 打印错误信息
                Log.e("请求失败", "请求失败", t);
            }
        });
    }

    private void initView() {
        // 题号
        question_number = findViewById(R.id.question_number);
        questionTextView = findViewById(R.id.questionTextView);

        // 答案
        answerLayout = findViewById(R.id.answer_layout);
        question_answer = findViewById(R.id.question_answer);
        your_answer = findViewById(R.id.your_answer);

        // 选项
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);

        // 图片
        questionImageView = findViewById(R.id.questionImageView);

        explanationTextView = findViewById(R.id.explanation);

        //倒计时
        countdown_layout = findViewById(R.id.countdown_layout);
        countdown_timer = findViewById(R.id.countdown_timer);

        // 按钮
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        submitButton = findViewById(R.id.submitButton);
        submit_exam_button = findViewById(R.id.submit_exam_button);
    }

    private void method() {
        //上一题按钮
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex >= 1) {
                    currentQuestionIndex--;
                    //隐藏答案
                    answerLayout.setVisibility(View.GONE);
                    displayQuestion(currentQuestionIndex);
                }
            }
        });
        //下一题按钮
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++;
                    //隐藏答案
                    answerLayout.setVisibility(View.GONE);
                    displayQuestion(currentQuestionIndex);
                }
            }
        });
        //确认答案按钮
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedOption = optionsRadioGroup.getCheckedRadioButtonId();
                if (selectedOption != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedOption);
                    int selectedIndex = optionsRadioGroup.indexOfChild(selectedRadioButton);

                    String userAnswer = "";
                    switch (selectedIndex) {
                        case 0:
                            userAnswer = "A";
                            break;
                        case 1:
                            userAnswer = "B";
                            break;
                        case 2:
                            userAnswer = "C";
                            break;
                        case 3:
                            userAnswer = "D";
                            break;
                    }

                    if (selectedIndex == Integer.parseInt(questions.get(currentQuestionIndex).getAnswer()) - 1) {
                        explanationTextView.setText("回答正确! " + "\n" + "本题技巧:  " + questions.get(currentQuestionIndex).getExplains());
                        questions.get(currentQuestionIndex).setStatus(1);
                        score++;
                    } else {
                        explanationTextView.setText("回答错误. " + "\n" + "本题技巧:  " + questions.get(currentQuestionIndex).getExplains());
                        //1.加入错题本
                        questions.get(currentQuestionIndex).setStatus(2);
                        //2.更新错题本
                        MyApplication.appExecutors.diskIO().execute(() -> {
                            //如果错题本中没有该题目，则插入
                            if (db.questionDao().getQuestionById(questions.get(currentQuestionIndex).getId()) == null) {
                                ErrorQuestion errorQuestion = new ErrorQuestion(questions.get(currentQuestionIndex), currentUser.getId());
                                db.errorQuestionDao().insert(errorQuestion);
                            }
                        });
                    }
                    //2.显示正确答案
                    answerValue = answerMap.get(questions.get(currentQuestionIndex).getAnswer());
                    question_answer.setText("正确答案: " + answerValue);
                    your_answer.setText("你的答案: " + userAnswer);
                    answerLayout.setVisibility(View.VISIBLE);

                    //3.更新数据库
                    questions.get(currentQuestionIndex).setUserAnswer("你的答案: " + userAnswer);
                    MyApplication.appExecutors.diskIO().execute(() -> {
                        db.questionDao().update(questions.get(currentQuestionIndex));
                    });
                }
            }
        });

        //交卷按钮
        submit_exam_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 停止倒计时
                if (timer != null) {
                    timer.cancel();
                }
                // 保存试卷
                saveExamRecord(recordEndTime());
                // 跳转到结果页面
                Intent intent = new Intent(ExamActivity.this, ExamRecordActivity.class);
                intent.putExtra("score", score);
                startActivity(intent);

            }
        });

        //倒计时
        // 设置倒计时时间为 60 分钟（3600000 毫秒）
        // 启动倒计时
        countdown_layout.setVisibility(View.VISIBLE);
        timer = new CountDownTimer(3600000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                countdown_timer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                countdown_timer.setText("时间到!");
                // 在这里处理倒计时结束的逻辑，例如自动提交试卷
                countdown_layout.setVisibility(View.GONE);
                //获取花费时间
                String spaceTime = recordEndTime();
                //保存试卷
                saveExamRecord(spaceTime);
            }
        }.start();
    }

    //保存试卷
    private void saveExamRecord(String spaceTime) {
        MyApplication.appExecutors.diskIO().execute(() -> {
            // TODO: 2024/6/28  保存试卷
            ExamRecord examRecord = new ExamRecord();
            examRecord.setTime(spaceTime);
            examRecord.setScore(score);
            examRecord.setUserId(currentUser.getId());
            db.examRecordDao().insert(examRecord);
        });
    }

    // 销毁时停止倒计时
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    //装载数据
    private void displayQuestion(int index) {
        if (questions != null && !questions.isEmpty()) {
            Question question = questions.get(index);

            if (question.getUserAnswer() != null && !question.getUserAnswer().equals("")) {
                your_answer.setText(question.getUserAnswer());
                question_answer.setText("正确答案: " + answerMap.get(question.getAnswer()));
                answerLayout.setVisibility(View.VISIBLE);

                explanationTextView.setText("本题技巧:  " + "\n" + question.getExplains());
            } else {
                answerLayout.setVisibility(View.GONE);
                explanationTextView.setText("");
            }

            // 题号
            question_number.setText("第" + (index + 1) + "题");

            // 题目
            questionTextView.setText(question.getQuestion());

            // 图片
            if (question.getUrl() != null && !question.getUrl().equals("")) {
                questionImageView.setVisibility(View.VISIBLE);
                Picasso.get().load(question.getUrl()).into(questionImageView);
            } else {
                questionImageView.setVisibility(View.GONE);
            }

            // 选项
            ((RadioButton) optionsRadioGroup.getChildAt(0)).setText(question.getItem1());
            ((RadioButton) optionsRadioGroup.getChildAt(1)).setText(question.getItem2());

            // 当第3,4个选项都为空的时候表示判断题,item1:正确 item2:错误
            if (question.getItem4() != null && !question.getItem4().equals("") && question.getItem3() != null && !question.getItem3().equals("")) {
                ((RadioButton) optionsRadioGroup.getChildAt(2)).setText(question.getItem3());
                ((RadioButton) optionsRadioGroup.getChildAt(3)).setText(question.getItem4());
                ((RadioButton) optionsRadioGroup.getChildAt(2)).setVisibility(View.VISIBLE);
                ((RadioButton) optionsRadioGroup.getChildAt(3)).setVisibility(View.VISIBLE);
            } else {
                ((RadioButton) optionsRadioGroup.getChildAt(2)).setVisibility(View.GONE);
                ((RadioButton) optionsRadioGroup.getChildAt(3)).setVisibility(View.GONE);
            }

            // 清除选项
            optionsRadioGroup.clearCheck();
        }
    }

    //记录时间
    private String recordEndTime() {
        endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        long minutes = (timeTaken / 1000) / 60;
        long seconds = (timeTaken / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
