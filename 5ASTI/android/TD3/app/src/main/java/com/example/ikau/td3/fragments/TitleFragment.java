package com.example.ikau.td3.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ikau.td3.R;

import java.util.ArrayList;

/**
 * Fragment affichant une Listview contenant la liste des titres des images du feed Flickr.
 *
 * Utilisé par MainActivity sur ActionsEnum.TITLES.
 */
public class TitleFragment extends Fragment
{
    public TitleFragment()
    {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_titles, container, false);
        this.insertTitles(v); // Init des titles
        return v;
    }

    private void insertTitles(View v)
    {
        // Récupération de l'ArrayList de titres
        ArrayList<String> titles = getArguments().getStringArrayList("titles");

        // Récupération de la Listview
        ListView listView = (ListView) v.findViewById(R.id.listViewTitles);

        // Création et confiuration de l'adaptater
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, titles);
        listView.setAdapter(adapter);
    }
}
