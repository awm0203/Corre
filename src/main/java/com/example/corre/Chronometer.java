package com.example.corre;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.Random;
public class Chronometer implements Runnable {


    public static final long MILLIS_TO_MINUTES = 60000;
    public static final long MILLS_TO_HOURS = 3600000;

    double m = 0.00;
    Context mContext;
    long mStartTime;
    boolean mIsRunning;
    double totTimeinHour;

    public Chronometer(Context context) {
        mContext = context;
    }

    public Chronometer(Context context, long startTime) {
        this(context);
        mStartTime = startTime;
    }

    public void start() {
        if(mStartTime == 0) { //if the start time was not set before! e.g. by second constructor
            mStartTime = System.currentTimeMillis();
        }
        mIsRunning = true;
    }

    public void stop() {
        mIsRunning = false;

    }

    public boolean isRunning() {
        return mIsRunning;
    }
    public long getStartTime() {
        return mStartTime;
    }



    @SuppressLint("DefaultLocale")
    @Override
    public void run() {
            while(mIsRunning) {
                long since = System.currentTimeMillis() - mStartTime;
                totTimeinHour = since;

                //Update Timer
                int seconds = (int) (since / 1000) % 60;
                int minutes = (int) ((since / (MILLIS_TO_MINUTES)) % 60);
                int hours = (int) ((since / (MILLS_TO_HOURS)));
                int millis = (int) since % 1000;
                //updateTimerText(String.format("%02d:%02d:%02d:%03d ", hours, minutes, seconds, millis));


                //Update Distance
                double runTime = seconds + minutes*60;
                Random random = new Random();
                if(runTime>1 && seconds%5==0){
                    m+=random.nextFloat()*0.0001+0.0001;
                }
                String x = (String.format("%.2f", m));
                //updateDistanceText(x);



                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }

}