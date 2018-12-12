package insa.luu.td1;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Icon dans l'action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.android_icon);
        actionBar.setDisplayUseLogoEnabled(true);

        // Timepicker en format 24 h
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
    }

    /**
     * Callback pour le bouton "Button" situé à gauche de la TextView au milieu de l'écran.
     * @param v
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onClickButtonLinearLayout(View v)
    {
        Log.i("DebugTD1", "MainActivity.onClickButtonLinearLayout");

        // Récupération du texte de l'input
        TextView input   = (TextView) findViewById(R.id.plainTextUserlogin);
        String inputText = input.getText().toString();

        // Construction du string
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        String newText = String.format("%s doit faire les courses à %d:%d",
                inputText, timePicker.getHour(), timePicker.getMinute());

        // Copie dans le textview du linear layout
        TextView textView = (TextView) findViewById(R.id.textViewLinearLayout);
        textView.setText(newText);
    }

    /**
     * Callback du bouton "Quitter" tout en bas de l'écran.
     * @param v
     */
    protected void onClickQuitter(View v)
    {
        Log.i("DebugTD1", "MainActivity.onClickQuitter");

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Callback du bouton "Faire les courses" redirigeant vers ListeActivity.
     * @param v
     */
    protected void onClickCourses(View v)
    {
        Log.i("DebugTD1", "MainActivity.onClickCourses");

        // Récupération du login
        TextView input = (TextView) findViewById(R.id.plainTextUserlogin);
        String login   = input.getText().toString();

        // Création de l'intent + passage du login
        Intent intent = new Intent(this, ListeActivity.class);
        intent.putExtra("LOGIN", login);
        startActivity(intent);
    }
}
