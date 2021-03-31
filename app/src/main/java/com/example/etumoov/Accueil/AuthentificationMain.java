package com.example.etumoov.Accueil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.etumoov.Connect.Connexion_EtuMoov;
import com.example.etumoov.Connect.Inscription_EtuMoov;
import com.example.etumoov.Profil.ProfilActivity;
import com.example.etumoov.Profil.ProfilRegisterActivity;
import com.example.etumoov.R;
import com.example.etumoov.Réveil.AlarmActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import AffichageCours.Classes.CalendarJour;
import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;
import BD_Utilisateur.Models_Utilisateur.Profil;
import BD_Utilisateur.Models_Utilisateur.Utilisateur;
import Meteo.MeteoActivity;
import Paramètres.SettingsActivity;

public class AuthentificationMain extends AppCompatActivity {

    private Button btn_inscrire, btn_connect;
    private DataBaseHelper db;

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Toast.makeText(AuthentificationMain.this, "Veuillez patienter !", Toast.LENGTH_SHORT).show();
            FirebaseDatabase.getInstance().getReference("Profil").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Profil profil = snapshot.getValue(Profil.class);
                    if (profil != null){
                        Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
                        intent.putExtra("ID_Utilisateur", String.valueOf(profil.getId_user()));
                        SharedPreferences.Editor editor = getSharedPreferences("cle_id", MODE_PRIVATE).edit();
                        editor.putString("cle_id_recup", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        editor.apply();
                        startActivity(intent);
                        finish();
                    } else {
                        FirebaseDatabase.getInstance().getReference("Utilisateur").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Utilisateur utilisateur = snapshot.getValue(Utilisateur.class);
                                if (utilisateur != null){
                                    db = new DataBaseHelper(getApplicationContext());
                                    Utilisateur user = db.getUtilisateurbyEmail(utilisateur.getEmail());
                                    Intent intent = new Intent(getApplicationContext(), ProfilRegisterActivity.class);
                                    intent.putExtra("ID_Utilisateur", String.valueOf(user.getId_user()));
                                    finish();
                                    startActivity(intent);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(AuthentificationMain.this, "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AuthentificationMain.this, "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification_main);
        btn_inscrire = findViewById(R.id.btn_inscrire);
        btn_connect = findViewById(R.id.btn_authentification);

        btn_inscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Inscription_EtuMoov.class));
                finish();
            }
        });

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Connexion_EtuMoov.class));
                finish();
            }
        });
    }
    
}