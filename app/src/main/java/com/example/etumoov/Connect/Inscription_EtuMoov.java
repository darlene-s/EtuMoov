package com.example.etumoov.Connect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.etumoov.MainActivity;
import com.example.etumoov.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import BD_Utilisateur.Models_Utilisateur.Utilisateur;

public class Inscription_EtuMoov extends AppCompatActivity {
    private EditText regNom, regPrenom, regEmail, regPassword;
    private TextView regLogin;
    private Button regBtn;
    private FirebaseAuth firebaseAuth;
    private String nom, prenom, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regNom = findViewById(R.id.reg_nom);
        regPrenom = findViewById(R.id.reg_prenom);
        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_btn);
        regLogin = findViewById(R.id.reg_login_btn);
        //progressBar.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

        regLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = regEmail.getText().toString();
                prenom = String.valueOf(regPrenom.getText());
                nom = String.valueOf(regNom.getText());
                password = regPassword.getText().toString();

                if (email.isEmpty()) {
                    regEmail.setError("Email manquant");
                }

                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    regEmail.setError("Email invalide");
                }

                if (prenom.isEmpty()) {
                    regPrenom.setError("Prénom manquant");
                }

                if (nom.isEmpty()) {
                    regPrenom.setError("nom manquant");
                }

                if (password.isEmpty()) {
                    regPassword.setError("Mot de passe manquant");
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Utilisateur user = new Utilisateur(nom, prenom, email, password);

                            FirebaseDatabase.getInstance().getReference("Utilisateur")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Inscription_EtuMoov.this, "Nouvel utilisateur créé avec succès", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Inscription_EtuMoov.this, "Erreur lors de l'inscription. Veuillez réessayer", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(Inscription_EtuMoov.this, "Erreur lors de l'inscription. Veuillez réessayer", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}


