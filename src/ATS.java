import com.sun.jdi.InvalidTypeException;
import java.io.*;
import java.util.ArrayList;

public class ATS extends Utilisateur implements Serializable {

    private int AnneeDeRecrutement;
    private String ServiceDeRattachement;

    public ATS(String nom, String prenom, String password, String mat, double repC, double repP, String sdr)
            throws MatriculeException, ReputationException, InvalidTypeException {
        super(nom, prenom, password, mat, repC, repP);

        if (mat.charAt(4) != '1') {
            throw new InvalidTypeException("Ce matricule ne correspand pas a un matricule d'enseignant");
        }

        String temp = "";

        for (int i = 0; i < 4; i++) {
            temp = temp + mat.charAt(i);
        }
        
        this.AnneeDeRecrutement = Integer.parseInt(temp);
        this.ServiceDeRattachement = sdr;
    }

    // Getters and Setters
    public int getAnneeDeRecrutement() {
        return this.AnneeDeRecrutement;
    }

    public String getServiceDeRattachement() {
        return this.ServiceDeRattachement;
    }

    public void setAnneeDeRecrutement(int adr) {
        this.AnneeDeRecrutement = adr;
    }

    public void setServiceDeRattachement(String sdr) {
        this.ServiceDeRattachement = sdr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ATS e = (ATS) o;
        return this.getMatricule().equals(e.getMatricule());
    }

    @Override
    public void ajouterUtilisateur() throws UtilisateurExistDeja, UserBanException {
        String filePath = "../FichiersDeSauvegarde/fichierATS";
        File file = new File(filePath);
        ArrayList<ATS> existingATS = new ArrayList<>();

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
                        ATS ats = (ATS) in.readObject();

                        if (this.getMatricule().equals(ats.getMatricule())) {
                            throw new UtilisateurExistDeja("Utilisateur avec ce matricule existe deja");
                        }

                        existingATS.add(ats);
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur écriture fichier: " + e.getMessage());
            }
        }

        existingATS.add(this);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            for (ATS ats : existingATS) {
                out.writeObject(ats);
            }
        } catch (IOException e) {
            System.err.println("Erreur écriture fichier: " + e.getMessage());
        }
    }

    public void banUtilisateur(String matricule) throws UserBanException {
        char typeUtilisateur = matricule.charAt(4);
        String sourcePath;

        switch (typeUtilisateur) {
            case '1' -> throw new UserBanException("Les utilisateurs ATS ne peuvent pas être bannis.");
            case '2' -> sourcePath = "../FichiersDeSauvegarde/fichierEnseignant";
            case '3' -> sourcePath = "../FichiersDeSauvegarde/fichierEtudiant";
            default -> throw new UserBanException("Matricule invalide.");
        }

        File sourceFile = new File(sourcePath);
        File bannedFile = new File("../FichiersDeSauvegarde/bannedUsers");

        ArrayList<Utilisateur> sourceList = new ArrayList<>();
        ArrayList<Utilisateur> bannedList = new ArrayList<>();
        Utilisateur cible = null;
        boolean cibleInSource = false;
        boolean cibleInBanned = false;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(sourceFile))) {
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
        } catch (IOException | ClassNotFoundException e) {
            throw new UserBanException("Erreur de lecture du fichier source.");
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(bannedFile))) {
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
        } catch (IOException | ClassNotFoundException ignored) {
        }

        if (!cibleInSource && !cibleInBanned) {
            throw new UserBanException("Utilisateur introuvable.");
        } else if (!cibleInSource && cibleInBanned) {
            throw new UserBanException("Utilisateur déjà banni.");
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(sourceFile))) {
            for (Utilisateur u : sourceList) {
                out.writeObject(u);
            }
        } catch (IOException e) {
            throw new UserBanException("Erreur lors de l'écriture du fichier source.");
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(bannedFile))) {
            bannedList.add(cible);
            for (Utilisateur u : bannedList) {
                out.writeObject(u);
            }
        } catch (IOException e) {
            throw new UserBanException("Erreur lors de l'ajout à la liste des bannis.");
        }
    }

    public void unbanUtilisateur(String matricule) throws UserUnbanException {
        char typeUtilisateur = matricule.charAt(4);
        String sourcePath;

        switch (typeUtilisateur) {
            case '2' -> sourcePath = "../FichiersDeSauvegarde/fichierEnseignant";
            case '3' -> sourcePath = "../FichiersDeSauvegarde/fichierEtudiant";
            default -> throw new UserUnbanException("Matricule invalide.");
        }

        File sourceFile = new File(sourcePath);
        File bannedFile = new File("../FichiersDeSauvegarde/bannedUsers");

        ArrayList<Utilisateur> bannedList = new ArrayList<>();
        ArrayList<Utilisateur> sourceList = new ArrayList<>();
        Utilisateur cible = null;
        boolean cibleInBanned = false;
        boolean cibleInSource = false;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(bannedFile))) {
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
        } catch (IOException | ClassNotFoundException e) {
            throw new UserUnbanException("Erreur lecture fichier bannis.");
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(sourceFile))) {
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
        } catch (IOException | ClassNotFoundException ignored) {
        }

        if (!cibleInBanned && !cibleInSource) {
            throw new UserUnbanException("Utilisateur introuvable.");
        } else if (!cibleInBanned && cibleInSource) {
            throw new UserUnbanException("Utilisateur déjà débanni.");
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(bannedFile))) {
            for (Utilisateur u : bannedList) {
                out.writeObject(u);
            }
        } catch (IOException e) {
            throw new UserUnbanException("Erreur écriture fichier bannis.");
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(sourceFile))) {
            sourceList.add(cible);
            for (Utilisateur u : sourceList) {
                out.writeObject(u);
            }
        } catch (IOException e) {
            throw new UserUnbanException("Erreur restauration utilisateur.");
        }
    }
}
