package io.github.xzhang1234.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xiaoyun on 7/1/17.
 */

enum DownloadStatus {
    IDLE,
    PROCESSING,
    NOT_INITIALIZED,
    FAILED_OR_EMPTY,
    OK
}


class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";

    interface OnDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus status);
    }

    private DownloadStatus downloadStatus;
    private final OnDownloadComplete callback;


    public GetRawData(OnDownloadComplete callback) {
        this.downloadStatus = DownloadStatus.IDLE;
        this.callback = callback;
    }


    void runInSameThread(String s) {
        Log.d(TAG, "runInSameThread starts");

        if(callback != null) {
            callback.onDownloadComplete(doInBackground(s), downloadStatus);
        }

        Log.d(TAG, "runInSameThread ends");
    }


    @Override
    protected String doInBackground(String... params) {
        if (params == null) {
            downloadStatus = DownloadStatus.NOT_INITIALIZED;
            return null;
        }

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            downloadStatus = DownloadStatus.PROCESSING;

            URL url = new URL(params[0]);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int response = connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was " + response);

            StringBuilder result = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while (null != (line = reader.readLine())) {
                result.append(line).append("\n");
            }

            downloadStatus = DownloadStatus.OK;
            return result.toString();

        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IO Exception reading data: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: IO Exception closing reader: " + e.getMessage());

                }
            }

        }

        downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
