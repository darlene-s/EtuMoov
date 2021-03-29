package com.example.etumoov;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import AffichageCours.Classes.CalendarJour;
import AffichageCours.Rappels.Rappels_Affichage;
import Paramètres.SettingsActivity;
import jeu.calcul.CalculActivity;
import jeu.clicker.ClickerActivity;
import jeu.memory.Memory;
/* main temporaire */
public class MainActivity extends AppCompatActivity {
    private TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick1(View view){
        startActivity(new Intent(getApplicationContext(), CalendarJour.class));
        finish();
    }

    public void onClick2(View view){
        startActivity(new Intent(getApplicationContext(), Rappels_Affichage.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
    }
}