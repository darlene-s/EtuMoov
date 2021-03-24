package com.example.etumoov;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText regNom, regPrenom, regEmail, regPassword;
    TextView regLogin;
    Button regBtn;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.Theme_SquareRoute);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        regNom = findViewById(R.id.reg_nom);
        regPrenom = findViewById(R.id.reg_prenom);
        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_btn);
        regLogin = findViewById(R.id.reg_login_btn);
        //progressBar.setVisibility(View.GONE);

        regLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = regEmail.getText().toString();
                String prenom = regPrenom.getText().toString();
                String nom = regNom.getText().toString();
                String password = regPassword.getText().toString();

                if (email.isEmpty()) {
                    regEmail.setError("Email manquant");
                    return;
                }

                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    regEmail.setError("Email invalide");
                    return;
                }

                if (prenom.isEmpty()) {
                    regPrenom.setError("Prénom manquant");
                    return;
                }

                if (nom.isEmpty()) {
                    regPrenom.setError("nom manquant");
                    return;
                }

                if (password.isEmpty()) {
                    regPassword.setError("Mot de passe manquant");
                    return;
                }

             //   progressBar.setVisibility(View.VISIBLE);
                
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task ) {
                        if(task.isSuccessful()){
                            User user = new User(email,prenom);

                            FirebaseDatabase.getInstance().getReference("Utilisateur")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>(){
                                @Override
                                public void onComplete(@NonNull Task<Void> task){
                                    if(task.isSuccessful()){
                                        //progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Register.this, "Nouvel utilisateur créé avec succès", Toast.LENGTH_SHORT).show();
                                    }else{
                                        //progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Register.this, "Erreur lors de l'inscription. Veuillez réessayer", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), activity1.class));
                            finish();
                        }
                        else{
                            Toast.makeText(Register.this, "Erreur lors de l'inscription. Veuillez réessayer", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}


