import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ATS extends Utilisateur {
    private LocalDate AnneeDeRecrutement;
    private String ServiceDeRattachement;

    public ATS(String nom, String prenom, String username, String password, String mat, double rep, LocalDate adr, String sdr) {
        super(nom, prenom, username, password, mat, rep);
        this.ServiceDeRattachement = sdr;
        this.AnneeDeRecrutement = adr;
    }
    //Getters
    public LocalDate getAnneeDeRecrutement() {
        return this.AnneeDeRecrutement;
    }

    public String getServiceDeRattachement() {
        return this.ServiceDeRattachement;
    }
    //Setters
    public void setAnneeDeRecrutement(LocalDate adr) {
        this.AnneeDeRecrutement = adr;
    }

    public void setServiceDeRattachement(String sdr) {
        this.ServiceDeRattachement = sdr;
    }

    public void ajouterATS(String matricule) {

    }
    @Override
    public String toString(){
        return "Année de recrutement: "+ this.AnneeDeRecrutement + "Service de ratachement: "+ this.ServiceDeRattachement;
    }
    public void banUtilisateur(String matricule) {
        char typeUtilisateur = matricule.charAt(4);
        String sourcePath = "";

        if (typeUtilisateur == '1') {
            System.out.println("Erreur : les utilisateurs ATS ne peuvent pas être bannis.");
            return;
        } else if (typeUtilisateur == '2') {
            sourcePath = "../FichiersDeSauvegarde/fichierEnseignant";
        } else if (typeUtilisateur == '3') {
            sourcePath = "../FichiersDeSauvegarde/fichierEtudiant";
        } else {
            System.out.println("Matricule invalide.");
            return;
        }

        File sourceFile = new File(sourcePath);
        File bannedFile = new File("../FichiersDeSauvegarde/bannedUsers");

        ArrayList<Utilisateur> sourceList = new ArrayList<>();
        ArrayList<Utilisateur> bannedList = new ArrayList<>();
        Utilisateur cible = null;
        boolean cibleInSource = false;
        boolean cibleInBanned = false;

        // Lire source
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(sourceFile));
            while (true) {
                try {
                    Utilisateur u = (Utilisateur) in.readObject();
                    if (!u.getMatricule().equals(matricule)) {
                        sourceList.add(u);
                    } else {
                        cible = u;
                        cibleInSource = true;
                    }
                } catch (EOFException e) {
                    break;
                }
            }
            in.close();
        } catch (Exception e) {
            System.out.println("Erreur de lecture du fichier source.");
        }

        // Lire bannis
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(bannedFile));
            while (true) {
                try {
                    Utilisateur u = (Utilisateur) in.readObject();
                    if (u.getMatricule().equals(matricule)) {
                        cibleInBanned = true;
                    }
                    bannedList.add(u);
                } catch (EOFException e) {
                    break;
                }
            }
            in.close();
        } catch (Exception e) {
            // fichier bannis peut être vide
        }

        // Analyse des cas
        if (!cibleInSource && !cibleInBanned) {
            System.out.println("Erreur : utilisateur introuvable.");
            return;
        } else if (!cibleInSource && cibleInBanned) {
            System.out.println("Erreur : utilisateur déjà banni.");
            return;
        }

        // Réécriture fichier source
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(sourceFile));
            for (Utilisateur u : sourceList) {
                out.writeObject(u);
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier source.");
            return;
        }

        // Ajout dans bannis
        try {
            bannedList.add(cible);
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(bannedFile));
            for (Utilisateur u : bannedList) {
                out.writeObject(u);
            }
            out.close();
            System.out.println("Utilisateur banni avec succès.");
        } catch (IOException e) {
            System.out.println("Erreur lors de l'ajout à la liste des bannis.");
        }
    }

    public void unbanUtilisateur(String matricule) {
        char typeUtilisateur = matricule.charAt(4);
        String sourcePath = "";

        if (typeUtilisateur == '2') {
            sourcePath = "../FichiersDeSauvegarde/fichierEnseignant";
        } else if (typeUtilisateur == '3') {
            sourcePath = "../FichiersDeSauvegarde/fichierEtudiant";
        } else {
            System.out.println("Matricule invalide.");
            return;
        }

        File sourceFile = new File(sourcePath);
        File bannedFile = new File("../FichiersDeSauvegarde/bannedUsers");

        ArrayList<Utilisateur> bannedList = new ArrayList<>();
        ArrayList<Utilisateur> sourceList = new ArrayList<>();
        Utilisateur cible = null;
        boolean cibleInBanned = false;
        boolean cibleInSource = false;

        // Lire bannis
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(bannedFile));
            while (true) {
                try {
                    Utilisateur u = (Utilisateur) in.readObject();
                    if (u.getMatricule().equals(matricule)) {
                        cible = u;
                        cibleInBanned = true;
                    } else {
                        bannedList.add(u);
                    }
                } catch (EOFException e) {
                    break;
                }
            }
            in.close();
        } catch (Exception e) {
            System.out.println("Erreur lecture fichier bannis.");
        }

        // Lire source
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(sourceFile));
            while (true) {
                try {
                    Utilisateur u = (Utilisateur) in.readObject();
                    if (u.getMatricule().equals(matricule)) {
                        cibleInSource = true;
                    }
                    sourceList.add(u);
                } catch (EOFException e) {
                    break;
                }
            }
            in.close();
        } catch (Exception e) {
            // peut être vide
        }

        if (!cibleInBanned && !cibleInSource) {
            System.out.println("Erreur : utilisateur introuvable.");
            return;
        } else if (!cibleInBanned && cibleInSource) {
            System.out.println("Erreur : utilisateur déjà débanni.");
            return;
        }

        // Écrire nouveau fichier bannis
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(bannedFile));
            for (Utilisateur u : bannedList) {
                out.writeObject(u);
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Erreur écriture fichier bannis.");
            return;
        }

        // Ajouter à source
        try {
            sourceList.add(cible);
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(sourceFile));
            for (Utilisateur u : sourceList) {
                out.writeObject(u);
            }
            out.close();
            System.out.println("Utilisateur débanni avec succès.");
        } catch (IOException e) {
            System.out.println("Erreur restauration utilisateur.");
        }
    }

}
