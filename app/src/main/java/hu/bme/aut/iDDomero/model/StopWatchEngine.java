package hu.bme.aut.iDDomero.model;

import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

public class StopWatchEngine
{
    private long starttime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedtime = 0L;
    private int secs = 0;
    private int mins = 0;
    private int milliseconds = 0;
    private boolean running;
    Handler handler = new Handler();
    TextView out;

    public StopWatchEngine(TextView output){
        setOutputView(output);
    }

    private Runnable updateTimer = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - starttime;
            updatedtime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedtime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            milliseconds = (int) (updatedtime % 1000);
            out.setText(getTimeText());
            handler.postDelayed(this, 0);
        }};


    public void stopClock(){
        starttime = 0L;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedtime = 0L;
        running = false;
        secs = 0;
        mins = 0;
        milliseconds = 0;
        handler.removeCallbacks(updateTimer);
        out.setTextColor(Color.RED);
    }

    public void startClock(){
        starttime = SystemClock.uptimeMillis();
        handler.postDelayed(updateTimer, 0);
        running = true;
        out.setTextColor(Color.BLUE);
    }

    private String getTimeText(){
        return "" + mins + ":" + String.format("%02d", secs) + ":" + String.format("%03d", milliseconds);
    }

    private void setOutputView(TextView textView){
        out = textView;
    }
}
