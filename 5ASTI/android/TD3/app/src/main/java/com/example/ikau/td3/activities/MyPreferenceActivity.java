package com.example.ikau.td3.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.ikau.td3.R;
import com.example.ikau.td3.fragments.MyPreferencesFragment;

public class MyPreferenceActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_preference);

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Préférences");
        }

        this.getFragmentManager()
                .beginTransaction()
                .replace(R.id.myPreferenceFragment, new MyPreferencesFragment())
                .commit();
    }
}
