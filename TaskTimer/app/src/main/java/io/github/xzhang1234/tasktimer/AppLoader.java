package io.github.xzhang1234.tasktimer;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.security.InvalidParameterException;

/**
 * Created by xiaoyun on 7/11/17.
 */

public class AppLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "AppLoader";

    public static final int MAIN_ACTIVITY_SORT_BY_CREATING_TIME_LOADER_ID = 1;
    public static final int MAIN_ACTIVITY_SORT_BY_NAME_LOADER_ID = 2;
    public static final int MAIN_ACTIVITY_SORT_BY_PRIORITY_LOADER_ID = 3;

    private CursorRecyclerViewAdapter mAdapter;
    private Context mContext;

    public AppLoader(CursorRecyclerViewAdapter mAdapter, Context mContext) {
        this.mAdapter = mAdapter;
        this.mContext = mContext;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: starts with id " + id);
        String[] projection = {TasksContract.Columns._ID, TasksContract.Columns.TASKS_NAME,
                TasksContract.Columns.TASKS_DESCRIPTION, TasksContract.Columns.TASKS_SORTORDER};
        String sortOrder;

        switch(id) {
            case MAIN_ACTIVITY_SORT_BY_CREATING_TIME_LOADER_ID:
                sortOrder = TasksContract.Columns._ID;
                break;
            case MAIN_ACTIVITY_SORT_BY_NAME_LOADER_ID:
                sortOrder = TasksContract.Columns.TASKS_NAME + "," + TasksContract.Columns._ID;
                break;
            case MAIN_ACTIVITY_SORT_BY_PRIORITY_LOADER_ID:
                sortOrder = TasksContract.Columns.TASKS_SORTORDER + "," + TasksContract.Columns._ID;
                break;
            default:
                throw new InvalidParameterException(TAG + ".onCreateLoader called with invalid loader id" + id);
        }
        return new CursorLoader(
                mContext,
                TasksContract.CONTENT_URI,
                projection,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "Entering onLoadFinished");
        mAdapter.swapCursor(data);
        int count = mAdapter.getItemCount();
        Log.d(TAG, "onLoadFinished: count is " + count);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: starts");
        mAdapter.swapCursor(null);
    }
}
