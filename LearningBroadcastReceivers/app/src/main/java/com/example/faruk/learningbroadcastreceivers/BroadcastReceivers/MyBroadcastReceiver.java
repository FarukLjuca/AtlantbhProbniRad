package com.example.faruk.learningbroadcastreceivers.BroadcastReceivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.faruk.learningbroadcastreceivers.Activities.NotificationActivity;
import com.example.faruk.learningbroadcastreceivers.R;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Faruk on 08/06/16.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    /**
     * Method that is called when new broadcast is received by broadcast receiver
     * @param context Context that is used to create notifications
     * @param intent Intent that method was called with
     */
    @Override
    public void onReceive(final Context context, Intent intent) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean pref = sharedPref.getBoolean(context.getString(R.string.pref), true);
        if (pref) {
            if (intent.getAction().equals("com.example.faruk.lerningbroadcastreceiver.TEST")) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(android.R.drawable.btn_star)
                                .setContentTitle("Notification")
                                .setContentText("This is some text!");

                int mNotificationId = 23243;
                NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());

            } else if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
                Toast.makeText(context, "Wireless state changed", Toast.LENGTH_SHORT).show();
            } else if (intent.getAction().equals("android.net.wifi.STATE_CHANGED")) {
                Toast.makeText(context, "State changed", Toast.LENGTH_SHORT).show();
            }
            else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
                Bundle bundle = intent.getExtras();
                final String title = bundle.getString("title");
                final String message = bundle.getString("message");
                String image = bundle.getString("image");

                Intent newIntent = new Intent(context, NotificationActivity.class);
                newIntent.putExtra("title", title);
                newIntent.putExtra("message", message);
                newIntent.putExtra("image", image);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                Glide.with(context)
                        .load(image)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
                                remoteViews.setImageViewBitmap(R.id.ivNotificationImage, resource);
                                remoteViews.setTextViewText(R.id.tvNotificationTitle, title);
                                remoteViews.setTextViewText(R.id.tvNotificationMessage, message);
                                remoteViews.setOnClickPendingIntent(R.id.ivNotificationImage, pendingIntent);

                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(context)
                                                .setSmallIcon(android.R.drawable.btn_star)
                                                .setTicker("Noification is created")
                                                .setAutoCancel(true)
                                                .setContentIntent(pendingIntent)
                                                .setContent(remoteViews);

                                int mNotificationId = 23243;
                                NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                Notification notification = mBuilder.build();
                                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                                mNotifyMgr.notify(mNotificationId, notification);
                            }
                        });

                /*
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(context).load(image).asBitmap().into(100, 100).get();
                    //InputStream is = (InputStream) new URL(image).getContent();
                    //bitmap = BitmapFactory.decodeStream(is);
                    //is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(android.R.drawable.btn_star)
                                .setLargeIcon(bitmap)
                                .setContentTitle(title)
                                .setContentText(message)
                                .setContentIntent(pendingIntent);

                int mNotificationId = 23243;
                NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = mBuilder.build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                mNotifyMgr.notify(mNotificationId, notification);
                */
            }
        }
    }
}
