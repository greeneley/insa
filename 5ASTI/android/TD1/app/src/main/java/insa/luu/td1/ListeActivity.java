package insa.luu.td1;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

        // Adaptater listview
        List<String>         courses = Arrays.asList(getResources().getStringArray(R.array.hardcoded_course));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, courses);

        // List View configuration
        ListView listView = (ListView) findViewById(R.id.listViewActivityListe);
        listView.setAdapter(adapter);
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
    }
}
