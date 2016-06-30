package com.atlantbh.atlantchat.api;

import com.atlantbh.atlantchat.classes.helpers.SuccessResponse;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


/**
 * Created by Faruk on 20/06/16.
 */
public interface MessageApi {
    @FormUrlEncoded
    @POST("api.php?action=message_send")
    Call<SuccessResponse> sendMessage (@Field("message") String message, @Field("userId") long userId);
}
