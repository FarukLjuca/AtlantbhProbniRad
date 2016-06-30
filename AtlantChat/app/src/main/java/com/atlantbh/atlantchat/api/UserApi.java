package com.atlantbh.atlantchat.api;

import com.atlantbh.atlantchat.classes.helpers.SuccessResponse;
import com.atlantbh.atlantchat.classes.helpers.SuccessResponseInteger;
import com.atlantbh.atlantchat.classes.helpers.SuccessResponseUser;
import com.atlantbh.atlantchat.classes.realm.User;

import java.util.List;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;


/**
 * Created by Faruk on 20/06/16.
 */
public interface UserApi {
    @FormUrlEncoded
    @POST("api.php?action=user_login")
    Call<SuccessResponseInteger> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("api.php?action=user_register")
    Call<SuccessResponseUser> register(@Field("name") String name, @Field("email") String email, @Field("password") String password, @Field("imageUrl") String imageUrl);

    @POST("api.php?action=users_get")
    Call<List<User>> getUsers();

    @FormUrlEncoded
    @POST("api.php?action=user_get")
    Call<User> getUser(@Field("userId") long userId);
}
