package com.example.etumoov.Accueil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.etumoov.Connect.Connexion_EtuMoov;
import com.example.etumoov.Connect.Inscription_EtuMoov;
import com.example.etumoov.ProfilActivity;
import com.example.etumoov.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import BD_Utilisateur.Models_Utilisateur.Profil;

public class AuthentificationMain extends AppCompatActivity {

    private Button btn_inscrire, btn_connect;

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseDatabase.getInstance().getReference("Profil").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Profil profil = snapshot.getValue(Profil.class);
                    if (profil != null){
                        Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
                        intent.putExtra("ID_Utilisateur", String.valueOf(profil.getId_user()));
                        startActivity(intent);
                        finish();
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