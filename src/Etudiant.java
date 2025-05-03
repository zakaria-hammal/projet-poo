import java.io.*;
import java.time.LocalDate;
public class Etudiant extends Utilisateur implements Serializable {
    LocalDate anneeAdmistion;
    Faculte fac;
    String specialite;

    public Etudiant(String nom, String prenom, String password, String mat,
                    double repC, double repP, LocalDate anneeAdmistion, Faculte fac, String specialite,)
            throws MatriculeException, ReputationException {
        super(nom, prenom, password, mat, repC, repP);
        this.anneeAdmistion = anneeAdmistion;
        this.specialite = specialite;
        this.fac=fac;
    }
    public String getSpecialite() {
        return specialite;
    }

    public LocalDate getAnneeAdmistion() {
        return anneeAdmistion;
    }
    public void getFaculteEt(int choix) {
        System.out.println("Inserez votre Faculte: " + "1)Math,\n" +
                "2)Informatique,\n" +
                "3)Physique,\n" +
                "4)Chimie,\n" +
                "5)Biologie,\n" +
                " 6)GénieC,\n" + "7)GénieE,\n" +
                "8)GénieMP,\n" +
                "9)Géologie");
        if (choix >= 1 && choix <= 9) {
            switch (choix) {
                case 1:
                    this.fac = Faculte.Math;
                    break;
                case 2:
                    this.fac = Faculte.Informatique;
                    break;
                case 3:
                    this.fac = Faculte.Physique;
                    break;
                case 4:
                    this.fac = Faculte.Chimie;
                    break;
                case 5:
                    this.fac = Faculte.Biologie;
                    break;
                case 6:
                    this.fac = Faculte.GénieC;
                    break;
                case 7:
                    this.fac = Faculte.GénieE;
                    break;
                case 8:
                    this.fac = Faculte.GénieMP;
                    break;
                case 9:
                    this.fac = Faculte.Géologie;
                    break;
            }
        } else System.out.println("Erreur");
    }
    public void setAnneeAdmistion(LocalDate anneeAdmistion) {
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
    public static boolean VerifierEtudiant(String filePath, Etudiant x) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            while (true) {
                Etudiant e = (Etudiant) in.readObject();
                if (e.getMatricule().equals(x.getMatricule()) && e.getpassword().equals(x.getpassword())) {
                    return true;
                }
            }
        } catch (EOFException e) {
            // End of file reached — no match found
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors de la lecture : " + e.getMessage());
        }
        return false;
    }

}