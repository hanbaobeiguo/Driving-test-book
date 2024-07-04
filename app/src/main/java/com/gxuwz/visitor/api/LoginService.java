package com.gxuwz.visitor.api;

import com.gxuwz.visitor.model.vo.LoginResponse;
import com.gxuwz.visitor.model.vo.QuestionResponse;
import com.gxuwz.visitor.model.vo.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginService {
    @POST("user/login")
    Call<LoginResponse> Login(
            @Query("phone") String phone,
            @Query("pwd") String pwd
    );
}
