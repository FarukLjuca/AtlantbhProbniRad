package com.example.faruk.learningcontentproviders.ContentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.faruk.learningcontentproviders.Database.DatabaseHelper;
import com.example.faruk.learningcontentproviders.Database.MusicContract;

public class MusicContentProvider extends ContentProvider {
    public MusicContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        /*
        MatrixCursor cursor = new MatrixCursor(new String[] { "Autor", "Pjesma" });
        cursor.addRow(new String[] { "Bon Jovi", "It's my life" });
        cursor.addRow(new String[] { "Eminem", "Rap God" });
        */

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(MusicContract.MusicEntry.TABLE_NAME,
                new String[] { MusicContract.MusicEntry.COLUMN_NAME_AUTOR, MusicContract.MusicEntry.COLUMN_NAME_PJESMA },
                null, null, null, null, null);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
