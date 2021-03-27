package com.example.etumoov.NavigationMap.NaviMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etumoov.NavigationMap.NaviBD.Universite;
import com.example.etumoov.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TravelTime extends AppCompatActivity {
    private DatabaseReference reference;
    private ListView UniversiteList;
    private ArrayList<String> UnivArray = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_time);
        UniversiteList = (ListView)findViewById(R.id.etablissements);
        ArrayAdapter<String> UnivArrayAdapter = new ArrayAdapter<String>(TravelTime.this, android.R.layout.simple_list_item_1,
                UnivArray);
        reference = FirebaseDatabase.getInstance().getReference("Universite");
        reference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Universite universite = dataSnapshot.getValue(Universite.class);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Toast.makeText(TravelTime.this, "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
            }
        });
    }

}



