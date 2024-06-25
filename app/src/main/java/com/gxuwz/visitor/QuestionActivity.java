package com.gxuwz.visitor;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gxuwz.visitor.api.QuestionApiService;
import com.gxuwz.visitor.model.bean.Question;
import com.gxuwz.visitor.model.vo.AnswerResponse;
import com.gxuwz.visitor.model.vo.QuestionResponse;
import com.gxuwz.visitor.network.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QuestionActivity extends AppCompatActivity {
    private LinearLayout answerLayout;
    private TextView questionTextView,question_number,question_answer,your_answer;
    private ImageView questionImageView;
    private RadioGroup optionsRadioGroup;
    private TextView explanationTextView;
    private Button previousButton,nextButton,submitButton;
    private String answerValue;
    private Map<String, String> answerMap;
    private List<Question> questions;
    
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_question);
        initView();

        int subject = getIntent().getIntExtra("subject",1);
        String model = getIntent().getStringExtra("model");
        String testType = getIntent().getStringExtra("testType");
//        Toast.makeText(getApplicationContext(), "参数为"+subject+model+testType, Toast.LENGTH_SHORT).show();
        HttpMethod(subject,model,testType);
        method();

    }

    private void HttpMethod(int subject,String model,String testType) {


        // 网络请求
        Retrofit retrofit = RetrofitClient.getClient("http://v.juhe.cn/");
        QuestionApiService apiService = retrofit.create(QuestionApiService.class);


        // 设置请求参数
        String apiKey = "2503a403bdd2b6b5aaaf841a9ff6e33d";

        Call<QuestionResponse> Questioncall = apiService.getQuestions(apiKey,subject,model,testType);
        Call<AnswerResponse> Answercall = apiService.getAnswers(apiKey);

        Questioncall.enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    // 处理请求成功时题库数据
                    questions = response.body().getResult();
//                    Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
                    //装载数据
                    displayQuestion(currentQuestionIndex);
                } else {
                    // 打印响应错误信息
                    Log.e("QuestionActivity", "Response not successful: " + response.message());
                }
            }
                @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {
                // 打印错误信息
                Log.e("QuestionActivity", "Request failed", t);
            }
        });



        Answercall.enqueue(new Callback<AnswerResponse>() {
            @Override
            public void onResponse(Call<AnswerResponse> call, Response<AnswerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    // 处理请求成功时
                    answerMap = response.body().getResult();


//           Toast.makeText(getApplicationContext(), "Answer请求成功", Toast.LENGTH_SHORT).show();
                    //装载数据

                } else {
                    Toast.makeText(getApplicationContext(), "Answer请求失败", Toast.LENGTH_SHORT).show();

                    // 打印响应错误信息
                    Log.e("QuestionActivity", "Response not successful: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<AnswerResponse> call, Throwable t) {
                // 打印错误信息
                Log.e("QuestionActivity", "Request failed", t);
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


        // 按钮
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        submitButton = findViewById(R.id.submitButton);
    }
    private void method() {
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex > 1) {
                    currentQuestionIndex--;
                    displayQuestion(currentQuestionIndex);
                    //隐藏答案
                    answerLayout.setVisibility(View.GONE);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++;
                    displayQuestion(currentQuestionIndex);
                    //隐藏答案
                    answerLayout.setVisibility(View.GONE);
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
                    } else {
                        explanationTextView.setText("回答错误. " +"\n" + "本题技巧:  " + questions.get(currentQuestionIndex).getExplains());

                        //1.加入错题本


                    }
                    //2.显示答案
                    your_answer.setText("你的答案: " + userAnswer);
                    answerLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void displayQuestion(int index) {
        if (questions != null && !questions.isEmpty()) {
        Question question = questions.get(index);
        // 答案
         answerValue = answerMap.get(question.getAnswer());
        question_answer.setText("正确答案: " + answerValue);

        // 题号
        question_number.setText("第" + (index + 1) + "题");

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
        explanationTextView.setText("");
        }
    }
}
