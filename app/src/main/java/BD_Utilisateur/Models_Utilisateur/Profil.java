package BD_Utilisateur.Models_Utilisateur;

public class Profil {
    private int id_profil, id_user, score;
    private double tps_preparation, tps_supplementaires;
    private String timerMemory, timerCookie;

    public Profil(){}
    public Profil(double tps_p, double tps_s, int score, String timerMemory, String timerCookie, int id_u){
        this.tps_preparation = tps_p;
        this.tps_supplementaires = tps_s;
        this.score = score;
        this.timerMemory = timerMemory;
        this.timerCookie = timerCookie;
        this.id_user = id_u;
    }
    public Profil(int id_p, double tps_p, double tps_s, int score, String timerMemory, String timerCookie,  int id_u){
        this.id_profil = id_p;
        this.tps_preparation = tps_p;
        this.tps_supplementaires = tps_s;
        this.score = score;
        this.timerMemory = timerMemory;
        this.timerCookie = timerCookie;
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

    public String getTimerMemory() {
        return timerMemory;
    }

    public void setTimerMemory(String timerMemory) {
        this.timerMemory = timerMemory;
    }

    public String getTimerCookie() {
        return timerCookie;
    }

    public void setTimerCookie(String timerCookie) {
        this.timerCookie = timerCookie;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
