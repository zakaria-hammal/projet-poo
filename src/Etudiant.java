import java.io.*;
import java.time.LocalDate;
public class Etudiant extends Utilisateur implements Serializable {
    LocalDate anneeAdmistion;
    Faculte fac;
    String specialite;
// Constructeur
    public Etudiant(String nom, String prenom, String password, String mat,
                    double repC, double repP, LocalDate anneeAdmistion, Faculte fac, String specialite,)
            throws MatriculeException, ReputationException {
        super(nom, prenom, password, mat, repC, repP);
        this.anneeAdmistion = anneeAdmistion;
        this.specialite = specialite;
        this.fac=fac;
    }
    // Getteurs
    public String getSpecialite() {
        return specialite;
    }

    public LocalDate getAnneeAdmistion() {
        return anneeAdmistion;
    }
    public Faculte getFaculteEn(int choix) {
        System.out.println("Inserez votre Faculte: " +
                "1)Math,\n" +
                "2)Informatique,\n" +
                "3)Physique,\n" +
                "4)Chimie,\n" +
                "5)Biologie,\n" +
                "6)GénieC,\n" +
                "7)GénieE,\n" +
                "8)GénieMP,\n" +
                "9)Géologie");

        switch (choix) {
            case 1: return Faculte.Math;
            case 2: return Faculte.Informatique;
            case 3: return Faculte.Physique;
            case 4: return Faculte.Chimie;
            case 5: return Faculte.Biologie;
            case 6: return Faculte.GénieC;
            case 7: return Faculte.GénieE;
            case 8: return Faculte.GénieMP;
            case 9: return Faculte.Géologie;
            default:
                System.out.println("Erreur");
                return null;
        }
    }
    // Setteurs
    public void setAnneeAdmistion(LocalDate anneeAdmistion) {
        this.anneeAdmistion = anneeAdmistion;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public void setFac(Faculte fac) {
        this.fac = fac;
    }
    // Methode responsable d'inscription des etudiants dans l'app.
    public static void RajouterEtudiant(String filePath, Etudiant x) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath, true))) {
            out.writeObject(x);
            System.out.println("Vous etes bien inscrit.");
        } catch (IOException e) {
            System.err.println("Une erreur est survenue lors de l'écriture de l'objet : " + e.getMessage());
        }
    }
    }
