package com.atlantbh.atlantchat.services;

import android.app.IntentService;
import android.content.Intent;

import com.atlantbh.atlantchat.model.Session;
import com.atlantbh.atlantchat.model.realm.User;
import com.atlantbh.atlantchat.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class UsersSyncService extends IntentService {

    public UsersSyncService() {
        super("com.atlantbh.atlantchat.UsersSyncService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Realm realm = AppUtil.getRealm(this);
        realm.beginTransaction();
        RealmResults<User> results = realm.where(User.class).findAll();
        realm.commitTransaction();

        List<User> users = new ArrayList<>();
        for (User user : results) {
            users.add(new User(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getImage()));
        }

        realm.close();

        Session.setUsers(users);
        /*
        Retrofit retrofit = AppUtil.getRetrofit();
        UserApi userApi = retrofit.create(UserApi.class);
        Call<List<User>> call = userApi.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.body() != null) {
                    List<User> users = response.body();

                    Session.setUsers(users);

                    Log.i(AppUtil.LOG_NAME, "Finished user sync");

                    Realm realm = AppUtil.getRealm(UsersSyncService.this);
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(users);
                    realm.commitTransaction();
                    realm.close();
                }
                else {
                    Log.e(AppUtil.LOG_NAME, "Get users returned empty body");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        */
    }
}
