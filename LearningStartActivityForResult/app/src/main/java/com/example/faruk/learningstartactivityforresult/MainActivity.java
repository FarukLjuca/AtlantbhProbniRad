package com.example.faruk.learningstartactivityforresult;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView selectedSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedSong = (TextView) findViewById(R.id.tvSelectedSong);
    }

    /**
     * Method that hadles starting new activity for result
     * @param view View that was clicked
     */
    public void startActivityForResult(View view) {
        Intent intent = new Intent(this, ResultActivity.class);
        startActivityForResult(intent, ResultActivity.REQUEST_CODE_GET_MUSIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, intent);
            String string = intent.getExtras().getString("song");
            selectedSong.setText(string);
        }
        else {
            selectedSong.setText("Song was not selected");
        }
    }
}