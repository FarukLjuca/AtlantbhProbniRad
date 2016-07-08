package com.atlantbh.atlantchat.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlantbh.atlantchat.R;
import com.atlantbh.atlantchat.activities.ChatActivity;
import com.atlantbh.atlantchat.activities.LoginRegisterActivity;
import com.atlantbh.atlantchat.utils.AppUtil;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatHeadService extends Service {
    public static final String ACTION_CHAT_HEADS = "action_chat_heads";

    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private RelativeLayout layout;
    private CircleImageView chatHead;
    private TextView messageNumber;
    private static int unreadMessages = 0;
    /**
     * This attribute is used to detect if user is dragging chat head or clicking on it
     * if he is clicking, new activity should be opened
     */
    private boolean open;

    @Override
    public IBinder onBind(Intent intent) {
        // We will not use this
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void startChatHeads(String url) {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        if (unreadMessages == 0) {
            layout = new RelativeLayout(this);
            chatHead = new CircleImageView(this);

            Picasso.with(getApplicationContext()).load(url).into(chatHead);

            messageNumber = new TextView(this);
            layout.addView(chatHead);
            layout.addView(messageNumber);

            int pixels = (int) (60 * AppUtil.getScale() + 0.5f);

            params = new WindowManager.LayoutParams(
                    pixels,
                    pixels,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = 0;
            params.y = 200;

            windowManager.addView(layout, params);
        }

        layout.setVisibility(View.VISIBLE);

        unreadMessages ++;

        //chatHead.setImageResource(R.drawable.ic_account_circle_primary_100px);

        messageNumber.setText(String.valueOf(unreadMessages));
        messageNumber.setBackground(this.getResources().getDrawable(R.drawable.accent_circle));
        messageNumber.setPadding(5, 3, 5, 3);
        messageNumber.setTextSize(16);

        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        open = true;

                        return true;
                    case MotionEvent.ACTION_UP:
                        if (open) {
                            Intent intent = new Intent(ChatHeadService.this, LoginRegisterActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            layout.setVisibility(View.GONE);
                            unreadMessages = 0;
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(layout, params);

                        open = false;

                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_CHAT_HEADS)) {
            startChatHeads(intent.getExtras().getString("url"));
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null) windowManager.removeView(chatHead);
        unreadMessages = 0;
    }
}
