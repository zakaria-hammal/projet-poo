import java.io.*;
<<<<<<< HEAD
import java.util.ArrayList;
=======
import java.io.Serializable;

>>>>>>> b81cc453c10b972ad95022a46ad6187d36d81da6
public class Etudiant extends Utilisateur implements Serializable {
    int anneeAdmistion;
    Faculte fac;
    String specialite;

<<<<<<< HEAD
    public Etudiant(String nom, String prenom, String password, String mat, double repC, double repP, Faculte fac, String specialite) throws MatriculeException, ReputationException {
=======
    public Etudiant(String nom, String prenom, String password, String mat,
                    double repC, double repP, int anneeAdmistion, Faculte fac, String specialite)
            throws MatriculeException, ReputationException {
>>>>>>> b81cc453c10b972ad95022a46ad6187d36d81da6
        super(nom, prenom, password, mat, repC, repP);
        String temp = "";

        for (int i = 0; i < 4; i++) {
            temp = temp + mat.charAt(i);
        }
        
        this.anneeAdmistion = Integer.parseInt(temp);
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
<<<<<<< HEAD
=======
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
>>>>>>> b81cc453c10b972ad95022a46ad6187d36d81da6

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public void setFac(Faculte fac) {
        this.fac = fac;
    }

    public static void AjouterEtudiant(Etudiant x) throws UtilisateurExistDeja {
        ArrayList<Etudiant> temp = new ArrayList<>();
        Etudiant etudiant;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierEtudiant"));
            while (true) {
                try {
                    etudiant = (Etudiant) in.readObject();
                    if (x.getMatricule() == null ? etudiant.getMatricule() == null : x.getMatricule().equals(etudiant.getMatricule())) {
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

        temp.add(x);

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("../FichiersDeSauvegarde/fichierEtudiant"));
            for (Etudiant elem : temp) {
                out.writeObject((Etudiant) elem);
            }
            
        } catch (IOException e) {
            System.err.println("Une erreur est survenue lors de l'écriture de l'objet : " + e.getMessage());
        }
    }
    }
