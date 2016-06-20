package com.example.faruk.learningbroadcastreceivers.Api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Faruk on 10/06/16.
 */
public interface TokenApi {
    @GET("index.php")
    Call<Void> sendToken(@Query("token") String token);
}
