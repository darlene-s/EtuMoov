package jeu.memory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.example.etumoov.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Collections;

import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;
import BD_Utilisateur.Models_Utilisateur.Profil;

public class Memory extends AppCompatActivity {
    // déclaration des attributs
    private ImageView iv_11, iv_12, iv_13, iv_14, iv_21, iv_22, iv_23, iv_24;
    private Integer[] cardArray = {11, 12, 13, 14, 21, 22, 23, 24};
    private int image11, image12, image13, image14, image21, image22, image23, image24;
    private Chronometer chrono;
    DataBaseHelper bd;
    DatabaseReference ref;


    private int firstCard, secondCard, clickedFirst, clickedSecond;
    private int cardNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        bd = new DataBaseHelper(this);
        ref = FirebaseDatabase.getInstance().getReference("Utilisateur");

        //initialisation des images
        iv_11 = findViewById(R.id.iv_11);
        iv_12 = findViewById(R.id.iv_12);
        iv_13 = findViewById(R.id.iv_13);
        iv_14 = findViewById(R.id.iv_14);
        iv_21 = findViewById(R.id.iv_21);
        iv_22 = findViewById(R.id.iv_22);
        iv_23 = findViewById(R.id.iv_23);
        iv_24 = findViewById(R.id.iv_24);

        //attribut un tag à chaque image
        iv_11.setTag("0");
        iv_12.setTag("1");
        iv_13.setTag("2");
        iv_14.setTag("3");
        iv_21.setTag("4");
        iv_22.setTag("5");
        iv_23.setTag("6");
        iv_24.setTag("7");

        //appel de la fonction qui affiche le dos des cartes
        frontOfCardsRessources();

        Collections.shuffle(Arrays.asList(cardArray));

        chrono = (Chronometer) findViewById(R.id.chrono_memo);
        chrono.start();

        iv_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt((String) v.getTag());
                doStuff(iv_11, theCard);
            }
        });
        iv_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt((String) v.getTag());
                doStuff(iv_12, theCard);
            }
        });
        iv_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt((String) v.getTag());
                doStuff(iv_13, theCard);
            }
        });
        iv_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt((String) v.getTag());
                doStuff(iv_14, theCard);
            }
        });
        iv_21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt((String) v.getTag());
                doStuff(iv_21, theCard);
            }
        });
        iv_22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt((String) v.getTag());
                doStuff(iv_22, theCard);
            }
        });
        iv_23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt((String) v.getTag());
                doStuff(iv_23, theCard);
            }
        });
        iv_24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = Integer.parseInt((String) v.getTag());
                doStuff(iv_24, theCard);
            }
        });
    }

    // Verifie si deux cartes sont "identiques"
    private void doStuff(ImageView iv, int card) {
        if (cardArray[card] == 11) {
            iv.setImageResource(image11);
        } else if (cardArray[card] == 12) {
            iv.setImageResource(image12);
        } else if (cardArray[card] == 13) {
            iv.setImageResource(image13);
        } else if (cardArray[card] == 14) {
            iv.setImageResource(image14);
        } else if (cardArray[card] == 21) {
            iv.setImageResource(image21);
        } else if (cardArray[card] == 22) {
            iv.setImageResource(image22);
        } else if (cardArray[card] == 23) {
            iv.setImageResource(image23);
        } else if (cardArray[card] == 24) {
            iv.setImageResource(image24);
        }
        if (cardNumber == 1) {
            firstCard = cardArray[card];
            if (firstCard > 20) {
                firstCard = firstCard - 10;
            }
            cardNumber = 2;
            clickedFirst = card;
            iv.setEnabled(false);

        } else if (cardNumber == 2) {
            secondCard = cardArray[card];
            if (secondCard > 20) {
                secondCard = secondCard - 10;
            }
            cardNumber = 1;
            clickedSecond = card;

            iv_11.setEnabled(false);
            iv_12.setEnabled(false);
            iv_13.setEnabled(false);
            iv_14.setEnabled(false);
            iv_21.setEnabled(false);
            iv_22.setEnabled(false);
            iv_23.setEnabled(false);
            iv_24.setEnabled(false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    calculate();
                }
            }, 1000);
        }
    }

    private void calculate() {
        if (firstCard == secondCard) {
            if (clickedFirst == 0) {
                iv_11.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 1) {
                iv_12.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 2) {
                iv_13.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 3) {
                iv_14.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 4) {
                iv_21.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 5) {
                iv_22.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 6) {
                iv_23.setVisibility(View.INVISIBLE);
            } else if (clickedFirst == 7) {
                iv_24.setVisibility(View.INVISIBLE);
            }
            if (clickedSecond == 0) {
                iv_11.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 1) {
                iv_12.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 2) {
                iv_13.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 3) {
                iv_14.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 4) {
                iv_21.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 5) {
                iv_22.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 6) {
                iv_23.setVisibility(View.INVISIBLE);
            } else if (clickedSecond == 7) {
                iv_24.setVisibility(View.INVISIBLE);
            }

        } else {
            iv_11.setImageResource(R.drawable.ic_sphereviolet);
            iv_12.setImageResource(R.drawable.ic_sphereviolet);
            iv_13.setImageResource(R.drawable.ic_sphereviolet);
            iv_14.setImageResource(R.drawable.ic_sphereviolet);
            iv_21.setImageResource(R.drawable.ic_sphereviolet);
            iv_22.setImageResource(R.drawable.ic_sphereviolet);
            iv_23.setImageResource(R.drawable.ic_sphereviolet);
            iv_24.setImageResource(R.drawable.ic_sphereviolet);
        }

        iv_11.setEnabled(true);
        iv_12.setEnabled(true);
        iv_13.setEnabled(true);
        iv_14.setEnabled(true);
        iv_21.setEnabled(true);
        iv_22.setEnabled(true);
        iv_23.setEnabled(true);
        iv_24.setEnabled(true);
        FinDeJeu();
    }

    private void frontOfCardsRessources() {
        image11 = R.drawable.ic_cafe;
        image12 = R.drawable.ic_planet;
        image13 = R.drawable.ic_livre;
        image14 = R.drawable.ic_cloud;
        image21 = R.drawable.ic_cafe;
        image22 = R.drawable.ic_planet;
        image23 = R.drawable.ic_livre;
        image24 = R.drawable.ic_cloud;
    }

    // Vérifie s'il n'y a plus d'images visible sur la page
    private void FinDeJeu() {
        if (iv_11.getVisibility() == View.INVISIBLE && iv_12.getVisibility() == View.INVISIBLE &&
                iv_13.getVisibility() == View.INVISIBLE && iv_14.getVisibility() == View.INVISIBLE &&
                iv_21.getVisibility() == View.INVISIBLE && iv_22.getVisibility() == View.INVISIBLE &&
                iv_23.getVisibility() == View.INVISIBLE && iv_24.getVisibility() == View.INVISIBLE) {
            chrono.stop();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Memory.this);
            alertDialogBuilder
                    .setMessage("Vous avez pris " + chrono.getText() + "sec")
                    .setCancelable(false)
                    .setPositiveButton("QUITTER", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            Profil profil = bd.getProfils();
            int j = profil.getTimerCookie().compareTo(String.valueOf(chrono.getText()));
            if (j <= 0)
                bd.updateMemory(profil.getId_profil(), String.valueOf(chrono.getText()));
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
           // stopService(new Intent(this, AlarmService.class));
        }
       // ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("timerMemor").setValue(chrono.getText());
    }
}