package com.gxuwz.visitor.api;

import com.gxuwz.visitor.model.bean.User;
import com.gxuwz.visitor.model.vo.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    @GET("user/{username}")
    Call<UserResponse> getUser(@Path("username") String username);
    @POST("user/register")
    Call<UserResponse> registerUser(@Body User user);
}
