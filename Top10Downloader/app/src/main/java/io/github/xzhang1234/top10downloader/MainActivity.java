package io.github.xzhang1234.top10downloader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView listApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listApps = (ListView) findViewById(R.id.xmlListView);
        Log.d(TAG, "onCreate: starting AsyncTask");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/xml");

        Log.d(TAG, "onCreate: done");
    }

    private class DownloadData extends AsyncTask<String, Void, String> {

        private static final String TAG = "DownloadData";
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "doInBackground: starts with " + params[0]);
            String rssFeed = downloadXML(params[0]);
            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: Error downloading");
            }
            return rssFeed;
        }

        @Override
        protected void onPostExecute(String xmlData) {
            super.onPostExecute(xmlData);
            Log.d(TAG, "onPostExecute: parameter is " + xmlData);
            ParseApplications parseApplication = new ParseApplications();
            parseApplication.parse(xmlData);
            ArrayList<FeedEntry> apps = parseApplication.getApplications();
            ArrayAdapter arrayAdapter = new FeedAdapter(MainActivity.this, R.layout.list_record, apps);
            listApps.setAdapter(arrayAdapter);
        }

        private String downloadXML(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code is " + response);

                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                int charsRead;
                char[] inputBuffer = new char[500];
                while(true) {
                    charsRead = reader.read(inputBuffer);
                    if (charsRead < 0) {
                        break;
                    }
                    if (charsRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();
                return xmlResult.toString();
            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: Invalid URL " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "downloadXML: IO Exception reading data: " + e.getMessage());
            } catch (SecurityException e) {
                Log.e(TAG, "downloadXML: Security Exception. Needs permission, " + e.getMessage());
                e.printStackTrace();
            }
            return null;

        }


    }
}
