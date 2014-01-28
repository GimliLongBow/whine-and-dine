package com.substratalcode.whineanddine.db;

import android.provider.BaseColumns;

/* Inner class that defines the table contents */
public abstract class FeedingEntry implements BaseColumns {
    public static final String TABLE_NAME = "feedings";
    public static final String COLUMN_START_TIME = "startTime";
    public static final String COLUMN_LEFT_TIME = "leftTime";
    public static final String COLUMN_RIGHT_TIME = "rightTime";

    public static final String SORT_ID= _ID + " DESC";
}