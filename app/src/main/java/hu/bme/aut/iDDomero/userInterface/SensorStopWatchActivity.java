package hu.bme.aut.iDDomero.userInterface;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;

import hu.bme.aut.iDDomero.R;
import hu.bme.aut.iDDomero.model.SeismicDataPoint;
import hu.bme.aut.iDDomero.model.SettingsData;
import hu.bme.aut.iDDomero.model.StopWatchEngine;
import hu.bme.aut.iDDomero.model.TimesData;


public class SensorStopWatchActivity extends AppCompatActivity implements SensorEventListener {

    private static final int X_AXIS_INDEX = 0;
    private static final int Y_AXIS_INDEX = 1;
    private static final int Z_AXIS_INDEX = 2;

    private Button startBtn;
    private Button pref;
    private TextView textMaxMin;
    private TextView time;
    private TextView title;
    private TextView drinker;
    private TextView bestTime;

    //http://stackoverflow.com/questions/3733867/stop-watch-logic
    private StopWatchEngine stopWatch;

    private static int defaultCoolDown;
    private static int bufferSize;
    private float accuracyFactor = 3;
    private int coolDown;
    private MovementBarBuilder movementBarBuilder;
    private static final int BARS_COUNT = 34;

    private float max;
    private float min;
    boolean measureStopped = true;
    boolean clockRunnung = false;
    boolean firstAfterBigAccel = false;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Queue<SeismicDataPoint> seismicActivityBuffer;

    private String activeProfile;
    private CharSequence bestTimeOfActivePlayer;
    private int highScoreSaveMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_stopwatch);
        setDefaultValues();
        initWidgets();
        initSensor();
        setOnClickListeners();
        setActualSettings();
    }

    private void setActualSettings() {
        activeProfile = SettingsData.getInstance(getApplicationContext()).getActivePlayer();
        accuracyFactor = SettingsData.getInstance(getApplicationContext()).getSensitivity();
        drinker.setText(activeProfile + getString(R.string.drinks));
        bestTime.setText(getBestTimeOfActivePlayer());
        highScoreSaveMode = SettingsData.getInstance(getApplicationContext()).getManageMode();
        //todo load best zime
    }

    private void initSensor() {
        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.seismicActivityBuffer = new LinkedList<>();
    }

    private void setOnClickListeners() {
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!measureStopped) {
                    stopMeasure();
                }else{
                    startMeasure();
                }
            }
        });

    }

    private void initWidgets() {
        title = (TextView) findViewById(R.id.titleText);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/BubbleMan.ttf");
        title.setTypeface(type);
        startBtn = (Button) findViewById(R.id.startbutton);
        startBtn.setTypeface(type);
        drinker = (TextView) findViewById(R.id.activeDrinkerText);
        bestTime = (TextView) findViewById(R.id.bestTimeinfo);
        textMaxMin = (TextView) findViewById(R.id.textmaxmin);
        time = (TextView) findViewById(R.id.timer);
        stopWatch = new StopWatchEngine(time);
        movementBarBuilder =new MovementBarBuilder(BARS_COUNT);
    }

    private void setDefaultValues() {
        Resources resources = getResources();
        defaultCoolDown = Integer.parseInt(resources.getString(R.string.default_cool_down));
        bufferSize = Integer.parseInt(resources.getString(R.string.buffer_size));
        coolDown = defaultCoolDown;
        max = 0;
        min = Float.MAX_VALUE;
    }

    protected void stopMeasure(){
        measureStopped = true;
        startBtn.setText(R.string.action_start);
        stopWatch.stopClock();
        clockRunnung = false;

    }
    protected void startMeasure(){
        measureStopped = false;
        startBtn.setText(R.string.action_stop);
        min = Float.MAX_VALUE;
        max = 0;
        seismicActivityBuffer = new LinkedList<SeismicDataPoint>();
        time.setText(R.string.start_time);
    }



    @Override
    protected void onResume() {
        super.onResume();
        this.sensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (this.measureStopped || coolDown-- > 0) {
            return;
        }
        coolDown = defaultCoolDown;

        if (firstAfterBigAccel){
            firstAfterBigAccel = false;
            return;
        }

        fetchNextSeismicData(event);
        float movement = getMovement();
        showMovement(movement);
        checkStopWatchAction(movement);

    }

    private void showMovement(float movement) {
        int movementRate = (int) (movement / accuracyFactor * BARS_COUNT);
        textMaxMin.setText(movementBarBuilder.getBars(movementRate));
        if(movementRate < BARS_COUNT / 3){
            textMaxMin.setTextColor(Color.GREEN);
        } else if (movementRate < BARS_COUNT / 3 * 2) {
            textMaxMin.setTextColor(Color.YELLOW);
        } else {
            textMaxMin.setTextColor(Color.RED);
        }
    }

    private void checkStopWatchAction(float movement) {
        if (movement > accuracyFactor){
            if (clockRunnung){
                stopTheStopWatch();
                handleHighScore();
            } else {
                startTheStopWatch();
            }
        }
    }

    private void handleHighScore() {
        if (highScoreSaveMode == SettingsData.DONT_SAVE){
            return;
        } else if( highScoreSaveMode == SettingsData.AUTOSAVE){
            saveHighScore();
        } else {
            askIfSaveHighScore();
        }
        bestTime.setText(getBestTimeOfActivePlayer());
    }

    private void saveHighScore() {
        TimesData newHighScore = new TimesData(activeProfile, time.getText().toString());
        newHighScore.save();
    }

    private void askIfSaveHighScore() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        saveHighScore();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(SensorStopWatchActivity.this);
        builder.setMessage(R.string.save_highscore_question).setPositiveButton(R.string.yes, dialogClickListener).setNegativeButton(R.string.no, dialogClickListener).show();
    }

    private void stopTheStopWatch() {
        stopMeasure();
    }

    private void startTheStopWatch() {
        stopWatch.startClock();
        startMeasure();
        clockRunnung = true;
        firstAfterBigAccel = true;
    }

    private float getMovement() {
        min = Float.MAX_VALUE;
        max = 0;
        for(SeismicDataPoint pointIter : seismicActivityBuffer){
            if (min > pointIter.getAllAccel())
                min = pointIter.getAllAccel();

            if (max < pointIter.getAllAccel())
                max = pointIter.getAllAccel();
        }
        return max - min;
    }

    private void fetchNextSeismicData(SensorEvent event) {
        SeismicDataPoint point = new SeismicDataPoint(event.values[X_AXIS_INDEX], event.values[Y_AXIS_INDEX], event.values[Z_AXIS_INDEX]);
        this.seismicActivityBuffer.add(point);
        if (seismicActivityBuffer.size() >= bufferSize){
            seismicActivityBuffer.remove();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public String getBestTimeOfActivePlayer() {
        //todo
        return "";
    }
}
