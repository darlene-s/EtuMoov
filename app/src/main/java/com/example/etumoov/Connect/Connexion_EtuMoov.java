package com.example.etumoov.Connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etumoov.MainActivity;
import com.example.etumoov.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import AffichageCours.Classes.CalendarJour;
import AffichageCours.Rappels.Rappels_Affichage;
import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;
import BD_Utilisateur.Models_Utilisateur.Profil;
import BD_Utilisateur.Models_Utilisateur.Utilisateur;

public class Connexion_EtuMoov extends AppCompatActivity {

    private FirebaseUser user;
    private EditText connexionEmail,connexionPassword;
    private TextView connexionInscription, connexionForgotPassword;
    private Button connexionButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private DataBaseHelper db;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion__etu_moov);

        //Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        connexionEmail = findViewById(R.id.reg_email);
        connexionPassword = findViewById(R.id.reg_password);
        connexionButton = findViewById(R.id.reg_login_btn);
        connexionInscription = findViewById(R.id.text_inscription);
        connexionForgotPassword = findViewById(R.id.text_mdpOublie);
        db = new DataBaseHelper(this);

        connexionInscription.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Inscription_EtuMoov.class)));

        connexionForgotPassword.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ForgotPassword_EtuMoov.class)));
        connexionButton.setOnClickListener(v -> {
            String email = connexionEmail.getText().toString();
            String password = connexionPassword.getText().toString();

            if(email.isEmpty()){
                connexionEmail.setError("Email manquant");
                return;
            }

            if(password.isEmpty()){
                connexionPassword.setError("Mot de passe manquant");
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnFailureListener(e -> Toast.makeText(Connexion_EtuMoov.this, "Mot de passe ou email erroné veuillez réessayer" , Toast.LENGTH_SHORT).show());
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
                user = FirebaseAuth.getInstance().getCurrentUser();
                reference = FirebaseDatabase.getInstance().getReference("Utilisateur");
                userID = user.getUid();

                reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot){
                        Utilisateur user = snapshot.getValue(Utilisateur.class);
                        if(user != null){
                            String prenom = user.getPrenom();
                            if(!db.UserExist(user.getEmail()))
                                db.insertUser(user);
                            Utilisateur utilisateur = db.getUtilisateurbyEmail(user.getEmail());
                            Toast.makeText(Connexion_EtuMoov.this, "Connexion réussie ! Bienvenue " + prenom + " !" , Toast.LENGTH_LONG).show();
                            db.close();
                            Intent intent = new Intent(getApplicationContext(), CalendarJour.class);
                            intent.putExtra("ID_Utilisateur", utilisateur.getId_user());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Connexion_EtuMoov.this, " Votre email ou mot de passe est invalide", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error){
                        Toast.makeText(Connexion_EtuMoov.this, "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), CalendarJour.class));
        }
    }
}