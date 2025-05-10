import com.sun.jdi.InvalidTypeException;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Enseignant extends Utilisateur implements Serializable{
    Faculte fac;
    int annee;
    public Enseignant(String nom, String prenom, String password, String mat,double repC, double repP,Faculte fac) throws MatriculeException, ReputationException, InvalidTypeException{
        super(nom, prenom, password, mat, repC, repP);

        if (mat.charAt(4) != '2') {
            throw new InvalidTypeException("Ce matricule ne correspand pas a un matricule d'enseignant");
        }
        
        String temp = "";

        for (int i = 0; i < 4; i++) {
            temp = temp + mat.charAt(i);
        }
        
        this.annee = Integer.parseInt(temp);
        this.fac=fac;
    }
    
    public int getAnnee(){
        return this.annee;
    }
    
    public Faculte getFaculte() {
        return this.fac;
    }

    public void setFaculte(Faculte fac) {
        this.fac = fac;
    }

    @Override
    public void ajouterUtilisateur() throws UtilisateurExistDeja, UserBanException {
        ArrayList<Enseignant> teachers = new ArrayList<>();
        File file = new File("../FichiersDeSauvegarde/fichierEnseignant");
        
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
                        Enseignant existing = (Enseignant) in.readObject();

                        if (this.getMatricule().equals(existing.getMatricule())) {
                            throw new UtilisateurExistDeja("Utilisateur avec ce matricule existe déjà");
                        }
                        
                        teachers.add(existing);
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur lecture fichier: " + e.getMessage());
                throw new UtilisateurExistDeja("Erreur système - impossible d'ajouter l'enseignant");
            }
        }
        
        teachers.add(this);
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            for (Enseignant teacher : teachers) {
                out.writeObject(teacher);
            }
        } catch (IOException e) {
            System.err.println("Erreur écriture fichier: " + e.getMessage());
            throw new UtilisateurExistDeja("Erreur système - impossible d'enregistrer");
        }
    }

}
