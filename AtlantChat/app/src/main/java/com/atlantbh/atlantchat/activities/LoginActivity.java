package com.atlantbh.atlantchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.atlantbh.atlantchat.R;
import com.atlantbh.atlantchat.api.UserApi;
import com.atlantbh.atlantchat.classes.helpers.LoginResponse;
import com.atlantbh.atlantchat.services.DeviceRegistrationService;
import com.atlantbh.atlantchat.utils.AppUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    // Disable login button until response arrives.
    // This will prevent two or more requests from being fired.
    private boolean enable = true;

    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        startService(new Intent(this, DeviceRegistrationService.class));

        email = (EditText) findViewById(R.id.etLoginEmail);
        password = (EditText) findViewById(R.id.etLoginPassword);
    }

    public void login(View view) {
        if (enable) {
            enable = false;

            Retrofit retrofit = AppUtil.getRetrofit();
            UserApi userApi = retrofit.create(UserApi.class);
            Call<LoginResponse> call = userApi.login(email.getText().toString(), password.getText().toString());
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.body() != null) {
                        LoginResponse loginResponse = response.body();

                        if (loginResponse.getCount() == 0) {
                            Toast.makeText(LoginActivity.this, "Please check your username and password", Toast.LENGTH_SHORT).show();
                            enable = true;
                        } else if (loginResponse.getCount() == 1) {
                            Toast.makeText(LoginActivity.this, "Successful login", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, ChatActivity.class));
                        } else {
                            Log.e(AppUtil.LOG_NAME, "More than one user is registered with same email");
                        }
                    } else {
                        Log.e(AppUtil.LOG_NAME, "Server returned empty body during login");
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
