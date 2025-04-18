public class Utilisateur {
    private String nom;
    private String prenom;
    private int matricule;
    private double reputation = 2.5;

    public Utilisateur(String nom, String prenom, int mat, double rep) {
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = mat;
        this.reputation = rep;
    }

    // Getters :
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

    // Setters :
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
}
