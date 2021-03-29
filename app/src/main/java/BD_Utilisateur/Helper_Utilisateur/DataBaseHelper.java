package BD_Utilisateur.Helper_Utilisateur;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import AffichageCours.Rappels.Rappel;
import BD_Utilisateur.Models_Cours.Cours;
import BD_Utilisateur.Models_Utilisateur.Navigation;
import BD_Utilisateur.Models_Utilisateur.Planning;
import BD_Utilisateur.Models_Utilisateur.Profil;
import BD_Utilisateur.Models_Utilisateur.Utilisateur;

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
    /*
    Partie de la classe qui correspond à la gestion de la base de données dans son ensemble
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strSQL1 = "CREATE TABLE IF NOT EXISTS " + T_Utilisateur
                + "("
                + "id_user INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nom Text,"
                + "prenom TEXT,"
                + "mail TEXT,"
                + "mdp TEXT,"
                + "UNIQUE(mail)"
                + ")";

        String strSQL2 = "CREATE TABLE IF NOT EXISTS " + T_Profil
                + "(id_profil INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "tps_preparation INTEGER,"
                + "tps_suppl INTEGER,"
                + "score INTEGER,"
                + "timerMemory  TEXT,"
                + "timerCookie TEXT,"
                + "id_user INTEGER,"
                + "FOREIGN KEY(id_user) REFERENCES utilisateur(id_user) ON DELETE CASCADE"
                + ")";

        String strSQL3 = "CREATE TABLE IF NOT EXISTS " + T_Navigation
                + "(id_nav INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "tps_trajet INTEGER,"
                + "domicile TEXT,"
                + "destination TEXT,"
                + "id_profil INTEGER,"
                + "FOREIGN KEY(id_profil) REFERENCES profil(id_profil) ON DELETE CASCADE"
                + ")";

        String strSQL4 = "CREATE TABLE IF NOT EXISTS " + T_Planning
                + "(id_planning INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "jour DATE,"
                + "Horaire Date,"
                + "id_profil INTEGER,"
                + "FOREIGN KEY(id_profil) REFERENCES profil(id_profil) ON DELETE CASCADE"
                + ")";

        String strSQL5 = "CREATE TABLE T_Cours ("
                + "   idCours integer primary key autoincrement,"
                + "   prof String,"
                + "   nomCours String,"
                + "   location String,"
                + "   heureDebutCours String,"
                + "   heureFinCours String,"
                + "   dateCours String"
                + ")";
        String strSQL6 = "CREATE TABLE T_Lien ("
                + "   idLien integer primary key autoincrement,"
                + "   lien String"
                + ")";
        String strSQL7 = "CREATE TABLE T_Rappel ("
                + "   idRappel integer primary key autoincrement,"
                + "   Titre String,"
                + "   Description String,"
                + "   Date String"
                + ")";

        db.execSQL(strSQL1);
        db.execSQL(strSQL2);
        db.execSQL(strSQL3);
        db.execSQL(strSQL4);
        db.execSQL(strSQL5);
        db.execSQL(strSQL6);
        db.execSQL(strSQL7);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE Utilisateur");
        db.execSQL("DROP TABLE Profil");
        db.execSQL("DROP TABLE Navigation");
        db.execSQL("DROP TABLE Planning");
        db.execSQL("DROP TABLE T_Cours");
        db.execSQL("DROP TABLE T_Lien");
        db.execSQL("DROP TABLE T_Rappel");
        onCreate(db);
    }

    public void deleteBD() {
        this.getWritableDatabase().execSQL("DELETE FROM Utilisateur");
        this.getWritableDatabase().execSQL("DELETE FROM Profil");
        this.getWritableDatabase().execSQL("DELETE FROM Navigation");
        this.getWritableDatabase().execSQL("DELETE FROM Planning");
        this.getWritableDatabase().execSQL("DELETE FROM T_Cours");
        this.getWritableDatabase().execSQL("DELETE FROM T_Lien");
        this.getWritableDatabase().execSQL("DELETE FROM T_Rappel");
    }

    public void deleteCours() {
        this.getWritableDatabase().execSQL("DELETE FROM T_Cours");
    }
    public void deleteDataUser(){
        this.getWritableDatabase().execSQL("DELETE FROM Utilisateur");
        this.getWritableDatabase().execSQL("DELETE FROM Profil");
        this.getWritableDatabase().execSQL("DELETE FROM Navigation");
    }

    /*
    * Partie permettant de pouvoir vider complètement la table contenant les cours
    * lors d'une réinitialisation du lien
     */
    public boolean ifCoursEmpty(){
        String strSQL = "SELECT * FROM T_Cours";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
        cursor.moveToFirst();
        if(cursor.getCount() == 0 || cursor == null){
            return true;
        }
        else {
            return false;
        }
    }

    public void viderCours(){
        if(!ifCoursEmpty())
            this.getWritableDatabase().execSQL("DELETE FROM T_Cours");
    }

    public void openDataBase() {
        String dbpath = mContext.getDatabasePath(db_name).getPath();
        if (myDatabase != null && myDatabase.isOpen()) {
            return;
        }
        myDatabase = SQLiteDatabase.openDatabase(dbpath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDataBase() {
        if (myDatabase != null)
            myDatabase.close();
    }

    /*
    Partie concernant l'insertion des données dans la base de données
     */
    public void insertUser(Utilisateur user) {
        try {
            String strSQL = "insert into " + T_Utilisateur
                    + "(nom, prenom, mail, mdp) VALUES ('"
                    + user.getNom() + "','"
                    + user.getPrenom() + "','"
                    + user.getEmail() + "','"
                    + user.getMdp() + "')";
            this.getWritableDatabase().execSQL(strSQL);
        } catch (Exception e) {
            Toast.makeText(mContext, "Erreur dans l'ajout des informations utilisateur dans la base de données", Toast.LENGTH_LONG);
        }
    }

    public void insertProfil(Profil profil) {
        try {
            String strSQL = "insert into " + T_Profil
                    + "(tps_preparation, tps_suppl, score, timerMemory, timerCookie, id_user) VALUES ("
                    + profil.getTps_preparation() + ","
                    + profil.getTps_supplementaires() + ","
                    + profil.getScore() + ",'"
                    + profil.getTimerMemory() + "','"
                    + profil.getTimerCookie() + "',"
                    + profil.getId_user() + ")";
            this.getWritableDatabase().execSQL(strSQL);
        } catch (Exception e) {
            Toast.makeText(mContext, "Erreur dans l'ajout des informations pour le profil dans la base de données", Toast.LENGTH_LONG);
        }
    }

    public void insertNavigation(Navigation navigation) {
        try {
            String strSQL = "insert into " + T_Navigation
                    + "(tps_trajet, domicile, destination, id_profil) VALUES ('"
                    + navigation.getTps_trajet() + "',"
                    + navigation.getDomicile() + "',"
                    + navigation.getDestination() + "',"
                    + navigation.getId_profil() + "')";
            this.getWritableDatabase().execSQL(strSQL);
        } catch (Exception e) {
            Toast.makeText(mContext, "Erreur dans l'ajout des informations de navigation dans la base de données", Toast.LENGTH_LONG);
        }
    }

    public void insertPlanning(Planning planning) {
        try {
            String strSQL = "insert into " + T_Planning
                    + "(jour, Horaire, id_profil) VALUES ('"
                    + planning.getDate_jour() + "',"
                    + planning.getHoraire() + "',"
                    + planning.getId_profil() + "')";
            this.getWritableDatabase().execSQL(strSQL);
        } catch (Exception e) {
            Toast.makeText(mContext, "Erreur dans l'ajout des informations du planning dans la base de données", Toast.LENGTH_LONG);
        }
    }

    public void insertCours(String prof, String nom, String loc, String heureDeb, String heureFin, String date) {
        nom = nom.replace("'", "''");
        String strSql = "insert into T_Cours (prof, nomCours,location,heureDebutCours,heureFinCours,dateCours) VALUES ('"
                + prof + "', '" + nom + "', '" + loc + "', '" + heureDeb + "', '" + heureFin + "', '" + date + "')";
        this.getWritableDatabase().execSQL(strSql);
    }

    public void insertLien(String lien) {
        this.getWritableDatabase().execSQL("DELETE FROM T_LIEN");
        String strSql = "insert into T_Lien (lien) VALUES ('" + lien + "')";
        this.getWritableDatabase().execSQL(strSql);
    }

    public void insertRappel(String titre, String description, String date) {
        String strSql = "insert into T_Rappel (Titre, Description, Date) VALUES ('" + titre + "','" + description + "','" + date + "')";
        this.getWritableDatabase().execSQL(strSql);
    }

    /*
    Partie concernant la récupération d'une ligne spécifique d'une table de la BD
     */
    public Utilisateur getUtilisateurbyId(int id) {
        try {
            String strSQL = "SELECT *FROM Utilisateur where id_user =" +id;
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
    public Utilisateur getUtilisateurbyEmail(String email) {
        try {
            String strSQL = "SELECT *FROM Utilisateur where mail  = '" + email +"'";
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

    public Profil getProfil(int id) {
        try {
            String strSQL = "SELECT *FROM Profil WHERE id_user =" + id;
            Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                do {
                    return new Profil(cursor.getInt(cursor.getColumnIndex("id_profil")), cursor.getInt(cursor.getColumnIndex("tps_preparation")), cursor.getInt(cursor.getColumnIndex("tps_suppl")), cursor.getInt(cursor.getColumnIndex("score")), cursor.getString(cursor.getColumnIndex("timerMemory")), cursor.getString(cursor.getColumnIndex("timerCookie")),  cursor.getInt(cursor.getColumnIndex("id_user")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(mContext, "Erreur dans la récupération du profil", Toast.LENGTH_LONG);
        }
        return null;
    }

    public Navigation getNavigation(int id) {
        try {
            String strSQL = "SELECT *FROM Navigation where id_nav =" + id;
            Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                do {
                    return new Navigation(cursor.getInt(cursor.getColumnIndex("id_nav")), cursor.getDouble(cursor.getColumnIndex("tps_preparation")), cursor.getString(cursor.getColumnIndex("domicile")), cursor.getString(cursor.getColumnIndex("destination")), cursor.getInt(cursor.getColumnIndex("id_profil")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(mContext, "Erreur dans la récupération des infos de la navigation", Toast.LENGTH_LONG);
        }
        return null;
    }

    public Planning getPlanning(int id) {
        try {
            String strSQL = "SELECT *FROM Planning WHERE id_planning=" + id;
            Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                do {
                    return new Planning(cursor.getInt(cursor.getColumnIndex("id_planning")), cursor.getString(cursor.getColumnIndex("jour")), cursor.getString(cursor.getColumnIndex("Horaire")), cursor.getInt(cursor.getColumnIndex("id_profil")));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(mContext, "Erreur lors de la récupération des infos du navigation.", Toast.LENGTH_LONG);
        }
        return null;
    }

    /*
    Récupération globale de données de l'utilisateur de l'appli
     */
    public List<Utilisateur> getListUsers() {
        Utilisateur user = null;
        List<Utilisateur> userlist = new ArrayList<>();
        String strSQL = "SELECT *FROM Utilisateur";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            user = new Utilisateur(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            userlist.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return userlist;
    }

    public List<Navigation> getListNavigationsProfil() {
        List<Navigation> ListeNavigations = new ArrayList<>();
        String strSQL = "SELECT *FROM NAVIGATION";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Navigation nav = new Navigation(cursor.getInt(cursor.getColumnIndex("id_nav")), cursor.getDouble(cursor.getColumnIndex("tps_preparation")), cursor.getString(cursor.getColumnIndex("domicile")), cursor.getString(cursor.getColumnIndex("destination")), cursor.getInt(cursor.getColumnIndex("id_profil")));
            ListeNavigations.add(nav);
            cursor.moveToNext();
        }
        cursor.close();
        return ListeNavigations;
    }

    public List<Planning> getListPlanningsProfil() {
        List<Planning> ListePlannings = new ArrayList<>();
        String strSQL = "SELECT *FROM PLANNING";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Planning planning = new Planning(cursor.getInt(cursor.getColumnIndex("id_planning")), cursor.getString(cursor.getColumnIndex("jour")), cursor.getString(cursor.getColumnIndex("Horaire")), cursor.getInt(cursor.getColumnIndex("id_profil")));
            ListePlannings.add(planning);
            cursor.moveToNext();
        }
        cursor.close();
        return ListePlannings;
    }

    public ArrayList<Cours> getCours() {
        ArrayList<Cours> cours = new ArrayList<>();

        String strSql = "SELECT * FROM T_Cours";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Cours cour = new Cours(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
            cours.add(cour);
            cursor.moveToNext();
        }
        cursor.close();

        return cours;
    }

    public String getLien() {
        String result = null;

        String strSql = "SELECT * FROM T_Lien";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result = cursor.getString(1);
            cursor.moveToNext();
        }
        cursor.close();

        return result;
    }

    public ArrayList<Rappel> getRappels() {
        ArrayList<Rappel> rappels = new ArrayList<>();
        String strSql = "SELECT * FROM T_Rappel";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Rappel rappel = new Rappel(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            rappels.add(rappel);
            cursor.moveToNext();
        }
        cursor.close();

        return rappels;
    }

    /*
    Partie concernant la suppression d'informations dans les tables de notre base de données
     */
    public void SuppNavigation(int id) {
        String strSQL = "DELETE FROM Navigation WHERE id_nav =" + id;
        this.getWritableDatabase().execSQL(strSQL);
    }

    public void SuppPlanning(int id) {
        String strSQL = "DELETE FROM Planning WHERE id_planning =" + id;
        this.getWritableDatabase().execSQL(strSQL);
    }

    public void SuppRappel(String titre) {
        String strSQL = "DELETE FROM T_Rappel WHERE Titre ='" + titre + "'";
        this.getWritableDatabase().execSQL(strSQL);
    }

    public boolean isLienEmpty() {
        String strSql = "SELECT * FROM T_Lien";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSql, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0 || cursor == null) {
            return true;
        } else return false;

    }

    public boolean UserExist(String email){
        try {
            String strSQL = "SELECT *FROM Utilisateur where mail  = '" + email +"'";
            Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getString(cursor.getColumnIndex("mail")).contentEquals(email))
                        return true;
                    else
                        return false;
                } while(cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {}
        return false;
    }

    public boolean ProfilExist(int id){
        try {
            String strSQL = "SELECT *FROM Profil where id_user =" + id;
            Cursor cursor = this.getReadableDatabase().rawQuery(strSQL, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getInt(cursor.getColumnIndex("id_user")) == id)
                        return true;
                    else
                        return false;
                } while(cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {}
        return false;
    }
}
