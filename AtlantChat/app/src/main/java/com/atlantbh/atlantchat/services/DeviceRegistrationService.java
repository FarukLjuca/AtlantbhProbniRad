package com.atlantbh.atlantchat.services;

import android.app.IntentService;
import android.content.Intent;

import com.atlantbh.atlantchat.R;
import com.atlantbh.atlantchat.api.TokenApi;
import com.atlantbh.atlantchat.utils.AppUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DeviceRegistrationService extends IntentService {

    public DeviceRegistrationService() {
        super("com.atlantbh.atlantchat.services.DeviceRegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);

        try {
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppUtil.BASE_URL)
                    .build();

            TokenApi tokenApi = retrofit.create(TokenApi.class);
            Call<Void> call = tokenApi.sendToken(token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {}

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
