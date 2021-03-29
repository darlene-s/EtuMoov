package com.example.etumoov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Random;


public class MeteoClickerActivity extends AppCompatActivity {
    TextView textscore;
    ImageView tornado, hotsun, smilingsun, thunderstorm, smilingmoon, twoclouds;
    ImageView[] IMGS = {tornado, hotsun, smilingsun, thunderstorm, smilingmoon, twoclouds};
    int score = 0;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteo_clicker);

        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.start();

        textscore = findViewById(R.id.textscore);
        tornado = findViewById(R.id.tornado);
        hotsun = findViewById(R.id.hotsun);
        smilingsun = findViewById(R.id.smilingsun);
        thunderstorm = findViewById(R.id.thunderstorm);
        smilingmoon = findViewById(R.id.smilingmoon);
        twoclouds = findViewById(R.id.twoclouds);

        IMGS[0] = tornado;
        IMGS[1] = hotsun;
        IMGS[2] = smilingsun;
        IMGS[3] = thunderstorm;
        IMGS[4] = smilingmoon;
        IMGS[5] = twoclouds;

        for (int i = 0; i <= 5; i++) {
            IMGS[i].setVisibility(View.INVISIBLE);
        }
        int i = random();
        IMGS[i].setVisibility(View.VISIBLE);
        IMGS[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMGS[i].setVisibility(View.INVISIBLE);
                score++;
                textscore.setText("Score : 0" + score);
                newImage(IMGS);
            }
        });
    }

    private int random() {
        Random r = new Random();
        int i = r.nextInt(5 - 0) + 0;
        return i;
    }

    private void newImage(ImageView[] t) {
        if (score < 10) {
            int j = random();
            t[j].setVisibility(View.VISIBLE);
            t[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    t[j].setVisibility(View.INVISIBLE);
                    score++;
                    textscore.setText("Score : " + score);
                    newImage(IMGS);
                }
            });
        } else {
            player.stop();
            finish();
        }
    }
}
