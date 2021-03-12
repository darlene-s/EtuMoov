package bdd.models;

public class Navigation {

    private int id_nav;
    private double tps_trajet;
    private String domicile;
    private String destination;
    private int id_profil;

    public Navigation(){}

    public Navigation(double tps, String dom, String dest, int id_p){
        this.tps_trajet = tps;
        this.domicile = dom;
        this.destination = dest;
        this.id_profil = id_p;
    }

    public Navigation(int id, double tps, String dom, String dest, int id_p){
        this.id_nav = id;
        this.tps_trajet = tps;
        this.domicile = dom;
        this.destination = dest;
        this.id_profil = id_p;
    }

    public int getId_nav() {
        return id_nav;
    }

    public void setId_nav(int id_nav) {
        this.id_nav = id_nav;
    }

    public double getTps_trajet() {
        return tps_trajet;
    }

    public void setTps_trajet(double tps_trajet) {
        this.tps_trajet = tps_trajet;
    }

    public String getDomicile() {
        return domicile;
    }

    public void setDomicile(String domicile) {
        this.domicile = domicile;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getId_profil() {
        return id_profil;
    }

    public void setId_profil(int id_profil) {
        this.id_profil = id_profil;
    }
}
