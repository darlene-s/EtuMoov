package bdd.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import bdd.models.Utilisateur;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String log = "DataBaseHelper";
    private static final String dblocation = "faut trouver mettre le fichier";
    private static final String Utilisateur = "Utilisateur";
    private static final String Profil = "Profil";
    private static final String Navigation = "Navigation";
    private static final String Planning = "Planning";
    private Context mContext;
    private static final int db_version = 1;
    private static final String db_name = "BDDPPPJS4";
    private SQLiteDatabase myDatabase;

    public DataBaseHelper(Context context) {
        super(context, db_name, null, db_version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strQL1 = "CREATE TABLE IF NOT EXISTS " + Utilisateur
                + "("
                + "id_user INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nom Text,"
                + "prenom TEXT,"
                + "mail TEXT,"
                + "mdp TEXT,"
                + "UNIQUE(mail)"
                + ")";

        String strQL2 ="CREATE TABLE IF NOT EXISTS " + Profil
                + "(id_profil INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "tps_preparation INTEGER,"
                + "tps_suppl INTEGER,"
                + "id_user INTEGER,"
                + "FOREIGN KEY(id_user) REFERENCES utilisateur(id_user) ON DELETE CASCADE"
                +")";

        String strQL3 = "CREATE TABLE IF NOT EXISTS " + Navigation
                + "(id_nav INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "tps_trajet INTEGER,"
                + "domicile TEXT,"
                + "destination TEXT,"
                + "id_profil INTEGER,"
                + "FOREIGN KEY(id_profil) REFERENCES profil(id_profil) ON DELETE CASCADE"
                + ")";

        String strQL4 = "CREATE TABLE IF NOT EXISTS " + Planning
                + "(id_planning INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "jour DATE,"
                + "Horaire Date,"
                + "id_profil INTEGER,"
                + "FOREIGN KEY(id_profil) REFERENCES profil(id_profil) ON DELETE CASCADE"
                + ")";

        db.execSQL(strQL1);
        db.execSQL(strQL2);
        db.execSQL(strQL3);
        db.execSQL(strQL4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE Utilisateur");
        db.execSQL("DROP TABLE Profil");
        db.execSQL("DROP TABLE Navigation");
        db.execSQL("DROP TABLE Planning");
        onCreate(db);
    }

    public void insertUser(bdd.models.Utilisateur user){
        String strSQL = "insert into " + Utilisateur
                + "(nom, prenom, mail, mdp) VALUES ('"
                + user.getNom() + "','"
                + user.getPrenom() + "','"
                + user.getEmail() + "','"
                + user.getMdp() + "')"
                ;
        this.getWritableDatabase().execSQL(strSQL);
        closeDataBase();
    }
     public void insertProfil (bdd.models.Profil profil){
        String strSQL = "insert into " + Profil
                + "(tps_preparation, tps_suppl, id_user) VALUES ('"
                + profil.getTps_preparation() + "',"
                + profil.getTps_supplementaires() + "',"
                + profil.getId_user() + "')";
        this.getWritableDatabase().execSQL(strSQL);
        closeDataBase();
     }
     public void insertNavigation(bdd.models.Navigation navigation){
        String strSQL = "insert into " + Navigation
                + "(tps_trajet, domicile, destination, id_profil) VALUES ('"
                + navigation.getTps_trajet() + "',"
                + navigation.getDomicile() + "',"
                + navigation.getDestination() + "',"
                + navigation.getId_profil() + "')";
        this.getWritableDatabase().execSQL(strSQL);
        closeDataBase();
     }
     public void insertPlanning (bdd.models.Planning planning){
        String strSQL = "insert into " + Planning
                + "(jour, Horaire, id_profil) VALUES ('"
                + planning.getDate_jour() + "',"
                + planning.getHoraire() + "',"
                + planning.getId_profil() + "')";
        this.getWritableDatabase().execSQL(strSQL);
        closeDataBase();
     }

    public void openDataBase(){
        String dbpath = mContext.getDatabasePath(db_name).getPath();
        if (myDatabase != null && myDatabase.isOpen()){
            return;
        }
        myDatabase = SQLiteDatabase.openDatabase(dbpath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDataBase(){
        if (myDatabase!=null)
            myDatabase.close();
    }

    public List<bdd.models.Utilisateur> getListUsers(){
        bdd.models.Utilisateur user = null;
        List<bdd.models.Utilisateur> userlist = new ArrayList<>();
        String strSQL ="SELECT *FROM Utilisateur";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            user = new Utilisateur(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            userlist.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return userlist;
    }
}
