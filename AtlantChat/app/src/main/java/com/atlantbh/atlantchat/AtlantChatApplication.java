package com.atlantbh.atlantchat;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.atlantbh.atlantchat.services.DeviceRegistrationService;
import com.atlantbh.atlantchat.services.UsersSyncService;
import com.atlantbh.atlantchat.utils.AppUtil;

/**
 * Created by Faruk on 21/06/16.
 */
public class AtlantChatApplication extends Application {
    private static boolean activityVisible;

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, DeviceRegistrationService.class));
        startService(new Intent(this, UsersSyncService.class));

        AppUtil.setScale(getApplicationContext());
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
        Log.i(AppUtil.LOG_NAME, "Visible true");
    }

    public static void activityPaused() {
        activityVisible = false;
        Log.i(AppUtil.LOG_NAME, "Visible false");
    }
}
