package com.example.faruk.learningbroadcastreceivers.Activities;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.faruk.learningbroadcastreceivers.R;

public class NotificationActivity extends AppCompatActivity {
    private String title;
    private String message;
    private String image;

    private TextView tvTitle;
    private TextView tvMessage;
    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        ivImage = (ImageView) findViewById(R.id.ivLinkImage);

        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.cancel(0);

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
            Bundle bundle = intent.getExtras();
            title = bundle.getString("title");
            message = bundle.getString("message");
            image = bundle.getString("image");
        }

        tvTitle.setText(title);
        tvMessage.setText(message);
        Glide.with(this)
                .load(image)
                .into(ivImage);
    }
}
