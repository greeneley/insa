package com.example.ikau.td3.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.ikau.td3.activities.MainActivity;
import com.example.ikau.td3.enums.ActionsEnum;
import com.example.ikau.td3.fragments.GridViewFragment;
import com.example.ikau.td3.fragments.PlainJSONFragment;
import com.example.ikau.td3.fragments.RecyclerViewFragment;
import com.example.ikau.td3.fragments.TitleFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Tâche asynchrone principale récupérant d'abord un feed Flickr puis traitant selon ActionsEnum.
 *
 * Utilisée par MainActivity pour moduler son fragment principal.
 */
public class AsyncFlickrTask extends AbstractAsyncTask<Object, Integer, JSONObject>
{

    /* ==============================================================
     *                          Propriétés
     * ==============================================================
     */
    /**
     * L'action demandée par MainActivity, précisant le fragment final.
     */
    private ActionsEnum action;

    /**
     * Référence faible vers MainActivity.
     */
    private WeakReference<Activity> activityWkRef;


    /* ==============================================================
     *                          Constructeur
     * ==============================================================
     */
    public AsyncFlickrTask(Activity activity)
    {
        this.activityWkRef = new WeakReference<Activity>(activity);
        this.action        = ActionsEnum.NONE;
    }


    /* ==============================================================
     *                          Overrides
     * ==============================================================
     */
    @Override
    protected void onPreExecute()
    {
        // On affiche la barre de chargement avant le traitement
        MainActivity activity = (MainActivity) this.activityWkRef.get();
        if(activity != null)
        {
            activity.showProgressBar();
        }
    }

    /**
     * On télécharge d'abord le feed Flickr contenant les 20 dernières images.
     * @param params String URL du feed Flickr, ActionsEnum post-traitement voulu.
     * @return JSON issu du feed Flickr.
     */
    @Override
    protected JSONObject doInBackground(Object... params)
    {
        // Vérification des paramètres
        if(params.length < 2)
        {
            Log.i("TLT", "[ERR] AsyncFlickrTask.doInBackground: param missing");
            return null;
        }

        // Récupération de l'action
        this.action = (ActionsEnum) params[1];

        // Requête du JSON
        try {
            // Récupération de l'url
            URL url = new URL((String)params[0]);

            // Ouverture de la connection;
            URLConnection connection = url.openConnection();
            connection.connect();

            // Récupération du JSON
            return this.getJsonFromFlickr(connection);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if(jsonObject == null)
        {
            Log.e("TLT", "[ERR] AsyncFlickrTask.onPostExecute: result is null");
            return;
        }
        if(this.activityWkRef.get() == null)
        {
            Log.e("TLT", "[ERR] AsyncFlickrTask.onPostExecute: MainActivity is null");
            return;
        }

        switch(this.action)
        {
            case PLAIN_JSON:
                this.showPlainJSON(jsonObject);
                break;

            case TITLES:
                this.showTitles(jsonObject);
                break;

            case IMAGES:
                this.showImages(jsonObject);
                break;

            case ADVANCED:
                this.showAdvanced(jsonObject);
                break;
        }
    }


    /* ==============================================================
     *                        Méthodes privées
     * ==============================================================
     */

    /**
     * Affiche le contenu brut de la requête JSON dans PlainJSONFragment.
     * @param jsonObject JSON issu du feed Flickr.
     */
    private void showPlainJSON(JSONObject jsonObject)
    {
        // Création du bundle du fragment
        Bundle args = new Bundle();
        args.putString("json", jsonObject.toString());

        // Création du fragment
        PlainJSONFragment fragment = new PlainJSONFragment();
        fragment.setArguments(args);

        // Modification du fragment dans MainActivity
        MainActivity activity = (MainActivity)this.activityWkRef.get();
        activity.setMainFragment(fragment);

        Log.i("TLT", "[OK] AsyncFlickrTask.showPlainJSON");
    }

    /**
     * Affiche la liste des titres des images dans TitlesFragment.
     * @param jsonObject JSON issu du feed Flickr.
     */
    private void showTitles(JSONObject jsonObject)
    {
        // Création de la liste de titres
        ArrayList<String> titles = new ArrayList<String>();
        try {
            JSONArray items = jsonObject.getJSONArray("items");
            for(int i=0; i<items.length(); i++)
            {
                titles.add(items.getJSONObject(i).getString("title"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Création du bundle du fragment
        Bundle args = new Bundle();
        args.putStringArrayList("titles", titles);

        // Création du fragment
        TitleFragment fragment = new TitleFragment();
        fragment.setArguments(args);

        // Modification du fragment dans MainActivity
        MainActivity activity = (MainActivity)this.activityWkRef.get();
        activity.setMainFragment(fragment);
    }

    /**
     * Affiche l'ensemble des images dans GridViewFragment et son adapter.
     * @param jsonObject JSON issu du feed Flickr.
     */
    private void showImages(JSONObject jsonObject)
    {
        // Récupération de la liste des URL des images
        ArrayList<String> urls = new ArrayList<>();
        try {
            JSONArray items = jsonObject.getJSONArray("items");
            for(int i=0; i<items.length(); i++)
            {
                urls.add(items.getJSONObject(i)
                              .getJSONObject("media")
                              .getString("m")

                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Création du bundle du fragment
        Bundle args = new Bundle();
        args.putStringArrayList("urls", urls);

        // Création du fragment
        GridViewFragment fragment = new GridViewFragment();
        fragment.setArguments(args);

        // Modification du fragment dans MainActivity
        MainActivity activity = (MainActivity)this.activityWkRef.get();
        activity.setMainFragment(fragment);
    }

    /**
     * Affiche une vue personnalisée dans RecyclerViewFragment et son adapter.
     * @param jsonObject JSON issu du feed Flickr.
     */
    private void showAdvanced(JSONObject jsonObject)
    {
        // Récupération de l'url de l'image, l'auteur et le titre
        ArrayList<String> authors = new ArrayList<>();
        ArrayList<String> titles  = new ArrayList<>();
        ArrayList<String> urls    = new ArrayList<>();
        try {
            JSONArray items = jsonObject.getJSONArray("items");
            for(int i=0; i<items.length(); i++)
            {
                String author = items.getJSONObject(i)
                                     .getString("author");
                authors.add(author.substring(author.indexOf("(\"")+2, author.indexOf("\")")));

                titles.add(items.getJSONObject(i)
                                .getString("title")
                );

                urls.add(items.getJSONObject(i)
                              .getJSONObject("media")
                              .getString("m")

                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Création du bundle du fragment
        Bundle args = new Bundle();
        args.putStringArrayList("authors", authors);
        args.putStringArrayList("titles", titles);
        args.putStringArrayList("urls", urls);

        // Création du fragment
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setArguments(args);

        // Modification du fragment dans MainActivity
        MainActivity activity = (MainActivity)this.activityWkRef.get();
        activity.setMainFragment(fragment);
    }
}
