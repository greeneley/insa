package com.example.ikau.td3.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.ikau.td3.R;
import com.example.ikau.td3.fragments.MyPreferencesFragment;

/**
 * Activité de préférence affichant l'unique fragment de préférence de l'application.
 *
 * Elle n'hérite pas de PreferenceActivity car j'ai eu de gros problèmes pour gérer
 * la compatibilité avec les librairies de support v4 et v7...
 * À la place, elle affiche directement le fragment de préférence comme un fragment usuel.
 */
public class MyPreferenceActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_preference); // Le layout associé

        // Init de l'actionbar
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Préférences");
        }

        // Affichage du fragment (unique) de préférence.
        this.getFragmentManager()
                .beginTransaction()
                .replace(R.id.myPreferenceFragment, new MyPreferencesFragment())
                .commit();
    }
}
