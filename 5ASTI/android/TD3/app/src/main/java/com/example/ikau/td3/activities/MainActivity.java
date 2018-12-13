package com.example.ikau.td3.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ikau.td3.R;
import com.example.ikau.td3.enums.ActionsEnum;
import com.example.ikau.td3.fragments.PlaceholderFragment;
import com.example.ikau.td3.fragments.ProgressSpinnerFragment;
import com.example.ikau.td3.tasks.AsyncFlickrTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set default fragment
        this.setMainFragment(new PlaceholderFragment());
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

    private void setMainFragment(Fragment fragment)
    {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.layoutFragment, fragment)
            .commit();
    }

    public void showProgressBar()
    {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.layoutFragment, new ProgressSpinnerFragment())
            .commit();
    }
}
