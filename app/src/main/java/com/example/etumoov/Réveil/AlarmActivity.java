package com.example.etumoov.Réveil;

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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.etumoov.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {
    PendingIntent pendingIntent;
    Intent alarmReceiverIntent;
    AlarmManager alarmManager;
    Button btn_alarm_auto;
    Button btn_snooze;
    Button btn_play;
    Calendar calendar;
    Context context;
    TextView textAlarm;

    //binder
    AlarmService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        this.context = this;

        btn_alarm_auto = findViewById(R.id.btn_ajout_alarme_auto);
        textAlarm = findViewById(R.id.textViewAlarm);

        alarmReceiverIntent = new Intent(this.context, AlarmBroadcastReceiver.class);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        calendar = calendar.getInstance();

        //thread pour changer la visibilité des boutons
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
                                    try {
                                        textAlarm.setVisibility(View.INVISIBLE);
                                        btn_alarm_auto.setVisibility(View.INVISIBLE);

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
        //mettre le reveil en prenant en compte les données de l'utilisateur : heure du 1er cours, temps d'itineraire etc
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 53);
        calendar.set(Calendar.SECOND, 0);

        alarmReceiverIntent.putExtra("extra","alarm on");
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        textAlarm.setText("Alarme prévue pour : " + calendar.getTime());
        Toast.makeText(this, "ALARME LANCÉE", Toast.LENGTH_SHORT).show();
    }

    public void alarmSnooze(View view) {
        alarmReceiverIntent.putExtra("extra","alarm off");

        //cancel the alarm
        alarmManager.cancel(pendingIntent);
        textAlarm.setText("Aucune alarme programmée");

        //stop the ringtone
        //sends a message to stop directly to the ringtonePlayingService
        sendBroadcast(alarmReceiverIntent);

        btn_snooze.setVisibility(View.INVISIBLE);
        btn_play.setVisibility(View.INVISIBLE);
        btn_alarm_auto.setVisibility(View.VISIBLE);
        textAlarm.setVisibility(View.VISIBLE);

        alarmReceiverIntent.putExtra("extra","alarm on");
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        calendar = Calendar.getInstance();
        alarmManager.set(AlarmManager.RTC_WAKEUP, (calendar.getTimeInMillis()+300000), pendingIntent);
        textAlarm.setText("Alarme prévue dans 5 minutes");
    }

    public void startActivity(View view) {
        alarmReceiverIntent.putExtra("extra","alarm off");
        alarmManager.cancel(pendingIntent);
        textAlarm.setText("Aucune alarme programmée");
        sendBroadcast(alarmReceiverIntent);

        btn_snooze.setVisibility(View.INVISIBLE);
        btn_play.setVisibility(View.INVISIBLE);
        btn_alarm_auto.setVisibility(View.VISIBLE);
        textAlarm.setVisibility(View.VISIBLE);

        //Intent myIntent = new Intent(this, MeteoClickerActivity.class);
        //startActivity(myIntent);
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
