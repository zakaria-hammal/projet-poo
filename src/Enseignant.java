import java.io.*;
import java.io.Serializable;

public class Enseignant extends Utilisateur implements Serializable{
    Faculte fac;
    int annee;
    public Enseignant(String nom, String prenom, String password, String mat,
                    double repC, double repP,int annee,Faculte fac)
            throws MatriculeException, ReputationException{
        super(nom, prenom, password, mat, repC, repP);
        this.annee=annee;
        this.fac=fac;
    }
    public int getAnnee(){
        return this.annee;
    }
    public Faculte getFaculte() {
        return this.fac;
    }
    public Faculte setFaculteEn(int choix) {
        switch (choix) {
            case 1: this.fac= Faculte.Math;
            case 2: this.fac= Faculte.Informatique;
            case 3: this.fac= Faculte.Physique;
            case 4: this.fac= Faculte.Chimie;
            case 5: this.fac= Faculte.Biologie;
            case 6: this.fac= Faculte.GénieC;
            case 7: this.fac= Faculte.GénieE;
            case 8: this.fac= Faculte.GénieMP;
            case 9: this.fac= Faculte.Géologie;
            default:
                System.out.println("Erreur");
        }
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public static void RajouterEnseignant(String filePath, Enseignant x) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath, true))) {
            out.writeObject(x);
            System.out.println("Vous etes bien inscrit.");
        } catch (IOException e) {
            System.err.println("Une erreur est survenue lors de l'écriture de l'objet : " + e.getMessage());
        }
    }
}
