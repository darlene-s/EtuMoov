package bdd.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bdd.models.Navigation;
import bdd.models.Planning;
import bdd.models.Profil;
import bdd.models.Utilisateur;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String log = "DataBaseHelper";
    private static final String T_Utilisateur = "Utilisateur";
    private static final String T_Profil = "Profil";
    private static final String T_Navigation = "Navigation";
    private static final String T_Planning = "Planning";
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
        String strQL1 = "CREATE TABLE IF NOT EXISTS " + T_Utilisateur
                + "("
                + "id_user INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nom Text,"
                + "prenom TEXT,"
                + "mail TEXT,"
                + "mdp TEXT,"
                + "UNIQUE(mail)"
                + ")";

        String strQL2 ="CREATE TABLE IF NOT EXISTS " + T_Profil
                + "(id_profil INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "tps_preparation INTEGER,"
                + "tps_suppl INTEGER,"
                + "id_user INTEGER,"
                + "FOREIGN KEY(id_user) REFERENCES utilisateur(id_user) ON DELETE CASCADE"
                +")";

        String strQL3 = "CREATE TABLE IF NOT EXISTS " + T_Navigation
                + "(id_nav INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "tps_trajet INTEGER,"
                + "domicile TEXT,"
                + "destination TEXT,"
                + "id_profil INTEGER,"
                + "FOREIGN KEY(id_profil) REFERENCES profil(id_profil) ON DELETE CASCADE"
                + ")";

        String strQL4 = "CREATE TABLE IF NOT EXISTS " + T_Planning
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
    public void deleteDB(){
        this.getWritableDatabase().execSQL("DROP TABLE Utilisateur");
        this.getWritableDatabase().execSQL("DROP TABLE Profil");
        this.getWritableDatabase().execSQL("DROP TABLE Navigation");
        this.getWritableDatabase().execSQL("DROP TABLE Planning");
    }

    public void insertUser(Utilisateur user){
        try {
            String strSQL = "insert into " + T_Utilisateur
                    + "(nom, prenom, mail, mdp) VALUES ('"
                    + user.getNom() + "','"
                    + user.getPrenom() + "','"
                    + user.getEmail() + "','"
                    + user.getMdp() + "')"
                    ;
            this.getWritableDatabase().execSQL(strSQL);
        } catch(Exception e){
            Toast.makeText(mContext, "Erreur dans l'ajout des informations utilisateur dans la base de données", Toast.LENGTH_LONG);
        }
    }
     public void insertProfil (Profil profil){
         try {
             String strSQL = "insert into " + T_Profil
                     + "(tps_preparation, tps_suppl, id_user) VALUES ('"
                     + profil.getTps_preparation() + "',"
                     + profil.getTps_supplementaires() + "',"
                     + profil.getId_user() + "')";
             this.getWritableDatabase().execSQL(strSQL);
         } catch(Exception e){
             Toast.makeText(mContext, "Erreur dans l'ajout des informations pour le profil dans la base de données", Toast.LENGTH_LONG);
         }
     }
     public void insertNavigation(Navigation navigation){
         try {
             String strSQL = "insert into " + T_Navigation
                     + "(tps_trajet, domicile, destination, id_profil) VALUES ('"
                     + navigation.getTps_trajet() + "',"
                     + navigation.getDomicile() + "',"
                     + navigation.getDestination() + "',"
                     + navigation.getId_profil() + "')";
             this.getWritableDatabase().execSQL(strSQL);
         } catch(Exception e){
             Toast.makeText(mContext, "Erreur dans l'ajout des informations de navigation dans la base de données", Toast.LENGTH_LONG);
         }
     }
     public void insertPlanning (Planning planning){
         try {
             String strSQL = "insert into " + T_Planning
                     + "(jour, Horaire, id_profil) VALUES ('"
                     + planning.getDate_jour() + "',"
                     + planning.getHoraire() + "',"
                     + planning.getId_profil() + "')";
             this.getWritableDatabase().execSQL(strSQL);
         } catch(Exception e){
             Toast.makeText(mContext, "Erreur dans l'ajout des informations du planning dans la base de données", Toast.LENGTH_LONG);
         }
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

    public List<Utilisateur> getListUsers(){
        Utilisateur user = null;
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

    public Utilisateur getUtilisateur(int id) {
        try {
            String strSQL = "SELECT *FROM Utilisateur where id_user =" + id;
            Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                do {
                    return new Utilisateur(cursor.getInt(cursor.getColumnIndex("id_user")), cursor.getString(cursor.getColumnIndex("nom")), cursor.getString(cursor.getColumnIndex("prenom")), cursor.getString(cursor.getColumnIndex("mail")), cursor.getString(cursor.getColumnIndex("mdp")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(mContext, "Erreur dans la récupération des infos de l'utilisateur", Toast.LENGTH_LONG);
        }
        return null;
    }

    public Profil getProfil(Utilisateur user){
        try {
            String strSQL = "SELECT *FROM Profil WHERE id_user =" + user.getId_user();
            Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                do {
                    return new Profil(cursor.getInt(cursor.getColumnIndex("id_profil")), cursor.getDouble(cursor.getColumnIndex("tps_preparation")), cursor.getDouble(cursor.getColumnIndex("tps_suppl")), cursor.getInt(cursor.getColumnIndex("id_user")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch(Exception e){
            Toast.makeText(mContext, "Erreur dans la récupération du profil", Toast.LENGTH_LONG);
        }
        return null;
    }

    public Navigation getNavigation(Profil profil){
        try {
            String strSQL = "SELECT *FROM Navigation where id_profil =" + profil.getId_profil();
            Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                do {
                    return new Navigation(cursor.getInt(cursor.getColumnIndex("id_nav")), cursor.getDouble(cursor.getColumnIndex("tps_preparation")), cursor.getString(cursor.getColumnIndex("domicile")), cursor.getString(cursor.getColumnIndex("destination")), cursor.getInt(cursor.getColumnIndex("id_profil")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch(Exception e){
            Toast.makeText(mContext, "Erreur dans la récupération des infos de la navigation", Toast.LENGTH_LONG);
        }
        return null;
    }

    public Planning getPlanning(Profil profil){
        try {
            String strSQL = "SELECT *FROM Planning WHERE id_profil="+profil.getId_profil();
            Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                do {
                    return new Planning(cursor.getInt(cursor.getColumnIndex("id_planning")), cursor.getString(cursor.getColumnIndex("jour")), cursor.getString(cursor.getColumnIndex("Horaire")), cursor.getInt(cursor.getColumnIndex("id_profil")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch(Exception e){
            Toast.makeText(mContext, "Erreur lors de la récupération des infos du navigation.", Toast.LENGTH_LONG);
        }
        return null;
    }
}
