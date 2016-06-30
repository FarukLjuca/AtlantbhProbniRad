package com.atlantbh.atlantchat.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.atlantbh.atlantchat.AtlantChatApplication;
import com.atlantbh.atlantchat.R;
import com.atlantbh.atlantchat.adapters.ChatAdapter;
import com.atlantbh.atlantchat.api.MessageApi;
import com.atlantbh.atlantchat.api.UserApi;
import com.atlantbh.atlantchat.classes.Session;
import com.atlantbh.atlantchat.classes.helpers.SuccessResponse;
import com.atlantbh.atlantchat.classes.realm.Message;
import com.atlantbh.atlantchat.classes.realm.User;
import com.atlantbh.atlantchat.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ChatActivity extends AppCompatActivity {
    private MessageApi messageApi;
    // We need this receiver to detect new messages
    private BroadcastReceiver receiver;
    private ChatAdapter adapter;

    @BindView(R.id.lvChat)
    ListView chat;
    @BindView(R.id.etChatMessage)
    EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Realm realm = AppUtil.getRealm(this);
        RealmResults<Message> results = realm.where(Message.class).findAll();
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            messages.add(results.get(i));
        }
        adapter = new ChatAdapter(this, messages);
        chat.setAdapter(adapter);
        scrollChatToBottom();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.google.android.c2dm.intent.RECEIVE");

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
                    Log.i(AppUtil.LOG_NAME, "Received intent.");
                    Bundle bundle = intent.getExtras();
                    String messageText = bundle.getString("message");
                    long userId = Long.valueOf(bundle.getString("userId"));

                    syncUser(userId, messageText);
                }
            }
        };
        registerReceiver(receiver, filter);

        // Define once - use for every message that is sent
        Retrofit retrofit = AppUtil.getRetrofit();
        messageApi = retrofit.create(MessageApi.class);
    }

    public void sendMessage(View view) {
        // If there is no message, there is no point sending one
        if (message.getText().toString().trim().length() == 0) {
            return;
        }

        Log.i(AppUtil.LOG_NAME, "Sending user id: " + Session.getUserId());
        Call<SuccessResponse> call = messageApi.sendMessage(message.getText().toString(), Session.getUserId());
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Response<SuccessResponse> response, Retrofit retrofit) {
                if (response.body() != null) {
                    SuccessResponse successResponse = response.body();
                    if (successResponse.isSuccess()) {
                        Log.i(AppUtil.LOG_NAME, "Message sent");
                    } else {
                        Toast.makeText(ChatActivity.this, "There was a problem with sending message", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(AppUtil.LOG_NAME, "Empty body in message send");
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }

        });

        message.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_profile:
                Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AtlantChatApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AtlantChatApplication.activityPaused();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void scrollChatToBottom() {
        chat.post(new Runnable() {
            @Override
            public void run() {
                chat.setSelection(adapter.getCount() - 1);
            }
        });
    }

    private void syncUser(final long id, final String messageText) {
        boolean contains = false;

        for (User user : Session.getUsers()) {
            if (user.getId() == id) {
                contains = true;
                break;
            }
        }

        if (contains) {
            adapter.addMessage(new Message(id, messageText));
            chat.smoothScrollToPosition(adapter.getCount() - 1);
        } else {
            Retrofit retrofit = AppUtil.getRetrofit();
            UserApi userApi = retrofit.create(UserApi.class);
            Call<User> call = userApi.getUser(id);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Response<User> response, Retrofit retrofit) {
                    if (response.body() != null) {
                        User user = response.body();

                        Session.getUsers().add(user);

                        adapter.addMessage(new Message(id, messageText));
                        chat.smoothScrollToPosition(adapter.getCount() - 1);

                        Realm realm = AppUtil.getRealm(ChatActivity.this);

                        if (realm.where(User.class).equalTo("id", user.getId()).findAll().size() == 0) {
                            realm.beginTransaction();

                            User realmUser = realm.createObject(User.class);
                            realmUser.setId(user.getId());
                            realmUser.setName(user.getName());
                            realmUser.setEmail(user.getEmail());
                            realmUser.setImage(user.getImage());

                            realm.commitTransaction();
                        }
                        realm.close();
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }


            });
        }
    }
}
