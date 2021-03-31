package com.example.etumoov.NavigationMap.NaviMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etumoov.NavigationMap.NaviBD.Universite;
import com.example.etumoov.Profil.ProfilActivity;
import com.example.etumoov.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TravelTime extends AppCompatActivity {
    private DatabaseReference reference;
    private ListView UniversiteList;
    private ArrayList<String> UnivArray = new ArrayList<>();
    private EditText numero, rue, codePostal, numero2, rue2, codePostal2;
    private Button btn_valide, btn_loc;
    private APIGoogleDistance api;
    private TextView duree;
    private long sec, minutes, hours, days;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double longitude, latitude;
    private boolean choix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_time);
        numero = findViewById(R.id.txt_edit_numero);
        rue = findViewById(R.id.txt_edit_rue);
        codePostal = findViewById(R.id.txt_edit_codePostal);
        numero2 = findViewById(R.id.txt_edit_numero2);
        rue2 = findViewById(R.id.txt_edit_rue2);
        codePostal2 = findViewById(R.id.txt_edit_codePostal2);
        btn_valide = findViewById(R.id.button_valider);
        duree = findViewById(R.id.txt_view_trajet_tps);
        btn_loc = findViewById(R.id.btn_localisation);
        api = new APIGoogleDistance();
        choix = false;
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("cle_id", ProfilActivity.MODE_PRIVATE);
        String cle_id = prefs.getString("cle_id_recup", "");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        ArrayAdapter<String> UnivArrayAdapter = new ArrayAdapter<String>(TravelTime.this, android.R.layout.simple_list_item_1,
                UnivArray);
        reference = FirebaseDatabase.getInstance().getReference("Universite");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Universite universite = dataSnapshot.getValue(Universite.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TravelTime.this, "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
            }
        });

        btn_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(TravelTime.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(TravelTime.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });

        btn_valide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res = null;
                if(choix)
                    res = api.getTravelTime(numero.getText() + " " + rue.getText() + "," + codePostal.getText(), numero2.getText() + " " + rue2.getText() + "," + codePostal2.getText(),0,0);
                else res = api.getTravelTime(numero.getText() + " " + rue.getText() + "," + codePostal.getText(), numero2.getText() + " " + rue2.getText() + "," + codePostal2.getText(),longitude,latitude);
                if(!(res == ""))
                    calculateTime(Long.parseLong(res));
                else Toast.makeText(TravelTime.this, "impossible de trouver un chemin correct", Toast.LENGTH_SHORT).show();
                duree.setText(hours + " heures " + minutes + " minutes");
            }
        });
    }

    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(TravelTime.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(TravelTime.this,Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        Toast.makeText(TravelTime.this,"Adresse trouver !", Toast.LENGTH_SHORT).show();
                        numero.setFocusable(false);
                        rue.setFocusable(false);
                        codePostal.setFocusable(false);
                        choix = true;
                        longitude = addressList.get(0).getLongitude();
                        latitude = addressList.get(0).getLatitude();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void calculateTime(long seconds) {
        sec = seconds % 60;
        minutes = seconds % 3600 / 60;
        hours = seconds % 86400 / 3600;
    }
}



