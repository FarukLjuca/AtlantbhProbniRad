package com.atlantbh.atlantchat.api;

import com.atlantbh.atlantchat.model.helpers.SuccessResponse;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Faruk on 01/07/16.
 */
public interface ChatApi {
    @FormUrlEncoded
    @POST("api.php?action=chat_seen")
    Call<SuccessResponse> chatSeen (@Field("userId") long userId, @Field("chatId") int chatId);
}
