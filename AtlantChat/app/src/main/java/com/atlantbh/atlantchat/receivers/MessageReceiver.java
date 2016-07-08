package com.atlantbh.atlantchat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;

import com.atlantbh.atlantchat.AtlantChatApplication;
import com.atlantbh.atlantchat.R;
import com.atlantbh.atlantchat.api.UserApi;
import com.atlantbh.atlantchat.model.Session;
import com.atlantbh.atlantchat.model.realm.Message;
import com.atlantbh.atlantchat.model.realm.User;
import com.atlantbh.atlantchat.services.ChatHeadService;
import com.atlantbh.atlantchat.utils.AppUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class MessageReceiver extends BroadcastReceiver {
    private Context context;
    private long userId;
    private String message;
    private Date time;
    private String action;

    public MessageReceiver() {
        Log.i(AppUtil.LOG_NAME, "Message receiver created");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
            Log.i(AppUtil.LOG_NAME, "Received intent.");
            Bundle bundle = intent.getExtras();
            action = bundle.getString("action");

            message = bundle.getString("message");
            userId = Integer.valueOf(bundle.getString("userId"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                time = format.parse(bundle.getString("time"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            addMessageToDatabase();
            notifyServer();

            if (!AtlantChatApplication.isActivityVisible()) {
                Realm realm = AppUtil.getRealm(context);
                RealmResults<User> results = realm.where(User.class).equalTo("id", userId).findAll();
                User user = results.get(0);

                addHeadChatBubble(user.getImage());
                syncUser(userId);
            }
        }
    }

    private void addMessageToDatabase() {
        Realm realm = AppUtil.getRealm(context);

        SharedPreferences sharedPref = context.getSharedPreferences("pref", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPref.edit();
        //editor.putStringSet("ids", new ArraySet<String>() {});
        //editor.commit();

        Set<String> ids = sharedPref.getStringSet("ids", null);

        if (ids != null) {
            for (String s : ids) {
                realm.beginTransaction();
                Message message = realm.createObject(Message.class);
                message.setMessage(this.message);
                message.setUserId(this.userId);
                message.setOwner(Integer.valueOf(s));
                message.setTime(this.time);

                realm.commitTransaction();
            }
        }
        else {
            Log.e(AppUtil.LOG_NAME, "ids is null");
        }

        realm.close();
        Log.i(AppUtil.LOG_NAME, "Written in database.");
    }

    private void notifyServer() {

    }

    private void addHeadChatBubble(String url) {
        Intent intent = new Intent(context, ChatHeadService.class);
        intent.setAction(ChatHeadService.ACTION_CHAT_HEADS);
        intent.putExtra("url", url);
        context.startService(intent);
    }

    private void syncUser(long id) {
        boolean contains = false;

        for (User user : Session.getUsers()) {
            if (user.getId() == id) {
                contains = true;
                break;
            }
        }

        if (!contains) {
            Retrofit retrofit = AppUtil.getRetrofit();
            UserApi userApi = retrofit.create(UserApi.class);
            Call<User> call = userApi.getUser(id);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Response<User> response, Retrofit retrofit) {
                    if (response.body() != null) {
                        User user = response.body();

                        Session.getUsers().add(user);

                        Realm realm = AppUtil.getRealm(context);

                        if (realm.where(User.class).equalTo("id", user.getId()).findAll().size() == 0) {
                            realm.beginTransaction();

                            User realmUser = realm.createObject(User.class);
                            realmUser.setId(user.getId());
                            realmUser.setName(user.getName());
                            realmUser.setEmail(user.getEmail());
                            realmUser.setImage(user.getImage());

                            realm.commitTransaction();
                        }
                        realm.close();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }

            });
        }
    }
}
