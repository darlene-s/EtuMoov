package AffichageCours.Rappels;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.etumoov.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;

public class Rappels_Affichage extends AppCompatActivity {
    private List<Rappel> ListeRappels;
    private DataBaseHelper db;
    private ImageView btn_add;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rappel_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tableLayout = findViewById(R.id.Affichage_Rappel);
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent AddRappelActivity= new Intent(getApplicationContext(), AjoutRappel.class);
                startActivity(AddRappelActivity);
                finish();
            }
        });
        db = new DataBaseHelper(this);
        ListeRappels = db.getRappels();
        Collections.sort(ListeRappels, new Comparator<Rappel>() {
            @Override
            public int compare(Rappel o1, Rappel o2) {
                return o1.getDate().compareTo(o2.getDate());

            }
        });

        for (int i = 0; i < ListeRappels.size(); ++i) {
                // Paramètres de design des lignes du layout
                TableRow row = new TableRow(this);
                TableLayout.LayoutParams p = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT);
                p.setMargins(0, 50, 0, 0);
                row.setPadding(100, 100, 0, 100);
                row.setLayoutParams(p);
                row.setBackgroundColor(Color.parseColor("#FFFFFFFF"));

                //Paramètres de design des textes présents dans une ligne
                TextView textTitre = new TextView(getApplicationContext());
                textTitre.setText(ListeRappels.get(i).getTitre());
                textTitre.setTextColor(Color.parseColor("#FF000000"));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                params.setMargins(50, 0, 0 , 0);
                textTitre.setLayoutParams(params);
                textTitre.setTextSize(15);

                //Paramètres
                TextView textDate= new TextView(getApplicationContext());
                textDate.setText(ListeRappels.get(i).getDate());
                textDate.setTextSize(15);
                textDate.setTextColor(Color.parseColor("#FF000000"));
                row.addView(textTitre);
                row.addView(textDate);
                tableLayout.addView(row);

                int index = i;
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage(ListeRappels.get(index).getDescription());
                        builder.setTitle("Description de votre rappel");
                        builder.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setNegativeButton("Supprimer le rappel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.SuppRappel(ListeRappels.get(index).getTitre());
                                dialog.cancel();
                                finish();
                                startActivity(getIntent());
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
        }
        db.close();
    }

}