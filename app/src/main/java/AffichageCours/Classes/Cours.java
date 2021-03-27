package AffichageCours.Classes;

public class Cours {
    private String nomProf;
    private String nomCours;
    private String nomSalle;
    private String debutCours;
    private String finCours;
    private String dateCours;

    public Cours(String prof, String cours, String salle, String debut, String fin, String date){
        this.nomProf = prof;
        this.nomCours = cours;
        this.nomSalle = salle;
        this.debutCours = debut;
        this.finCours = fin;
        this.dateCours = date;
    }

    public String getNomProf() {
        return nomProf;
    }

    public void setNomProf(String nomProf) {
        this.nomProf = nomProf;
    }

    public String getNomSalle() {
        return nomSalle;
    }

    public void setNomSalle(String nomSalle) {
        this.nomSalle = nomSalle;
    }

    public String getNomCours() {
        return nomCours;
    }

    public void setNomCours(String nomCours) {
        this.nomCours = nomCours;
    }

    public String getDebutCours() {
        return debutCours;
    }

    public void setDebutCours(String debutCours) {
        this.debutCours = debutCours;
    }

    public String getFinCours() {
        return finCours;
    }

    public void setFinCours(String finCours) {
        this.finCours = finCours;
    }

    public String getDateCours() {
        return dateCours;
    }

    public void setDateCours(String dateCours) {
        this.dateCours = dateCours;
    }
}
