
import java.io.Serializable;

public class Utilisateur implements Serializable {
    private String nom;
    private String prenom;
    private int matricule;
    private double reputation = 2.5;
    private Profil profil;

    public Utilisateur(String nom, String prenom, int mat, double rep) {
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = mat;
        this.reputation = rep;
    }

    // Getteurs :
    public String getNom() {
        return this.nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public int getMatricule() {
        return this.matricule;
    }

    public double getReputation() {
        return this.reputation;
    }

    // Setteurs :
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setMatricule(int mat) {
        this.matricule = mat;
    }

    public void setReputation(int rep) {
        this.reputation = rep;
    }

    // Fin des accesseurs

    /* à terminer
    public static Utilisateur login() {
        ObjectOutputStream out;

        try {
            FileOutputStream fos = new FileOutputStream("FichiersDeSauvgarde/fichiersATS");    
            out = new ObjectOutputStream(fos);
        } catch (Exception e) {
            System.out.println("Erreur Systeme");
            return null;
        }
    }
    */
}
