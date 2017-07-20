package io.github.xzhang1234.todolist;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
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
    public static final int LIST_FINISHED_ACTIVITY_SORT_BY_CREATING_TIME_LOADER_ID = 4;
    public static final int LIST_FINISHED_ACTIVITY_SORT_BY_NAME_LOADER_ID = 5;
    public static final int LIST_FINISHED_ACTIVITY_SORT_BY_PRIORITY_LOADER_ID = 6;

    private RecyclerView.Adapter mAdapter;
    private Context mContext;

    public AppLoader(RecyclerView.Adapter mAdapter, Context mContext) {
        this.mAdapter = mAdapter;
        this.mContext = mContext;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: starts with id " + id);
        String[] projection = {TasksContract.Columns._ID, TasksContract.Columns.TASKS_NAME,
                TasksContract.Columns.TASKS_DESCRIPTION, TasksContract.Columns.TASKS_SORTORDER,
                TasksContract.Columns.TASKS_STATUS};
        String sortOrder;
        String selection = "STATUS=?";
        String[] selectionArgs = new String[1];

        switch(id) {
            case MAIN_ACTIVITY_SORT_BY_CREATING_TIME_LOADER_ID:
                sortOrder = TasksContract.Columns._ID;
                selectionArgs[0] = String.valueOf(TasksContract.TaskStatus.UNFINISHED.ordinal());
                break;
            case MAIN_ACTIVITY_SORT_BY_NAME_LOADER_ID:
                sortOrder = TasksContract.Columns.TASKS_NAME + "," + TasksContract.Columns._ID;
                selectionArgs[0] = String.valueOf(TasksContract.TaskStatus.UNFINISHED.ordinal());
                break;
            case MAIN_ACTIVITY_SORT_BY_PRIORITY_LOADER_ID:
                sortOrder = TasksContract.Columns.TASKS_SORTORDER + "," + TasksContract.Columns._ID;
                selectionArgs[0] = String.valueOf(TasksContract.TaskStatus.UNFINISHED.ordinal());
                break;
            case LIST_FINISHED_ACTIVITY_SORT_BY_CREATING_TIME_LOADER_ID:
                sortOrder = TasksContract.Columns._ID;
                selectionArgs[0] = String.valueOf(TasksContract.TaskStatus.FINISHED.ordinal());
                break;
            case LIST_FINISHED_ACTIVITY_SORT_BY_NAME_LOADER_ID:
                sortOrder = TasksContract.Columns.TASKS_NAME + "," + TasksContract.Columns._ID;
                selectionArgs[0] = String.valueOf(TasksContract.TaskStatus.FINISHED.ordinal());
                break;
            case LIST_FINISHED_ACTIVITY_SORT_BY_PRIORITY_LOADER_ID:
                sortOrder = TasksContract.Columns.TASKS_SORTORDER + "," + TasksContract.Columns._ID;
                selectionArgs[0] = String.valueOf(TasksContract.TaskStatus.FINISHED.ordinal());
                break;
            default:
                throw new InvalidParameterException(TAG + ".onCreateLoader called with invalid loader id" + id);
        }
        return new CursorLoader(
                mContext,
                TasksContract.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "Entering onLoadFinished");
        switch(loader.getId()) {
            case MAIN_ACTIVITY_SORT_BY_CREATING_TIME_LOADER_ID:
            case MAIN_ACTIVITY_SORT_BY_NAME_LOADER_ID:
            case MAIN_ACTIVITY_SORT_BY_PRIORITY_LOADER_ID:
                ((CursorRecyclerViewAdapter) mAdapter).swapCursor(data);
                break;
            case LIST_FINISHED_ACTIVITY_SORT_BY_CREATING_TIME_LOADER_ID:
            case LIST_FINISHED_ACTIVITY_SORT_BY_NAME_LOADER_ID:
            case LIST_FINISHED_ACTIVITY_SORT_BY_PRIORITY_LOADER_ID:
                ((ListFinishedCursorRecyclerViewAdapter) mAdapter).swapCursor(data);
                break;
            default:
                throw new InvalidParameterException(TAG + ".onCreateLoader called with invalid loader id" + loader.getId());
        }
        int count = mAdapter.getItemCount();
        Log.d(TAG, "onLoadFinished: count is " + count);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: starts");
        switch(loader.getId()) {
            case MAIN_ACTIVITY_SORT_BY_CREATING_TIME_LOADER_ID:
            case MAIN_ACTIVITY_SORT_BY_NAME_LOADER_ID:
            case MAIN_ACTIVITY_SORT_BY_PRIORITY_LOADER_ID:
                ((CursorRecyclerViewAdapter) mAdapter).swapCursor(null);
                break;
            case LIST_FINISHED_ACTIVITY_SORT_BY_CREATING_TIME_LOADER_ID:
            case LIST_FINISHED_ACTIVITY_SORT_BY_NAME_LOADER_ID:
            case LIST_FINISHED_ACTIVITY_SORT_BY_PRIORITY_LOADER_ID:
                ((ListFinishedCursorRecyclerViewAdapter) mAdapter).swapCursor(null);
                break;
            default:
                throw new InvalidParameterException(TAG + ".onCreateLoader called with invalid loader id" + loader.getId());
        }
    }
}
