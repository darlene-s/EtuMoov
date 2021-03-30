package com.example.etumoov.Profil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etumoov.Accueil.AuthentificationMain;
import com.example.etumoov.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;
import BD_Utilisateur.Models_Utilisateur.Profil;

public class ProfilActivity extends AppCompatActivity {

    private TextView textTps_p, textTps_s, textScore;
    private Button btn_deconnexion;
    private DataBaseHelper db;
    private DatabaseReference reference;

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            if (FirebaseDatabase.getInstance().getReference("Profil").child(FirebaseAuth.getInstance().getCurrentUser().getUid()) == null){
                Intent intent = new Intent(getApplicationContext(), ProfilRegisterActivity.class);
                finish();
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        textTps_p = findViewById(R.id.text_tps_p);
        textTps_s = findViewById(R.id.text_tps_s);
        textScore = findViewById(R.id.textScore);
        btn_deconnexion = findViewById(R.id.btn_deconnexion);
        db = new DataBaseHelper(this);

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