package io.github.xzhang1234.tasktimer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity implements CursorRecyclerViewAdapter.OnTaskClickListener,
        AddEditActivityFragment.OnSaveClicked, AppDialog.DialogEvents {
    private static final String TAG = "MainActivity";

    private boolean mTwoPane = false;
    private static final String ADD_EDIT_FRAGMENT = "AddEditFragment";
    private static final int DELETE_DIALOG_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(findViewById(R.id.task_details_container) != null) {
            // The detail container view will be present only in the large-screen layouts (res/values-land and res/values-sw600dp).
            // If this view is present, then the activity should be in two-pane mode.
            mTwoPane = true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.menumain_addTask:
                taskEditRequest(null);
                break;
            case R.id.menumain_showFinished:
                Intent intent = new Intent(this, ListFinishedActivity.class);
                startActivity(intent);
                break;
            case R.id.menumain_sort_by_creating_time:
                editor = getApplicationContext().getSharedPreferences("Pref", MODE_PRIVATE).edit();
                editor.putInt("ORDER", AppLoader.MAIN_ACTIVITY_SORT_BY_CREATING_TIME_LOADER_ID);
                editor.commit();
                getSupportLoaderManager().initLoader(AppLoader.MAIN_ACTIVITY_SORT_BY_CREATING_TIME_LOADER_ID, null,
                        (LoaderManager.LoaderCallbacks<Cursor>) getSupportFragmentManager().findFragmentById(R.id.fragment));
                break;
            case R.id.menumain_sort_by_name:
                editor = getApplicationContext().getSharedPreferences("Pref", MODE_PRIVATE).edit();
                editor.putInt("ORDER", AppLoader.MAIN_ACTIVITY_SORT_BY_NAME_LOADER_ID);
                editor.commit();
                getSupportLoaderManager().initLoader(AppLoader.MAIN_ACTIVITY_SORT_BY_NAME_LOADER_ID, null,
                        (LoaderManager.LoaderCallbacks<Cursor>) getSupportFragmentManager().findFragmentById(R.id.fragment));
                break;
            case R.id.menumain_sort_by_priority:
                editor = getApplicationContext().getSharedPreferences("Pref", MODE_PRIVATE).edit();
                editor.putInt("ORDER", AppLoader.MAIN_ACTIVITY_SORT_BY_PRIORITY_LOADER_ID);
                editor.commit();
                getSupportLoaderManager().initLoader(AppLoader.MAIN_ACTIVITY_SORT_BY_PRIORITY_LOADER_ID, null,
                        (LoaderManager.LoaderCallbacks<Cursor>) getSupportFragmentManager().findFragmentById(R.id.fragment));
                break;
            case R.id.menumain_generate:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEditClick(Task task) {
        taskEditRequest(task);
    }

    @Override
    public void onDeleteClick(Task task) {
        Log.d(TAG, "onDeleteClick: starts");

        AppDialog dialog = new AppDialog();
        Bundle args = new Bundle();
        args.putInt(AppDialog.DIALOG_ID, DELETE_DIALOG_ID);
        args.putString(AppDialog.DIALOG_MESSAGE, getString(R.string.deldiag_message, task.getName()));
        args.putInt(AppDialog.DIALOG_POSITIVE_RID, R.string.deldiag_positive_caption);
        args.putLong("TaskId", task.getId());

        dialog.setArguments(args);
        dialog.show(getFragmentManager(), null);
    }

    @Override
    public void onFinishClick(Task task) {
        Log.d(TAG, "onFinishClick: starts");

        //update task status
        ContentResolver contentResolver = this.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(TasksContract.Columns.TASKS_STATUS, 1);
        if(values.size() != 0) {
            Log.d(TAG, "onClick: updating task");
            contentResolver.update(TasksContract.buildTaskUri(task.getId()), values, null, null);
        }

        Log.d(TAG, "onFinishClick: ends");
    }


    private void taskEditRequest(Task task) {
        Log.d(TAG, "taskEditRequest: starts");
        if(mTwoPane) {
            Log.d(TAG, "taskEditRequest: in two-pane mode (tablet)");
            AddEditActivityFragment fragment = new AddEditActivityFragment();

            Bundle arguments = new Bundle();
            arguments.putSerializable(Task.class.getSimpleName(), task);
            fragment.setArguments(arguments);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.task_details_container, fragment);
            fragmentTransaction.commit();
        } else {
            Log.d(TAG, "taskEditRequest: in single-pane mode (phone)");
            // in single-pane mode, start the detail activity for the selected item Id.
            Intent detailIntent = new Intent(this, AddEditActivity.class);
            if(task != null) { // editing a task
                detailIntent.putExtra(Task.class.getSimpleName(), task);
                startActivity(detailIntent);
            } else { // adding a new task
                startActivity(detailIntent);
            }
        }
    }

    @Override
    public void onSaveClicked() {
        Log.d(TAG, "onSaveClicked: starts");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.task_details_container);
        if(fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

    @Override
    public void onPositiveDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onPositiveDialogResult: called");
        Long taskId = args.getLong("TaskId");
        if(BuildConfig.DEBUG && taskId == 0) {
            throw new AssertionError("Task ID is zero");
        }
        getContentResolver().delete(TasksContract.buildTaskUri(taskId), null, null);
    }

    @Override
    public void onNegativeDialogResult(int dialogId, Bundle args) {
        Log.d(TAG, "onNegativeDialogResult: called");
    }

    @Override
    public void onDialogCancelled(int dialogId) {
        Log.d(TAG, "onDialogCancelled: called");
    }

}
