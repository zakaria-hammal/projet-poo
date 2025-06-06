/*
 * Prepare par TRIBECHE Rayan / HAMMAL Zakaria
 */

import com.sun.jdi.InvalidTypeException;
import java.io.*;
import java.security.spec.InvalidParameterSpecException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int choix;

        Scanner sc = new Scanner(System.in);
        
        Utilisateur utilisateur;
        
        while (true) { 
            utilisateur = null;
            choix = 0;
            while (choix < 1 || choix > 3) {
                System.out.println("Bonjour\nPriere de choisir un des choix suivants :\n1- Se connecter\n2- S'inscrire\n3- Quitter");
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
                                // Si l'utilisateur est un ATS, afficher le menu ATS
                                if (utilisateur instanceof ATS) {
                                    afficherMenuATS(sc, (ATS) utilisateur);
                                }
                            }
                        }
                        
                    }
                    
                    case 2 -> {
                        int valid = 0;
                        while (valid == 0) { 
                            System.out.println("Vous etes un ...\n1- Etudiant\n2- Enseignant");
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
                                        ((Etudiant) utilisateur).ajouterUtilisateur();
                                        valid = 1;
                                    } catch (MatriculeException | ReputationException | UserBanException | UtilisateurExistDeja | InvalidTypeException e) {
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
                                        ((Enseignant) utilisateur).ajouterUtilisateur();
                                        valid = 1;
                                        System.out.println("Bienvenue " + utilisateur.getNom() + " " + utilisateur.getPrenom());
                                    } catch (MatriculeException | ReputationException | UserBanException | UtilisateurExistDeja | InvalidTypeException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }

                                default -> {
                                    System.out.println("Type Invalid!!!\nReessayez");
                                }
                            }
                        }

                        if (utilisateur instanceof ATS) {
                            afficherMenuATS(sc, (ATS) utilisateur);
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

            if (utilisateur == null) {
                System.exit(0);
            }

            Profil profil = utilisateur.getProfil();

            if(profil == null) {
                profil = new Profil();
            }

            choix = 0;
            System.out.println("Definir votre profil pour ajourd'hui : ");
            while (choix != 1 && choix != 2) {
                System.out.println("Vous etes un : \n1- Chauffeur\n2- Passager");
                try {
                    choix = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Entier Invalide");
                }

                if (choix != 1 && choix != 2) {
                    System.out.println("Reessayer !!!");
                }
            }
            
            Preferences pref = new Preferences();
            int choixPref = 0;

            System.out.println("Preciser vos preferences : ");
            System.out.println("Acceptez-vous des garçons durant la course ?\n1- Oui\n2- Non");

            while (choixPref != 1 && choixPref != 2) {
                try {
                    choixPref = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Entier Invalide");
                }

                if (choixPref != 1 && choixPref != 2) {
                    System.out.println("Reesseyer !!!");
                }
            }

            if (choixPref == 1) {
                pref.setAccepterGarcon(true);
            }
            else {
                pref.setAccepterGarcon(true);
            }

            System.out.println("Acceptez-vous des filles durant la course ?\n1- Oui\n2- Non");
            
            choixPref = 0;
            while (choixPref != 1 && choixPref != 2) {
                choixPref = Integer.parseInt(sc.nextLine());

                if (choixPref != 1 && choixPref != 2) {
                    System.out.println("Reesseyer !!!");
                }
            }

            if (choixPref == 1) {
                pref.setAccepterFilles(true);
            }
            else {
                pref.setAccepterFilles(true);
            }

            System.out.println("Acceptez-vous de la musique durant la course ?\n1- Oui\n2- Non");

            choixPref = 0;
            while (choixPref != 1 && choixPref != 2) {
                choixPref = Integer.parseInt(sc.nextLine());

                if (choixPref != 1 && choixPref != 2) {
                    System.out.println("Reesseyer !!!");
                }
            }

            if (choixPref == 1) {
                pref.setAccepterMusique(true);
            }
            else {
                pref.setAccepterMusique(true);
            }

            System.out.println("Acceptez-vous des bagages durant la course ?\n1- Oui\n2- Non");

            choixPref = 0;
            while (choixPref != 1 && choixPref != 2) {
                choixPref = Integer.parseInt(sc.nextLine());

                if (choixPref != 1 && choixPref != 2) {
                    System.out.println("Reesseyer !!!");
                }
            }

            if (choixPref == 1) {
                pref.setAccepterBagages(true);
            }
            else {
                pref.setAccepterBagages(true);
            }

            String commentaires;

            System.out.println("Autres commentaires : ");
            commentaires = sc.nextLine();

            pref.setAutres(commentaires);

            profil.setPreferences(pref);

            if(choix == 1) {
                profil.setStatus(Status.Chauffeur);
                int repet = 0;
                
                Point point;
                ArrayList<Point> points = new ArrayList<>();

                System.out.println("Definir les points par lesquels vous allez passer :");
                while (repet == 0) {
                    choix = 0;
                    while (choix < 1 || choix > Point.values().length) {
                        for (Point elem : Point.values()) {
                            System.out.println(String.valueOf(elem.ordinal() + 1) + "- " + elem.toString());
                        }

                        choix = Integer.parseInt(sc.nextLine());
        
                        if(choix < 1 || choix > Point.values().length) {
                            System.out.println("Reessayer");
                        }
                    }

                    point = Point.values()[choix - 1];
                    points.add(point);

                    System.out.println("Ajouter ?\n1 - Oui\n2- Non");
                    
                    repet = -1;
                    while (repet != 0 && repet != 1) {
                        repet = Integer.parseInt(sc.nextLine()) - 1;
                    }

                }

                profil.setItenairaireChauffeur(points);

                TypeCourse type;

                choix = 0;
                while (choix < 1 || choix > TypeCourse.values().length) {
                    System.out.println("Votre type de course");
                    for (TypeCourse elem : TypeCourse.values()) {
                        System.out.println(String.valueOf(elem.ordinal() + 1) + "- " + elem.toString());
                    }

                    choix = Integer.parseInt(sc.nextLine());

                    if(choix < 1 || choix > TypeCourse.values().length) {
                        System.out.println("Reessayer");
                    }

                }

                type = TypeCourse.values()[choix - 1];
                profil.setTypeCourse(type);

                System.out.println("Changer les horaires de disponibilite ?\n1- Oui");
                choix = Integer.parseInt(sc.nextLine());

                if (choix == 1) {
                    System.out.println("Horaires de disponibilité (hh:mm) :");
                    ArrayList<ArrayList<LocalTime>> horaires = new ArrayList<>();

                    ArrayList<LocalTime> pourUnJour;

                    String[] joursDeLaSemaine = {"Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
                    
                    for (int i = 0; i < 7; i++) {
                        choix = 1;
                        pourUnJour = new ArrayList<>();

                        while (choix == 1) {
                            System.out.println("Pour le " + joursDeLaSemaine[i] + "\n1- Ajouter\n2- Jour Suivant");
                            choix = Integer.parseInt(sc.nextLine());

                            if(choix == 1) {
                                System.out.print("Entrez l'heure :\t");
                                pourUnJour.add(LocalTime.parse(sc.nextLine()));
                            }
                        }
                        
                        horaires.add(pourUnJour);
                    }
                    
                    profil.setHoraires(horaires);
                }
                
            }
            else {
                Point point;
                profil.setStatus(Status.Passager);
                choix = 0;
                while (choix < 1 || choix > Point.values().length) {
                    System.out.println("Ou voulez vous qu'on vous recupere ?");
                    for (Point elem : Point.values()) {
                        System.out.println(String.valueOf(elem.ordinal() + 1) + "- " + elem.toString());
                    }

                    choix = Integer.parseInt(sc.nextLine());
                    if(choix < 1 || choix > Point.values().length) {
                        System.out.println("Reessayer");
                    }

                }
                
                point = Point.values()[choix - 1];
                profil.setItenairairePassager(point);
            }

            utilisateur.setProfil(profil);

            ArrayList<Course> courses;

            if(profil.getStatus() == Status.Chauffeur) {
                Course courseCourrente = null;
                boolean stayLoggedIn = true;
                while (stayLoggedIn) {
                    choix = 0;
                    while (choix != 1 && choix != 2 && choix != 3) {
                        if (courseCourrente == null) {
                            System.out.println("1- Plannifiee une course\n2- Commencer une course\n3- Quitter");
                        }
                        else {
                            System.out.println("1- Plannifiee une course\n2- Terminer une course\n3- Quitter");
                        }

                        choix = Integer.parseInt(sc.nextLine());

                        if (choix != 1 && choix != 2 && choix !=3) {
                            System.out.println("Reessayer !!!");
                        }
                    }

                    Course course;
                    switch (choix) {
                        case 1 -> {
                            LocalDateTime dateTime;
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                            while (true) {
                                try {
                                    System.out.print("Date et temps prevu : (dd/MM/yyyy HH:mm) \t");
                                    dateTime = LocalDateTime.parse(sc.nextLine(), formatter);
                                    break;   
                                } catch (DateTimeParseException e) {
                                    System.out.println("Format invalide");
                                }   
                            }

                            try {
                                course = new Course(utilisateur, dateTime);
                                try {
                                    course.plannifiee();
                                } catch (CourseInvalide ex) {
                                    System.out.println(ex.getMessage());
                                }

                                System.out.println("Course planifieee avec succes");
                            } catch (StatutInvalideException | TropTardException | IOException | DateTimeParseException ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                    
                        case 2 -> {
                            if (courseCourrente == null) {
                                courses = Course.getCoursesPlanifiee(utilisateur);
                                if(courses == null || courses.isEmpty()) {
                                    System.out.println("Vous n'avez pas de courses planifiee");
                                }
                                else {
                                    choix = -1;
                                    while (choix < 0 || choix > courses.size()) { 
                                        for (int i = 0; i < courses.size(); i++) {
                                            System.out.println(String.valueOf(i + 1) + "- Prévu le : " + courses.get(i).getHeureDatePrevu());
                                        }
                
                                        System.out.print("Choisir : \t");
                                        choix = Integer.parseInt(sc.nextLine()) - 1;
                                        
                                        if (choix < 1 || choix > courses.size()) {
                                            System.out.println("Reessayez !!!");
                                        }
                                    }
                                    
                                    try {
                                        courseCourrente = courses.get(choix);
                                        courseCourrente.commencer();
                                        System.out.println("Course commencee avec succes");  
                                    } catch (TropTotException ex) {
                                        System.out.println(ex.getMessage());
                                        courseCourrente = null;
                                    }
                                }
                                    
                            }
                            else {
                                int evaC;
                                System.out.print("Evaluation du chauffeur : \t");
                                evaC = Integer.parseInt(sc.nextLine());

                                String commentaireC;
                                System.out.print("Commentaire du chauffeur : \t");
                                commentaireC = sc.nextLine();

                                int[] evaP = new int[courseCourrente.getNbPassager()];
                                
                                ArrayList<String> commentairesP = new ArrayList<>();
                                for(int i = 0; i < evaP.length; i++) {
                                    System.out.print("Evaluation du passager " + (i + 1)+ " :\t");
                                    evaP[i] = Integer.parseInt(sc.nextLine());
                                    System.out.print("Commentaire du passager " + (i + 1)+ " :\t");
                                    commentairesP.add(sc.nextLine());
                                }
                                
                                try {
                                    courseCourrente.terminer(evaC, evaP, commentaireC, commentairesP);
                                } catch (EvaluationInvalideException | InvalidParameterSpecException ex) {
                                    System.out.println(ex.getMessage());
                                }
                            }

                        }
                        
                        case 3 -> {
                            stayLoggedIn = false;
                        }

                        default -> {
                            System.out.println("Choix invalide !!!");
                        }
                    
                    }
                }
            }
            else {
                boolean stayLoggedIn  = true;
                while (stayLoggedIn) {
                    choix = 0;
                    while (choix != 1 && choix != 2) {
                        System.out.println("1- Rejoidre une course\n2- Quitter");
                        choix = Integer.parseInt(sc.nextLine());

                        if (choix != 1 && choix != 2) {
                            System.out.println("Reessayer !!!");
                        }
                    }
                    
                    switch (choix) {
                        case 1 -> {
                            courses = Course.getCoursesCompatible(utilisateur);
                            if(courses == null || courses.isEmpty()) {
                            System.out.println("Pas courses à rejoindre !!!");
                            }
                            else {
                                System.out.println("Voici la liste des courses qui sont compatibles avec vos exigences :");
                                for (int i = 0; i < courses.size(); i++) {
                                    System.out.println(String.valueOf(i + 1) + "- Prévu le : " + courses.get(i).getHeureDatePrevu());
                                }

                                System.out.print("Choisir : \t");
                                choix = -1;
                                while (choix < 0 || choix > courses.size()) {
                                    choix = Integer.parseInt(sc.nextLine()) - 1;
                                    
                                    if (choix < 0 || choix > courses.size()) {
                                        System.out.println("Reessayer");
                                    }
                                }

                                try {
                                    courses.get(choix).ajouterPassager(utilisateur);

                                    System.out.println("Ajoute avec succes");
                                } catch (CourseCompleteException | EtatCourseInvalideException | StatutInvalideException ex) {
                                    System.out.println(ex.getMessage());
                                } 
                            }   
                        }
                        
                        case 2 -> {
                            stayLoggedIn = false;
                        }
                    
                        default -> {
                            System.out.println("Choix invalide !!!");
                        }
                    }
                    
                }
                
            }   
        }

}
    
    private static void afficherMenuATS(Scanner sc, ATS ats) {
        int choix = 0;

        while (choix != 11) {
            System.out.println("\n--- Menu Administration ---");
            System.out.println("1- Afficher les étudiants");
            System.out.println("2- Afficher les enseignants");
            System.out.println("3- Afficher les ATS");
            System.out.println("4- Afficher les courses en cours");
            System.out.println("5- Afficher le planning journalier");
            System.out.println("6- Afficher le planning hebdomadaire");
            System.out.println("7- Afficher l'historique des courses passées");
            System.out.println("8- Bannir un utilisateur");
            System.out.println("9- Débannir un utilisateur");
            System.out.println("10- Afficher les statistiques");
            System.out.println("11- Quitter");

            try {
                choix = Integer.parseInt(sc.nextLine());

                switch (choix) {
                    case 1 -> afficherEtudiants(ats);
                    case 2 -> afficherEnseignants(ats);
                    case 3 -> afficherATS(ats);
                    case 4 -> afficherCoursesEnCours(ats);
                    case 5 -> afficherPlanningJournalier(ats);
                    case 6 -> afficherPlanningHebdomadaire(ats);
                    case 7 -> afficherCoursesPassees(ats);
                    case 8 -> {
                        System.out.print("Entrez le matricule de l'utilisateur à bannir: ");
                        String matricule = sc.nextLine();
                        try {
                            ats.banUtilisateur(matricule);
                            System.out.println("Utilisateur " + matricule + " banni avec succès."); // Message de succès si aucune exception
                        } catch (UserBanException e) {
                            System.err.println("Erreur lors du bannissement: " + e.getMessage());
                        }
                    }
                    case 9 -> {
                        System.out.print("Entrez le matricule de l'utilisateur à débannir: ");
                        String matricule = sc.nextLine();
                        try {
                            ats.unbanUtilisateur(matricule);
                            System.out.println("Utilisateur " + matricule + " débanni avec succès."); // Message de succès si aucune exception
                        } catch (UserUnbanException e) {
                            System.err.println("Erreur lors du débannissement: " + e.getMessage());
                        }
                    }
                    case 10 ->{
                        afficherStatistiques(ats);
                    }
                    case 11 -> System.out.println("Retour au menu principal");
                    default -> System.out.println("Choix invalide!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }
    }

    private static boolean verifierATS(Utilisateur ats) {
        if (!(ats instanceof ATS)) {
            System.out.println("Erreur: Seuls les utilisateurs ATS ont accès à cette fonctionnalité.");
            return false;
        }
        return true;
    }

    public static void afficherEtudiants(Utilisateur ats) {
        if (!verifierATS(ats)) return;

        File fichierEtudiant = new File("../FichiersDeSauvegarde/fichierEtudiant");

        if (!fichierEtudiant.exists() || fichierEtudiant.length() == 0) {
            System.out.println("Aucun étudiant enregistré.");
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichierEtudiant))) {
            System.out.println("\n--- Liste des étudiants ---");
            System.out.println("Matricule\tNom\tPrénom\tFaculté\tSpécialité\tRéputation");
            System.out.println("-----------------------------------------------------------");

            while (true) {
                try {
                    Etudiant etudiant = (Etudiant) in.readObject();
                    System.out.println(etudiant.getMatricule() + "\t" +
                            etudiant.getNom() + "\t" +
                            etudiant.getPrenom() + "\t" +
                            etudiant.getFaculte() + "\t" +
                            etudiant.getSpecialite() + "\t" +
                            etudiant.getReputationChauffeur() + "/" + etudiant.getReputationPassager());
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur lors de la lecture du fichier étudiant: " + e.getMessage());
        }
    }

    
    public static void afficherEnseignants(Utilisateur ats) {
        if (!verifierATS(ats)) return;

        File fichierEnseignant = new File("../FichiersDeSauvegarde/fichierEnseignant");

        if (!fichierEnseignant.exists() || fichierEnseignant.length() == 0) {
            System.out.println("Aucun enseignant enregistré.");
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichierEnseignant))) {
            System.out.println("\n--- Liste des enseignants ---");
            System.out.println("Matricule\tNom\tPrénom\tFaculté\tRéputation");
            System.out.println("-------------------------------------------");

            while (true) {
                try {
                    Enseignant enseignant = (Enseignant) in.readObject();
                    System.out.println(enseignant.getMatricule() + "\t" +
                            enseignant.getNom() + "\t" +
                            enseignant.getPrenom() + "\t" +
                            enseignant.getFaculte() + "\t" +
                            enseignant.getReputationChauffeur() + "/" + enseignant.getReputationPassager());
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur lors de la lecture du fichier enseignant: " + e.getMessage());
        }
    }

    public static void afficherATS(Utilisateur ats) {
        if (!verifierATS(ats)) return;

        File fichierATS = new File("../FichiersDeSauvegarde/fichierATS");

        if (!fichierATS.exists() || fichierATS.length() == 0) {
            System.out.println("Aucun ATS enregistré.");
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichierATS))) {
            System.out.println("\n--- Liste des ATS ---");
            System.out.println("Matricule\tNom\tPrénom\tAnnée Recrutement\tService\tRéputation");
            System.out.println("----------------------------------------------------------------");

            while (true) {
                try {
                    ATS atsUser = (ATS) in.readObject();
                    System.out.println(atsUser.getMatricule() + "\t" +
                            atsUser.getNom() + "\t" +
                            atsUser.getPrenom() + "\t" +
                            atsUser.getAnneeDeRecrutement() + "\t" +
                            atsUser.getServiceDeRattachement() + "\t" +
                            atsUser.getReputationChauffeur() + "/" + atsUser.getReputationPassager());
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur lors de la lecture du fichier ATS: " + e.getMessage());
        }
    }

    public static void afficherCoursesEnCours(Utilisateur ats) {
        if(!verifierATS(ats)) return;

        File fichierCoursesEnCours = new File("../FichiersDeSauvegarde/fichierCourseEnCours");

        if(!fichierCoursesEnCours.exists() || fichierCoursesEnCours.length() == 0) {
            System.out.println("Aucune course en cours.");
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichierCoursesEnCours))) {
            System.out.println("\n--- Courses en cours ---");

            System.out.println("Chauffeur\tType de trajet\tNombre de passagers\tDate et heure prévue");
            System.out.println("--------------------------------------------------------------------------------");

            while (true) {
                try {
                    Course course = (Course) in.readObject();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");//C'es tpour afficher dans un meilleur format
                    System.out.println(course.getChauffeur().getNom() + " " + course.getChauffeur().getPrenom() + "\t" +
                            course.getTypeTrajet() + "\t" +
                            course.getPassagers().size() + "\t" +
                            course.getHeureDatePrevu().format(formatter));
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur lors de la lecture des courses en cours: " + e.getMessage());
        }
    }

    public static void afficherPlanningJournalier(Utilisateur ats) {
        if(!verifierATS(ats)) return;

        File fichierPlanning = new File("../FichiersDeSauvegarde/fichierCoursePlanifiee");

        if(!fichierPlanning.exists() || fichierPlanning.length() == 0) {
            System.out.println("Aucun planning journalier disponible.");
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichierPlanning))) {
            System.out.println("\n--- Planning Journalier ---");

            System.out.println("Chauffeur\tType de trajet\tPoints d'arrêt\tNombre de passagers\tDate et heure prévue");
            System.out.println("--------------------------------------------------------------------------------------------------");

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endOfDay = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59);

            while (true) {
                try {
                    Course course = (Course) in.readObject();

                    // Afficher seulement les courses prévues pour aujourd'hui
                    if (course.getHeureDatePrevu().isAfter(now) && course.getHeureDatePrevu().isBefore(endOfDay)) {
                        StringBuilder pointsArret = new StringBuilder();
                        for (Point point : course.getPointsArret()) {
                            pointsArret.append(point.toString()).append(", ");
                        }
                        String pointsArretStr = pointsArret.length() > 2 ?
                                pointsArret.substring(0, pointsArret.length() - 2) : pointsArret.toString();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");//C'es tpour afficher dans un meilleur format
                        System.out.println(course.getChauffeur().getNom() + " " + course.getChauffeur().getPrenom() + "\t" +
                                course.getTypeTrajet() + "\t" +
                                pointsArretStr + "\t" +
                                course.getPassagers().size() + "\t" +
                                course.getHeureDatePrevu().format(formatter));
                    }
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur lors de la lecture du planning journalier: " + e.getMessage());
        }
    }

    public static void afficherPlanningHebdomadaire(Utilisateur ats) {
        if(!verifierATS(ats)) return;

        File fichierPlanning = new File("../FichiersDeSauvegarde/fichierCoursePlanifiee");

        if(!fichierPlanning.exists() || fichierPlanning.length() == 0) {
            System.out.println("Aucun planning hebdomadaire disponible.");
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichierPlanning))) {
            System.out.println("\n--- Planning Hebdomadaire ---");
            System.out.println("Jour\tChauffeur\tType de trajet\tPoints d'arrêt\tNombre de passagers\tDate et heure prévue");
            System.out.println("-----------------------------------------------------------------------------------------------------------");

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endOfWeek = now.plusDays(7);

            while (true) {
                try {
                    Course course = (Course) in.readObject();

                    // Afficher seulement les courses prévues pour la semaine à venir
                    if (course.getHeureDatePrevu().isAfter(now) && course.getHeureDatePrevu().isBefore(endOfWeek)) {
                        StringBuilder pointsArret = new StringBuilder();
                        for (Point point : course.getPointsArret()) {
                            pointsArret.append(point.toString()).append(", ");
                        }
                        // Supprimer la dernière virgule et espace si présents
                        String pointsArretStr = pointsArret.length() > 2 ?
                                pointsArret.substring(0, pointsArret.length() - 2) : pointsArret.toString();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");//C'es tpour afficher dans un meilleur format
                        System.out.println(course.getHeureDatePrevu().getDayOfWeek() + "\t" +
                                course.getChauffeur().getNom() + " " + course.getChauffeur().getPrenom() + "\t" +
                                course.getTypeTrajet() + "\t" +
                                pointsArretStr + "\t" +
                                course.getPassagers().size() + "\t" +
                                course.getHeureDatePrevu().format(formatter));
                    }
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur lors de la lecture du planning hebdomadaire: " + e.getMessage());
        }
    }

    public static void afficherCoursesPassees(Utilisateur ats) {
        if(!verifierATS(ats)) return;

        File fichierHistorique = new File("../FichiersDeSauvegarde/fichierCourseTerminee");

        if(!fichierHistorique.exists() || fichierHistorique.length() == 0) {
            System.out.println("Aucun historique de courses disponible.");
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichierHistorique))) {
            System.out.println("\n--- Historique des Courses Passées ---");
            System.out.println("Date\tChauffeur\tType de trajet\tNombre de passagers\tÉvaluation chauffeur\tCommentaire");
            System.out.println("---------------------------------------------------------------------------------------------");

            while (true) {
                try {
                    Course course = (Course) in.readObject();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");//C'es tpour afficher dans un meilleur format
                    System.out.println(course.getHeureDatePrevu().format(formatter) + "\t" +
                            course.getChauffeur().getNom() + " " + course.getChauffeur().getPrenom() + "\t" +
                            course.getTypeTrajet() + "\t" +
                            course.getPassagers().size() + "\t" +
                            course.getEvaluationChauffeur() + "/5\t" +
                            course.getCommentaireChauffeur());
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur lors de la lecture de l'historique des courses: " + e.getMessage());
        }
    }

    public static void afficherStatistiques(Utilisateur ats) {
        if (!verifierATS(ats)) return;

        System.out.println("\n--- Statistiques du Système ---");

        // 1. Compter le nombre d'étudiants, enseignants, ATS et utilisateurs actifs
        int nbEtudiants = 0;
        int nbEnseignants = 0;
        int nbATS = 0;
        int nbUtilisateursActifs = 0;

        ArrayList<Etudiant> etudiants = new ArrayList<>();
        ArrayList<Enseignant> enseignants = new ArrayList<>();
        ArrayList<ATS> atsUsers = new ArrayList<>();

        // Lecture des étudiants
        File fichierEtudiant = new File("../FichiersDeSauvegarde/fichierEtudiant");
        if (fichierEtudiant.exists() && fichierEtudiant.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichierEtudiant))) {
                while (true) {
                    try {
                        Etudiant etudiant = (Etudiant) in.readObject();
                        etudiants.add(etudiant);
                        nbEtudiants++;
                        nbUtilisateursActifs++; // Tous les utilisateurs dans ce fichier sont actifs
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erreur lors de la lecture du fichier étudiant: " + e.getMessage());
            }
        }

        // Lecture des enseignants
        File fichierEnseignant = new File("../FichiersDeSauvegarde/fichierEnseignant");
        if (fichierEnseignant.exists() && fichierEnseignant.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichierEnseignant))) {
                while (true) {
                    try {
                        Enseignant enseignant = (Enseignant) in.readObject();
                        enseignants.add(enseignant);
                        nbEnseignants++;
                        nbUtilisateursActifs++; // Tous les utilisateurs dans ce fichier sont actifs
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erreur lors de la lecture du fichier enseignant: " + e.getMessage());
            }
        }

        // Lecture des ATS
        File fichierATS = new File("../FichiersDeSauvegarde/fichierATS");
        if (fichierATS.exists() && fichierATS.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichierATS))) {
                while (true) {
                    try {
                        ATS atsUser = (ATS) in.readObject();
                        atsUsers.add(atsUser);
                        nbATS++;
                        nbUtilisateursActifs++; // Tous les utilisateurs dans ce fichier sont actifs
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erreur lors de la lecture du fichier ATS: " + e.getMessage());
            }
        }

        // NOUVEAU: Lecture des utilisateurs bannis pour compter correctement les étudiants et enseignants
        File fichierBannis = new File("../FichiersDeSauvegarde/fichierBannis");
        if (fichierBannis.exists() && fichierBannis.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichierBannis))) {
                while (true) {
                    try {
                        Utilisateur utilisateurBanni = (Utilisateur) in.readObject();
                        // Ajouter aux compteurs appropriés sans les ajouter aux listes actives
                        if (utilisateurBanni instanceof Etudiant) {
                            nbEtudiants++;
                        } else if (utilisateurBanni instanceof Enseignant) {
                            nbEnseignants++;
                        }
                        // Notez: Pas d'incrémentation de nbUtilisateursActifs car ces utilisateurs sont bannis
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erreur lors de la lecture du fichier des utilisateurs bannis: " + e.getMessage());
            }
        }

        // Afficher les compteurs
        System.out.println("Nombre d'étudiants: " + nbEtudiants);
        System.out.println("Nombre d'enseignants: " + nbEnseignants);
        System.out.println("Nombre d'ATS: " + nbATS);
        System.out.println("Nombre d'utilisateurs actifs: " + nbUtilisateursActifs);
        System.out.println();

        // 2. Top 10 des meilleurs chauffeurs
        ArrayList<Utilisateur> tousUtilisateurs = new ArrayList<>();
        tousUtilisateurs.addAll(etudiants);
        tousUtilisateurs.addAll(enseignants);
        tousUtilisateurs.addAll(atsUsers);

        // Trier par réputation chauffeur (décroissant)
        tousUtilisateurs.sort((u1, u2) -> Double.compare(u2.getReputationChauffeur(), u1.getReputationChauffeur()));

        System.out.println("--- Top 10 des Meilleurs Chauffeurs ---");
        System.out.println("Nom\tPrénom\tType\tRéputation");
        System.out.println("------------------------------------");

        int count = 0;
        for (Utilisateur utilisateur : tousUtilisateurs) {
            if (count >= 10) break;

            String type = "";
            if (utilisateur instanceof Etudiant) type = "Étudiant";
            else if (utilisateur instanceof Enseignant) type = "Enseignant";
            else if (utilisateur instanceof ATS) type = "ATS";

            System.out.println(utilisateur.getNom() + "\t" +
                    utilisateur.getPrenom() + "\t" +
                    type + "\t" +
                    utilisateur.getReputationChauffeur() + "/5");
            count++;
        }
        System.out.println();

        // 3. Top 10 des pires chauffeurs
        tousUtilisateurs.sort((u1, u2) -> Double.compare(u1.getReputationChauffeur(), u2.getReputationChauffeur()));

        System.out.println("--- Top 10 des Pires Chauffeurs ---");
        System.out.println("Nom\tPrénom\tType\tRéputation");
        System.out.println("------------------------------------");

        count = 0;
        for (Utilisateur utilisateur : tousUtilisateurs) {
            if (count >= 10) break;

            String type = "";
            if (utilisateur instanceof Etudiant) type = "Étudiant";
            else if (utilisateur instanceof Enseignant) type = "Enseignant";
            else if (utilisateur instanceof ATS) type = "ATS";

            System.out.println(utilisateur.getNom() + "\t" +
                    utilisateur.getPrenom() + "\t" +
                    type + "\t" +
                    utilisateur.getReputationChauffeur() + "/5");
            count++;
        }
        System.out.println();

        // 4. Passagers à bannir (réputation < 2)
        System.out.println("--- Passagers à Bannir (Réputation < 2) ---");
        System.out.println("Matricule\tNom\tPrénom\tType\tRéputation");
        System.out.println("-----------------------------------------------");

        for (Utilisateur utilisateur : tousUtilisateurs) {
            if (utilisateur.getReputationPassager() < 2.0) {
                String type = "";
                if (utilisateur instanceof Etudiant) type = "Étudiant";
                else if (utilisateur instanceof Enseignant) type = "Enseignant";
                else if (utilisateur instanceof ATS) type = "ATS";

                System.out.println(utilisateur.getMatricule() + "\t" +
                        utilisateur.getNom() + "\t" +
                        utilisateur.getPrenom() + "\t" +
                        type + "\t" +
                        utilisateur.getReputationPassager() + "/5");
            }
        }
        System.out.println();

        // 5. Statistiques des courses par catégorie d'utilisateur et par faculté
        // Lire toutes les courses terminées
        ArrayList<Course> coursesTerminees = new ArrayList<>();
        File fichierCoursesTerminees = new File("../FichiersDeSauvegarde/fichierCourseTerminee");

        if (fichierCoursesTerminees.exists() && fichierCoursesTerminees.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichierCoursesTerminees))) {
                while (true) {
                    try {
                        Course course = (Course) in.readObject();
                        coursesTerminees.add(course);
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erreur lors de la lecture des courses terminées: " + e.getMessage());
            }
        }

        // Compter les courses par catégorie d'utilisateur
        int coursesEtudiants = 0;
        int coursesEnseignants = 0;
        int coursesATS = 0;

        // Compter les courses par faculté
        int[] coursesFacultes = new int[Faculte.values().length];

        for (Course course : coursesTerminees) {
            Utilisateur chauffeur = course.getChauffeur();

            if (chauffeur instanceof Etudiant) {
                coursesEtudiants++;
                Etudiant etudiant = (Etudiant) chauffeur;
                int faculteIndex = etudiant.getFaculte().ordinal();
                coursesFacultes[faculteIndex]++;
            } else if (chauffeur instanceof Enseignant) {
                coursesEnseignants++;
                Enseignant enseignant = (Enseignant) chauffeur;
                int faculteIndex = enseignant.getFaculte().ordinal();
                coursesFacultes[faculteIndex]++;
            } else if (chauffeur instanceof ATS) {
                coursesATS++;
            }
        }

        // Afficher les statistiques par catégorie
        System.out.println("--- Courses par Catégorie d'Utilisateur ---");
        System.out.println("Catégorie\tNombre de courses");
        System.out.println("-------------------------------");
        System.out.println("Étudiants\t" + coursesEtudiants);
        System.out.println("Enseignants\t" + coursesEnseignants);
        System.out.println("ATS\t\t" + coursesATS);

        // Déterminer la catégorie qui propose le plus de courses
        String categoriePlusActive = "";
        int maxCourses = Math.max(coursesEtudiants, Math.max(coursesEnseignants, coursesATS));

        if (maxCourses == coursesEtudiants) {
            categoriePlusActive = "Étudiants";
        } else if (maxCourses == coursesEnseignants) {
            categoriePlusActive = "Enseignants";
        } else {
            categoriePlusActive = "ATS";
        }

        System.out.println("\nLa catégorie proposant le plus de courses est: " + categoriePlusActive);
        System.out.println();

        // Afficher les statistiques par faculté
        System.out.println("--- Courses par Faculté ---");
        System.out.println("Faculté\tNombre de courses");
        System.out.println("-------------------------");

        int maxCoursesFaculte = 0;
        int indexMaxFaculte = 0;

        for (int i = 0; i < coursesFacultes.length; i++) {
            System.out.println(Faculte.values()[i] + "\t" + coursesFacultes[i]);

            if (coursesFacultes[i] > maxCoursesFaculte) {
                maxCoursesFaculte = coursesFacultes[i];
                indexMaxFaculte = i;
            }
        }

        System.out.println("\nLa faculté proposant le plus de courses est: " + Faculte.values()[indexMaxFaculte]);
    }

}


