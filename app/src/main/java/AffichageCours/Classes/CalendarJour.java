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

import androidx.appcompat.app.AppCompatActivity;

import com.example.etumoov.IcsManager;
import com.example.etumoov.R;
import com.example.etumoov.ScannerActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;
import BD_Utilisateur.Models_Cours.Cours;
import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;

public class CalendarJour extends AppCompatActivity implements View.OnClickListener {

    private ImageView LeftArrow, RightArrow, btn_qr, btn_refresh;
    private int datecount = 0;
    private TextView DateView;
    private ListView listView;
    private CoursAdapter mAdapter;
    private ArrayList<Cours> coursList;
    private List<VEvent> event;
    private String lienIntent;
    private String result = "";
    private BufferedReader reader;
    private DataBaseHelper db;
    private IcsManager ics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
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