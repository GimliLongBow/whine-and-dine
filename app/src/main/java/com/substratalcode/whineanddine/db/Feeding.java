package com.substratalcode.whineanddine.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Feeding {
    private int entryId;
    private Date startTime;
    public long leftTime = 0l; // Left time in seconds.
    public long rightTime = 0l; // Right time in seconds.

    private static final String DATE_FORMAT = "M/d, h:m a";
    private static SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public Feeding() {
        startTime = new Date();
    }

    public Feeding(int id, String start, String left, String right) {
        entryId = id;
        try {
            startTime = sdf.parse(start);
        } catch (ParseException ex) {
            startTime = new Date();
        }
        leftTime = Long.parseLong(left);
        rightTime = Long.parseLong(right);
    }

    public Feeding(String start, String left, String right) {
        try {
            startTime = sdf.parse(start);
        } catch (ParseException ex) {
            startTime = new Date();
        }
        leftTime = Long.parseLong(left);
        rightTime = Long.parseLong(right);
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(String timeString) {
        try {
            startTime = sdf.parse(timeString);
        } catch (ParseException ex) {
            startTime = new Date();
        }
    }

    public void setStartTime(Date dateObj) {
        startTime = dateObj;
    }

    public int getEntryId() {return entryId;}
    public void setEntryId(int id) {entryId = id;}

    public String toString() {

        return String.format(
                "Start Time: %s, L: %s R %s",
                sdf.format(startTime),
                leftTime / 60, // TODO: Convert to minutes.
                rightTime / 60 // TODO: Convert to minutes.
        );
    }
}
