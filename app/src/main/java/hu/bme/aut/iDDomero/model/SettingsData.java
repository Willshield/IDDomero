package hu.bme.aut.iDDomero.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class SettingsData {

    private float sensitivity;
    private boolean startStopWithButton;
    private String activePlayer;
    private static final String STORAGE_KEY = "SettingsData";
    private SharedPreferences sharedPreferences;
    public static final int AUTOSAVE = 0;
    public static final int ASK = 1;
    public static final int DONT_SAVE = 2;
    private static final String sensitivityKey= "sensitivity";
    private static final String startStopWithButtonKey= "startStop";
    private static final String manageModeKey= "mode";
    private static final String activePlayerKey= "activePlayer";
    private int manageMode;


    //Singleton
    private static SettingsData instance = null;
    public static SettingsData getInstance(Context context){
        if (instance != null){
            return instance;
        } else {
            instance = new SettingsData(context);
            return instance;
        }
    }

    private SettingsData(){};

    private SettingsData(Context context){
        sharedPreferences = context.getSharedPreferences(STORAGE_KEY ,Context.MODE_PRIVATE);

        sensitivity = sharedPreferences.getFloat(sensitivityKey, 3.0f);
        startStopWithButton = sharedPreferences.getBoolean(startStopWithButtonKey,false);
        manageMode = sharedPreferences.getInt(manageModeKey, AUTOSAVE);
        activePlayer = sharedPreferences.getString(activePlayerKey, "Anonymous");
    }

    public float getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(float sensitivity) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        this.sensitivity = sensitivity;
        editor.putFloat(sensitivityKey, sensitivity);
        editor.commit();
    }

    public boolean isStartStopWithButton() {
        return startStopWithButton;
    }

    public void setStartStopWithButton(boolean startStopWithButton) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        this.startStopWithButton = startStopWithButton;
        editor.putBoolean(startStopWithButtonKey, startStopWithButton);
        editor.commit();
    }

    public int getManageMode() {
        return manageMode;
    }

    public void setManageMode(int manageMode) {
        if(manageMode == AUTOSAVE || manageMode == ASK || manageMode == DONT_SAVE){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            this.manageMode = manageMode;
            editor.putInt(manageModeKey, manageMode);
            editor.commit();
        } else {
            throw new IllegalArgumentException("Illegal mode set");
        }
    }

    public String getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(String activePlayer) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        this.activePlayer = activePlayer;
        editor.putString(activePlayerKey, activePlayer);
        editor.commit();
    }

}
