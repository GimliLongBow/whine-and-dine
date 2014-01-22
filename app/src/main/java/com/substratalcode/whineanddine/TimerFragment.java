package com.substratalcode.whineanddine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TimerFragment extends Fragment {

    public static TimerFragment newInstance() {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public TimerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);


        return rootView;
    }
}
