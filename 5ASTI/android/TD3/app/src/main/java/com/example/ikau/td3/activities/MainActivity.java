package com.example.ikau.td3.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ikau.td3.R;
import com.example.ikau.td3.enums.ActionsEnum;
import com.example.ikau.td3.fragments.PlaceholderFragment;
import com.example.ikau.td3.fragments.ProgressSpinnerFragment;
import com.example.ikau.td3.tasks.AsyncFlickrTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Définit le fragment par défaut
        this.setMainFragment(new PlaceholderFragment());

        // Init des prefs
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.contains("keyword"))
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("keyword", "cats");
            editor.apply();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_favorite:
                return true;

            case R.id.menu_preferences:
                Intent intent = new Intent(this, MyPreferenceActivity.class);
                this.startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onClickButtonResults(View v)
    {
        String urlString = this.getSearchUrl();
        new AsyncFlickrTask(this).execute(urlString, ActionsEnum.PLAIN_JSON);
    }

    protected void onClickButtonTitles(View v)
    {
        String urlString = this.getSearchUrl();
        new AsyncFlickrTask(this).execute(urlString, ActionsEnum.TITLES);
    }

    protected void onClickButtonImages(View v)
    {
        String urlString = this.getSearchUrl();
        new AsyncFlickrTask(this).execute(urlString, ActionsEnum.IMAGES);
    }

    protected void onClickButtonAdvanced(View v)
    {
        String urlString = this.getSearchUrl();
        new AsyncFlickrTask(this).execute(urlString, ActionsEnum.ADVANCED);
    }

    public void setMainFragment(Fragment fragment)
    {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.layoutFragment, fragment)
            .commit();
    }

    public void showProgressBar()
    {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.layoutFragment, new ProgressSpinnerFragment())
            .commit();
    }

    private String getSearchUrl()
    {
        // Accès aux préférences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String keyword = prefs.getString("keyword", "cats");

        // Création du string
        return "https://www.flickr.com/services/feeds/photos_public.gne?format=json&tags="+keyword;
    }
}
