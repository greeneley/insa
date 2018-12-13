package com.example.ikau.td3.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.ikau.td3.R;
import com.example.ikau.td3.adapters.ImageAdapter;

import java.util.ArrayList;

public class GridViewFragment extends Fragment
{
    public ArrayList<View> images;

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
        this.images = new ArrayList<>();
        for(int i=0; i<9; i++)
        {
            this.images.add(new ProgressBar(this.getContext()));
        }

        GridView gridView = v.findViewById(R.id.gridViewImages);
        gridView.setAdapter(new ImageAdapter(this.images));
    }
}
