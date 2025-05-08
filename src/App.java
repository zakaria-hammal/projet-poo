
import com.sun.jdi.InvalidTypeException;
import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int choix = 0;

        Scanner sc = new Scanner(System.in);
        
        Utilisateur utilisateur = null;

        while (choix < 1 || choix > 3) {
            System.out.println("Bonjour\nPriere de choisir un des choix suivant :\n1- Se connecter\n2- S'inscrire\n3- Quitter");
            choix = Integer.parseInt(sc.nextLine());
            switch (choix) {
                case 1 -> {
                    String mat;
                    String password;
                    
                    while (utilisateur == null) {
                        System.out.print("Entrez votre matricule : \t");
                        mat = sc.nextLine();

                        System.out.print("Entrez votre mot de passe :\t");
                        password = sc.nextLine();

                        utilisateur = Utilisateur.login(mat, password);

                        if(utilisateur == null) {
                            System.out.println("Utilisateur introuvable\n Reesseyez!!!");
                        }
                        else {
                            System.out.println("Bienvenue " + utilisateur.getNom() + " " + utilisateur.getPrenom());
                        }
                    }
                    
                }
                
                case 2 -> {
                    int valid = 0;
                    while (valid == 0) { 
                        System.out.println("Vous etes un ...\n1- Etudiant\n2- Enseignat");
                        int type;
        
                        type = Integer.parseInt(sc.nextLine());
                        String password;
                        String nom;
                        String prenom;
                        String mat;
                        
                        System.out.print("Votre nom :\t");
                        nom = sc.nextLine();
                        System.out.print("Votre prenom :\t");
                        prenom = sc.nextLine();
        
                        System.out.print("Vos informations de connexion\nVotre matricule :\t");
                        mat = sc.nextLine();
                        System.out.print("Votre mot de passe :\t");
                        password = sc.nextLine();
        
                        switch (type) {
                            case 1 -> {
                                int numfac = 0;
                                Faculte faculte;
        
                                while (numfac < 1 || numfac > 9) { 
                                    System.out.println("Vous etes de la faculte :");
                                    for (Faculte elem : Faculte.values()) {
                                        System.out.println(String.valueOf((elem.ordinal() + 1)) + " - " + elem.toString());
                                    }  
        
                                    numfac = Integer.parseInt(sc.nextLine());
        
                                    if (numfac < 1 || numfac > 9) {
                                        System.out.println("Faculte invalid\n Reessayer");
                                    }
                                }
        
                                faculte = Faculte.values()[numfac - 1];
        
                                String specalite;
                                System.out.print("Votre specialite \t");
                                specalite = sc.nextLine();
        
                                try {
                                    utilisateur = new Etudiant(nom, prenom, password, mat, 2.5, 2.5, faculte, specalite);
                                    Etudiant.ajouterEtudiant((Etudiant) utilisateur);
                                    valid = 1;
                                } catch (MatriculeException | ReputationException | UtilisateurExistDeja | InvalidTypeException e) {
                                    System.out.println(e.getMessage());
                                }
                            }

                            case 2 -> {
                                int numfac = 0;
                                Faculte faculte;
        
                                while (numfac < 1 || numfac > 9) { 
                                    System.out.println("Vous etes de la faculte :");
                                    for (Faculte elem : Faculte.values()) {
                                        System.out.println(String.valueOf((elem.ordinal() + 1)) + " - " + elem.toString());
                                    }  
        
                                    numfac = Integer.parseInt(sc.nextLine());
        
                                    if (numfac < 1 || numfac > 9) {
                                        System.out.println("Faculte invalid\n Reessayer");
                                    }
                                }
        
                                faculte = Faculte.values()[numfac - 1];
        
                                try {
                                    utilisateur = new Enseignant(nom, prenom, password, mat, 2.5, 2.5, faculte);
                                    Enseignant.AjouterEnseignant((Enseignant) utilisateur);
                                    valid = 1;
                                    System.out.println("Bienvenue " + utilisateur.getNom() + " " + utilisateur.getPrenom());
                                } catch (MatriculeException | ReputationException | UtilisateurExistDeja | InvalidTypeException e) {
                                    System.out.println(e.getMessage());
                                }
                            }

                            default -> {
                                System.out.println("Type Invalid!!!\nReessayez");
                            }
                        }
                    }

                }
                    
                case 3 -> {
                    System.exit(0);
                }

                default -> {
                    System.out.println("Choix invalid !!!\nReessayer");
                }
            }

        }
        
        Profil profil = new Profil();
        choix = 0;
        System.out.println("Definir votre profil pour ajourd'hui : ");
        while (choix != 1 && choix != 2) {
            System.out.println("Vous etes un : \n1- Chauffeur\n2- Passager");
            choix = Integer.parseInt(sc.nextLine());

            if (choix != 1 && choix != 2) {
                System.out.println("Reessayer !!!");
            }
        }
        
        if(choix == 1) {
            profil.setStatus(Status.Chauffeur);
        }
        else {
            profil.setStatus(Status.Passager);
        }
    }
}
