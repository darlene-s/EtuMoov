package com.example.etumoov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {
    //Animation étudiants
    ImageView etudiants;
    Animation bottomAnimation;
    private final int SCREEN_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        etudiants = findViewById(R.id.ic_etudiants);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        etudiants.setAnimation(bottomAnimation);

        //Redirection sur la page principale "MainActivity"
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //démarrage d'une page
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SCREEN_TIMEOUT);
    }
}