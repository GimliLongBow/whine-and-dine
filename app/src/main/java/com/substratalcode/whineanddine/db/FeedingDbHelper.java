package com.substratalcode.whineanddine.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FeedingDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "feedings.db";
    public static final int DATABASE_VERSION = 1;

    /**
     * SQL Creation.
     */
    public static final String SQL_CREATE_DATABASE = String.format(
            "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT);",
            FeedingEntry.TABLE_NAME,
            FeedingEntry._ID,
            FeedingEntry.COLUMN_START_TIME,
            FeedingEntry.COLUMN_LEFT_TIME,
            FeedingEntry.COLUMN_RIGHT_TIME
    );

    public static final String SQL_DELETE_DATABASE =  String.format(
            "DROP TABLE IF EXISTS %s",
            FeedingEntry.TABLE_NAME
    );

    public static final String SQL_RETRIEVE_ALL = String.format(
            "SELECT * FROM %s",
            FeedingEntry.TABLE_NAME
    );

    /**
     * Constructor!
     * @param context Context of the constructor.
     */
    public FeedingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creation.
     * @param db Database to store app data in.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DATABASE);
    }

    /**
     * Conversion utility.
     * @param db Original database.
     * @param oldVersion Upgrading from.
     * @param newVersion Upgrading to
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(FeedingDbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL(SQL_DELETE_DATABASE);
        onCreate(db);
    }
}
