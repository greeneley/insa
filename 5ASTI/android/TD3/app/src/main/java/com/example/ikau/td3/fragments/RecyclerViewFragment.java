package com.example.ikau.td3.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ikau.td3.R;
import com.example.ikau.td3.adapters.RecyclerAdapter;
import com.example.ikau.td3.enums.DownloadAction;
import com.example.ikau.td3.tasks.AsyncDownloadBitmapTask;

import java.util.ArrayList;

public class RecyclerViewFragment extends Fragment
{
    public RecyclerViewFragment(){}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        this.populateRecycler(v);
        return v;
    }

    private void populateRecycler(View v)
    {
        // Création de l'adapter
        Bundle args = this.getArguments();
        RecyclerAdapter adapter = new RecyclerAdapter(args.getStringArrayList("titles"), args.getStringArrayList("authors"));

        // Récupération et initialisation du recyclerview
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        ArrayList<String> urls = this.getArguments().getStringArrayList("urls");
        for(int i=0; i<urls.size(); i++)
        {
            new AsyncDownloadBitmapTask(this.getView()).execute(urls.get(i), i, DownloadAction.RecyclerView);
        }
    }
}
