package com.example.corre;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;
import static com.example.corre.Chronometer.MILLIS_TO_MINUTES;
import static com.example.corre.Chronometer.MILLS_TO_HOURS;


public class RunFragment extends Fragment {

    public static final String START_TIME = "START_TIME";
    public static final String CHRONO_WAS_RUNNING = "CHRONO_WAS_RUNNING";
    public static final String TV_TIMER_TEXT = "TV_TIMER_TEXT";

    TextView mTvTimer;
    Button mBtnStart, mBtnStop;
    TextView mTvDistance;
    DatabaseReference databaseRuns;
    Chronometer mChrono;
    Thread mThreadChrono;
    Context mContext;
    Timer t;
    double m = 0.00;
    double totTimeinHour;
    boolean mIsRunning;
    long mStartTime;

        TimerTask timer = new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long since = System.currentTimeMillis() - mStartTime;
                        totTimeinHour = since;
                        //Update Timer
                        int seconds = (int) (since / 1000) % 60;
                        int minutes = (int) ((since / (MILLIS_TO_MINUTES)) % 60);
                        int hours = (int) ((since / (MILLS_TO_HOURS)));
                        int millis = (int) since % 1000;
                        mTvTimer.setText(String.format("%02d:%02d:%02d:%03d ", hours, minutes, seconds, millis));


                        //Update Distance
                        double runTime = seconds + minutes * 60;
                        Random random = new Random();
                        if (runTime > 1 && seconds % 5 == 0) {
                            m += random.nextFloat() * 0.0001 + 0.0001;
                        }
                        mTvDistance.setText(String.format("%.2f", m));

                    }
                });
            }
        };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run, container, false);
        mContext = getContext();

        databaseRuns = FirebaseDatabase.getInstance().getReference("Runs");

        mBtnStart = (Button) view.findViewById(R.id.btn_start);
        mBtnStop = (Button) view.findViewById(R.id.btn_stop);
        mTvTimer = (TextView) view.findViewById(R.id.tv_timer);
        mTvDistance =(TextView)view.findViewById(R.id.tv_distance);


        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChrono == null) {
                    t = new Timer();
                    mChrono = new Chronometer(mContext);
                    mThreadChrono = new Thread(mChrono);
                    mStartTime = System.currentTimeMillis();
                    mIsRunning = true;

                    mThreadChrono.start();
                    mChrono.start();
                    t.scheduleAtFixedRate(timer, 0, 15);

                }
            }
        });



        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mChrono != null) {
                    mChrono.stop();
                    t.cancel();
                    mIsRunning = false;
                    mThreadChrono.interrupt();
                    mThreadChrono = null;
                    mChrono = null;


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Save Run?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String id = databaseRuns.push().getKey();
                                    Run r = Run.makeRun((String)(mTvDistance.getText()), (String) mTvTimer.getText(), id);
                                    databaseRuns.child(id).setValue(r);
                                    Toast toast = Toast.makeText(getActivity(),
                                            "Run Saved", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new ReportFragment()).commit();

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, new ReportFragment()).commit();

                                }
                            });


                    AlertDialog alert = builder.create();
                    alert.show();
     }
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        loadInstance();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveInstance();

        if(mChrono != null && mChrono.isRunning()) {

        }
    }

    @Override
    public void onDestroy() {

        saveInstance();

        if(mChrono == null || !mChrono.isRunning()) {

        }

        super.onDestroy();
    }

    private void saveInstance() {
        SharedPreferences pref = this.getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //TODO move tags to a static class
        if(mChrono != null && mChrono.isRunning()) {
            editor.putBoolean(CHRONO_WAS_RUNNING, mChrono.isRunning());
            editor.putLong(START_TIME, mChrono.getStartTime());

        } else {
            editor.putBoolean(CHRONO_WAS_RUNNING, false);
            editor.putLong(START_TIME, 0);

        }

        editor.putString(TV_TIMER_TEXT, mTvTimer.getText().toString());

        editor.commit();
    }


    private void loadInstance() {

        SharedPreferences pref = this.getActivity().getPreferences(MODE_PRIVATE);

        if(pref.getBoolean(CHRONO_WAS_RUNNING, false)) {
            long lastStartTime = pref.getLong(START_TIME, 0);
            if(lastStartTime != 0) {

                if(mChrono == null) {

                    if(mThreadChrono != null) {
                        mThreadChrono.interrupt();
                        mThreadChrono = null;
                    }


                    mChrono = new Chronometer(mContext, lastStartTime);
                    mThreadChrono = new Thread(mChrono);
                    mThreadChrono.start();
                    mChrono.start();
                }
            }
        }

    }



}
