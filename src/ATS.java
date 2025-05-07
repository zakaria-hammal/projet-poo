import java.io.*;
import java.util.ArrayList;

public class ATS extends Utilisateur implements Serializable {
    private final int AnneeDeRecrutement;
    private String ServiceDeRattachement;

    public ATS(String nom, String prenom, String password, String mat, double repC, double repP, String sdr)
            throws MatriculeException, ReputationException {
        super(nom, prenom, password, mat, repC, repP);
        String temp = "";

        for (int i = 0; i < 4; i++) {
            temp = temp + mat.charAt(i);
        }
        
        this.AnneeDeRecrutement = Integer.parseInt(temp);
        this.ServiceDeRattachement = sdr;
    }

    //Getters
    public int getAnneeDeRecrutement() {
        return this.AnneeDeRecrutement;
    }

    public String getServiceDeRattachement() {
        return this.ServiceDeRattachement;
    }
    //Setters

    public void setServiceDeRattachement(String sdr) {
        this.ServiceDeRattachement = sdr;
    }

    public void ajouterATS(String filePath) {
        File file = new File(filePath);
        ArrayList<ATS> existingATS = new ArrayList<>();

        // Read existing objects if the file exists and has content
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                while (true) {
                    try {
                        ATS ats = (ATS) in.readObject();
                        existingATS.add(ats);
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur lors de la lecture du fichier: " + e.getMessage());
            }
        }

        // Add this ATS instance to the list
        existingATS.add(this);

        // Write the updated list back to the file
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            for (ATS ats : existingATS) {
                out.writeObject(ats);
            }
            System.out.println("ATS ajouté avec succès.");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans le fichier: " + e.getMessage());
        }
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