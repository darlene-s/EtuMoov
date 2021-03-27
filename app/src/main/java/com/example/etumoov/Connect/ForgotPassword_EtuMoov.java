package com.example.etumoov.Connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.etumoov.MainActivity;
import com.example.etumoov.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword_EtuMoov extends AppCompatActivity {
    private EditText emailEditText;
    private Button resetPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password__etu_moov);

        emailEditText = findViewById(R.id.forgotpassword_email);
        resetPassword = findViewById(R.id.btn_submit);
        auth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();
        if(email.isEmpty()) {
            emailEditText.setError("L'email est nécéssaire pour réinitialiser le mot de passe");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Veuillez entrer une adresse mail valide");
            emailEditText.requestFocus();
            return;
        }
        //progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPassword_EtuMoov.this,"Un email de récupération vous a été envoyé",Toast.LENGTH_LONG).show();
                    //progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else {
                    Toast.makeText(ForgotPassword_EtuMoov.this, "Veuillez réessayer à nouveau, votre mail est probablement invalide", Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}