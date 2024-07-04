package com.gxuwz.visitor.api;

import com.gxuwz.visitor.model.vo.QuestionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuestionApiService {
    @GET("jztk/query")
    Call<QuestionResponse> getQuestions(
            @Query("key") String apiKey,
            @Query("subject") int subject,
            @Query("model") String model,
            @Query("testType") String testType
    );

}
