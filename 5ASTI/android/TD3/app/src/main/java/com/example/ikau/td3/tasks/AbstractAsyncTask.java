package com.example.ikau.td3.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

abstract class AbstractAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
{
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

        return new JSONObject(jsonString);
    }

    /**
     * Crédits https://stackoverflow.com/questions/8992964/android-load-from-url-to-bitmap
     * @param urlImage
     * @param index
     * @return
     * @throws IOException
     * @throws JSONException
     */
    Bitmap getBitmapFromUrlString(String urlImage, int index) throws IOException, JSONException
    {
        // Ouverture de la connexion
        URL url                       = new URL(urlImage);
        URLConnection connectionImage = url.openConnection();
        connectionImage.setDoInput(true);
        connectionImage.connect();

        // Récupération du bitmap
        InputStream input = connectionImage.getInputStream();
        Bitmap myBitmap   = BitmapFactory.decodeStream(input);
        return myBitmap;
    }
}
