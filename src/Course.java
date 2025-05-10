/*
 * Prepare par DJEHA Youunes
 */

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.spec.InvalidParameterSpecException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Course implements Serializable {
    private Etat etat;
    private LocalDateTime dateHeurePrevu;
    private Utilisateur chauffeur;
    private ArrayList<Utilisateur> passagers;
    private final TypeCourse typeTrajet;

    private ArrayList<Point> pointsArret;

    private int evaluationChauffeur;
    private int[] evaluationsPassagers;
    private String commentaireChauffeur;
    private ArrayList<String> commentairesPassagers;

    public Course(Utilisateur chauffeur, LocalDateTime dateHeurePrevu) throws StatutInvalideException {
        if (chauffeur.getProfil().getStatus() != Status.Chauffeur) {
            throw new StatutInvalideException("L'utilisateur doit être un chauffeur pour créer une course");
        }

        this.chauffeur = chauffeur;
        this.dateHeurePrevu = dateHeurePrevu;
        this.typeTrajet = chauffeur.getProfil().getTypeCourse();
        this.etat = Etat.PLANIFIEE;
        this.passagers = new ArrayList<>();
        this.commentairesPassagers = new ArrayList<>();
        this.pointsArret = chauffeur.getProfil().getItenairaireChauffeur();
        this.etat = Etat.PLANIFIEE;
    }
    
    // Corrige par Zakaria HAMMAL
    public void plannifiee() throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("../FichiersDeSauvegarde/fichierCoursePlanifiee", true))) {
            out.writeObject(this);
        }
    }

    public void ajouterPassager(Utilisateur passager) throws StatutInvalideException, CourseCompleteException, EtatCourseInvalideException {
        if (etat != Etat.PLANIFIEE) {
            throw new EtatCourseInvalideException("Impossible d'ajouter un passager à une course " + etat);
        }
        
        if (passagers.size() >= 4) {
            throw new CourseCompleteException("La course est complète");
        }
        
        if (passager.getProfil().getStatus() != Status.Passager) {
            throw new StatutInvalideException("L'utilisateur doit être un passager pour rejoindre une course");
        }

        passagers.add(passager);

        File file = new File("../FichiersDeSauvegarde/fichierCoursePlanifiee");
        ArrayList<Course> courses = new ArrayList<>();
    
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                while (true) {
                    try {
                        Course course = (Course) in.readObject();
                        if (course.getChauffeur().getMatricule().equals(this.chauffeur.getMatricule()) && 
                            course.getHeureDatePrevu().equals(this.dateHeurePrevu)) {
                            courses.add(this);
                        } else {
                            courses.add(course);
                        }
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            courses.add(this);
        }
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            for (Course course : courses) {
                out.writeObject(course);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static ArrayList<Course> getCoursesCompatible(Utilisateur passager) {
        ArrayList<Course> maListe = new ArrayList<>();
        File file = new File("../FichiersDeSauvegarde/fichierCoursePlanifiee");
        
        if (!file.exists() || file.length() == 0) {
            return maListe;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    Course course = (Course) in.readObject();
                    if (course.chauffeur.getProfil().getPreferences().acceptable(
                            passager.getProfil().getPreferences()) && (course.passagers.size() < 4)) {
                        for (Point elem : course.chauffeur.getProfil().getItenairaireChauffeur()) {
                            if (elem == passager.getProfil().getItenairairePassager()) {
                                maListe.add(course);
                                break;
                            }
                        }
                    }
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur de lecture des courses planifiees: " + e.getMessage());
        }
        return maListe;
    }

    // Corrige par Zakaria HAMMAL
    public void commencer() throws TropTotException {
        this.etat = Etat.EN_COURS;
        File planifieeFile = new File("../FichiersDeSauvegarde/fichierCoursePlanifiee");
        File enCoursFile = new File("../FichiersDeSauvegarde/fichierCourseEnCours");
        
        ArrayList<Course> remainingCourses = new ArrayList<>();
        ArrayList<Course> coursesEnCours = new ArrayList<>();
        Course currentCourse = null;

        if (LocalDateTime.now().isBefore(this.dateHeurePrevu)) {
            throw new TropTotException("C'est pas encore l'heure");
        }

        if (planifieeFile.exists() && planifieeFile.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(planifieeFile))) {
                while (true) {
                    try {
                        Course temp = (Course) in.readObject();
                        if (temp.chauffeur.getMatricule().equals(this.chauffeur.getMatricule()) && 
                            temp.dateHeurePrevu.equals(this.dateHeurePrevu)) {
                            currentCourse = temp;
                        } else {
                            remainingCourses.add(temp);
                        }
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur de lecture des courses planifiees: " + e.getMessage());
                return;
            }
        }

        if (currentCourse == null) {
            return;
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(planifieeFile))) {
            for (Course course : remainingCourses) {
                out.writeObject(course);
            }
        } catch (IOException e) {
            System.err.println("Erreur d'ecriture des courses planifiees: " + e.getMessage());
            return;
        }

        if (enCoursFile.exists() && enCoursFile.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(enCoursFile))) {
                while (true) {
                    try {
                        coursesEnCours.add((Course) in.readObject());
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur de lecture des courses en cours: " + e.getMessage());
            }
        }

        coursesEnCours.add(currentCourse);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(enCoursFile))) {
            for (Course course : coursesEnCours) {
                out.writeObject(course);
            }
        } catch (IOException e) {
            System.err.println("Erreur d'ecriture des courses en cours: " + e.getMessage());
        }
    }

    // Corrige par Zakaria HAMMAL
    public void terminer(int evaluationC, int[] evaluationsP, String commentaireChauffeur, ArrayList<String> commentairesPassagers) throws InvalidParameterSpecException, EvaluationInvalideException {
        this.etat = Etat.TERMINEE;
        if(this.passagers.size() != evaluationsP.length || this.passagers.size() != commentairesPassagers.size()) {
            throw new InvalidParameterSpecException("Le nombre d'evaluations et / ou commentaire ne correspand pas au nombre de passagers");
        }

        if (evaluationC < 0 || evaluationC > 5) {
            throw new EvaluationInvalideException();
        }

        this.evaluationChauffeur = evaluationC;

        this.evaluationsPassagers = new int[this.passagers.size()];

        this.commentaireChauffeur = commentaireChauffeur;

        try {
            chauffeur.newRatingChauffeur(this, evaluationC);   
        } catch (MatriculeException | UserNotFoundException | IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        for (int i = 0; i < evaluationsP.length; i++) {
            try {
                if (evaluationsP[i] < 0 || evaluationsP[i] > 5) {
                    throw new EvaluationInvalideException();
                }

                this.evaluationsPassagers[i] = evaluationsP[i];
                this.passagers.get(i).newRatingPassager(this, evaluationsP[i]);
                this.commentairesPassagers.set(i, commentairesPassagers.get(i));
            } catch (MatriculeException | UserNotFoundException | IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
                return;
            }
            
        }

        File enCoursFile = new File("../FichiersDeSauvegarde/fichierCourseEnCours");
        File termineeFile = new File("../FichiersDeSauvegarde/fichierCourseTerminee");
        
        ArrayList<Course> remainingOngoing = new ArrayList<>();
        ArrayList<Course> completedCourses = new ArrayList<>();
        Course currentCourse = null;

        if (enCoursFile.exists() && enCoursFile.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(enCoursFile))) {
                while (true) {
                    try {
                        Course temp = (Course) in.readObject();
                        if (temp.chauffeur.getMatricule().equals(this.chauffeur.getMatricule()) && 
                            temp.dateHeurePrevu.equals(this.dateHeurePrevu)) {
                            currentCourse = temp;
                        } else {
                            remainingOngoing.add(temp);
                        }
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur de lecture des courses en cours: " + e.getMessage());
                return;
            }
        }

        if (currentCourse == null) {
            return;
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(enCoursFile))) {
            for (Course course : remainingOngoing) {
                out.writeObject(course);
            }
        } catch (IOException e) {
            System.err.println("Erreur d'ecriture des courses en cours: " + e.getMessage());
            return;
        }

        if (termineeFile.exists() && termineeFile.length() > 0) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(termineeFile))) {
                while (true) {
                    try {
                        completedCourses.add((Course) in.readObject());
                    } catch (EOFException e) {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur de lecture des courses termines: " + e.getMessage());
            }
        }

        completedCourses.add(currentCourse);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(termineeFile))) {
            for (Course course : completedCourses) {
                out.writeObject(course);
            }
        } catch (IOException e) {
            System.err.println("Erreur d'ecriture des courses termines: " + e.getMessage());
        }
    }

    public static ArrayList<Course> getCoursesPlanifiee() {
        File file = new File("../fichierCoursePlanifiee");

        if (!file.exists() || file.length() == 0) {
            return null;
        }

        ArrayList<Course> courses = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    courses.add((Course) in.readObject());
                } catch (EOFException ex) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
        return courses;
    }
    
    public static ArrayList<Course> getCoursesPlanifiee(Utilisateur chauffeur) {
        File file = new File("../fichierCoursePlanifiee");

        if (!file.exists() || file.length() == 0) {
            return null;
        }

        ArrayList<Course> courses = new ArrayList<>();
        Course course;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    course = (Course) in.readObject();
                    if(course.chauffeur.getMatricule().equals(chauffeur.getMatricule()))
                    {
                        courses.add(course);
                    }
                } catch (EOFException ex) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
        return courses;
    }

    public static ArrayList<Course> getCoursesEnCours() {
        File file = new File("../fichierCourseEnCours");

        if (!file.exists() || file.length() == 0) {
            return null;
        }

        ArrayList<Course> courses = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    courses.add((Course) in.readObject());
                } catch (EOFException ex) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
        return courses;
    }
    
    public static ArrayList<Course> getCoursesEnCours(Utilisateur chauffeur) {
        File file = new File("../fichierCourseEnCours");

        if (!file.exists() || file.length() == 0) {
            return null;
        }

        ArrayList<Course> courses = new ArrayList<>();
        Course course;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    course = (Course) in.readObject();
                    if(course.chauffeur.getMatricule().equals(chauffeur.getMatricule()))
                    {
                        courses.add(course);
                    }
                } catch (EOFException ex) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
        return courses;
    }

    public static ArrayList<Course> getCoursesTerminee() {
        File file = new File("../fichierCourseTerminee");

        if (!file.exists() || file.length() == 0) {
            return null;
        }

        ArrayList<Course> courses = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    courses.add((Course) in.readObject());
                } catch (EOFException ex) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
        return courses;
    }
    
    public static ArrayList<Course> getCoursesTerminee(Utilisateur chauffeur) {
        File file = new File("../fichierCourseTerminee");

        if (!file.exists() || file.length() == 0) {
            return null;
        }

        ArrayList<Course> courses = new ArrayList<>();
        Course course;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    course = (Course) in.readObject();
                    if(course.chauffeur.getMatricule().equals(chauffeur.getMatricule()))
                    {
                        courses.add(course);
                    }
                } catch (EOFException ex) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        
        return courses;
    }

    public TypeCourse getTypeTrajet() {
        return typeTrajet;
    }

    public ArrayList<Point> getPointsArret() {
        return pointsArret;
    }

    public Utilisateur getChauffeur() {
        return this.chauffeur;
    }

    public ArrayList<Utilisateur> getPassagers() {
        return this.passagers;
    }

    public int getEvaluationChauffeur() {
        return evaluationChauffeur;
    }

    public String getCommentaireChauffeur() {
        return commentaireChauffeur;
    }

    public LocalDateTime getHeureDatePrevu() {
        return dateHeurePrevu;
    }

    public int getNbPassager() {
        return passagers.size();
    }
}
