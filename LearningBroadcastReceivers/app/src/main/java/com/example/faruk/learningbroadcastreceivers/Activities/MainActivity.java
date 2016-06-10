package com.example.faruk.learningbroadcastreceivers.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faruk.learningbroadcastreceivers.R;
import com.example.faruk.learningbroadcastreceivers.Services.RegistrationIntentService;
import com.google.android.gms.iid.InstanceID;

public class MainActivity extends AppCompatActivity {
    private ImageView image;
    private Switch s;
    private static boolean allowNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);

        //SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        //boolean pref = sharedPref.getBoolean(getString(R.string.pref), true);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean pref = sharedPref.getBoolean(getString(R.string.pref), true);
        allowNotifications = pref;

        image = (ImageView) findViewById(R.id.ivImage);
        s = (Switch) findViewById(R.id.swAllowNotifications);
        if (s != null) {
            s.setChecked(allowNotifications);
            s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    allowNotifications = isChecked;

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                    //SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(getString(R.string.pref), isChecked);
                    editor.apply();
                }
            });
        }
    }

    // Method is sending broadcast
    public void sendBroadcast(View view) {
        Intent intent = new Intent();
        intent.setAction("com.example.faruk.lerningbroadcastreceiver.TEST");
        sendBroadcast(intent);
    }

    // Method for accessing camera fro image capture
    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
    }

    // Method for getting the result of camera access
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);
        }
    }

    public void checkPref(View view) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean pref = sharedPref.getBoolean(getString(R.string.pref), true);
        Toast.makeText(MainActivity.this, pref ? "true" : "false", Toast.LENGTH_SHORT).show();
    }
}
