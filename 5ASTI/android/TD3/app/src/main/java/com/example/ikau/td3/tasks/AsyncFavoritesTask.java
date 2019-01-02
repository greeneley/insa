package com.example.ikau.td3.tasks;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.example.ikau.td3.activities.MainActivity;
import com.example.ikau.td3.database.MyDatabase;
import com.example.ikau.td3.fragments.RecyclerViewFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Tâche asynchrone permettant de télécharger les images résidant dans la BDD.
 */
public class AsyncFavoritesTask extends AbstractAsyncTask<Void, Integer, Boolean>
{

    /* ==============================================================
     *                          Propriétés
     * ==============================================================
     */
    /**
     * Référence faible vers MainActivity.
     */
    private WeakReference<Activity> activityWeakReference;

    /**
     * Une clé pour utiliser l'API de Flickr.
     */
    private String apiKey;

    // Infos sur les objets présents dans la BDD.
    private ArrayList<String> urls;
    private ArrayList<String> authors;
    private ArrayList<String> titles;


    /* ==============================================================
     *                          Constructeur
     * ==============================================================
     */
    public AsyncFavoritesTask(Activity activity, String apiKey)
    {
        this.activityWeakReference = new WeakReference<Activity>(activity);
        this.apiKey                = apiKey;
    }

    /* ==============================================================
     *                          Overrides
     * ==============================================================
     */
    @Override
    protected void onPreExecute()
    {
        // On affiche la barre de chargement avant le traitement
        MainActivity activity = (MainActivity) this.activityWeakReference.get();
        if(activity != null)
        {
            activity.showProgressBar();
        }
    }

    /**
     * Récupère les données de la BDD et reconstruit les informations utiles.
     * @param voids
     * @return true si aucun problème, false sinon.
     */
    @Override
    protected Boolean doInBackground(Void... voids)
    {
        if(this.activityWeakReference.get() == null)
        {
            Log.i("INSA", "AsyncFavoritesTask.doInBackground: ref is null");
            return false;
        }

        // Récupération de l'instance de la BDD
        MyDatabase instance = new MyDatabase(this.activityWeakReference.get());
        SQLiteDatabase db   = instance.getWritableDatabase();

        // Récupération du cursor
        Cursor cursor = db.query(
                MyDatabase.TABLE_NAME, // Le nom de la table
                null, // On fait un SELECT *
                null, null, null, null, null
        );

        // Construction des arrays de données
        this.urls    = new ArrayList<>();
        this.authors = new ArrayList<>();
        this.titles  = new ArrayList<>();
        while(cursor.moveToNext())
        {
            String photo_id = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabase.COLUMN_PHOTO_ID));
            String secret   = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabase.COLUMN_SECRET));
            this.addInfosFromAPI(photo_id, secret);
        }
        cursor.close();
        db.close();

        return true;
    }

    /**
     * Si aucun problème : construit le fragment et l'affiche dans MainActivity.
     * @param aBoolean État du doInBackground.
     */
    @Override
    protected void onPostExecute(Boolean aBoolean)
    {
        if(!aBoolean)
        {
            Log.i("INSA", "AsyncFavoritesTask.onPostExecute: doInBackground has failed");
            return;
        }
        if(this.activityWeakReference.get() == null)
        {
            Log.i("INSA", "AsyncFavoritesTask.onPostExecute: ref is null");
            return;
        }

        // Création du bundle du fragment
        Bundle args = new Bundle();
        args.putStringArrayList("authors", this.authors);
        args.putStringArrayList("titles", this.titles);
        args.putStringArrayList("urls", this.urls);

        // Création du fragment
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setArguments(args);

        // Modification du fragment dans MainActivity
        MainActivity activity = (MainActivity)this.activityWeakReference.get();
        activity.setMainFragment(fragment);
    }

    /* ==============================================================
     *                       Méthodes privées
     * ==============================================================
     */

    /**
     * Fait une requête HTTPS pour récupérer les données de chaque item favori dans la BDD.
     * @param photo_id L'id de la photo.
     * @param secret Le secret associé à la photo.
     */
    private void addInfosFromAPI(String photo_id, String secret)
    {
        // Construction du lien de l'API
        String linkAPI = String.format(
                "https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&api_key=%s&photo_id=%s&secret=%s&format=json&nojsoncallback=1",
                this.apiKey, photo_id, secret);

        // Requête du JSON
        try {
            // Récupération de l'url
            URL url = new URL(linkAPI);

            // Ouverture de la connection;
            URLConnection connection = url.openConnection();
            connection.connect();

            // Récupération du JSON
            JSONObject json = this.getJsonFromURL(connection).getJSONObject("photo");

            // Récupération des informations
            this.urls.add(this.getLinkPhoto(json, photo_id, secret));
            this.titles.add(json.getJSONObject("title").getString("_content"));
            this.authors.add(json.getJSONObject("owner").getString("username"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("INSA", e.getMessage());
            Log.e("INSA", "La clé de l'API est-elle correcte ?");
        }
    }

    /**
     * Récupère le lien direct vers la photo qui servira pour télécharger l'image.
     * https://www.flickr.com/services/api/misc.urls.html
     *
     * @param json Le json contenant les infos de la photo depuis l'API Flickr.
     * @param photo_id L'id de la photo.
     * @param secret Le secret associé à la photo.
     * @return Le lien direct vers la photo.
     * @throws JSONException
     */
    private String getLinkPhoto(JSONObject json, String photo_id, String secret) throws JSONException
    {
        // Récupération : farm, server
        String farm   = json.getString("farm");
        String server = json.getString("server");

        // Création du lien
        return String.format(
                "https://farm%s.staticflickr.com/%s/%s_%s_m.jpg",
                farm, server, photo_id, secret
        );

    }
}
