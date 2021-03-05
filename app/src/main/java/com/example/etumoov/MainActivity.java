package com.example.etumoov;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText regNom, regPrenom, regEmail, regPassword;
    Button regBtn, regToLoginBtn;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        regNom = findViewById(R.id.reg_nom);
        regPrenom = findViewById(R.id.reg_prenom);
        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_btn);
        //regToLoginBtn = findViewById(R.id.reg_login_btn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = regNom.getText().toString();
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Utilisateur");
                String prenom = regPrenom.getText().toString();
                String email = regEmail.getText().toString();
                String password = regPassword.getText().toString();
                UserHelp helperClass = new UserHelp(nom, prenom, email, password);

                reference.child(email).setValue(helperClass);
            }
        });
    }
}