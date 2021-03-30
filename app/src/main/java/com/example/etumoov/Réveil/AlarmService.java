package com.example.etumoov.Réveil;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AlarmService extends Service {
    private MediaPlayer player;
    boolean isRunning;
    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        AlarmService getService() {
            // Return this instance of ringtonePlayingService so clients can call public methods
            return AlarmService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String state = intent.getStringExtra("extra");
        //Toast.makeText(this, state, Toast.LENGTH_SHORT).show();

        if(state.contentEquals("alarm off"))
            startId = 0;
        else
            startId = 1;

        if(!this.isRunning && startId == 1){

            player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
            player.start();
            isRunning = true;

        } else if (this.isRunning && startId == 0){

            player.stop();
            player.reset();
            isRunning = false;

        } else if (!this.isRunning && startId == 0){

            this.isRunning = false;

        } else if (this.isRunning && startId == 1){

            this.isRunning = true;

        } else {

        }

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //arrête l'alarme lorsque le service est interrompu (cf jeux)
        isRunning = false;
    }

    public boolean showButtons(){
        if(this.isRunning)
            return true;
        else
            return false;
    }
}