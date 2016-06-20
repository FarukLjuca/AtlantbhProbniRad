package com.atlantbh.atlantchat.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Faruk on 20/06/16.
 */
public interface MessageApi {
    @FormUrlEncoded
    @POST("index.php")
    Call<Void> sendMessage (@Field("message") String message, @Field("userId") long userId);
}
