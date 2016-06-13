package com.example.faruk.learningbroadcastreceivers.Services;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import com.example.faruk.learningbroadcastreceivers.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

import java.io.IOException;

/**
 * Created by Faruk on 09/06/16.
 */
public class MyInstanceIDService extends InstanceIDListenerService {
    /**
     * Method that is called whent token is refreshed
     */
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
