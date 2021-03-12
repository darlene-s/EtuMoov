package bdd.models;

public class Profil {
    private int id_profil;
    private double tps_preparation;
    private double tps_supplementaires;
    private int id_user;

    public Profil(){}
    public Profil(double tps_p, double tps_s, int id_u){
        this.tps_preparation = tps_p;
        this.tps_supplementaires = tps_s;
        this.id_user = id_u;
    }
    public Profil(int id_p, double tps_p, double tps_s, int id_u){
        this.id_profil = id_p;
        this.tps_preparation = tps_p;
        this.tps_supplementaires = tps_s;
        this.id_user = id_u;
    }

    public int getId_profil() {
        return id_profil;
    }

    public void setId_profil(int id_profil) {
        this.id_profil = id_profil;
    }

    public double getTps_preparation() {
        return tps_preparation;
    }

    public void setTps_preparation(int tps_preparation) {
        this.tps_preparation = tps_preparation;
    }

    public double getTps_supplementaires() {
        return tps_supplementaires;
    }

    public void setTps_supplementaires(int tps_supplementaires) {
        this.tps_supplementaires = tps_supplementaires;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }
}
