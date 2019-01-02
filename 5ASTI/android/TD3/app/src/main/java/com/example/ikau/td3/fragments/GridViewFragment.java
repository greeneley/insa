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
import com.example.ikau.td3.adapters.GridViewAdapter;
import com.example.ikau.td3.enums.DownloadAction;
import com.example.ikau.td3.tasks.AsyncDownloadBitmapTask;

import java.util.ArrayList;

/**
 * Fragment affiché par le bouton 'Afficher les images'.
 *
 * C'est un fragment contenant une GridView en 3xK de layout fragment_gridview.xml.
 * Le fragment utilise un adapter personnalisé : GridViewAdapter.
 * Il est créé par AsyncFlickTask à la fin de la tâche asynchrone.
 */
public class GridViewFragment extends Fragment
{
    public GridViewFragment()
    {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gridview, container, false);
        this.populateGrid(v); // On init l'adapter
        return v;
    }

    /**
     * On initialise l'adapter une fois que la vue est créée et avant de l'afficher.
     * @param v La view du fragment.
     */
    private void populateGrid(View v)
    {
        // Création des items de base (ici des ProgressBar).
        ArrayList<View> views = new ArrayList<>();
        for(int i=0; i<this.getArguments().getStringArrayList("urls").size(); i++)
        {
            views.add(new ProgressBar(this.getContext()));
        }

        // Création de l'adapter
        GridViewAdapter adapter = new GridViewAdapter(views);

        // Création de la GridView
        GridView gridView = v.findViewById(R.id.gridViewImages);
        gridView.setAdapter(adapter);
    }

    /**
     * Une fois que l'on est sûr que la vue est disponible, on demande à des tâches
     * asynchrones de télécharger les images.
     */
    @Override
    public void onStart()
    {
        super.onStart();

        // Pour chaque urls, on demande à une tâche de télécharger l'image.
        ArrayList<String> urls = this.getArguments().getStringArrayList("urls");
        for(int i=0; i<urls.size(); i++)
        {
            new AsyncDownloadBitmapTask(this.getView()).execute(urls.get(i), i, DownloadAction.GridView);
        }

    }
}
