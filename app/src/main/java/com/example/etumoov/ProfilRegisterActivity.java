package com.example.etumoov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.etumoov.Connect.Inscription_EtuMoov;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;
import BD_Utilisateur.Models_Utilisateur.Profil;
import BD_Utilisateur.Models_Utilisateur.Utilisateur;

public class ProfilRegisterActivity extends AppCompatActivity {

    private EditText tps_prepa, tps_supp;
    private Button btn_creation;
    private DataBaseHelper db;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_register);
        tps_prepa = findViewById(R.id.tps_prepa);
        tps_supp = findViewById(R.id.tps_suppl);
        btn_creation = findViewById(R.id.btn_creation);
        db = new DataBaseHelper(this);
        Intent intent = getIntent();
        String str = intent.getStringExtra("ID_Utilisateur");
        int id = Integer.parseInt(str);
        btn_creation.setOnClickListener(v -> {
            String vTps_P = tps_prepa.getText().toString();
            String vTps_S = tps_supp.getText().toString();
            if (vTps_P.isEmpty())
                tps_prepa.setError("Le champ est vide");
            else {
                Utilisateur user = db.getUtilisateurbyId(id);
                Profil profil = new Profil(Double.parseDouble(vTps_P), Double.parseDouble(vTps_S), 0, "", "", user.getId_user());
                if (profil != null){
                    FirebaseDatabase.getInstance().getReference("Profil").child(String.valueOf(user.getId_user())).setValue(profil);
                    Toast.makeText(ProfilRegisterActivity.this, "Profil enregistré", Toast.LENGTH_LONG).show();
                    db.insertProfil(profil);
                    Intent nextIntent = new Intent(getApplicationContext(), ProfilActivity.class);
                    nextIntent.putExtra("ID_Utilisateur", String.valueOf(user.getId_user()));
                    startActivity(nextIntent);
                    db.close();
                    finish();
                }
                else
                    Toast.makeText(ProfilRegisterActivity.this, "Erreur lors de l'inscription. Veuillez réessayer", Toast.LENGTH_LONG).show();
            }
        });
    }
}