package com.atlantbh.atlantchat.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.atlantbh.atlantchat.AtlantChatApplication;
import com.atlantbh.atlantchat.api.UserApi;
import com.atlantbh.atlantchat.classes.Session;
import com.atlantbh.atlantchat.classes.realm.Message;
import com.atlantbh.atlantchat.classes.realm.User;
import com.atlantbh.atlantchat.services.ChatHeadService;
import com.atlantbh.atlantchat.utils.AppUtil;

import io.realm.Realm;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class MessageReceiver extends BroadcastReceiver {
    private Context context;
    private long userId;
    private String message;
    private String action;

    public MessageReceiver() {
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

            addMessageToDatabase();
            notifyServer();

            if (!AtlantChatApplication.isActivityVisible()) {
                addHeadChatBubble();
                syncUser(userId);
            }
        }
    }

    private void addMessageToDatabase() {
        Realm realm = AppUtil.getRealm(context);
        realm.beginTransaction();

        Message message = realm.createObject(Message.class);
        message.setMessage(this.message);
        message.setUserId(this.userId);

        realm.commitTransaction();
        realm.close();
        Log.i(AppUtil.LOG_NAME, "Written in database.");
    }

    private void notifyServer() {

    }

    private void addHeadChatBubble() {
        Intent intent = new Intent(context, ChatHeadService.class);
        intent.setAction(ChatHeadService.ACTION_CHAT_HEADS);
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
