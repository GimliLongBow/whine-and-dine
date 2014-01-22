package com.substratalcode.whineanddine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HistoryFragment extends Fragment {

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public HistoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        return rootView;
    }
}
