package AffichageCours.Rappels;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appmobilev2.DataBase.DataBaseManager;
import com.example.appmobilev2.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AjoutRappel extends AppCompatActivity {

    private EditText titre, description, date;
    private Button button;
    private DataBaseManager db;
    private String vTitre, vDescription, vDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_rappel);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        db = new DataBaseManager(this);
        titre = findViewById(R.id.reg_title);
        description = findViewById(R.id.reg_descrip);
        date = findViewById(R.id.reg_date);
        button = findViewById(R.id.btn_ajout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vTitre = titre.getText().toString();
                vDescription = description.getText().toString();
                vDate = date.getText().toString();
                if(chechValid(vDate)) {
                    db.insertRappel(vTitre, vDescription, vDate);
                    db.close();
                    Intent RappelsActivity= new Intent(getApplicationContext(), Rappels_Affichage.class);
                    startActivity(RappelsActivity);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "La date rentrée n'est pas au bon format", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean chechValid(String date){
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.isLenient();
        try {
            format.parse(date);
        } catch (ParseException e){
            return false;
        }
        return true;
    }
}