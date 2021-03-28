package com.example.etumoov;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import AffichageCours.Classes.CalendarJour;
import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;

/**
 * Classe ScannerActivity, pour le scan du QR CODE
 *
 * @author Alexandre Hardy
 * @version 1.0
 */
public class ScannerActivity extends AppCompatActivity {

    private CodeScanner codeScanner;
    private CodeScannerView scannView;
    private DataBaseHelper db;

    /**
     * Fonction de création de l'activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        scannView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this,scannView);
        db = new DataBaseHelper(this);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        db.insertLien(result.getText());
                        Intent intent = new Intent(getApplicationContext(),CalendarJour.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }
}