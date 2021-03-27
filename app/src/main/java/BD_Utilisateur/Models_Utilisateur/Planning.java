package BD_Utilisateur.Models_Utilisateur;

public class Planning {
    private int id_planning;
    private String date_jour;
    private String horaire;
    private int id_profil;

    public Planning(){}

    public Planning(String jour, String horaire, int id_p){
        this.date_jour = jour;
        this.horaire = horaire;
        this.id_profil = id_p;
    }

    public Planning(int id, String jour, String horaire, int id_p){
        this.id_planning = id;
        this.date_jour = jour;
        this.horaire = horaire;
        this.id_profil = id_p;
    }


    public int getId_planning() {
        return id_planning;
    }

    public void setId_planning(int id_planning) {
        this.id_planning = id_planning;
    }

    public String getDate_jour() {
        return date_jour;
    }

    public void setDate_jour(String date_jour) {
        this.date_jour = date_jour;
    }

    public String getHoraire() {
        return horaire;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }

    public int getId_profil() {
        return id_profil;
    }

    public void setId_profil(int id_profil) {
        this.id_profil = id_profil;
    }
}
