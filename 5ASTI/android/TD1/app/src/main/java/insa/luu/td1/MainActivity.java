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
        Log.i("DebugTD1", "Action bar");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.android_icon);
        actionBar.setDisplayUseLogoEnabled(true);

        // Timepicker en format 24 h
        Log.i("DebugTD1", "TimePicker");
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onClickButtonLinearLayout(View v)
    {
        Log.i("DebugTD1", "onClickButtonLinearLayout");

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

    protected void onClickQuitter(View v)
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected void onClickCourses(View v)
    {
        // Récupération du login
        TextView input = (TextView) findViewById(R.id.plainTextUserlogin);
        String login   = input.getText().toString();

        // Création de l'intent + passage du login
        Intent intent = new Intent(this, ListeActivity.class);
        intent.putExtra("LOGIN", login);
        startActivity(intent);
    }
}
