package insa.luu.td1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
        this.getJsonFromAsync(login);

        // Sauvegarde login
        this.saveLogin(login);
    }

    protected void getJsonFromAsync(String login)
    {
        Log.i("DebugTD1", "getJsonFromAsync");
        new AsyncJsonTask(this).execute("http://10.0.2.2:1337/courses", login);
    }

    protected void saveLogin(String login)
    {
        Log.i("DebugTD1", "saveLogin");
        SharedPreferences sharedPreferences = this.getPreferences(this.MODE_PRIVATE);
        SharedPreferences.Editor editor     = sharedPreferences.edit();
        editor.putString("login", login);
        editor.apply();
    }
}
