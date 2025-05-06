import java.io.*;
import java.io.Serializable;

public class Etudiant extends Utilisateur implements Serializable {
    int anneeAdmistion;
    Faculte fac;
    String specialite;

    public Etudiant(String nom, String prenom, String password, String mat,
                    double repC, double repP, int anneeAdmistion, Faculte fac, String specialite)
            throws MatriculeException, ReputationException {
        super(nom, prenom, password, mat, repC, repP);
        this.anneeAdmistion = anneeAdmistion;
        this.specialite = specialite;
        this.fac=fac;
    }
    public String getSpecialite() {
        return this.specialite;
    }

    public int getAnneeAdmistion() {
        return this.anneeAdmistion;
    }
    public Faculte getFaculte(){
        return this.fac;
    }
    public void setFaculteEn(int choix) {
                switch (choix) {
                    case 1:
                        this.fac = Faculte.Math;
                    case 2:
                        this.fac = Faculte.Informatique;
                    case 3:
                        this.fac = Faculte.Physique;
                    case 4:
                        this.fac = Faculte.Chimie;
                    case 5:
                        this.fac = Faculte.Biologie;
                    case 6:
                        this.fac = Faculte.GénieC;
                    case 7:
                        this.fac = Faculte.GénieE;
                    case 8:
                        this.fac = Faculte.GénieMP;
                    case 9:
                        this.fac = Faculte.Géologie;
                    default:
                        System.out.println("Erreur");
                }
    }
    public void setAnneeAdmistion(int anneeAdmistion) {
        this.anneeAdmistion = anneeAdmistion;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public void setFac(Faculte fac) {
        this.fac = fac;
    }
    public static void RajouterEtudiant(String filePath, Etudiant x) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath, true))) {
            out.writeObject(x);
            System.out.println("Vous etes bien inscrit.");
        } catch (IOException e) {
            System.err.println("Une erreur est survenue lors de l'écriture de l'objet : " + e.getMessage());
        }
    }
    }
