package BD_Utilisateur.Models_Cours;

public class Rappel {
    private String titre;
    private String description;
    private String date;

    public Rappel(String titre, String desc, String date) {
        this.titre = titre;
        this.description =desc;
        this.date = date;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
