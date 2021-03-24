package com.example.etumoov;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import bdd.helper.DataBaseHelper;
import bdd.models.Utilisateur;

public class MainActivity extends AppCompatActivity {
    private DataBaseHelper db;
    private TextView text;
    private EditText regNom, regPrenom, regEmail, regPassword;
    private Button regBtn, regToLoginBtn;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DataBaseHelper(this);
        regNom = findViewById(R.id.reg_nom);
        regPrenom = findViewById(R.id.reg_prenom);
        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        text = findViewById(R.id.textView);
        regBtn = findViewById(R.id.reg_btn);
        //regToLoginBtn = findViewById(R.id.reg_login_btn);
        //text.setText(db.getUtilisateur(1).getPrenom());

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilisateur user = new Utilisateur(regNom.getText().toString(), regPrenom.getText().toString(), regEmail.getText().toString(), regPassword.getText().toString());
                db.insertUser(user);
                //text.setText(db.getUtilisateur(1).getPrenom());
                Log.d("dezdez", db.getUtilisateur(1).getPrenom());
            }
        });
        db.close();
    }
}