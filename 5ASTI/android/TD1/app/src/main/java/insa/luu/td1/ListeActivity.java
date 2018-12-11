package insa.luu.td1;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ListeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);

        // Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(this.getClass().getSimpleName());
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.android_icon);
        actionBar.setDisplayUseLogoEnabled(true);

        // List View configuration
        ListView listView = (ListView) findViewById(R.id.listViewActivityListe);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupération du contenu
                String contenu = parent.getItemAtPosition(position).toString();

                // Création de l'intent
                Intent intent = new Intent(ListeActivity.this, Acheter.class);
                intent.putExtra("achat", contenu);
                startActivity(intent);
            }
        });

        // Récupération de l'intent à l'origine de l'activity
        Intent intent = getIntent();
        String login  = intent.getStringExtra("LOGIN");

        // Affichage du login
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(login);

        // Test Json
        //this.getJsonFromVolley();
        this.getJsonFromAsync(login);
    }

    protected void getJsonFromVolley()
    {
        //String url = "http://IPMACHINE:1337/courses.json";
        String url = "http://10.0.2.2:1337/courses";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("DebugTD1", "onResponse");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.i("DebugTD1", "onErrorResponse");
                    }
                });

        queue.add(jsonObjectRequest);
    }

    protected void getJsonFromAsync(String input)
    {
        new AsyncJsonTask(this).execute("http://10.0.2.2:1337/courses", input);
    }
}
