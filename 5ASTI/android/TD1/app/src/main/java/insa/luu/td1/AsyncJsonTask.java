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

public class AsyncJsonTask extends AsyncTask<String, Void, Void> {

    private WeakReference<Activity> weakReferenceActivity;
    private JSONArray array;

    public AsyncJsonTask(Activity activity)
    {
        this.weakReferenceActivity = new WeakReference<Activity>(activity);
        this.array = null;
    }

    @Override
    protected Void doInBackground(String... params)
    {
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
                    Log.i("DebugTD1", "HttpURLConnection.HTTP_OK");

                    // Récupération du contenu
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line+"\n");
                    }

                    // Création du json et traitement du résultat
                    JSONObject json = new JSONObject(buffer.toString());
                    this.array      = json.getJSONArray(params[1]);

                default:
                    return null;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(this.array != null)
        {
            Log.i("DebugTD1", "AsyncJsonTask.onPostExecute");

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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.weakReferenceActivity.get(), android.R.layout.simple_list_item_1, courses);
            listView.setAdapter(adapter);
        }
        else
        {
            // Vérification de la Liste Activity
            ListView listView    = (ListView) this.weakReferenceActivity.get().findViewById(R.id.listViewActivityListe);
            if(listView == null) return;

            // Courses par defaut
            List<String>         courses = Arrays.asList(this.weakReferenceActivity.get().getResources().getStringArray(R.array.hardcoded_course));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.weakReferenceActivity.get(), android.R.layout.simple_list_item_1, courses);
        }
    }
}
