package insa.luu.td1;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Acheter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acheter);

        // Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(this.getClass().getSimpleName());
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.android_icon);
        actionBar.setDisplayUseLogoEnabled(true);

        // Récupération l'extra dans l'intent d'origine
        Intent intent = getIntent();
        String extra  = intent.getStringExtra("achat");

        // Set du texte
        TextView textView = (TextView) findViewById(R.id.textViewAchatVar);
        textView.setText(extra);
    }
}
