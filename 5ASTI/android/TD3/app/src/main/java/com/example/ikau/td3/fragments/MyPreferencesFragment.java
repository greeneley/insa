package com.example.ikau.td3.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.ikau.td3.R;

public class MyPreferencesFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_fragment);
    }
}
