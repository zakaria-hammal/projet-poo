import java.io.*;
import java.time.LocalDate;
public class Enseignant extends Utilisateur{
    Faculte fac;
    LocalDate annee;
    public Enseignant(String nom, String prenom, String password, String mat,
                    double repC, double repP,LocalDate annee,Faculte fac)
            throws MatriculeException, ReputationException{
        super(nom, prenom, password, mat, repC, repP);
        this.annee=annee;
        this.fac=fac;
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
    public LocalDate getAnnee(){
        return annee;
    }
    public void setFaculte(Faculte fac) {
        this.fac = fac;
    }

    public void setAnnee(LocalDate annee) {
        this.annee = annee;
    }

    public static void RajouterEnseignant(String filePath, Etudiant x) {
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
    }}