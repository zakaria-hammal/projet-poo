import java.io.*;
import java.util.ArrayList;

public class Etudiant extends Utilisateur implements Serializable {
    int anneeAdmistion;
    Faculte fac;
    String specialite;

    public Etudiant(String nom, String prenom, String password, String mat, double repC, double repP, Faculte fac, String specialite) throws MatriculeException, ReputationException {
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