package com.substratalcode.whineanddine;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.os.Handler;

public class TimerFragment extends Fragment {

    /**
     * The logging tag for the activity.
     */
    private static final String TAG = "TimerFragment";

    /**
     * The timer for the main tab view.
     */
    private Handler customHandler;

    /**
     * The private variables to hold references to the view elements.
     */
    TextView timerMain;
    TextView timerMs;
    ToggleButton leftBtn;
    ToggleButton rightBtn;
    Button doneBtn;

    /**
     * Timer variables to hold values.
     */
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    boolean running = false;

    public static TimerFragment newInstance() {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public TimerFragment() {
        customHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);

        timerMain = (TextView) rootView.findViewById(R.id.timerMain);
        timerMs = (TextView) rootView.findViewById(R.id.timerMs);
        leftBtn = (ToggleButton) rootView.findViewById(R.id.toggle_left);
        rightBtn = (ToggleButton) rootView.findViewById(R.id.toggle_right);
        doneBtn = (Button) rootView.findViewById(R.id.button_done);

        // Click listeners!
        leftBtn.setOnClickListener(toggleButtonsListener);
        rightBtn.setOnClickListener(toggleButtonsListener);
        doneBtn.setOnClickListener(doneButtonListener);

        return rootView;
    }

    public void startTimer(View view) {
        if(!running) {
            // If it isn't running, fire it up.
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
            // Change the text on the Start button.
            running = true;

            Log.v(TAG, "Starting.");
        } else {
            // Just pause it.
            timeSwapBuff += timeInMilliseconds;
            customHandler.removeCallbacks(updateTimerThread);
            running = false;

            Log.v(TAG, "Pausing");
        }
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerMain.setText("" + String.format("%02d", mins) + ":" + String.format("%02d", secs) );
            timerMs.setText(String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }

    };

    OnClickListener toggleButtonsListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startTimer(v);
        }
    };

    OnClickListener doneButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
