package com.substratalcode.whineanddine.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class FeedingsDataSource {
    private SQLiteDatabase database;
    private FeedingDbHelper dbHelper;
    private String[] allColumns = { FeedingEntry._ID, FeedingEntry.COLUMN_START_TIME, FeedingEntry.COLUMN_LEFT_TIME, FeedingEntry.COLUMN_RIGHT_TIME };

    private static final String TAG = "FeedingsDataSource";

    /**
     * Initialize the data source.
     * @param context Context of the operation.
     */
    public FeedingsDataSource(Context context) {
        dbHelper = new FeedingDbHelper(context);
    }

    /**
     * Opens the database connection. May blow up if there is no DB or an SQL error.
     * @throws SQLException
     */
    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
        Log.w(TAG, "Opened a connection to the DB.");
    }

    /**
     * Closes the database helper connection.
     */
    public void close() {
        dbHelper.close();
        Log.w(TAG, "Closed a connection to the DB.");
    }

    /**
     *
     * @param feeding Feeding object to save.
     * @return ID of the entry just saved in the database.
     */
    public long saveFeeding(Feeding feeding) {
        // Create the values to insert into the DB.
        ContentValues vals = new ContentValues();
        vals.put(FeedingEntry.COLUMN_START_TIME, feeding.getStartTime().toString());
        vals.put(FeedingEntry.COLUMN_LEFT_TIME, feeding.leftTime);
        vals.put(FeedingEntry.COLUMN_RIGHT_TIME, feeding.rightTime);

        // Set up the cursor to insert it.
        return database.insert(FeedingEntry.TABLE_NAME, null, vals);
    }

    public void deleteFeeding(long id) {
        String where = FeedingEntry._ID+" = "+id;
        database.delete(FeedingEntry.TABLE_NAME, where, null);
        Log.w(FeedingsDataSource.class.getName(), "Deleted feeding with ID of " + id);
    }

    /**
     * Returns all the feedings in the database.
     * @return List of feedings
     */
    public List<Feeding> getAllFeedings() {
        List<Feeding> feedings = new ArrayList<Feeding>();

        Cursor feedingCursor = database.query(
                FeedingEntry.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                FeedingEntry.SORT_ID
        );

        feedingCursor.moveToFirst();
        while (!feedingCursor.isAfterLast()) {
            Feeding feeding = new Feeding(
                    feedingCursor.getInt(0),
                    feedingCursor.getString(1),
                    feedingCursor.getString(2),
                    feedingCursor.getString(3)
            );
            feedings.add(feeding);
            feedingCursor.moveToNext();
        }
        feedingCursor.close();
        return feedings;
    }

    public void loadTestData() {
        Random rand = new Random();
        int max = 900;
        int min = 10;
        int numTests = 50;
        int numSaved = 0;
        final int millisInDay = 24*60*60*1000;

        while(numSaved < numTests) {
            String leftTime = Integer.toString(rand.nextInt((max - min) + 1) + min);
            String rightTime = Integer.toString(rand.nextInt((max - min) + 1) + min);
            String startTime = Integer.toString(rand.nextInt(millisInDay));

            Feeding randFeeding = new Feeding(startTime, leftTime, rightTime);

            this.saveFeeding(randFeeding);
            numSaved++;
        }



    }
}
