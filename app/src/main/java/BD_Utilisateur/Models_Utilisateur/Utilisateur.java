package BD_Utilisateur.Models_Utilisateur;

public class Utilisateur {
    private int id_user;
    private String email;
    private String prenom;
    private String nom;
    private String password;

    public Utilisateur(){}
    public Utilisateur(String nom, String prenom, String email, String mdp){
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = mdp;
    }
    public Utilisateur(int id, String nom, String prenom, String email, String mdp){
        this.id_user = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = mdp;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return password;
    }

    public void setMdp(String mdp) {
        this.password = mdp;
    }
}
