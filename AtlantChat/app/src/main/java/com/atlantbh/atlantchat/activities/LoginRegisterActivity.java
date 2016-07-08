package com.atlantbh.atlantchat.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atlantbh.atlantchat.R;
import com.atlantbh.atlantchat.api.UserApi;
import com.atlantbh.atlantchat.model.Session;
import com.atlantbh.atlantchat.model.helpers.SuccessResponseInteger;
import com.atlantbh.atlantchat.model.helpers.SuccessResponseUser;
import com.atlantbh.atlantchat.model.realm.Message;
import com.atlantbh.atlantchat.utils.AppUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginRegisterActivity extends AppCompatActivity {
    // Disable login button until response arrives.
    // This will prevent two or more requests from being fired.
    private boolean enable = true;
    private boolean isRegister = true;

    @BindView(R.id.tvLogoChat)
    TextView logoChat;
    @BindView(R.id.tvLogoApp)
    TextView logoApp;
    @BindView(R.id.tvLoginTab)
    TextView loginTab;
    @BindView(R.id.tvRegisterTab)
    TextView registerTab;
    @BindView(R.id.llLoginLayout)
    LinearLayout loginLayout;
    @BindView(R.id.llRegisterLayout)
    LinearLayout registerLayout;
    @BindView(R.id.etRegisterName)
    EditText registerName;
    @BindView(R.id.etRegisterEmail)
    EditText registerEmail;
    @BindView(R.id.etRegisterPassword)
    EditText registerPassword;
    @BindView(R.id.etLoginEmail)
    EditText loginEmail;
    @BindView(R.id.etLoginPassword)
    EditText loginPassword;
    @BindView(R.id.pbContinue)
    ProgressBar progressBar;
    @BindView(R.id.btContinue)
    Button continueButton;

    private Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        ButterKnife.bind(this);

        setFonts();
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        SharedPreferences sharedPref = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        String email = sharedPref.getString("email", null);
        String password = sharedPref.getString("password", null);

        if (email != null && password != null) {
            login(email, password);
        }

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

    private void setFonts() {
        logoChat.setTypeface(AppUtil.getTypeface(getContext(), AppUtil.TYPEFACE_ROBOTO_BOLD));
        logoApp.setTypeface(AppUtil.getTypeface(getContext(), AppUtil.TYPEFACE_ROBOTO_BOLD));
        loginTab.setTypeface(AppUtil.getTypeface(getContext(), AppUtil.TYPEFACE_ROBOTO_MEDIUM));
        registerTab.setTypeface(AppUtil.getTypeface(getContext(), AppUtil.TYPEFACE_ROBOTO_MEDIUM));
        registerName.setTypeface(AppUtil.getTypeface(getContext(), AppUtil.TYPEFACE_ROBOTO_REGULAR));
        registerEmail.setTypeface(AppUtil.getTypeface(getContext(), AppUtil.TYPEFACE_ROBOTO_REGULAR));
        registerPassword.setTypeface(AppUtil.getTypeface(getContext(), AppUtil.TYPEFACE_ROBOTO_REGULAR));
        loginEmail.setTypeface(AppUtil.getTypeface(getContext(), AppUtil.TYPEFACE_ROBOTO_REGULAR));
        loginPassword.setTypeface(AppUtil.getTypeface(getContext(), AppUtil.TYPEFACE_ROBOTO_REGULAR));
    }

    public void loginTabClick(View view) {
        isRegister = false;

        loginTab.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlackText));
        registerTab.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGrayText));

        loginLayout.setVisibility(View.VISIBLE);
        registerLayout.setVisibility(View.GONE);
    }

    public void registerTabClick(View view) {
        isRegister = true;

        loginTab.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGrayText));
        registerTab.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlackText));

        loginLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.VISIBLE);
    }

    public void continueClick(View view) {
        if (enable) {
            disableContinue();

            if (isRegister) {
                Retrofit retrofit = AppUtil.getRetrofit();
                UserApi userApi = retrofit.create(UserApi.class);
                Call<SuccessResponseUser> call = userApi.register(registerName.getText().toString(), registerEmail.getText().toString(), registerPassword.getText().toString(), "");
                call.enqueue(new Callback<SuccessResponseUser>() {
                    @Override
                    public void onResponse(Response<SuccessResponseUser> response, Retrofit retrofit) {
                        if (response.body() != null) {
                            SuccessResponseUser successResponse = response.body();
                            if (!successResponse.isSuccess()) {
                                Toast.makeText(getContext(), "More than one user is registered with same email", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Successful login", Toast.LENGTH_SHORT).show();
                                // Todo: Save data into session

                                Session.setUserId(successResponse.getData().getId());

                                // Todo: Save session into Shared Preferences
                                startActivity(new Intent(getContext(), ProfileImageActivity.class));
                            }
                        } else {
                            Log.e(AppUtil.LOG_NAME, "Server returned empty body during login");
                        }
                        enableContinue();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                });
            } else {
                login(loginEmail.getText().toString(), loginPassword.getText().toString());
            }
        }
    }

    private void login (final String email, final String password) {
        Retrofit retrofit = AppUtil.getRetrofit();
        UserApi userApi = retrofit.create(UserApi.class);
        Call<SuccessResponseInteger> call = userApi.login(email, password);
        call.enqueue(new Callback<SuccessResponseInteger>() {
            @Override
            public void onResponse(Response<SuccessResponseInteger> response, Retrofit retrofit) {
                if (response.body() != null) {
                    SuccessResponseInteger successResponse = response.body();

                    if (!successResponse.isSuccess()) {
                        Toast.makeText(getContext(), "Please check your username and password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Successful login", Toast.LENGTH_SHORT).show();
                        Session.setUserId(successResponse.getData());
                        SharedPreferences sharedPref = getContext().getSharedPreferences("pref", Context.MODE_PRIVATE);

                        Set<String> ids = sharedPref.getStringSet("ids", null);

                        if (ids != null) {
                            boolean exists = false;
                            for (String s : ids) {
                                if (Long.valueOf(s).equals(Session.getUserId())) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (!exists) {
                                ids.add(String.valueOf(Session.getUserId()));
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putStringSet("ids", ids);
                                editor.apply();
                            }
                        }
                        else {
                            Log.e(AppUtil.LOG_NAME, "ids is null in LoginRegister, witch is ok");
                            ids = new HashSet<>();
                            ids.add(String.valueOf(Session.getUserId()));
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putStringSet("ids", ids);
                            editor.apply();
                        }
                        Log.i(AppUtil.LOG_NAME, "Check user id after login: " + successResponse.getData());
                        startActivity(new Intent(getContext(), ChatActivity.class));
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("email", email);
                        editor.putString("password", password);
                        editor.apply();
                    }
                } else {
                    Log.e(AppUtil.LOG_NAME, "Server returned empty body during login");
                }
                enableContinue();
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void enableContinue() {
        enable = true;
        progressBar.setVisibility(View.GONE);
        continueButton.setVisibility(View.VISIBLE);
    }

    private void disableContinue() {
        enable = false;
        progressBar.setVisibility(View.VISIBLE);
        continueButton.setVisibility(View.GONE);
    }
}
