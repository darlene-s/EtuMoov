package com.example.etumoov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import AffichageCours.Classes.CalendarJour;
import AffichageCours.Rappels.Rappels_Affichage;
import Meteo.MeteoActivity;
import jeu.calcul.CalculActivity;
import jeu.clicker.ClickerActivity;
import jeu.memory.Memory;
/* main temporaire */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.profil);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.reveil:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profil: return true;
                    case R.id.meteo:
                        startActivity(new Intent(getApplicationContext(), Meteo.MeteoActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.emploiDuTps:
                        startActivity(new Intent(getApplicationContext(), CalendarJour.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
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