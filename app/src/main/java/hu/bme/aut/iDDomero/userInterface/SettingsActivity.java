package hu.bme.aut.iDDomero.userInterface;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.iDDomero.R;
import hu.bme.aut.iDDomero.model.SettingsData;

public class SettingsActivity extends AppCompatActivity {

    private TextView title;
    private SeekBar sensitivityBar;
    private TextView sensitivityValueText;
    private TextView activeProfileText;
    private ImageView editActiveProfile;
    private Switch startStopWithButton;
    private Spinner manageModeSpinner;
    private Button setDefaults;

    private float sensitivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initWidgets();
        setOnClickListeners();
        loadData();
    }

    private void loadData() {
        loadSensitivity();
        loadActiveProfile();
        loadStartStop();
        loadMode();
    }

    private void loadMode() {
        int mode = SettingsData.getInstance(getApplicationContext()).getManageMode();
        manageModeSpinner.setSelection(mode);
    }

    private void loadStartStop() {
        boolean startstop = SettingsData.getInstance(getApplicationContext()).isStartStopWithButton();
        startStopWithButton.setChecked(startstop);
    }

    private void loadActiveProfile() {
        String name = SettingsData.getInstance(getApplicationContext()).getActivePlayer();
        activeProfileText.setText(name);
    }

    private void loadSensitivity() {
        sensitivity = SettingsData.getInstance(getApplicationContext()).getSensitivity();
        sensitivityBar.setProgress((int)(sensitivity * 2.0f));
        sensitivityValueText.setText("" + sensitivity);
    }

    private void setOnClickListeners() {
        setDefaults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: set defaults
                setDefaultSensitivity();
                setDefaultStartStop();
                setDefaultMode();

            }
        });

        sensitivityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                sensitivity = ((float) progress) / 2.0f;
                String text = "" + sensitivity;
                sensitivityValueText.setText(text);
                SettingsData.getInstance(getApplicationContext()).setSensitivity(sensitivity);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        editActiveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingsActivity.this, ProfileSelectorActivity.class);
                startActivity(intent);
                finish();
            }
        });

        startStopWithButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {
                if (on) {
                    SettingsData.getInstance(getApplicationContext()).setStartStopWithButton(true);
                } else {
                    SettingsData.getInstance(getApplicationContext()).setStartStopWithButton(false);
                }
            }
        });


        manageModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (manageModeSpinner.getSelectedItemPosition()){
                    case SettingsData.AUTOSAVE:
                        SettingsData.getInstance(getApplicationContext()).setManageMode(SettingsData.AUTOSAVE);
                        break;
                    case SettingsData.ASK:
                        SettingsData.getInstance(getApplicationContext()).setManageMode(SettingsData.ASK);
                        break;
                    case SettingsData.DONT_SAVE:
                        SettingsData.getInstance(getApplicationContext()).setManageMode(SettingsData.DONT_SAVE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    private void setDefaultMode() {
        manageModeSpinner.setSelection(SettingsData.AUTOSAVE);
        SettingsData.getInstance(getApplicationContext()).setManageMode(SettingsData.AUTOSAVE);
    }

    private void setDefaultStartStop() {
        startStopWithButton.setChecked(false);
        SettingsData.getInstance(getApplicationContext()).setStartStopWithButton(false);
    }

    private void setDefaultSensitivity() {
        sensitivity = 3.0f;
        sensitivityBar.setProgress(6);
        sensitivityValueText.setText("3.0");
        SettingsData.getInstance(getApplicationContext()).setSensitivity(sensitivity);
    }

    private void initWidgets() {
        title = (TextView) findViewById(R.id.titleText);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/BubbleMan.ttf");
        title.setTypeface(type);

        sensitivityBar = (SeekBar) findViewById(R.id.sensitivityBar);
        sensitivityValueText = (TextView) findViewById(R.id.sensitivityBarValue);
        activeProfileText = (TextView) findViewById(R.id.activeProfileText);
        editActiveProfile = (ImageView) findViewById(R.id.selectProfile);
        startStopWithButton = (Switch) findViewById(R.id.startStopWithButtonSwitch);
        manageModeSpinner = (Spinner) findViewById(R.id.modeSpinner);
        addModeSelectSpinner();
        setDefaults = (Button) findViewById(R.id.setDefaults);
    }

    private void addModeSelectSpinner() {
        List<String> modeList = new ArrayList<>();

        modeList.add(getString(R.string.autosave));
        modeList.add(getString(R.string.always_ask));
        modeList.add(getString(R.string.dont_save));

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modeList);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        manageModeSpinner.setAdapter(modeAdapter);
    }


}
