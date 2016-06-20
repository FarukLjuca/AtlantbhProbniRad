package com.atlantbh.atlantchat.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Faruk on 20/06/16.
 *
 * This class provides frequently used functions, strings and int flags.
 * This class also provides layer of optimization.
 */
public class AppUtil {
    public static final String BASE_URL = "https://atlantbhprobnirad.herokuapp.com/";
    public static final String LOG_NAME = "AtlantChat";

    /**
     * Utility method that returns instance of retrofit
     */
    private static Retrofit retrofit;
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppUtil.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
