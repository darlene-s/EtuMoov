package com.example.etumoov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import jeu.calcul.CalculActivity;
import jeu.clicker.ClickerActivity;
import jeu.memory.Memory;
/* main temporaire */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void salut(View view){
        startActivity(new Intent(getApplicationContext(), Memory.class));
        finish();
    }
    public void pouet(View view){
        startActivity(new Intent(getApplicationContext(), CalculActivity.class));
        finish();
    }

    public void Panda(View view){
        startActivity(new Intent(getApplicationContext(), ClickerActivity.class));
        finish();
    }



}