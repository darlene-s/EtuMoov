package AffichageCours.Classes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appmobilev2.DataBase.DataBaseManager;
import com.example.appmobilev2.QRCode.QrCodeActivity;
import com.example.appmobilev2.R;

import java.io.BufferedReader;
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

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;

public class CalendarJour extends AppCompatActivity implements View.OnClickListener {

    private ImageView LeftArrow, RightArrow, btn_qr, btn_retour;
    private int datecount = 0;
    private TextView DateView;
    private ListView listView;
    private CoursAdapter mAdapter;
    private ArrayList<Cours> coursList;
    private List<VEvent> event;
    private String lienIntent;
    private String result = "";
    private BufferedReader reader;
    private DataBaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_calendar_jour);

        this.findViewByID();
        db = new DataBaseManager(this);

        LeftArrow.setOnClickListener(this::onClick);
        RightArrow.setOnClickListener(this::onClick);
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("LienEDT")) {
            db.delete();
            lienIntent = intent.getStringExtra("LienEDT");
            db.insertLien(lienIntent);
            lireLien();
            createBD();
        }
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String todayDate = dateFormat.format(today);
        DateView.setText(todayDate);

        // JEU DE DONNEES POUR TEST LE BON FONCTIONNEMENT DE L'AFFICHAGE DES COURS
        coursList = new ArrayList<>();
        if(!db.isEmpty())
            createList();
        mAdapter = new CoursAdapter(this, getCoursDate(coursList, getSelectedDate(datecount)));
        listView.setAdapter(mAdapter);
        db.close();

        btn_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edtActivity = new Intent(getApplicationContext(), QrCodeActivity.class);
                startActivity(edtActivity);
                finish();
            }
        });
        btn_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(getApplicationContext(), com.example.appmobilev2.MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });
    }

    public void findViewByID() {
        LeftArrow = findViewById(R.id.LeftArrow);
        RightArrow = findViewById(R.id.RightArrow);
        DateView = findViewById(R.id.textViewDate);
        btn_qr = findViewById(R.id.button_qr);
        btn_retour = findViewById(R.id.btn_retour);
        listView = (ListView) findViewById(R.id.cours_list);
    }

    public void lireLien(){
        try {
            URL url = new URL(lienIntent);
            URLConnection conn = url.openConnection();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                result += inputLine + "\r\n";
            }
            List<ICalendar> ical = Biweekly.parse(result).all();
            event = ical.get(0).getEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public String createDate(int i) {
        String jour ="", mois ="", annee = "";
            if(event.get(i).getDateStart().getValue().getDate() < 10) {
                jour = "0";
                jour += String.valueOf(event.get(i).getDateStart().getValue().getDate());
            } else jour = String.valueOf(event.get(i).getDateStart().getValue().getDate());
            if(event.get(i).getDateStart().getValue().getMonth() < 10) {
                mois = "0";
                mois += String.valueOf(event.get(i).getDateStart().getValue().getMonth() + 1);
            }
            else mois = String.valueOf(event.get(i).getDateStart().getValue().getMonth());
            annee = String.valueOf(event.get(i).getDateStart().getValue().getYear() + 1900);
            return jour +"/" + mois + "/" + annee;
    }

    public String createHeureDebut(int i) {
        String heure ="", minutes ="";
        if(event.get(i).getDateStart().getValue().getHours() < 10) {
            heure = "0";
            heure += String.valueOf(event.get(i).getDateStart().getValue().getHours());
        } else heure = String.valueOf(event.get(i).getDateStart().getValue().getHours());
        if(event.get(i).getDateStart().getValue().getMinutes() < 10) {
            minutes = "0";
            minutes += String.valueOf(event.get(i).getDateStart().getValue().getMinutes());
        } else minutes = String.valueOf(event.get(i).getDateStart().getValue().getMinutes());
        return heure + ":" + minutes;
    }

    public String createHeureFin(int i) {
        String heure ="", minutes ="";
        if(event.get(i).getDateEnd().getValue().getHours() < 10) {
            heure = "0";
            heure += String.valueOf(event.get(i).getDateEnd().getValue().getHours());
        } else heure = String.valueOf(event.get(i).getDateEnd().getValue().getHours());
        if(event.get(i).getDateEnd().getValue().getMinutes() < 10) {
            minutes = "0";
            minutes += String.valueOf(event.get(i).getDateEnd().getValue().getMinutes());
        } else minutes = String.valueOf(event.get(i).getDateEnd().getValue().getMinutes());
        return heure + ":" + minutes;
    }

    public void createList() {
            //coursList.add(new com.example.appmobilev2.Classes.Cours("IDK",event.get(i).getSummary().getValue(),event.get(i).getLocation().getValue(),createHeureDebut(i),createHeureFin(i),createDate(i)));
            coursList = db.getCours();
    }

    public void createBD() {
        for(int i = 0; i < event.size();i++) {
            db.insertCours("IDK",event.get(i).getSummary().getValue(),event.get(i).getLocation().getValue(),createHeureDebut(i),createHeureFin(i),createDate(i));
        }
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