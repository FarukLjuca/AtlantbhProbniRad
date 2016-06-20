package com.atlantbh.atlantchat.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.atlantbh.atlantchat.R;
import com.atlantbh.atlantchat.api.MessageApi;
import com.atlantbh.atlantchat.classes.Session;
import com.atlantbh.atlantchat.utils.AppUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatActivity extends AppCompatActivity {
    private MessageApi messageApi;

    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        message = (TextView) findViewById(R.id.etChatMessage);

        Retrofit retrofit = AppUtil.getRetrofit();
        messageApi = retrofit.create(MessageApi.class);
    }

    public void sendMessage(View view) {
        Call<Void> call = messageApi.sendMessage(message.getText().toString(), Session.getUserId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {}

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });

        message.setText("");
    }
}
