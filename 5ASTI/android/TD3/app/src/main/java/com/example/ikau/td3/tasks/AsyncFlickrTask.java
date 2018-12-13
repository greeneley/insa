package com.example.ikau.td3.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.example.ikau.td3.R;
import com.example.ikau.td3.activities.MainActivity;
import com.example.ikau.td3.enums.ActionsEnum;
import com.example.ikau.td3.fragments.PlaceholderFragment;
import com.example.ikau.td3.fragments.PlainJSONFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

public class AsyncFlickrTask extends AsyncTask<Object, Integer, JSONObject> {

    private ActionsEnum action;
    private WeakReference<Activity> activityWkRef;

    public AsyncFlickrTask(Activity activity)
    {
        this.activityWkRef = new WeakReference<Activity>(activity);
        this.action        = ActionsEnum.NONE;
    }

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
        StringBuffer   buffer = new StringBuffer();
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

    private View getViewContainer()
    {
        return null;
    }

    private void showPlainJSON(JSONObject jsonObject)
    {
        // Vérification de l'état de l'activité
        MainActivity activity = (MainActivity) this.activityWkRef.get();
        if(activity == null)
        {
            Log.e("TLT", "[ERR] AsyncFlickrTask.showPlainJSON: MainActivity is null");
            return;
        }

        // Récupération du container pour le fragment
        LinearLayout container = (LinearLayout) activity.findViewById(R.id.layoutFragment);

        // Création du bundle du fragment
        Bundle args = new Bundle();
        args.putString("json", jsonObject.toString());

        // Création du fragment
        PlainJSONFragment fragment = new PlainJSONFragment();
        fragment.setArguments(args);

        // Modification du fragment dans MainActivity
        activity.setMainFragment(fragment);

        Log.i("TLT", "[OK] AsyncFlickrTask.showPlainJSON");
    }

    private void showTitles(JSONObject jsonObject)
    {
        // TODO
        //List<String> courses = Arrays.asList(this.activityWkRef.get().getResources().getStringArray(R.array.hardcoded_course));
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.activityWkRef.get(), android.R.layout.simple_list_item_1, courses);
        //listView.setAdapter(adapter);
    }

    private void showAdvanced(JSONObject jsonObject)
    {
        // TODO
    }
}
