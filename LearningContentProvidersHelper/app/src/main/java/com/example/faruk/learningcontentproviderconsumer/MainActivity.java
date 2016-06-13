package com.example.faruk.learningcontentproviderconsumer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get data from Content Provider and show it in Toast

        List<Song> list = new ArrayList<>();
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(Uri.parse("content://com.example.faruk.s.MusicContentProvider/muzika"), // Official CONTENT_URI from docs
                new String[] { "Autor", "Pjesma" }, // Select body text
                null,
                null,
                null);

        if (c != null) {
            int t = c.getCount();

            if (c.moveToFirst()) {
                for (int i = 0; i < t; i++) {
                    list.add(new Song(c.getString(0), c.getString(1)));
                    c.moveToNext();
                }
            } else {
                Toast.makeText(MainActivity.this, "There was a problem with Content Provider", Toast.LENGTH_SHORT).show();
            }
            c.close();

            String str = "";
            for (Song s : list) {
                str += s.getSinger() + " - " + s.getTitle() + "\n";
            }
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
        }
    }
}
