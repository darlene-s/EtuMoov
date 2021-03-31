package com.example.etumoov.Réveil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.etumoov.NavigationMap.MoovInTime.MoovInTimeMenu;
import com.example.etumoov.Profil.ProfilActivity;
import com.example.etumoov.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Random;

import AffichageCours.Classes.CalendarJour;
import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;
import BD_Utilisateur.Models_Utilisateur.Navigation;
import Meteo.MeteoActivity;

public class AlarmActivity extends AppCompatActivity {
    PendingIntent pendingIntent;
    Intent alarmReceiverIntent;
    AlarmManager alarmManager;
    Button btn_alarm_auto;
    Button btn_alarm_manual;
    Button btn_cancel_alarm;
    Button btn_snooze;
    Button btn_play;
    Calendar calendar;
    TimePickerDialog timePicker;
    Context context;
    TextView textAlarm;
    Class<?>[] listGames = {jeu.clicker.ClickerActivity.class, jeu.memory.Memory.class, jeu.calcul.CalculActivity.class};
    DataBaseHelper db;

    //binder
    AlarmService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        this.context = this;

        db = new DataBaseHelper(context);

        listGames[0] = jeu.clicker.ClickerActivity.class;
        listGames[1] = jeu.memory.Memory.class;
        listGames[2] = jeu.calcul.CalculActivity.class;

        btn_alarm_auto = findViewById(R.id.btn_ajout_alarme_auto);
        btn_alarm_manual = findViewById(R.id.btn_ajout_alarme_manuelle);
        btn_cancel_alarm = findViewById(R.id.btn_cancel_alarm);
        textAlarm = findViewById(R.id.textViewAlarm);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("cle_id", ProfilActivity.MODE_PRIVATE);
        String cle_id = prefs.getString("cle_id_recup", "");

        alarmReceiverIntent = new Intent(this.context, AlarmBroadcastReceiver.class);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        calendar = calendar.getInstance();

        Navigation nav = db.getNavigation();
        btn_alarm_manual.setText(String.valueOf(nav.getTps_trajet()));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.reveil);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.reveil:
                        return true;
                    case R.id.profil:
                        startActivity(new Intent(getApplicationContext(), ProfilActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.emploiDuTps:
                        startActivity(new Intent(getApplicationContext(), CalendarJour.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation:
                        startActivity(new Intent(getApplicationContext(), MoovInTimeMenu.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.meteo:
                        startActivity(new Intent(getApplicationContext(), MeteoActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });


        //thread pour changer la visibilité des boutons snooze et play
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
                                        btn_alarm_manual.setVisibility(View.INVISIBLE);
                                        btn_cancel_alarm.setVisibility(View.INVISIBLE);

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

        //thread pour gérer la visibilité du bouton cancel alarme
        Thread thread2 = new Thread(){
            @Override
            public void run(){
                try{
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String extra = alarmReceiverIntent.getStringExtra("extra");
                                if(extra == null || extra.contentEquals("alarm off")){
                                    try {
                                        btn_cancel_alarm.setVisibility(View.INVISIBLE);
                                        btn_alarm_auto.setVisibility(View.VISIBLE);
                                        btn_alarm_manual.setVisibility(View.VISIBLE);
                                    } catch(Exception e) {}
                                } else if(extra.contentEquals("alarm on")){
                                    btn_cancel_alarm.setVisibility(View.VISIBLE);
                                    btn_alarm_manual.setVisibility(View.INVISIBLE);
                                    btn_alarm_auto.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }
                } catch(InterruptedException e){}
            }
        };
        thread2.start();

        //click bouton alarme manuelle -> timepicker apparait et alarme est programmée
        btn_alarm_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                        calendar.set(Calendar.SECOND, 0);
                        textAlarm.setText("Alarme prévue pour : " + hourOfDay + ":" + minutes);
                        alarmReceiverIntent.putExtra("extra","alarm on");
                        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        Toast.makeText(context, "ALARME LANCÉE", Toast.LENGTH_SHORT).show();
                    }
                }, 0, 0, true);
                timePicker.show();
            }
        });
    }

    public void setAutoAlarm(View view) {
        //mettre le reveil en prenant en compte les données de l'utilisateur : heure du 1er cours, temps d'itineraire etc
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 53);
        calendar.set(Calendar.SECOND, 0);

        alarmReceiverIntent.putExtra("extra","alarm on");
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        //textAlarm.setText("Alarme prévue pour : " + calendar.getTime());
        Toast.makeText(this, "ALARME LANCÉE", Toast.LENGTH_SHORT).show();
    }

    //annule une alarme, s'il n'y en a pas ça va crash, donc faut gérer la visibilité du bouton cancel alarme (cf thread2)
    public void cancelAlarm(View view) {
        alarmReceiverIntent.putExtra("extra","alarm off");

        //cancel the alarm
        alarmManager.cancel(pendingIntent);
        textAlarm.setText("Aucune alarme programmée");

        //stop the ringtone
        //sends a message to stop directly to the ringtonePlayingService
        sendBroadcast(alarmReceiverIntent);

        Toast.makeText(this, "ALARME ANNULÉE", Toast.LENGTH_SHORT).show();
    }

    //stop l'alarme et la musique et set une alarme pour dans 5min après
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

        textAlarm.setVisibility(View.VISIBLE);
        btn_alarm_auto.setVisibility(View.VISIBLE);
        btn_alarm_manual.setVisibility(View.VISIBLE);
        btn_cancel_alarm.setVisibility(View.VISIBLE);

        alarmReceiverIntent.putExtra("extra","alarm on");
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        calendar = Calendar.getInstance();
        alarmManager.set(AlarmManager.RTC_WAKEUP, (calendar.getTimeInMillis()+300000), pendingIntent);
        textAlarm.setText("Alarme prévue dans 5 minutes");
    }

    //stop la musique et lance un jeu au pif
    public void startActivity(View view) {
        alarmReceiverIntent.putExtra("extra","alarm off");
        alarmManager.cancel(pendingIntent);
        textAlarm.setText("Aucune alarme programmée");
        sendBroadcast(alarmReceiverIntent);

        btn_snooze.setVisibility(View.INVISIBLE);
        btn_play.setVisibility(View.INVISIBLE);

        textAlarm.setVisibility(View.VISIBLE);
        btn_alarm_auto.setVisibility(View.VISIBLE);
        btn_alarm_manual.setVisibility(View.VISIBLE);
        btn_cancel_alarm.setVisibility(View.VISIBLE);

        Intent myIntent = new Intent(this, listGames[random()]);
        startActivity(myIntent);
    }

    private int random() {
        Random r = new Random();
        int i = r.nextInt(3 - 0) + 0;
        return i;
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
