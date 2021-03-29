package com.example.etumoov.Accueil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.etumoov.MainActivity;
import com.example.etumoov.R;

import java.util.Locale;

public class SplashScreenActivity extends AppCompatActivity {
    //Animation étudiants
    ImageView etudiants;
    Animation bottomAnimation;
    private final int SCREEN_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(this);
        String value = spf.getString(getString(R.string.langue_app), "0");
        setLangue(value);
        etudiants = findViewById(R.id.ic_etudiants);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        etudiants.setAnimation(bottomAnimation);

        //Redirection sur la page principale "MainActivity"
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //démarrage d'une page
                Intent intent = new Intent(getApplicationContext(), AuthentificationMain.class);
                startActivity(intent);
                finish();
            }
        }, SCREEN_TIMEOUT);
    }

    // Charge la langue enregistrée dans les préférences partagées
    private void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", AuthentificationMain.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLocale(language);
        //Toast.makeText(SplashScreenActivity.this, language, Toast.LENGTH_SHORT).show();
    }

    private void setLangue(String value){
        int id = Integer.parseInt(value);
        if (id == 0){
            setLocale("fr");
            loadLocale();
        }
        if (id == 1){
            setLocale("en");
            loadLocale();
        }
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        // Enregistre les données dans les préférences partagées
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }
}