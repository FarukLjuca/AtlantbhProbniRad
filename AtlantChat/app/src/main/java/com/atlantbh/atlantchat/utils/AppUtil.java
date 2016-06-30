package com.atlantbh.atlantchat.utils;

import android.content.Context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Faruk on 20/06/16.
 * <p/>
 * This class provides frequently used functions, strings and int flags.
 * This class also provides layer of optimization.
 */
public class AppUtil {
    public static final String BASE_URL = "https://atlantbhprobnirad.herokuapp.com/";
    public static final String BASE_IMGUR_URL = "https://api.imgur.com/3/";
    public static final String LOG_NAME = "AtlantChat";

    /**
     * Utility method that returns instance of retrofit
     */
    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    }).create();

            OkHttpClient client = new OkHttpClient();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.interceptors().add(interceptor);


            retrofit = new Retrofit.Builder()
                    .baseUrl(AppUtil.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    private static Retrofit imgurRetrofit;

    public static Retrofit getImgurRetrofit() {
        if (imgurRetrofit == null) {
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    }).create();


            OkHttpClient client = new OkHttpClient();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.interceptors().add(interceptor);

            imgurRetrofit = new Retrofit.Builder()
                    .baseUrl(AppUtil.BASE_IMGUR_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return imgurRetrofit;
    }

    public static Realm getRealm(Context context) {
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder(context)
                .deleteRealmIfMigrationNeeded()
                .build();
        return Realm.getInstance(realmConfiguration);
    }

    private static Float scale;

    public static void setScale(Context context) {
        scale = context.getResources().getDisplayMetrics().density;
    }

    public static float getScale() {
        return scale;
    }
}
