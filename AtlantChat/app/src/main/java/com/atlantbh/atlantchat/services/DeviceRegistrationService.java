package com.atlantbh.atlantchat.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.atlantbh.atlantchat.R;
import com.atlantbh.atlantchat.api.TokenApi;
import com.atlantbh.atlantchat.model.helpers.SuccessResponse;
import com.atlantbh.atlantchat.utils.AppUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


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

            Retrofit retrofit = AppUtil.getRetrofit();
            TokenApi tokenApi = retrofit.create(TokenApi.class);
            Call<SuccessResponse> call = tokenApi.registerDevice(token);
            call.enqueue(new Callback<SuccessResponse>() {
                @Override
                public void onResponse(Response<SuccessResponse> response, Retrofit retrofit) {
                    if (response.body() != null) {
                        SuccessResponse successResponse = response.body();
                        if (successResponse.isSuccess()) {
                            Log.i(AppUtil.LOG_NAME, "Device successfully registered with token");
                        } else {
                            Toast.makeText(getApplicationContext(), successResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Log.d(AppUtil.LOG_NAME, "Device registration service returned empty body");
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }

            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
