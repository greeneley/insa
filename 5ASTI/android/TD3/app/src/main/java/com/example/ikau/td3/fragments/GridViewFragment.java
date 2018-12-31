package com.example.ikau.td3.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.ikau.td3.R;
import com.example.ikau.td3.adapters.ImageAdapter;
import com.example.ikau.td3.tasks.AsyncDownloadTask;

import java.util.ArrayList;

public class GridViewFragment extends Fragment
{
    public GridViewFragment()
    {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gridview, container, false);
        this.populateGrid(v);
        return v;
    }

    private void populateGrid(View v)
    {
        // Création des items de base
        ArrayList<View> views = new ArrayList<>();
        for(int i=0; i<20; i++)
        {
            views.add(new ProgressBar(this.getContext()));
        }

        // Création de l'adapter
        ImageAdapter adapter = new ImageAdapter(views);

        // Création de la GridView
        GridView gridView = v.findViewById(R.id.gridViewImages);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        ArrayList<String> urls = this.getArguments().getStringArrayList("urls");
        for(int i=0; i<urls.size(); i++)
        {
            new AsyncDownloadTask(this.getView()).execute(urls.get(i), i);
        }

    }
}
