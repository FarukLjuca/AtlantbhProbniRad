package com.atlantbh.atlantchat.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atlantbh.atlantchat.R;
import com.atlantbh.atlantchat.api.UserApi;
import com.atlantbh.atlantchat.model.Session;
import com.atlantbh.atlantchat.model.helpers.SuccessResponseInteger;
import com.atlantbh.atlantchat.utils.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {
    // Disable login button until response arrives.
    // This will prevent two or more requests from being fired.
    private boolean enable = true;

    @BindView(R.id.etLoginEmail)
    EditText email;
    @BindView(R.id.etLoginPassword)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SYSTEM_ALERT_WINDOW)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SYSTEM_ALERT_WINDOW)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void login(View view) {
        if (enable) {
            enable = false;

            Retrofit retrofit = AppUtil.getRetrofit();
            UserApi userApi = retrofit.create(UserApi.class);
            Call<SuccessResponseInteger> call = userApi.login(email.getText().toString(), password.getText().toString());
            call.enqueue(new Callback<SuccessResponseInteger>() {
                @Override
                public void onResponse(Response<SuccessResponseInteger> response, Retrofit retrofit) {
                    if (response.body() != null) {
                        SuccessResponseInteger successResponse = response.body();

                        if (!successResponse.isSuccess()) {
                            Toast.makeText(LoginActivity.this, "Please check your username and password", Toast.LENGTH_SHORT).show();
                            enable = true;
                        } else {
                            Toast.makeText(LoginActivity.this, "Successful login", Toast.LENGTH_SHORT).show();
                            Session.setUserId(successResponse.getData());
                            Log.i(AppUtil.LOG_NAME, "Check user id after login: " + successResponse.getData());
                            startActivity(new Intent(LoginActivity.this, ChatActivity.class));
                            enable = true;
                        }
                    } else {
                        Log.e(AppUtil.LOG_NAME, "Server returned empty body during login");
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }


            });
        }
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
