package io.github.xzhang1234.tasktimer;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListFinishedActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "ListFinishedActivityFra";

    private ListFinishedCursorRecyclerViewAdapter mAdapter;
    private AppLoader mAppLoader;

    public ListFinishedActivityFragment() {
        Log.d(TAG, "ListFinishedActivityFragment: starts");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: starts");
        View view = inflater.inflate(R.layout.fragment_list_finished, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.task_list_finished);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ListFinishedCursorRecyclerViewAdapter(null, (ListFinishedCursorRecyclerViewAdapter.OnTaskClickListener) getActivity());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        mAppLoader = new AppLoader(mAdapter, getActivity());
        SharedPreferences pref = getContext().getSharedPreferences("Pref", MODE_PRIVATE);
        int order = pref.getInt("ORDER_FINISHED", AppLoader.LIST_FINISHED_ACTIVITY_SORT_BY_CREATING_TIME_LOADER_ID);
        getLoaderManager().initLoader(order, null, this);

        Log.d(TAG, "onCreateView: returning");
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mAppLoader.onCreateLoader(id, args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAppLoader.onLoadFinished(loader, data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAppLoader.onLoaderReset(loader);
    }

}
