package com.example.ikau.td3.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ikau.td3.R;

import org.json.JSONObject;

/**
 * C'est le fragment qui affiche les données brutes du JSON récupéré par l'url de feed Flickr.
 *
 * Affiché par MainActivity avec ActionsEnum.PLAIN_JSON.
 */
public class PlainJSONFragment extends Fragment
{
    public PlainJSONFragment()
    {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plain_json, container, false);
        this.setPlainJSON(v); // Init de la vue.
        return v;
    }

    /**
     * Initialisation des données du fragment lorsque l'on es sûr que la vue est prête.
     * @param v La view du fragment.
     */
    public void setPlainJSON(View v)
    {
        TextView textView = (TextView) v.findViewById(R.id.textViewPlainJSON);
        textView.setText(getArguments().getString("json"));

        // Permet de rendre le TextView scrollable de manière kinétique (le mouvement est gardé au levé du pouce).
        textView.setMovementMethod(new ScrollingMovementMethod());
    }
}
