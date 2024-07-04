package com.gxuwz.visitor.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gxuwz.visitor.MyApplication;
import com.gxuwz.visitor.R;
import com.gxuwz.visitor.api.QuestionApiService;
import com.gxuwz.visitor.model.bean.Question;
import com.gxuwz.visitor.model.vo.QuestionResponse;
import com.gxuwz.visitor.network.RetrofitClient;
import com.gxuwz.visitor.util.AppDatabase;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 空白模板Fragment
 */
public class QuestionBankFragment extends Fragment {
    private List<Question> questions1, questions4;
    private AppDatabase db;

    private LinearLayout loadQuestionLayout, progressBar;

    public QuestionBankFragment() {
        super();
    }

    /**
     * 创建带给定Bundle参数的Fragment实例的类方法
     */
    public static QuestionBankFragment newInstance() {
        QuestionBankFragment fragment = new QuestionBankFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questionbank, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadQuestionLayout = view.findViewById(R.id.loadQuestion_layout);
        progressBar = view.findViewById(R.id.progressBar);
        db = MyApplication.database;

        loadQuestionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "正在加载题库", Toast.LENGTH_SHORT).show();
                HttpMethod(1, "c1", "order");
            }
        });
    }

    private void HttpMethod(int subject, String model, String testType) {
        Retrofit retrofit = RetrofitClient.getClient("http://v.juhe.cn/");
        QuestionApiService apiService = retrofit.create(QuestionApiService.class);

        String apiKey = "2503a403bdd2b6b5aaaf841a9ff6e33d";

        Call<QuestionResponse> Questioncall1 = apiService.getQuestions(apiKey, 1, model, testType);
        Call<QuestionResponse> Questioncall4 = apiService.getQuestions(apiKey, 4, model, testType);

        CountDownLatch latch = new CountDownLatch(2);

        Questioncall1.enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    questions1 = response.body().getResult();
                    if (questions1 != null) {
                        Log.e("questions1", "size: " + questions1.size());
                    } else {
                        Log.e("QuestionBankFragment", "questions1 is null");
                    }
                } else {
                    Log.e("QuestionBankFragment", "Response not successful: " + response.message());
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {
                Log.e("QuestionBankFragment", "请求失败", t);
                latch.countDown();
            }
        });

        Questioncall4.enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    questions4 = response.body().getResult();
                    if (questions4 != null) {
                        Log.e("questions4", "size: " + questions4.size());
                    } else {
                        Log.e("QuestionBankFragment", "questions4 is null");
                    }
                } else {
                    Log.e("QuestionBankFragment", "Response not successful: " + response.message());
                }
                latch.countDown();
            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {
                Log.e("QuestionBankFragment", "请求失败", t);
                latch.countDown();
            }
        });

        MyApplication.appExecutors.diskIO().execute(() -> {
            try {
                latch.await();
                if (questions1 != null && questions4 != null) {
                    db.questionDao().deleteAllQuestions();
                    for (Question question : questions1) {
                        question.setSubject(1);
                        db.questionDao().insert(question);
                    }
                    Integer No = questions1.size() + 1;
                    for (Question question : questions4) {
                        question.setId(question.getId() + No);
                        question.setSubject(4);
                        db.questionDao().insert(question);
                    }
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), "题库加载成功", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    });
                } else {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), "题库加载失败，请稍后再试", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    });
                }
            } catch (InterruptedException e) {
                Log.e("QuestionBankFragment", "CountDownLatch interrupted", e);
            }
        });
    }
}
