package com.example.etumoov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class Connexion_EtuMoov extends AppCompatActivity {

    private FirebaseUser user;
    EditText connexionEmail,connexionPassword;
    TextView connexionInscription, connexionForgotPassword;
    Button connexionButton;
    FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
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
        connexionInscription = findViewById(R.id.reg_btn);
        //connexionForgotPassword = findViewById(R.id.forgotpassword);

        connexionInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Inscription_EtuMoov.class));
            }
        });

        connexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Connexion_EtuMoov.this, "Mot de passe ou email erroné veuillez réessayer" , Toast.LENGTH_SHORT).show();
                    }
                });
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        reference = FirebaseDatabase.getInstance().getReference("Utilisateur");
                        userID = user.getUid();

                        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener(){
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot){
                                User user = snapshot.getValue(User.class);
                                if(user != null){
                                    String prenom = user.prenom;
                                    Toast.makeText(Connexion_EtuMoov.this, "Connexion réussie ! Bienvenue " + prenom + " !" , Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), activity1.class));
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
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), activity1.class));
        }
    }
}