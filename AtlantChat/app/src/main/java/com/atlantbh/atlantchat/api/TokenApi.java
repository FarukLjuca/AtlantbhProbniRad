package com.atlantbh.atlantchat.api;

import com.atlantbh.atlantchat.model.helpers.SuccessResponse;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


/**
 * Created by Faruk on 20/06/16.
 */
public interface TokenApi {
    @FormUrlEncoded
    @POST("api.php?action=device_register")
    Call<SuccessResponse> registerDevice(@Field("token") String token);
}
