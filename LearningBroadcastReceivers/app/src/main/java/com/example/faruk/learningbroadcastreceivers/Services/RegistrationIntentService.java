package com.example.faruk.learningbroadcastreceivers.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.faruk.learningbroadcastreceivers.Api.TokenApi;
import com.example.faruk.learningbroadcastreceivers.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Faruk on 09/06/16.
 */
public class RegistrationIntentService extends IntentService {
    public RegistrationIntentService() { super("lala");}

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RegistrationIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("lala", "Usao u intent service");
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://atlantbhprobnirad.herokuapp.com/")
                    .build();

            TokenApi tokenApi = retrofit.create(TokenApi.class);
            Call<Void> call = tokenApi.sendToken(token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });

            Log.d("lala", token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
