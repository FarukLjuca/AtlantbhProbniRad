package com.example.faruk.learningcontentproviders.Database;

import android.provider.BaseColumns;

/**
 * Created by Faruk on 08/06/16.
 */
public final class MusicContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public MusicContract() {}

    /* Inner class that defines the table contents */
    public static abstract class MusicEntry implements BaseColumns {
        public static final String TABLE_NAME = "music";
        public static final String COLUMN_NAME_AUTOR = "Autor";
        public static final String COLUMN_NAME_PJESMA = "Pjesma";
    }
}
