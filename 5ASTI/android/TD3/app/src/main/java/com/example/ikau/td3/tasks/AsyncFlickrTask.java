package com.example.ikau.td3.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ikau.td3.enums.ActionsEnum;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AsyncFlickrTask extends AsyncTask<Object, Integer, JSONObject> {

    private ActionsEnum action;
    private WeakReference<Activity> activityWkRef;

    public AsyncFlickrTask(Activity activity)
    {
        this.activityWkRef = new WeakReference<Activity>(activity);
        this.action        = ActionsEnum.NONE;

    }

    @Override
    protected void onPreExecute() {
        this.showProgressSpinner();
    }

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

    private JSONObject getJsonFromFlickr(URLConnection openedConnection) throws IOException, JSONException
    {
        // Récupération et lecture du contenu
        BufferedReader reader = new BufferedReader(new InputStreamReader(openedConnection.getInputStream()));
        StringBuffer buffer = new StringBuffer();
        String line = "";

        while ((line = reader.readLine()) != null) {
            buffer.append(line+"\n");
        }

        // Création du json : on doit enlever jsonFlickrFeed() du résultat
        String jsonString = buffer.toString().substring(15, buffer.length()-1);

        return new JSONObject(jsonString);
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if(jsonObject == null) return;

        switch(this.action)
        {
            case PLAIN_JSON:
                this.showPlainJSON(jsonObject);
                break;

            case IMAGES:
                break;

            case ADVANCED:
                break;
        }
    }

    private void showPlainJSON(JSONObject jsonObject)
    {
        // TODO
    }

    private void showTitles(JSONObject jsonObject)
    {
        // TODO
    }

    private void showAdvanced(JSONObject jsonObject)
    {
        // TODO
    }
}
