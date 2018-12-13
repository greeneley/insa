package com.example.ikau.td3.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ikau.td3.R;

public class ProgressSpinnerFragment extends Fragment
{

    public ProgressSpinnerFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_progress_spinner, container, false);
        return v;
    }
}
