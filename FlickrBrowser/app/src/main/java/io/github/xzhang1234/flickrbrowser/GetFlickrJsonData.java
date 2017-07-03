package io.github.xzhang1234.flickrbrowser;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoyun on 7/2/17.
 */

public class GetFlickrJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFlickrJsonData";

    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    //return value
    private List<Photo> photoList;
    //input value
    private String baseURL;
    private String language;
    private boolean matchAll;

    //callBack function to notify main activity
    private final OnDataAvailable callBack;

    //inner control variable
    private boolean runningOnSameThread = false;


    public GetFlickrJsonData(String baseURL, String language, boolean matchAll, OnDataAvailable callBack) {
        this.baseURL = baseURL;
        this.language = language;
        this.matchAll = matchAll;
        this.callBack = callBack;
    }


    void executeOnSameThread(String searchCriteria) {
        Log.d(TAG, "executeOnSameThread starts");

        runningOnSameThread = true;
        String uri = createUri(searchCriteria, language, matchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(uri);

        Log.d(TAG, "executeOnSameThread ends");
    }


    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d(TAG, "doInBackground starts");
        String destinationUri = createUri(params[0], language, matchAll);

        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground ends");
        return photoList;
    }


    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute starts");

        if(callBack != null) {
            callBack.onDataAvailable(photoList, DownloadStatus.OK);
        }

        Log.d(TAG, "onPostExecute ends");
    }


    private String createUri(String searchCriteria, String lang, boolean matchAll) {
        Log.d(TAG, "createUri starts");

        return Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", lang)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }


    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete starts. Status = " + status);

        if (status == DownloadStatus.OK) {
            photoList = new ArrayList<>();

            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray items = jsonData.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject photo = items.getJSONObject(i);
                    String title = photo.getString("title");
                    String author = photo.getString("author");
                    String authorId = photo.getString("author_id");
                    String tags = photo.getString("tags");

                    JSONObject jsonMedia = photo.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");

                    String link = photoUrl.replaceFirst("_m.", "_b.");

                    photoList.add(new Photo(title, author, authorId, link, tags, photoUrl));

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json data " + e.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }

            if(runningOnSameThread && callBack != null) {
                callBack.onDataAvailable(photoList, status);
            }

            Log.d(TAG, "onDownloadComplete ends");
        }
    }
}
