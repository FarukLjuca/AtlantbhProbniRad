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

        getAllSmsFromProvider();
    }

    public void getAllSmsFromProvider() {
        List<Song> list = new ArrayList<>();
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(Uri.parse("content://com.example.faruk.s.MusicContentProvider/muzika"), // Official CONTENT_URI from docs
                new String[] { "Autor", "Pjesma" }, // Select body text
                null,
                null,
                null); // Default sort order

        int t = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < t; i++) {
                list.add(new Song(c.getString(0), c.getString(1)));
                c.moveToNext();
            }
        } else {
            throw new RuntimeException("You have no SMS in Inbox");
        }
        c.close();

        String str = "";
        for (Song s : list) {
            str += s.getSinger() + " - " + s.getTitle() + "\n";
        }
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
    }
}
