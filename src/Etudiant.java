import com.sun.jdi.InvalidTypeException;
import java.io.*;
import java.util.ArrayList;

public class Etudiant extends Utilisateur implements Serializable {
    int anneeAdmistion;
    Faculte fac;
    String specialite;

    public Etudiant(String nom, String prenom, String password, String mat, double repC, double repP, Faculte fac, String specialite) throws MatriculeException, ReputationException, InvalidTypeException {
        super(nom, prenom, password, mat, repC, repP);

        if (mat.charAt(4) != '1') {
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

    public static void ajouterEtudiant(Etudiant x) throws UtilisateurExistDeja {
        ArrayList<Etudiant> students = new ArrayList<>();
        File file = new File("../FichiersDeSauvegarde/fichierEtudiant");
        
        
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                while (true) {
                    try {
                        Etudiant existing = (Etudiant) in.readObject();
                        if (x.getMatricule().equals(existing.getMatricule())) {
                            throw new UtilisateurExistDeja("Utilisateur avec ce matricule existe deja");
                        }
                        students.add(existing);
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur lecture fichier: " + e.getMessage());
                throw new UtilisateurExistDeja("Erreur système - impossible d'ajouter l'étudiant");
            }
        }
        
        
        students.add(x);
        
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            for (Etudiant student : students) {
                out.writeObject(student);
            }
        } catch (IOException e) {
            System.err.println("Erreur écriture fichier: " + e.getMessage());
            throw new UtilisateurExistDeja("Erreur système - impossible d'enregistrer");
        }
    }
}