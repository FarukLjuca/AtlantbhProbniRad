package com.example.faruk.learningcontentproviders.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Faruk on 08/06/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MusicContract.MusicEntry.TABLE_NAME + " (" +
                    MusicContract.MusicEntry._ID + " INTEGER PRIMARY KEY," +
                    MusicContract.MusicEntry.COLUMN_NAME_AUTOR + TEXT_TYPE + COMMA_SEP +
                    MusicContract.MusicEntry.COLUMN_NAME_PJESMA + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MusicContract.MusicEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "FarukMusic.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

        // On creation of database we will fill it with default data
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        values.put(MusicContract.MusicEntry.COLUMN_NAME_AUTOR, "Bon Jovi");
        values.put(MusicContract.MusicEntry.COLUMN_NAME_PJESMA, "It's my life");

        values1.put(MusicContract.MusicEntry.COLUMN_NAME_AUTOR, "Eminem");
        values1.put(MusicContract.MusicEntry.COLUMN_NAME_PJESMA, "Rap God");

        db.insert(MusicContract.MusicEntry.TABLE_NAME, null, values);
        db.insert(MusicContract.MusicEntry.TABLE_NAME, null, values1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
