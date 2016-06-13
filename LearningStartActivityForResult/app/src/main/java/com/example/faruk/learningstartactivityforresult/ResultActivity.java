package com.example.faruk.learningstartactivityforresult;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ResultActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_GET_MUSIC = 0;

    private Spinner songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        songs = (Spinner) findViewById(R.id.spSongs);

        /**
         * Block of code for creating Array Adapter that gets items from resources
         */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.songs_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        songs.setAdapter(adapter);
    }

    /**
     * This method is called when OK button is called
     * @param view View is Button that was pressed
     */
    public void finishActivity(View view) {
        Intent result = new Intent("com.example.RESULT_ACTION", Uri.parse("content://result_uri"));
        result.putExtra("song", songs.getSelectedItem().toString());
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
