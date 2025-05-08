
import com.sun.jdi.InvalidTypeException;
import java.io.IOException;
import java.security.spec.InvalidParameterSpecException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
            choix = Integer.parseInt(sc.nextLine());

            if (choix != 1 && choix != 2) {
                System.out.println("Reessayer !!!");
            }
        }
        
        Preferences pref = new Preferences();
        int choixPref = 0;

        System.out.println("Preciser vos preferences : ");
        System.out.println("Acceptez-vous des garçons durant la course ?\n1-Oui\n2- Non");

        while (choixPref != 1 && choixPref != 2) {
            choixPref = Integer.parseInt(sc.nextLine());

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

        System.out.println("Acceptez-vous des filles durant la course ?\n1-Oui\n2- Non");
        
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

        System.out.println("Acceptez-vous de la musique durant la course ?\n1-Oui\n2- Non");

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

        System.out.println("Acceptez-vous des bagages durant la course ?\n1-Oui\n2- Non");

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
            while (true) {
                if (courseCourrente == null) {
                    System.out.println("1- Plannifiee une course\n2- Commencer une course\n3- Quitter");
                }
                else {
                    System.out.println("1- Plannifiee une course\n2- Terminer une course\n3- Quitter");
                }
                
                choix = Integer.parseInt(sc.nextLine());
                Course course;
                switch (choix) {
                    case 1 -> {
                        LocalDateTime dateTime;
                        System.out.print("Date et temps prevu : (dd/MM/yyyy HH:mm) \t");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                        dateTime = LocalDateTime.parse(sc.nextLine(), formatter);

                        try {
                            course = new Course(utilisateur, dateTime);
                            course.plannifiee();

                            System.out.println("Course planifieee avec succes");
                        } catch (StatutInvalideException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                
                    case 2 -> {
                        courses = Course.getCoursesEnCours(utilisateur);
                        
                        if (courses.isEmpty()) {
                            System.out.println("Vous n'avez pas de course planifiee");
                        }
                        else {
                            if (courseCourrente == null) {
                                choix = 0;
                                while (choix < 1 || choix > courses.size()) { 
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
                                    courseCourrente = courses.get(choix - 1);
                                    courseCourrente.commencer();
                                } catch (TropTotException ex) {
                                    System.out.println(ex.getMessage());
                                }
                                System.out.println("Course commencee avec succes");   
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

                    }
                    
                    case 3 -> {
                        break;
                    }

                    default -> {
                        System.out.println("Choix invalide !!!");
                    }
                }
            }
        }
        else {
            System.out.println("1- Rejoidre une course");

            courses = Course.getCoursesCompatible(utilisateur);
            System.out.println("Voici la liste des courses qui sont compatibles avec vos exigences :");
            for (int i = 0; i < courses.size(); i++) {
                System.out.println(String.valueOf(i + 1) + "- Prévu le : " + courses.get(i).getHeureDatePrevu());
            }

            System.out.print("Choisir : \t");
            choix = Integer.parseInt(sc.nextLine()) - 1;

            try {
                courses.get(choix).ajouterPassager(utilisateur);

                System.out.println("Ajoute avec succes");
            } catch (CourseCompleteException | EtatCourseInvalideException | StatutInvalideException ex) {
                System.out.println(ex.getMessage());
            } 
        }
    }
}
