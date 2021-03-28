package com.example.etumoov;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    PendingIntent pendingIntent;
    Intent alarmReceiverIntent;
    AlarmManager alarmManager;
    TimePicker timepicker;
    Button btn_alarm_auto;
    Button btn_alarm_manuelle;
    Button btn_snooze;
    Button btn_play;

    //binder
    AlarmService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_alarm_auto = findViewById(R.id.btn_ajout_alarme_auto);
        btn_alarm_manuelle = findViewById(R.id.btn_ajout_alarme_manuel);

        timepicker = (TimePicker) findViewById(R.id.timepicker);
        timepicker.setIs24HourView(true);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //new thread to show buttons
        Thread thread = new Thread(){
            @Override
            public void run(){
                try{
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(mService.showButtons()){
                                    Log.e("thread 2","alarm works");
                                    try {
                                        btn_alarm_auto.setVisibility(View.INVISIBLE);
                                        btn_alarm_manuelle.setVisibility(View.INVISIBLE);
                                        timepicker.setVisibility(View.INVISIBLE);

                                        btn_snooze = findViewById(R.id.btn_snooze);
                                        btn_play = findViewById(R.id.btn_play);

                                        btn_snooze.setVisibility(View.VISIBLE);
                                        btn_play.setVisibility(View.VISIBLE);
                                    } catch(Exception e) {}
                                }
                            }
                        });
                    }
                } catch(InterruptedException e){}
            }
        };
        thread.start();
    }

    public void setAutoAlarm(View view) {
        Calendar calendar = Calendar.getInstance();

        //mettre le reveil en prenant en compte les données de l'utilisateur : heure du 1er cours, temps d'itineraire etc
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 18);
        calendar.set(Calendar.SECOND, 0);

        alarmReceiverIntent = new Intent(this, AlarmBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmReceiverIntent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "ALARME LANCÉE AUTOMATIQUEMENT", Toast.LENGTH_SHORT).show();
    }

    public void setBasicAlarm(View view) {
        Calendar calendar = Calendar.getInstance();

        //ce sont ici les valeurs du timepicker
        calendar.set(Calendar.HOUR_OF_DAY, timepicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timepicker.getCurrentMinute());
        calendar.set(Calendar.SECOND, 0);

        alarmReceiverIntent = new Intent(this, AlarmBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmReceiverIntent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "ALARME LANCÉE MANUELLEMENT", Toast.LENGTH_SHORT).show();
    }

    public void alarmSnooze(View view) {
        alarmReceiverIntent = new Intent(this, AlarmBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, 300000, pendingIntent);
        Toast.makeText(this, "ALARME DANS 5 MIN", Toast.LENGTH_SHORT).show();
    }

    public void startActivity(View view) {
        Intent myIntent = new Intent(this, MeteoClickerActivity.class);
        startActivity(myIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to AlarmService
        Intent binder_intent = new Intent(this, AlarmService.class);
        bindService(binder_intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
        mBound = false;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            AlarmService.LocalBinder binder = (AlarmService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}