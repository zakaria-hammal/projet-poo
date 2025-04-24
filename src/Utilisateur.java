
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Utilisateur implements Serializable {
    private static int nb_utilisateur;

    private String password;
    private String nom;
    private String prenom;
    private final String matricule;
    private double reputation = 2.5;
    private Profil profil;
    private int nb_courses = 0;

    public Utilisateur(String nom, String prenom, String password, String mat, double rep) {
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
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

    public String getMatricule() {
        return this.matricule;
    }

    public double getReputation() {
        return this.reputation;
    }

    public Profil getProfil() {
        return this.profil;
    }

    public static int getNb_utilisateur() {
        return nb_utilisateur;
    }

    public int getNb_courses() {
        return nb_courses;
    }

    // Setteurs :
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int newRating(Course course, double note) {
        if(course == null) {
            return -1;
        }

        this.reputation = (this.reputation * this.nb_courses + note) / (this.nb_courses + 1);
        this.nb_courses++;

        return 0;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public int setPassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
            return 0;
        }

        return -1;
    }

    // Fin des accesseurs

    public static Utilisateur login(String matricule, String password) {
        ObjectInputStream out;

        try {
            Utilisateur utilisateur;
            File file = new File("../FichiersDeSauvegarde/fichierATS");
            
            if(file.exists() && file.length() != 0) {
                System.out.println("Something");
                out = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierATS"));
                while (true) {
                    try {
                        utilisateur = (Utilisateur) out.readObject();
                        if (utilisateur.matricule.equals(matricule) && utilisateur.password.equals(password)) {
                            return (ATS) utilisateur;
                        }
                    } catch (EOFException e) {
                        break;
                    }
            
                }
            }
            else {
                file = new File("../FichiersDeSauvegarde/fichierEnseignant");
                if(file.exists() && file.length() != 0) {
                    System.out.println("Something");
                    out = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierEnseignant"));
                    while (true) {
                        try {
                            utilisateur = (Utilisateur) out.readObject();
                            if (utilisateur.matricule.equals(matricule) && utilisateur.password.equals(password)) {
                                return (Enseignant) utilisateur;
                            }
                        } catch (EOFException e) {
                            break;
                        }
                
                    }
                }
                else {
                    file = new File("../FichiersDeSauvegarde/fichierEnseignant");
                    if(file.exists() &&  file.length() != 0) {
                        System.out.println("Something");
                        out = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierEtudiant"));
                        while (true) {
                            try {
                                utilisateur = (Utilisateur) out.readObject();
                                if (utilisateur.matricule.equals(matricule) && utilisateur.password.equals(password)) {
                                    return (Etudiant) utilisateur;
                                }
                            } catch (EOFException e) {
                                break;
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
