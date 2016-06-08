package com.example.faruk.learningbroadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Faruk on 08/06/16.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == "com.example.faruk.lerningbroadcastreceiver.TEST") {
            Toast.makeText(context, "Custom broadcast detected", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Wireless changed", Toast.LENGTH_SHORT).show();
        }
    }
}
