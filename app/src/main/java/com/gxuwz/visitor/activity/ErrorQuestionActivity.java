package com.gxuwz.visitor.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gxuwz.visitor.MyApplication;
import com.gxuwz.visitor.R;
import com.gxuwz.visitor.model.bean.ErrorQuestion;
import com.gxuwz.visitor.model.bean.Question;
import com.gxuwz.visitor.util.AppDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorQuestionActivity extends BaseActivity {
    private LinearLayout answerLayout;
    private TextView questionTextView,question_number,question_answer,your_answer;
    private ImageView questionImageView;
    private RadioGroup optionsRadioGroup;
    private TextView explanationTextView;
    private Button previousButton,nextButton,submitButton;

    private AppDatabase db;

    private String answerValue;
    private int errorQuestionCount;
    private Map<String, String> answerMap;
    private List<ErrorQuestion> questions;

    private int currentQuestionIndex = 0;

    public ErrorQuestionActivity() {
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
        setContentView(R.layout.subject_question);
        db = MyApplication.database;
        initQuestion();
        initView();
        method();


    }

    private void initQuestion() {
        MyApplication.appExecutors.diskIO().execute(() -> {
            // TODO: 2024/6/28  查询数据库
            questions = db.errorQuestionDao().getAllErrorQuestion();

            Log.d("TAG", "initQuestion: " + questions.size());
        });
        new Handler(Looper.getMainLooper()).post(() -> displayQuestion(currentQuestionIndex));
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


        // 按钮
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        submitButton = findViewById(R.id.submitButton);
    }
    private void method() {
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
                        explanationTextView.setText("回答正确! " +"\n" + "本题技巧:  " + questions.get(currentQuestionIndex).getExplains());

                        db.questionDao().updateStatusById(questions.get(currentQuestionIndex).getId());
                        db.errorQuestionDao().delete(questions.get(currentQuestionIndex));
                    } else {
                        explanationTextView.setText("回答错误. " +"\n" + "本题技巧:  " + questions.get(currentQuestionIndex).getExplains());
                    }
                    //2.显示正确答案
                    answerValue = answerMap.get(questions.get(currentQuestionIndex).getAnswer());
                    question_answer.setText("正确答案: " + answerValue);
                    your_answer.setText("你的答案: " + userAnswer);
                    answerLayout.setVisibility(View.VISIBLE);



                }
            }
        });
    }
    private void displayQuestion(int index) {
        if (questions != null && !questions.isEmpty()) {
            ErrorQuestion question = questions.get(index);

            if (question.getUserAnswer() != null && !question.getUserAnswer().equals("")){
                your_answer.setText(question.getUserAnswer());
                question_answer.setText("正确答案: " + answerMap.get(question.getAnswer()));
                answerLayout.setVisibility(View.VISIBLE);

                explanationTextView.setText(  "本题技巧:  "+"\n" + question.getExplains());
            }else {
                answerLayout.setVisibility(View.GONE);
                explanationTextView.setText("");

            }

            // 题号
            question_number.setText("第" + (index+1)+ "题");

            // 题目
            questionTextView.setText(question.getQuestion());

            // 图片
            if (question.getUrl() != null & !question.getUrl().equals("")) {

                questionImageView.setVisibility(View.VISIBLE);
                Picasso.get().load(question.getUrl()).into(questionImageView);
            }
            else {
                questionImageView.setVisibility(View.GONE);

            }


            // 选项
            ((RadioButton) optionsRadioGroup.getChildAt(0)).setText(question.getItem1());
            ((RadioButton) optionsRadioGroup.getChildAt(1)).setText(question.getItem2());

//            当第3,4个选项都为空的时候表示判断题,item1:正确 item2:错误
            if (question.getItem4() != null && !question.getItem4().equals("") && question.getItem3() != null && !question.getItem3().equals("") ) {
                ((RadioButton) optionsRadioGroup.getChildAt(2)).setText(question.getItem3());
                ((RadioButton) optionsRadioGroup.getChildAt(3)).setText(question.getItem4());
                ((RadioButton) optionsRadioGroup.getChildAt(2)).setVisibility(View.VISIBLE);
                ((RadioButton) optionsRadioGroup.getChildAt(3)).setVisibility(View.VISIBLE);
            }else {
                ((RadioButton) optionsRadioGroup.getChildAt(2)).setVisibility(View.GONE);
                ((RadioButton) optionsRadioGroup.getChildAt(3)).setVisibility(View.GONE);
            }

            // 清除选项

            optionsRadioGroup.clearCheck();
        }
    }
}
