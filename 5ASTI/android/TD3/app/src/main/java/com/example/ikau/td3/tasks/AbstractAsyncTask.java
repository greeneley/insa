package com.example.ikau.td3.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Classe abstraite personnalisée qui me permet de mettre en commun des fonctions utilisées par plusieurs
 * tâche aynshrone.
 *
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
abstract class AbstractAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
{
    /**
     * Récupère un JSON quelconque d'une connexion quelconque.
     * @param openedConnection La connexion préalablement ouverte.
     * @return Renvoie le JSON contenant dans la cible de la connexion.
     * @throws IOException
     * @throws JSONException
     */
    JSONObject getJsonFromURL(URLConnection openedConnection) throws IOException, JSONException
    {
        // Récupération et lecture du contenu
        BufferedReader reader = new BufferedReader(new InputStreamReader(openedConnection.getInputStream()));
        StringBuffer   buffer = new StringBuffer();
        String line = "";

        while ((line = reader.readLine()) != null) {
            buffer.append(line+"\n");
        }

        Log.d("INSA", this.getClass().getSimpleName()+".getJsonFromURL: [OK] JSON retrieved");
        return new JSONObject(buffer.toString());
    }

    /**
     * Récupère le JSON depuis l'adresse feed de Flickr en ayant traié la page cible.
     * @param openedConnection La connexion préalablement ouverte.
     * @return Renvoie le JSON déduite du feed Flickr.
     * @throws IOException
     * @throws JSONException
     */
    JSONObject getJsonFromFlickr(URLConnection openedConnection) throws IOException, JSONException
    {
        // Récupération et lecture du contenu
        BufferedReader reader = new BufferedReader(new InputStreamReader(openedConnection.getInputStream()));
        StringBuffer   buffer = new StringBuffer();
        String line = "";

        while ((line = reader.readLine()) != null) {
            buffer.append(line+"\n");
        }

        // Création du json : on doit enlever jsonFlickrFeed() du résultat
        String jsonString = buffer.toString().substring(15, buffer.length()-1);

        Log.d("INSA", this.getClass().getSimpleName()+".getJsonFromFlickr: [OK] JSON retrieved");
        return new JSONObject(jsonString);
    }

    /**
     * Crédits https://stackoverflow.com/questions/8992964/android-load-from-url-to-bitmap
     * Récupère l'image de l'url sous format Bitmap.
     *
     * @param urlImage L'url de l'image à récupérer.
     * @return Le bitmap de l'image du lien.
     * @throws IOException
     * @throws JSONException
     */
    Bitmap getBitmapFromUrlString(String urlImage) throws IOException, JSONException
    {
        // Ouverture de la connexion
        URL url                       = new URL(urlImage);
        URLConnection connectionImage = url.openConnection();
        connectionImage.setDoInput(true);
        connectionImage.connect();

        // Récupération du bitmap
        InputStream input = connectionImage.getInputStream();
        Bitmap myBitmap   = BitmapFactory.decodeStream(input);

        Log.d("INSA", this.getClass().getSimpleName()+".getBitmapFromUrlString: [OK] Bitmap retrieved");
        return myBitmap;
    }
}
