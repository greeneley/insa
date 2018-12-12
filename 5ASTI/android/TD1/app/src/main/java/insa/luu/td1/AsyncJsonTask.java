package insa.luu.td1;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tâche asynchrone effectuant la requête GET pour afficher les courses de la
 * ListView dans ListeActivity via une WeakReference.
 */
public class AsyncJsonTask extends AsyncTask<String, Void, Void> {

    /**
     * Reference vers ListeActivity où l'on a cliqué sur l'item.
     */
    private WeakReference<Activity> weakReferenceActivity;

    /**
     * Résultat de doInBackground.
     */
    private JSONArray array;

    /**
     * Constructeur recevant une référence de l'activité source.
     * @param activity
     */
    public AsyncJsonTask(Activity activity)
    {
        this.weakReferenceActivity = new WeakReference<Activity>(activity);
        this.array = null;
    }

    /**
     * Effectue la requête GET pour obtenir le JSON puis récupère l'array correspondant au login.
     * @param params param[0] == url (String), param[1] == login (String).
     * @return JSONArray correspondant au login.
     */
    @Override
    protected Void doInBackground(String... params)
    {
        // Vérification des params
        if(params.length < 2) return null;

        try {
            // Récupération et GET de la première URL
            URL url = new URL(params[0]);
            URLConnection connection = url.openConnection();
            connection.connect();

            // Vérification de l'état
            int responseCode = ((HttpURLConnection) connection).getResponseCode();
            switch (responseCode)
            {
                case HttpURLConnection.HTTP_OK:
                    Log.i("DebugTD1", "AsyncJsonTask.doInBackground.HttpURLConnection.HTTP_OK");

                    // Récupération et lecture du contenu
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line+"\n");
                    }

                    // Création du json et traitement du résultat
                    JSONObject json = new JSONObject(buffer.toString());
                    String login = params[1];
                    if(json.has(login))
                    {
                        this.array = json.getJSONArray(login);
                    }
                    break;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        Log.i("DebugTD1", "AsyncJsonTask.doInBackground.Error");
        return null;
    }

    /**
     * Modification de la vue après traitement de la tâche.
     * @param aVoid
     */
    @Override
    protected void onPostExecute(Void aVoid)
    {
        if(this.array != null) // GET OK
        {
            Log.i("DebugTD1", "AsyncJsonTask.onPostExecute:OK");

            // Vérification de la Liste Activity
            ListView listView    = (ListView) this.weakReferenceActivity.get().findViewById(R.id.listViewActivityListe);
            if(listView == null) return;

            // Création de l'array depuis la JsonArray
            List<String> courses = new ArrayList<>();
            for(int i=0; i<this.array.length(); i++)
            {
                try {
                    courses.add(this.array.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Création de l'adaptater pour la ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.weakReferenceActivity.get(), android.R.layout.simple_list_item_1, courses);
            listView.setAdapter(adapter);
        }
        else // GET erreur ou login introuvable
        {
            Log.i("DebugTD1", "AsyncJsonTask.onPostExecute:ERROR");

            // Vérification de la Liste Activity
            ListView listView = (ListView) this.weakReferenceActivity.get().findViewById(R.id.listViewActivityListe);
            if(listView == null) return;

            // Adaptater par défaut
            List<String>         courses = Arrays.asList(this.weakReferenceActivity.get().getResources().getStringArray(R.array.hardcoded_course));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.weakReferenceActivity.get(), android.R.layout.simple_list_item_1, courses);
            listView.setAdapter(adapter);
        }
    }
}
