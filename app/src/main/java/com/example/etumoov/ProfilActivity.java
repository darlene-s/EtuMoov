package com.example.etumoov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;
import BD_Utilisateur.Models_Utilisateur.Profil;

public class ProfilActivity extends AppCompatActivity {

    private TextView textTps_p, textTps_s, textScore;
    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        textTps_p = findViewById(R.id.text_tps_p);
        textTps_s = findViewById(R.id.text_tps_s);
        textScore = findViewById(R.id.textScore);
        db = new DataBaseHelper(this);

        Intent intent = getIntent();
        String str = intent.getStringExtra("ID_Utilisateur");
        int id = Integer.parseInt(str);
        Profil profil = db.getProfil(id);

        textTps_p.setText(String.valueOf(profil.getTps_preparation()));
        textTps_s.setText(String.valueOf(profil.getTps_supplementaires()));
        textScore.setText(String.valueOf(profil.getScore()));
    }
}