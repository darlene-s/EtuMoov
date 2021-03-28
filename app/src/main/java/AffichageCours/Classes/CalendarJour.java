package AffichageCours.Classes;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import AffichageCours.Scanner.IcsManager;
import com.example.etumoov.R;
import AffichageCours.Scanner.ScannerActivity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;
import BD_Utilisateur.Models_Cours.Cours;

public class CalendarJour extends AppCompatActivity implements View.OnClickListener {

    private ImageView LeftArrow, RightArrow, btn_qr, btn_refresh;
    private int datecount = 0;
    private TextView DateView;
    private ListView listView;
    private CoursAdapter mAdapter;
    private ArrayList<Cours> coursList;
    private DataBaseHelper db;
    private IcsManager ics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        //getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_calendar_jour);
        findViewByID();

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String todayDate = dateFormat.format(today);
        DateView.setText(todayDate);

        db = new DataBaseHelper(this);
        coursList = new ArrayList<>();

        createListView();

        LeftArrow.setOnClickListener(this::onClick);
        RightArrow.setOnClickListener(this::onClick);

        db.close();

        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edtActivity = new Intent(getApplicationContext(), ScannerActivity.class);
                startActivity(edtActivity);
                finish();
            }
        });
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createListView();
                Toast.makeText(getApplicationContext(), "Mise à jour réussie", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createListView() {
        if(!db.isLienEmpty()) {
            try {
                db.deleteCours();
                ics = new IcsManager(db.getLien(), this.getApplicationContext());
                createList();
                mAdapter = new CoursAdapter(this, getCoursDate(coursList, getSelectedDate(datecount)));
                listView.setAdapter(mAdapter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void findViewByID() {
        LeftArrow = findViewById(R.id.LeftArrow);
        RightArrow = findViewById(R.id.RightArrow);
        DateView = findViewById(R.id.textViewDate);
        btn_qr = findViewById(R.id.button_qr);
        btn_refresh = findViewById(R.id.btn_refresh);
        listView = (ListView) findViewById(R.id.cours_list);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.LeftArrow:
                datecount --;
                mAdapter = new CoursAdapter(this, getCoursDate(coursList, getSelectedDate(datecount)));
                listView.setAdapter(mAdapter);
                break;
            case R.id.RightArrow:
                datecount ++;
                mAdapter = new CoursAdapter(this, getCoursDate(coursList, getSelectedDate(datecount)));
                listView.setAdapter(mAdapter);
                break;
        }
    }

    public String getSelectedDate(int count){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, count);
        Date demain = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String demainDate = dateFormat.format(demain);
        DateView.setText(demainDate);
        return demainDate;
    }

    public void createList() {
        coursList = db.getCours();
    }

    public ArrayList<Cours> getCoursDate(ArrayList<Cours> list, String SelectedDate){
        ArrayList<Cours> listeCoursDay = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            if (SelectedDate.contentEquals(list.get(i).getDateCours())) {
                listeCoursDay.add(list.get(i));
            }
        }

        Collections.sort(listeCoursDay, new Comparator<Cours>() {
            @Override
            public int compare(Cours o1, Cours o2) {
                return o1.getDebutCours().compareTo(o2.getDebutCours());

            }
        });
        return listeCoursDay;
    }

}