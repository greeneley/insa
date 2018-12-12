package com.example.ikau.td3.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.ikau.td3.R;
import com.example.ikau.td3.enums.ActionsEnum;
import com.example.ikau.td3.tasks.AsyncFlickrTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onClickButtonResults(View v)
    {
        String urlString = "https://www.flickr.com/services/feeds/photos_public.gne?tags=cats&format=json";
        new AsyncFlickrTask(this).execute(urlString, ActionsEnum.PLAIN_JSON);
    }

    protected void onClickButtonTitles(View v)
    {
        // TODO
    }

    protected void onClickButtonImages(View v)
    {
        // TODO
    }

    protected void onClickButtonAdvanced(View v)
    {
        // TODO
    }
}
