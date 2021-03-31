package jeu.clicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.etumoov.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;
import BD_Utilisateur.Models_Utilisateur.Profil;

public class ClickerActivity extends AppCompatActivity {

    TextView textscore;
    ImageView tornado, hotsun, smilingsun, thunderstorm, smilingmoon, twoclouds;
    ImageView[] IMGS = {tornado, hotsun, smilingsun, thunderstorm, smilingmoon, twoclouds};
    int score = 0;
    Chronometer timer;
    DataBaseHelper db;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicker);
        ref = FirebaseDatabase.getInstance().getReference("Utilisateur");
        db = new DataBaseHelper(this);
        timer = (Chronometer) findViewById(R.id.chrono_clicker);
        timer.start();

        textscore = findViewById(R.id.textscore);
        tornado = findViewById(R.id.img1);
        hotsun = findViewById(R.id.img2);
        smilingsun = findViewById(R.id.img3);
        thunderstorm = findViewById(R.id.img4);
        smilingmoon = findViewById(R.id.img5);
        twoclouds = findViewById(R.id.img6);

        IMGS[0] = tornado;
        IMGS[1] = hotsun;
        IMGS[2] = smilingsun;
        IMGS[3] = thunderstorm;
        IMGS[4] = smilingmoon;
        IMGS[5] = twoclouds;

        for(int i = 0; i <= 5; i++) {
            IMGS[i].setVisibility(View.INVISIBLE);
        }
        int i = random();
        IMGS[i].setVisibility(View.VISIBLE);
        IMGS[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMGS[i].setVisibility(View.INVISIBLE);
                //score++;
                textscore.setText("Score : 0" + score);
                newImage(IMGS);
            }
        });
    }

    private int random() {
        Random r = new Random();
        int i = r.nextInt(5 - 0);
        return i;
    }

    private void newImage(ImageView[] t){
        if(score < 10){
            int j = random();
            t[j].setVisibility(View.VISIBLE);
            t[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    t[j].setVisibility(View.INVISIBLE);
                    score++;
                    textscore.setText("Score : " + score);
                    newImage(IMGS);
                }
            });
        } else {
            timer.stop();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ClickerActivity.this);
            alertDialogBuilder
                    .setMessage("Vous avez pris : " + timer.getText()  + " sec")
                    .setCancelable(false)
                    .setPositiveButton("QUITTER", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {finish();}
                    });
            Profil profil = db.getProfils();
            int j = profil.getTimerCookie().compareTo(String.valueOf(timer.getText()));
            if (j <= 0)
                db.updateCookie(profil.getId_profil(), String.valueOf(timer.getText()));
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
           // stopService(new Intent(this, AlarmService.class));
        }
        db.close();
      //  ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("timerClicker").setValue(timer.getText());
    }
}