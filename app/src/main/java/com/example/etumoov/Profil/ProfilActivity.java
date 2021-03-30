package com.example.etumoov.Profil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etumoov.Accueil.AuthentificationMain;
import com.example.etumoov.R;
import com.example.etumoov.Réveil.AlarmActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import AffichageCours.Classes.CalendarJour;
import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;
import BD_Utilisateur.Models_Utilisateur.Profil;
import BD_Utilisateur.Models_Utilisateur.Utilisateur;
import Meteo.MeteoActivity;

public class ProfilActivity extends AppCompatActivity {

    private TextView textTps_p, textTps_s, textScore;
    private Button btn_deconnexion;
    private DataBaseHelper db;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        textTps_p = findViewById(R.id.text_tps_p);
        textTps_s = findViewById(R.id.text_tps_s);
        textScore = findViewById(R.id.textScore);
        btn_deconnexion = findViewById(R.id.btn_deconnexion);
        db = new DataBaseHelper(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.profil);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.reveil:
                        startActivity(new Intent(getApplicationContext(),  AlarmActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profil:
                        return true;
                    case R.id.emploiDuTps:
                        startActivity(new Intent(getApplicationContext(), CalendarJour.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation:
                        startActivity(new Intent(getApplicationContext(), AlarmActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.meteo:
                        startActivity(new Intent(getApplicationContext(), MeteoActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        btn_deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deconnexion(v);
            }
        });
        Intent intent = getIntent();
        if (intent.hasExtra("ID_Utilisateur")){
            String str = intent.getStringExtra("ID_Utilisateur");
            int id = Integer.parseInt(str);
            if(!db.ProfilExist(id)){
                reference = FirebaseDatabase.getInstance().getReference("Profil");
                reference.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Profil profil = snapshot.getValue(Profil.class);
                        if (profil != null){
                            db.insertProfil(profil);
                            Profil pp = db.getProfil(id);
                            textTps_p.setText(String.valueOf(pp.getTps_preparation()));
                            textTps_s.setText(String.valueOf(pp.getTps_supplementaires()));
                            textScore.setText(String.valueOf(pp.getScore()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProfilActivity.this, "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Profil profil = db.getProfil(id);
                textTps_p.setText(String.valueOf(profil.getTps_preparation()));
                textTps_s.setText(String.valueOf(profil.getTps_supplementaires()));
                textScore.setText(String.valueOf(profil.getScore()));
            }
        }
        db.close();


    }

    private void deconnexion (View view){
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut(); // Déconnexion de l'utilisateur
            db.deleteDataUser();
            startActivity(new Intent(getApplicationContext(), AuthentificationMain.class));
            db.close();
            finish();
        }
        else {
            Toast.makeText(ProfilActivity.this, "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
        }
    }
}