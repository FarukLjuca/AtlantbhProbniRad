package com.atlantbh.atlantchat.api;

import com.atlantbh.atlantchat.classes.helpers.LoginResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Faruk on 20/06/16.
 */
public interface UserApi {
    @GET("index.php")
    Call<LoginResponse> login(@Query("email") String email, @Query("password") String password);
}
