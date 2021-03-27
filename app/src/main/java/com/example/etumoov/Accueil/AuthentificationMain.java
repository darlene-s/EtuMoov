package com.example.etumoov.Accueil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.etumoov.Connect.Connexion_EtuMoov;
import com.example.etumoov.Connect.Inscription_EtuMoov;
import com.example.etumoov.R;

public class AuthentificationMain extends AppCompatActivity {

    private Button btn_inscrire, btn_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification_main);

        btn_inscrire = findViewById(R.id.btn_inscrire);
        btn_connect = findViewById(R.id.btn_authentification);

        btn_inscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Inscription_EtuMoov.class));
                finish();
            }
        });

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Connexion_EtuMoov.class));
                finish();
            }
        });
    }
}