package io.github.xzhang1234.todolist;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ListFinishedActivity extends AppCompatActivity implements ListFinishedCursorRecyclerViewAdapter.OnTaskClickListener {
    private static final String TAG = "ListFinishedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_finished);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_finished, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        SharedPreferences.Editor editor;

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.menu_list_finished_sort_by_creating_time:
                editor = getApplicationContext().getSharedPreferences("Pref", MODE_PRIVATE).edit();
                editor.putInt("ORDER_FINISHED", AppLoader.LIST_FINISHED_ACTIVITY_SORT_BY_CREATING_TIME_LOADER_ID);
                editor.commit();
                getSupportLoaderManager().initLoader(AppLoader.LIST_FINISHED_ACTIVITY_SORT_BY_CREATING_TIME_LOADER_ID, null,
                        (LoaderManager.LoaderCallbacks<Cursor>) getSupportFragmentManager().findFragmentById(R.id.fragment));
                break;
            case R.id.menu_list_finished_sort_by_name:
                editor = getApplicationContext().getSharedPreferences("Pref", MODE_PRIVATE).edit();
                editor.putInt("ORDER_FINISHED", AppLoader.LIST_FINISHED_ACTIVITY_SORT_BY_NAME_LOADER_ID);
                editor.commit();
                getSupportLoaderManager().initLoader(AppLoader.LIST_FINISHED_ACTIVITY_SORT_BY_NAME_LOADER_ID, null,
                        (LoaderManager.LoaderCallbacks<Cursor>) getSupportFragmentManager().findFragmentById(R.id.fragment));
                break;
            case R.id.menu_list_finished_sort_by_priority:
                editor = getApplicationContext().getSharedPreferences("Pref", MODE_PRIVATE).edit();
                editor.putInt("ORDER_FINISHED", AppLoader.LIST_FINISHED_ACTIVITY_SORT_BY_PRIORITY_LOADER_ID);
                editor.commit();
                getSupportLoaderManager().initLoader(AppLoader.LIST_FINISHED_ACTIVITY_SORT_BY_PRIORITY_LOADER_ID, null,
                        (LoaderManager.LoaderCallbacks<Cursor>) getSupportFragmentManager().findFragmentById(R.id.fragment));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestoreClick(Task task) {
        Log.d(TAG, "onRestoreClick: starts");

        //revert status
        ContentResolver contentResolver = this.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(TasksContract.Columns.TASKS_STATUS, 0);
        if(values.size() != 0) {
            Log.d(TAG, "onClick: updating task");
            contentResolver.update(TasksContract.buildTaskUri(task.getId()), values, null, null);
        }

        Log.d(TAG, "onRestoreClick: ends");
    }

    @Override
    public void onDeleteClick(Task task) {
        Log.d(TAG, "onDeleteClick: starts");

        //delete task
        ContentResolver contentResolver = this.getContentResolver();
        contentResolver.delete(TasksContract.buildTaskUri(task.getId()), null, null);

        Log.d(TAG, "onDeleteClick: ends");
    }
}
