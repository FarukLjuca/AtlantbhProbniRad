package com.atlantbh.atlantchat.api;

import com.atlantbh.atlantchat.model.ImageImgur;
import com.atlantbh.atlantchat.model.ImgurResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;


/**
 * Created by Faruk on 28/06/16.
 */
public interface ImgurApi {
    @Headers({
            "Authorization: CLIENT-ID bdc887775350eba",
            "Content-Type: application/json"
    })
    @POST("upload")
    Call<ImgurResponse> upload(@Body ImageImgur image);
}
