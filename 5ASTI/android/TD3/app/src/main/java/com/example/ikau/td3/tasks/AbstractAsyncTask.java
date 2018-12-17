package com.example.ikau.td3.tasks;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
}
