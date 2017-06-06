package io.github.xzhang1234.learnbroadcastreceiver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void broadcastIntent(View view){
        Intent intent = new Intent();
        intent.setAction("io.github.xzhang1234.CUSTOM_INTENT");
        sendBroadcast(intent);
    }
}
