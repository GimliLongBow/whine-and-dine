package com.substratalcode.whineanddine.ui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.substratalcode.whineanddine.R;
import com.substratalcode.whineanddine.db.Feeding;
import com.substratalcode.whineanddine.db.FeedingsDataSource;

import java.sql.SQLException;
import java.util.List;

public class HistoryFragment extends ListFragment {

    public static final String TAG = "HistoryFragment";

    FeedingsDataSource datasource;
    ArrayAdapter<Feeding> adapter;

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
                             Bundle savedInstanceState) throws NullPointerException {
        // Set up the view variables.
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        // Load up the view!
        datasource = new FeedingsDataSource(getActivity());
        openDatasource();

        List<Feeding> values = datasource.getAllFeedings();

        adapter = new ArrayAdapter<Feeding>(getActivity(), android.R.layout.simple_list_item_1, values);

        setListAdapter(adapter);

        Log.w(TAG, "View created.");

        return rootView;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
        Log.w(TAG, "Clicked an item.");

    }

    @Override
    public void onResume() {
        openDatasource();
        super.onResume();
    }

    @Override
    public void onPause() {
        datasource.close();
        super.onPause();
    }

    public void loadData() {
        openDatasource();
        datasource.loadTestData();
        datasource.close();
        Log.w(TAG, "Loaded data.");

        adapter.notifyDataSetChanged();
    }

    private void openDatasource() {
        try {
            datasource.open();
        } catch (SQLException ex) {
            Log.w(TAG, "Crashed on resuming.");
            datasource.close();
        }
    }
}
