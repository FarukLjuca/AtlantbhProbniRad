package com.atlantbh.atlantchat.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.atlantbh.atlantchat.utils.AppUtil;

public class MessageReceiver extends BroadcastReceiver {
    public MessageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
            Bundle bundle = intent.getExtras();
            String message = bundle.getString("message");

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
