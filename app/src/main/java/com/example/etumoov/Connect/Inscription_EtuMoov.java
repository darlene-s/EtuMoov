package com.example.etumoov.Connect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.etumoov.MainActivity;
import com.example.etumoov.Profil.ProfilRegisterActivity;
import com.example.etumoov.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;
import BD_Utilisateur.Models_Utilisateur.Utilisateur;

public class Inscription_EtuMoov extends AppCompatActivity {
    private EditText regNom, regPrenom, regEmail, regPassword;
    private TextView regLogin;
    private Button regBtn;
    private Utilisateur user;
    private DataBaseHelper db;
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
        db = new DataBaseHelper(this);
        firebaseAuth = FirebaseAuth.getInstance();

        regLogin.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));

        regBtn.setOnClickListener(v -> {
            email = String.valueOf(regEmail.getText());
            prenom = String.valueOf(regPrenom.getText());
            nom = String.valueOf(regNom.getText());
            password = String.valueOf(regPassword.getText());

            if (prenom.isEmpty()) {
                regPrenom.setError("Prénom manquant");
            }

            else if (nom.isEmpty()) {
                regPrenom.setError("nom manquant");
            }

            else if (email.isEmpty()) {
                regEmail.setError("Email manquant");
            }

            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                regEmail.setError("Email invalide");
            }
            else if (password.isEmpty()) {
                regPassword.setError("Mot de passe manquant");
            }
            else if(password.matches("[0-9]+"))
                regPassword.setError("Votre mot de passe ne peut pas contenir que des chiffres");
            else if(password.matches("[a-zA-Z]+"))
                regPassword.setError("Votre mot de passe ne peut pas contenir que des lettres");
            else {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Utilisateur utilisateur = new Utilisateur(nom, prenom, email, password);
                            FirebaseDatabase.getInstance().getReference("Utilisateur")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(utilisateur).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        if(!db.UserExist(email))
                                            db.insertUser(utilisateur);
                                        Toast.makeText(Inscription_EtuMoov.this, "Nouvel utilisateur créé avec succès", Toast.LENGTH_SHORT).show();
                                        user = db.getUtilisateurbyEmail(utilisateur.getEmail());
                                        Intent intent = new Intent(getApplicationContext(), ProfilRegisterActivity.class);
                                        intent.putExtra("ID_Utilisateur", String.valueOf(user.getId_user()));
                                        intent.putExtra("clé", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        startActivity(intent);
                                        db.close();
                                        finish();
                                    } else {
                                        Toast.makeText(Inscription_EtuMoov.this, "Erreur lors de l'inscription. Veuillez réessayer", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else{
                            Toast.makeText(Inscription_EtuMoov.this, "Erreur lors de l'inscription. Veuillez réessayer", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}


