package com.substratalcode.whineanddine.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.substratalcode.whineanddine.R;
import com.substratalcode.whineanddine.db.Feeding;
import com.substratalcode.whineanddine.db.FeedingsDataSource;

import java.sql.SQLException;

public class TimerFragment extends Fragment {

    /**
     * The logging tag for the activity.
     */
    private static final String TAG = "TimerFragment";

    /**
     * The timer for the main tab view.
     */
    private Handler timeHandler;

    /**
     * The private variables to hold references to the view elements.
     */
    TextView timerMain;
    TextView timerMs;
    ToggleButton leftBtn;
    ToggleButton rightBtn;
    ImageButton doneBtn;

    /**
     * Timer variables to hold values.
     */
    private long leftTime = 0L; // Amount of time in this session spent on left.
    private long rightTime = 0L; // Amount of time in this session spent on right.
    private long startTimeMs = 0L; // Time which the button was clicked at.
    private long currentRunningTime  = 0L; // The amount of time since the left or right button was clicked.
    private boolean timerRunning = false;
    private int currentSide = 0; // Flag to determine if the feeding is on the left side or not.

    /**
     * Feeding variable to keep the current feeding around.
     */
    private Feeding currentFeeding = null;

    /**
     * Click listeners.
     */
    private OnClickListener toggleButtonsListener;
    private OnClickListener doneButtonListener;

    /**
     * Datasource!
     */
    FeedingsDataSource datasource;

    public static TimerFragment newInstance() {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public TimerFragment() {
        timeHandler = new Handler();
        currentFeeding = null;

        // Click listeners.
        toggleButtonsListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer(v);
            }
        };
        doneButtonListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the data!
                saveTimes();
                // Reset everything else.
                timerRunning = false;
                currentSide = 0;
                leftTime = rightTime = startTimeMs = 0L;
                timeHandler.removeCallbacks(updateTimerThread);
                timerMain.setText("00:00");
                timerMs.setText("000");
            }
        };

        // Timer thread startup.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);

        timerMain = (TextView) rootView.findViewById(R.id.timer_main);
        timerMs = (TextView) rootView.findViewById(R.id.timer_ms);
        leftBtn = (ToggleButton) rootView.findViewById(R.id.toggle_left);
        rightBtn = (ToggleButton) rootView.findViewById(R.id.toggle_right);
        doneBtn = (ImageButton) rootView.findViewById(R.id.button_done);

        // Click listeners!
        leftBtn.setOnClickListener(toggleButtonsListener);
        rightBtn.setOnClickListener(toggleButtonsListener);
        doneBtn.setOnClickListener(doneButtonListener);

        datasource = new FeedingsDataSource(getActivity());

        return rootView;
    }

    private void startTimer(View view) {
        if(!timerRunning) {
            if(currentFeeding == null) currentFeeding = new Feeding();

            // Fire up the timer.
            timerRunning = true;
            startTimeMs = SystemClock.uptimeMillis();
            timeHandler.postDelayed(updateTimerThread, 0);
        } else {
            // Store the current running time into either left or right:
            if(currentSide == leftBtn.getId()) leftTime += currentRunningTime;
            else if(currentSide == rightBtn.getId()) rightTime += currentRunningTime;

            if(currentSide == view.getId()) {
                // Just pause it.
                timeHandler.removeCallbacks(updateTimerThread);
                timerRunning = false;
            } else {
                startTimeMs = SystemClock.uptimeMillis();
            }
        }
        // The current side is the one that was just clicked.
        currentSide = view.getId();

        if(view.getId() == leftBtn.getId() && rightBtn.isChecked()) rightBtn.toggle();
        else if (view.getId() == rightBtn.getId() && leftBtn.isChecked()) leftBtn.toggle();
    }

    private void saveTimes() {
        currentFeeding.leftTime = leftTime/1000; // Convert from MS to Seconds.
        currentFeeding.rightTime = rightTime/1000;

        try{
            datasource.open();
            datasource.saveFeeding(currentFeeding);
            datasource.close();
            Log.w(TAG, "Finished saveTimes.");
        } catch (SQLException ex) {
            datasource.close();
        }
        currentFeeding = null;

    }

    /**
     * Thread to run the timer.
     */
    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            currentRunningTime = SystemClock.uptimeMillis() - startTimeMs;

            long timeToDisplay = currentRunningTime + leftTime + rightTime;

            int secs = (int) (timeToDisplay / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (timeToDisplay % 1000);

            // Update the UI.
            timerMain.setText("" + String.format("%02d", mins) + ":" + String.format("%02d", secs) );
            timerMs.setText(String.format("%03d", milliseconds));
            timeHandler.postDelayed(this, 0);
        }

    };
}
