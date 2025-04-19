
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Utilisateur implements Serializable {
    private String username;
    private String password;
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

    public Profil getProfil() {
        return this.profil;
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

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    // Fin des accesseurs

    /* à terminer*/
    public static Utilisateur login(String username, String password) {
        ObjectInputStream out;

        try {
            Utilisateur utilisateur;
            File file = new File("../FichiersDeSauvegarde/fichierATS");
            if(file.length() != 0) {
                out = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierATS"));
                while ((utilisateur = (ATS) out.readObject()) != null) {
                    if (utilisateur.username.equals(username) && utilisateur.password.equals(password)) {
                        return utilisateur;
                    }
                }
            }
            else {
                file = new File("../FichiersDeSauvegarde/fichierEnseignant");
                if(file.length() != 0) {
                    out = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierEnseignant"));
                    while ((utilisateur = (Enseignant) out.readObject()) != null) {
                        if (utilisateur.username.equals(username) && utilisateur.password.equals(password)) {
                            return utilisateur;
                        }
                    }
                }
                else {
                    file = new File("../FichiersDeSauvegarde/fichierEnseignant");
                    if(file.length() != 0) {
                        out = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierEtudiant"));
                        while ((utilisateur = (Etudiant) out.readObject()) != null) {
                            if (utilisateur.username.equals(username) && utilisateur.password.equals(password)) {
                                return utilisateur;
                            }
                        }
                    }
                }
            }

            return null;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
}
