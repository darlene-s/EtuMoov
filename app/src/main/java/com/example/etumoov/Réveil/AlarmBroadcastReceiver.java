package com.example.etumoov.Réveil;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //need to send the extra to the ringtonePlayingService
        String getExtra = intent.getStringExtra("extra");

        Intent serviceIntent = new Intent(context, AlarmService.class);

        serviceIntent.putExtra("extra", getExtra);

        //context.startForegroundService(serviceIntent);
        context.startService(serviceIntent);
    }
}