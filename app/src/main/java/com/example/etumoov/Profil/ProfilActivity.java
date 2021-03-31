package com.example.etumoov.Profil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etumoov.Accueil.AuthentificationMain;
import com.example.etumoov.NavigationMap.MoovInTime.MoovInTimeMenu;
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

    private TextView textTps_p, textTps_s, textScore, textScoreMemory, textScoreClicker, textNomPrenom;
    private Button btn_deconnexion;
    private DataBaseHelper db;
    private DatabaseReference referenceUser, referenceProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        textTps_p = findViewById(R.id.text_tps_p);
        textTps_s = findViewById(R.id.text_tps_s);
        textScore = findViewById(R.id.textScore);
        textScoreMemory = findViewById(R.id.textScoreMemory);
        textScoreClicker = findViewById(R.id.textScoreClicker);
        textNomPrenom = findViewById(R.id.textNomPrenom);

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
                        startActivity(new Intent(getApplicationContext(), MoovInTimeMenu.class));
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
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("cle_id", ProfilActivity.MODE_PRIVATE);
            String cle_id = prefs.getString("cle_id_recup", "");
            int id = Integer.parseInt(str);
            if(!db.UserExist(id)){
                referenceUser = FirebaseDatabase.getInstance().getReference("Utilisateur");
                referenceUser.child(cle_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Utilisateur utilisateur = snapshot.getValue(Utilisateur.class);
                        if (utilisateur != null){
                            db.insertUser(utilisateur);
                            Utilisateur user = db.getUser();
                            textNomPrenom.setText(user.getNom() + " "+ user.getPrenom());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProfilActivity.this, "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Utilisateur user = db.getUser();
                textNomPrenom.setText(user.getNom() + " " + user.getPrenom());
            }

            if(!db.ProfilExist(id)){
                referenceProfil = FirebaseDatabase.getInstance().getReference("Profil");
                referenceProfil.child(cle_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Profil profil = snapshot.getValue(Profil.class);
                        if (profil != null){
                            db.insertProfil(profil);
                            Profil pp = db.getProfils();
                            textTps_p.setText(String.valueOf(pp.getTps_preparation()));
                            textTps_s.setText(String.valueOf(pp.getTps_supplementaires()));
                            textScore.setText(String.valueOf(pp.getScore()));
                            textScoreMemory.setText(String.valueOf(pp.getTimerMemory()));
                            textScoreClicker.setText(String.valueOf(pp.getTimerCookie()));
                            SharedPreferences.Editor sh = getSharedPreferences("id_profil",MODE_PRIVATE).edit();
                            sh.putInt("id",pp.getId_user());
                            sh.apply();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProfilActivity.this, "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Profil profil = db.getProfils();
                textTps_p.setText(String.valueOf(profil.getTps_preparation()));
                textTps_s.setText(String.valueOf(profil.getTps_supplementaires()));
                textScore.setText(String.valueOf(profil.getScore()));
                textScoreMemory.setText(String.valueOf(profil.getTimerMemory()));
                textScoreClicker.setText(String.valueOf(profil.getTimerCookie()));
                SharedPreferences.Editor sh = getSharedPreferences("id_profil",MODE_PRIVATE).edit();
                sh.putInt("id",profil.getId_profil());
                sh.apply();
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