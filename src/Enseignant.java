<<<<<<< HEAD
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
public class Enseignant extends Utilisateur{
    Faculte fac;
    int annee;

    public Enseignant(String nom, String prenom, String password, String mat, double repC, double repP,Faculte fac) throws MatriculeException, ReputationException {
=======
import java.io.*;
import java.io.Serializable;

public class Enseignant extends Utilisateur implements Serializable{
    Faculte fac;
    int annee;
    public Enseignant(String nom, String prenom, String password, String mat,
                    double repC, double repP,int annee,Faculte fac)
            throws MatriculeException, ReputationException{
>>>>>>> b81cc453c10b972ad95022a46ad6187d36d81da6
        super(nom, prenom, password, mat, repC, repP);
        String temp = "";

        for (int i = 0; i < 4; i++) {
            temp = temp + mat.charAt(i);
        }
        
        this.annee = Integer.parseInt(temp);
        this.fac=fac;
    }
<<<<<<< HEAD
    
    public int getAnnee(){
        return this.annee;
    }
    
=======
    public int getAnnee(){
        return this.annee;
    }
>>>>>>> b81cc453c10b972ad95022a46ad6187d36d81da6
    public Faculte getFaculte() {
        return this.fac;
    }

    public void setFaculte(Faculte fac) {
        this.fac = fac;
    }

    public static void AjouterEnseignant(Enseignant x) throws UtilisateurExistDeja {
        ArrayList<Enseignant> temp = new ArrayList<>();
        Enseignant enseignant;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierEnseignant"));
            while (true) {
                try {
                    enseignant = (Enseignant) in.readObject();
                    if (x.getMatricule() == null ? enseignant.getMatricule() == null : x.getMatricule().equals(enseignant.getMatricule())) {
                        throw new UtilisateurExistDeja("Utilisateur avec ce matricule existe deja");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    break;
                }
            }
            
        } catch (IOException e) {
            System.err.println("Une erreur est survenue lors de l'écriture de l'objet : " + e.getMessage());
            return;
        }

<<<<<<< HEAD
        temp.add(x);

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("../FichiersDeSauvegarde/fichierEnseignant"));
            for (Enseignant elem : temp) {
                out.writeObject((Enseignant) elem);
            }
            
        } catch (IOException e) {
            System.err.println("Une erreur est survenue lors de l'écriture de l'objet : " + e.getMessage());
        }

        /*try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath, true))) {
=======
    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public static void RajouterEnseignant(String filePath, Enseignant x) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath, true))) {
>>>>>>> b81cc453c10b972ad95022a46ad6187d36d81da6
            out.writeObject(x);
            System.out.println("Vous etes bien inscrit.");
        } catch (IOException e) {
            System.err.println("Une erreur est survenue lors de l'écriture de l'objet : " + e.getMessage());
        }*/
    }
}
