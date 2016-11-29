package hu.bme.aut.iDDomero.userInterface;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import hu.bme.aut.iDDomero.R;
import hu.bme.aut.iDDomero.model.SettingsData;


public class MainMenuActivity extends AppCompatActivity {

    private TextView welcome;
    private TextView title;
    private Button drink;
    private Button selectProfile;
    private Button highscores;
    private Button settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    @Override
    protected void onResume(){
        super.onResume();
        initWidgets();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainMenuActivity.this, SensorStopWatchActivity.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainMenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        selectProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainMenuActivity.this, ProfileSelectorActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initWidgets() {
        title = (TextView) findViewById(R.id.titleText);
        welcome = (TextView) findViewById(R.id.welcome);
        drink = (Button) findViewById(R.id.drink_button);
        selectProfile = (Button) findViewById(R.id.select_profile_button);
        highscores = (Button) findViewById(R.id.highscores_button);
        settings = (Button) findViewById(R.id.settings_button);

        Resources res = getResources();
        welcome.setText(res.getString(R.string.welcome_message, SettingsData.getInstance(getApplicationContext()).getActivePlayer()));

        setTypefaces();
    }

    private void setTypefaces() {
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/BubbleMan.ttf");
        title.setTypeface(type);
        drink.setTypeface(type);
        selectProfile.setTypeface(type);
        highscores.setTypeface(type);
        settings.setTypeface(type);
    }


}
