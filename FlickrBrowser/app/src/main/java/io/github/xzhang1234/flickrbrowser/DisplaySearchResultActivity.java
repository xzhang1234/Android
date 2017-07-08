package io.github.xzhang1234.flickrbrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DisplaySearchResultActivity extends AppCompatActivity implements GetFlickrJsonData.OnDataAvailable,
        RecyclerItemClickListener.OnRecyclerClickListener {
    private static final String TAG = "DisplaySearchResultActi";

    private RecyclerView recyclerView;
    private FlickrRecyclerViewAdapter flickrRecyclerViewAdapter;

    private List<Photo> data;
    private static Bundle bundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        flickrRecyclerViewAdapter = new FlickrRecyclerViewAdapter(new ArrayList<Photo>(), this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(flickrRecyclerViewAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

        Log.d(TAG, "onCreate: ends");
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume starts");
        super.onResume();

        Intent intent = getIntent();
        String callingActivity = (String) intent.getSerializableExtra("CALLING_ACTIVITY");
        if (callingActivity != null && callingActivity.equals("MainActivity")) {
            String query = (String) intent.getSerializableExtra("QUERY_TAG");
            GetFlickrJsonData getFlickrJsonData = new GetFlickrJsonData("https://api.flickr.com/services/feeds/photos_public.gne", "en-us", true, this);
            getFlickrJsonData.execute(query);
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            //Retrieve data
            Gson gson = new Gson();
            String jsonData = sharedPreferences.getString("DATA", null);
            Type type = new TypeToken<List<Photo>>(){}.getType();
            this.data = gson.fromJson(jsonData, type);
            flickrRecyclerViewAdapter.loadNewData(data);

            // restore RecyclerView state
            if (bundleRecyclerViewState != null) {
                Parcelable listState = bundleRecyclerViewState.getParcelable("KEY_RECYCLER_STATE");
                recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            }
        }
        Log.d(TAG, "onResume ends");
    }


    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String jsonData = gson.toJson(data);
        sharedPreferences.edit().putString("DATA", jsonData).apply();

        // save RecyclerView state
        bundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        bundleRecyclerViewState.putParcelable("KEY_RECYCLER_STATE", listState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptinsMenu starts");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_display_result, menu);
        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected starts");

        int id = item.getItemId();
        if (id == R.id.action_search) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }

        Log.d(TAG, "onOptionsItemSelected() returned");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status) {
        Log.d(TAG, "onDataAvailable: starts");
        if (status == DownloadStatus.OK) {
            this.data = data;
            flickrRecyclerViewAdapter.loadNewData(data);
        } else {
            Log.e(TAG, "onDataAvailable failed with status " + status);
        }
        Log.d(TAG, "onDataAvailable: ends");
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: starts");
        Intent intent = new Intent(this, PhotoDetailsActivity.class);
        intent.putExtra("PHOTO", flickrRecyclerViewAdapter.getPhoto(position));
        intent.putExtra("POSITION", position);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(TAG, "onItemLongClick: starts");
    }


}
