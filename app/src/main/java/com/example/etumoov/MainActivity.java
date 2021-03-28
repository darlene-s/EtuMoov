package com.example.etumoov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import AffichageCours.Classes.CalendarJour;
import AffichageCours.Rappels.Rappels_Affichage;
import jeu.calcul.CalculActivity;
import jeu.clicker.ClickerActivity;
import jeu.memory.Memory;
/* main temporaire */
public class MainActivity extends AppCompatActivity {
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


}