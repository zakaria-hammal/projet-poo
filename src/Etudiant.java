/*
 * Preparee par BOUDJEMAA Wassim
 */

import com.sun.jdi.InvalidTypeException;
import java.io.*;
import java.util.ArrayList;

public class Etudiant extends Utilisateur implements Serializable {
    private int anneeAdmistion;
    private Faculte fac;
    private String specialite;

    public Etudiant(String nom, String prenom, String password, String mat,  Faculte fac, String specialite) throws MatriculeException, ReputationException, InvalidTypeException {
        super(nom, prenom, password, mat);

        if (mat.charAt(4) != '3') {
            throw new InvalidTypeException("Ce matricule ne correspand pas a un matricule d'etudiant");
        }
        
        String temp = "";

        for (int i = 0; i < 4; i++) {
            temp = temp + mat.charAt(i);
        }
        
        this.anneeAdmistion = Integer.parseInt(temp);
        this.specialite = specialite;
        this.fac=fac;
    }

    public Etudiant(String nom, String prenom, String password, String mat, double repC, double repP, Faculte fac, String specialite) throws MatriculeException, ReputationException, InvalidTypeException {
        super(nom, prenom, password, mat, repC, repP);

        if (mat.charAt(4) != '3') {
            throw new InvalidTypeException("Ce matricule ne correspand pas a un matricule d'etudiant");
        }
        
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
    
    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public void setFac(Faculte fac) {
        this.fac = fac;
    }

    // Corrigee par HAMMAL Zakaria
    @Override
    public void ajouterUtilisateur() throws UtilisateurExistDeja, UserBanException {
        ArrayList<Etudiant> students = new ArrayList<>();
        File file = new File("../FichiersDeSauvegarde/fichierEtudiant");
        
        File bannedUsersFile = new File("../FichiersDeSauvegatde/bannedUsers");

        if(bannedUsersFile.exists() && bannedUsersFile.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(bannedUsersFile))){
                while (true) {
                    try {
                        Etudiant temp = (Etudiant) in.readObject();
                        
                        if (this.getMatricule().equals(temp.getMatricule())) {
                            throw new UserBanException("Vous etes bannis");
                        }

                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur lecture fichier: " + e.getMessage());
            }
        }

        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                while (true) {
                    try {
                        Etudiant existing = (Etudiant) in.readObject();

                        if (this.getMatricule().equals(existing.getMatricule())) {
                            throw new UtilisateurExistDeja("Utilisateur avec ce matricule existe deja");
                        }
                        
                        students.add(existing);
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur lecture fichier: " + e.getMessage());
            }
        }
        
        students.add(this);
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            for (Etudiant student : students) {
                out.writeObject(student);
            }
        } catch (IOException e) {
            System.err.println("Erreur écriture fichier: " + e.getMessage());
        }
    }
}