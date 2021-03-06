package com.example.faruk.learningcontentproviders.Activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.example.faruk.learningcontentproviders.Adapeters.CustomAdapter;
import com.example.faruk.learningcontentproviders.Classes.MyContact;
import com.example.faruk.learningcontentproviders.Classes.MyMessage;
import com.example.faruk.learningcontentproviders.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mainList;

    private Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainList = (ListView) findViewById(R.id.lvMainList);
        if (mainList != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                // Permissions are not available and we have to request them
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS},
                        0);
            } else {
                // Permissions are available and we can proceed with work
                getContent();
            }
        }
    }

    // Method that is called when permissions are granted
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContent();
                }
            }
        }
    }

    private void getContent() {
        List<MyMessage> data = getAllSmsFromProvider();
        CustomAdapter adapter = new CustomAdapter(getContext(), data);
        mainList.setAdapter(adapter);
    }

    public List<MyMessage> getAllSmsFromProvider() {
        List<MyMessage> lstSms = new ArrayList<>();
        List<MyContact> lstContact = new ArrayList<>();
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, // Official CONTENT_URI from docs
                new String[]{Telephony.Sms.Inbox.PERSON, Telephony.Sms.Inbox.BODY}, // Select body text
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER); // Default sort order

        int totalSMS = c.getCount();

        // Now we need Contacts in order to assign id's to Person names
        Cursor c1 = cr.query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME},
                null,
                null,
                null);

        int totalContacts = c1.getCount();

        if (c1.moveToFirst()) {
            for (int i = 0; i < totalContacts; i++) {
                lstContact.add(new MyContact(c1.getInt(0), c1.getString(1)));
                c1.moveToNext();
            }
        } else {
            Toast.makeText(MainActivity.this, "You have no contacts", Toast.LENGTH_SHORT).show();
        }
        c1.close();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                String name = "";
                for (int j = 0; j < totalContacts; j++) {
                    if (lstContact.get(j).getId() == c.getInt(0)) {
                        name = lstContact.get(j).getDisplayName();
                    }
                }
                lstSms.add(new MyMessage(name, c.getString(1)));
                c.moveToNext();
            }
        } else {
            Toast.makeText(MainActivity.this, "You have no messages.", Toast.LENGTH_SHORT).show();
        }
        c.close();

        return lstSms;
    }
}
