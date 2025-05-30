/*
 * Preparee par HAMMAL Zakaria
 */

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Utilisateur implements Serializable {
    private String password;
    private String nom;
    private String prenom;
    private final String matricule;
    private double reputationChauffeur = 2.5;
    private double reputationPassager = 2.5;
    private Profil profil;
    private int nb_coursesChauffeur = 0;
    private int nb_coursesPassager = 0;

    public Utilisateur(String nom, String prenom, String password, String mat) throws MatriculeException {
        if (mat.length() == 0 || mat.length() != 13) {
            throw new MatriculeException("Taille du matricule invalide");
        }

        for (int i = 0; i < 13; i++) {
            if ((mat.charAt(i) > '9') || (mat.charAt(i) < '0')) {
                throw new MatriculeException("Matricule Invalide : Matricule contient des caracteres interdits");
            }
        }

        if (mat.charAt(4) != '1' && mat.charAt(4) != '2' && mat.charAt(4) != '3') {
            throw new MatriculeException("Matricule Invalide : Type d'utilisateur introuvable");
        }

        int annee = Integer.parseInt(String.valueOf(mat.charAt(1)) + mat.charAt(2) + mat.charAt(3) + mat.charAt(4));

        if (annee > LocalDate.now().getYear()) {
            throw new MatriculeException("Matricule Invalide : Annee de recutement ne peut pas dépasser l'annee actuelle");
        }

        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.matricule = mat;
    }

    public Utilisateur(String nom, String prenom, String password, String mat, double repC, double repP) throws MatriculeException, ReputationException {
        if (mat.length() == 0 || mat.length() != 13) {
            throw new MatriculeException("Taille du matricule invalide");
        }

        for (int i = 0; i < 13; i++) {
            if ((mat.charAt(i) > '9') || (mat.charAt(i) < '0')) {
                throw new MatriculeException("Matricule Invalide : Matricule contient des caracteres interdits");
            }
        }

        if (mat.charAt(4) != '1' && mat.charAt(4) != '2' && mat.charAt(4) != '3') {
            throw new MatriculeException("Matricule Invalide : Type d'utilisateur introuvable");
        }

        int annee = Integer.parseInt(String.valueOf(mat.charAt(1)) + mat.charAt(2) + mat.charAt(3) + mat.charAt(4));

        if (annee > LocalDate.now().getYear()) {
            throw new MatriculeException("Matricule Invalide : Annee de recutement ne peut pas dépasser l'annee actuelle");
        }

        if(repC < 0 || repC > 5 || repP < 0 || repP > 5) {
            throw new ReputationException("Reputation doit etre entre 0 et 5");
        }

        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.matricule = mat;
        this.reputationChauffeur = repC;
        this.reputationPassager = repP;
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
        if(nb_coursesChauffeur + nb_coursesPassager == 0) {
            return 2.5;
        } 

        return ((nb_coursesChauffeur * reputationChauffeur + nb_coursesPassager * reputationPassager) / (nb_coursesChauffeur + nb_coursesPassager));
    }

    public double getReputationChauffeur() {
        return this.reputationChauffeur;
    }

    public double getReputationPassager() {
        return this.reputationPassager;
    }

    public Profil getProfil() {
        return this.profil;
    }

    public int getNb_courses() {
        return nb_coursesChauffeur + nb_coursesPassager;
    }

    public int getNb_coursesChauffeur() {
        return this.nb_coursesChauffeur;
    }

    public int getNb_coursesPassager() {
        return this.nb_coursesPassager;
    }

    // Setteurs :
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public int newRatingChauffeur(Course course, double note) throws IOException, MatriculeException, ClassNotFoundException, UserNotFoundException {
        if(course == null) {
            return -1;
        }

        this.reputationChauffeur = (this.reputationChauffeur * this.nb_coursesChauffeur + note) / (this.nb_coursesChauffeur + 1);
        this.nb_coursesChauffeur++;

        Utilisateur utilisateur;
        ObjectInputStream in;
        ObjectOutputStream out;
        
        int n = 0;

        ArrayList<Utilisateur> temp = new ArrayList<>();

        switch (this.matricule.charAt(4)) {
            case '1' -> { 
                in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierATS"));
                while (true) {
                    try {
                        utilisateur = (ATS) in.readObject();
                        if (utilisateur.matricule.equals(matricule) && utilisateur.password.equals(password)) {
                            utilisateur.nb_coursesChauffeur = this.nb_coursesChauffeur;
                            utilisateur.reputationChauffeur = this.reputationChauffeur;
                            n++;
                        }
                        temp.add((ATS) utilisateur);
                    } catch (EOFException e) {
                        break;
                    }
            
                }

                out = new ObjectOutputStream(new FileOutputStream("../FichiersDeSauvegarde/fichierATS"));

                for (Utilisateur elem : temp) {
                    out.writeObject((ATS) elem);
                }
            }

            case '2' -> {
                in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierEnseignant"));
                while (true) {
                    try {
                        utilisateur = (Enseignant) in.readObject();
                        if (utilisateur.matricule.equals(matricule) && utilisateur.password.equals(password)) {
                            utilisateur.nb_coursesChauffeur = this.nb_coursesChauffeur;
                            utilisateur.reputationChauffeur = this.reputationChauffeur;
                            n++;
                        }
                        temp.add((Enseignant) utilisateur);
                    } catch (EOFException e) {
                        break;
                    }
            
                }

                out = new ObjectOutputStream(new FileOutputStream("../FichiersDeSauvegarde/fichierEnseignant"));

                for (Utilisateur elem : temp) {
                    out.writeObject((Enseignant) elem);
                }
            }
            
            case '3' -> { 
                in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierEtudiant"));

                while (true) {
                    try {
                        utilisateur = (Etudiant) in.readObject();
                        if (utilisateur.matricule.equals(matricule) && utilisateur.password.equals(password)) {
                            utilisateur.nb_coursesChauffeur = this.nb_coursesChauffeur;
                            utilisateur.reputationChauffeur = this.reputationChauffeur;
                            n++;
                        }
                        temp.add((Etudiant) utilisateur);
                    } catch (EOFException e) {
                        break;
                    }
                    
                }

                out = new ObjectOutputStream(new FileOutputStream("../FichiersDeSauvegarde/fichierEtudiant"));

                for (Utilisateur elem : temp) {
                    out.writeObject((Etudiant) elem);
                }
            }

            default -> {
                throw new MatriculeException("Matricule Invalide : Type d'utilisateur introuvable");
            }
        }


        if(n == 0) {
            throw new UserNotFoundException("Utilisateur Introuvable");
        }

        return 0;
    }

    public int newRatingPassager(Course course, double note) throws  IOException, MatriculeException, ClassNotFoundException, UserNotFoundException{
        if(course == null) {
            return -1;
        }

        this.reputationPassager = (this.reputationPassager * this.nb_coursesPassager + note) / (this.nb_coursesPassager + 1);
        this.nb_coursesPassager++;

        Utilisateur utilisateur;
        ObjectInputStream in;
        ObjectOutputStream out;
        
        int n = 0;

        ArrayList<Utilisateur> temp = new ArrayList<>();

        switch (this.matricule.charAt(4)) {
            case '1' -> { 
                in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierATS"));
                while (true) {
                    try {
                        utilisateur = (ATS) in.readObject();
                        if (utilisateur.matricule.equals(matricule) && utilisateur.password.equals(password)) {
                            utilisateur.nb_coursesPassager = this.nb_coursesPassager;
                            utilisateur.reputationPassager = this.reputationPassager;
                            n++;
                        }
                        temp.add((ATS) utilisateur);
                    } catch (EOFException e) {
                        break;
                    }
            
                }

                out = new ObjectOutputStream(new FileOutputStream("../FichiersDeSauvegarde/fichierATS"));

                for (Utilisateur elem : temp) {
                    out.writeObject((ATS) elem);
                }
            }

            case '2' -> {
                in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierEnseignant"));
                while (true) {
                    try {
                        utilisateur = (Enseignant) in.readObject();
                        if (utilisateur.matricule.equals(matricule) && utilisateur.password.equals(password)) {
                            utilisateur.nb_coursesPassager = this.nb_coursesPassager;
                            utilisateur.reputationPassager = this.reputationPassager;
                            n++;
                        }
                        temp.add((Enseignant) utilisateur);
                    } catch (EOFException e) {
                        break;
                    }
            
                }

                out = new ObjectOutputStream(new FileOutputStream("../FichiersDeSauvegarde/fichierEnseignant"));

                for (Utilisateur elem : temp) {
                    out.writeObject((Enseignant) elem);
                }
            }
            
            case '3' -> { 
                in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierEtudiant"));

                while (true) {
                    try {
                        utilisateur = (Etudiant) in.readObject();
                        if (utilisateur.matricule.equals(matricule) && utilisateur.password.equals(password)) {
                            utilisateur.nb_coursesPassager = this.nb_coursesPassager;
                            utilisateur.reputationPassager = this.reputationPassager;
                            n++;
                        }
                        temp.add((Etudiant) utilisateur);
                    } catch (EOFException e) {
                        break;
                    }
                    
                }

                out = new ObjectOutputStream(new FileOutputStream("../FichiersDeSauvegarde/fichierEtudiant"));

                for (Utilisateur elem : temp) {
                    out.writeObject((Etudiant) elem);
                }
            }

            default -> {
                throw new MatriculeException("Matricule Invalide : Type d'utilisateur introuvable");
            }
        }

        if (n == 0) {
            throw new UserNotFoundException("Utilisateur Introuvable");
        }

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

    public static Utilisateur login(String matricule, String password) throws IOException, ClassNotFoundException {
        ObjectInputStream in;
        Utilisateur utilisateur;
        File file;

        switch (matricule.charAt(4)) {
            case '1' -> { 
                file = new File("../FichiersDeSauvegarde/fichierATS");
                if(file.exists() && file.length() != 0) {
                    in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierATS"));
                    while (true) {
                        try {
                            utilisateur = (ATS) in.readObject();
                            if (utilisateur.matricule.equals(matricule) && utilisateur.password.equals(password)) {
                                return (ATS) utilisateur;
                            }
                        } catch (EOFException e) {
                            break;
                        }
                
                    }
                }

                return null;
            }

            case '2' -> {
                file = new File("../FichiersDeSauvegarde/fichierEnseignant");
                if(file.exists() && file.length() != 0) {
                    in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierEnseignant"));
                    while (true) {
                        try {
                            utilisateur = (Enseignant) in.readObject();
                            if (utilisateur.matricule.equals(matricule) && utilisateur.password.equals(password)) {
                                return (Enseignant) utilisateur;
                            }
                        } catch (EOFException e) {
                            break;
                        }
                
                    }
                }

                return null;
            }
        
            case '3' -> {
                file = new File("../FichiersDeSauvegarde/fichierEtudiant");
                if(file.exists() && file.length() != 0) {
                    in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierEtudiant"));
                    while (true) {
                        try {
                            utilisateur = (Etudiant) in.readObject();
                            if (utilisateur.matricule.equals(matricule) && utilisateur.password.equals(password)) {
                                return (Etudiant) utilisateur;
                            }
                        } catch (EOFException e) {
                            break;
                        }
                
                    }
                }

                return null;
            }

        }

        return null;
    }

    public abstract void ajouterUtilisateur() throws UtilisateurExistDeja, UserBanException;

    @Override
    public String toString() {
        return (nom + " " + prenom + " : \nReputation Passager : " + reputationPassager + "\nReputation Chauffeur : " + reputationChauffeur);
    }
    
}
