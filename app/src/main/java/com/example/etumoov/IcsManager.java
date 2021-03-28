package com.example.etumoov;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;
import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;

/**
 * Classe IcsManager, pour la connection et la sauvegarde des données dans la Base de données
 *
 * @author Alexandre Hardy
 * @version 1.0
 */

public class IcsManager {

    private List<VEvent> allClassesFromIcs;
    private DataBaseHelper db;

    public IcsManager(String linkOfIcsFile, Context context) throws IOException {
        getIcsFile(linkOfIcsFile);
        db = new DataBaseHelper(context);
        impelmentInDataBase();
        db.close();
    }


    /**
     * A partir d'une URL, obtient les informations sous forme d'une String
     * et les enregistres dans la Base de données
     *
     * @param link un lien de la forme d'une URL vers un fichier de type .ics
     * @return Un String correspondant au fichier ICS
     * @throws IOException
     */

    private void getIcsFile(String link) throws IOException {
        String  result="";
        URL url = new URL(link);
        URLConnection conn = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        while ((inputLine = reader.readLine()) != null)
            result += inputLine + "\r\n";
        List<ICalendar> ical = Biweekly.parse(result).all();
        allClassesFromIcs = ical.get(0).getEvents();
    }

    /**
     * Transforme une date sous une forme plus facile a lire, une String
     *
     * @param index l'index de la position dans la list "allClassesFromIcs"
     * @return Un String sous la forme jour / mois / annee
     */
    private String createDate(int index) {
        String jour ="", mois ="", annee = "";
        if(allClassesFromIcs.get(index).getDateStart().getValue().getDate() < 10) {
            jour = "0";
            jour += String.valueOf(allClassesFromIcs.get(index).getDateStart().getValue().getDate());
        } else jour = String.valueOf(allClassesFromIcs.get(index).getDateStart().getValue().getDate());
        if(allClassesFromIcs.get(index).getDateStart().getValue().getMonth() < 10) {
            mois = "0";
            mois += String.valueOf(allClassesFromIcs.get(index).getDateStart().getValue().getMonth() + 1);
        }
        else mois = String.valueOf(allClassesFromIcs.get(index).getDateStart().getValue().getMonth());
        annee = String.valueOf(allClassesFromIcs.get(index).getDateStart().getValue().getYear() + 1900);
        return jour +"/" + mois + "/" + annee;
    }

    /**
     * Transforme l'heure du début d'un cours sous une forme plus facile a lire, une String
     *
     * @param index l'index de la position dans la list "allClassesFromIcs"
     * @return Un String sous la forme heure : minutes
     */
    private String createHeureDebut(int index) {
        String heure ="", minutes ="";
        if(allClassesFromIcs.get(index).getDateStart().getValue().getHours() < 10) {
            heure = "0";
            heure += String.valueOf(allClassesFromIcs.get(index).getDateStart().getValue().getHours());
        } else heure = String.valueOf(allClassesFromIcs.get(index).getDateStart().getValue().getHours());
        if(allClassesFromIcs.get(index).getDateStart().getValue().getMinutes() < 10) {
            minutes = "0";
            minutes += String.valueOf(allClassesFromIcs.get(index).getDateStart().getValue().getMinutes());
        } else minutes = String.valueOf(allClassesFromIcs.get(index).getDateStart().getValue().getMinutes());
        return heure + ":" + minutes;
    }

    /**
     * Transforme l'heure de fin d'un cours sous une forme plus facile a lire, une String
     *
     * @param index l'index de la position dans la list "allClassesFromIcs"
     * @return Un String sous la forme heure : minutes
     */
    private String createHeureFin(int index) {
        String heure ="", minutes ="";
        if(allClassesFromIcs.get(index).getDateEnd().getValue().getHours() < 10) {
            heure = "0";
            heure += String.valueOf(allClassesFromIcs.get(index).getDateEnd().getValue().getHours());
        } else heure = String.valueOf(allClassesFromIcs.get(index).getDateEnd().getValue().getHours());
        if(allClassesFromIcs.get(index).getDateEnd().getValue().getMinutes() < 10) {
            minutes = "0";
            minutes += String.valueOf(allClassesFromIcs.get(index).getDateEnd().getValue().getMinutes());
        } else minutes = String.valueOf(allClassesFromIcs.get(index).getDateEnd().getValue().getMinutes());
        return heure + ":" + minutes;
    }


    /**
     * Implemente dans la Base de données les informations du fichier .ics
     *
     */
    private void impelmentInDataBase() {
        for(int i = 0; i < allClassesFromIcs.size();i++) {
            db.insertCours("IDK",allClassesFromIcs.get(i).getSummary().getValue(),allClassesFromIcs.get(i).getLocation().getValue(),
                    createHeureDebut(i),createHeureFin(i),createDate(i));
        }
    }
}
