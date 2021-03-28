package com.example.etumoov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;
import BD_Utilisateur.Models_Utilisateur.Profil;
import BD_Utilisateur.Models_Utilisateur.Utilisateur;

public class ProfilRegisterActivity extends AppCompatActivity {

    private EditText tps_prepa, tps_supp;
    private Button btn_creation;
    private DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_register);
        tps_prepa = findViewById(R.id.tps_prepa);
        tps_supp = findViewById(R.id.tps_suppl);
        btn_creation = findViewById(R.id.btn_creation);
        db = new DataBaseHelper(this);
        Intent intent = getIntent();
        int id = intent.getIntExtra("Id_Utilisateur", 0);
        btn_creation.setOnClickListener(v -> {
            String vTps_P = tps_prepa.getText().toString();
            String vTps_S = tps_supp.getText().toString();
            if (vTps_P.isEmpty())
                tps_prepa.setError("Le champ est vide");
            Utilisateur user = db.getUtilisateurbyId(id);
            db.insertProfil(new Profil(Double.parseDouble(vTps_P), Double.parseDouble(vTps_S), 0, "", "", user.getId_user()));
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }
}