
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.spec.InvalidParameterSpecException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Course implements Serializable{
    private Etat etat;
    private LocalDateTime dateHeurePrevu;
    private Utilisateur chauffeur;
    private ArrayList<Utilisateur> passagers;
    private final TypeTrajet typeTrajet;

    private ArrayList<Point> pointsArret;

    private int evaluationChauffeur;
    private int[] evaluationsPassagers;
    private String commentaireChauffeur;
    private ArrayList<String> commentairesPassagers;

    public Course(Utilisateur chauffeur, LocalDateTime dateHeurePrevu, TypeTrajet typeTrajet) throws StatutInvalideException {
        if (chauffeur.getProfil().getStatus() != Status.Chauffeur) {
            throw new StatutInvalideException("L'utilisateur doit être un chauffeur pour créer une course");
        }

        this.chauffeur = chauffeur;
        this.dateHeurePrevu = dateHeurePrevu;
        this.typeTrajet = typeTrajet;
        this.etat = Etat.PLANIFIEE;
        this.passagers = new ArrayList<>();
        this.commentairesPassagers = new ArrayList<>();
        this.pointsArret = chauffeur.getProfil().getItenairaireChauffeur();
        this.etat = Etat.PLANIFIEE;
    }

    public void plannifiee() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("../FichiersDeSauvegarde/fichierCoursePlanifiee"));

        out.writeObject(this);
    }

    public void ajouterPassager(Utilisateur passager) throws StatutInvalideException, CourseCompleteException, EtatCourseInvalideException {
        if (etat != Etat.PLANIFIEE) {
            throw new EtatCourseInvalideException("Impossible d'ajouter un passager à une course " + etat);
        }
        
        // Check if course is not full
        if (passagers.size() >= 4) { // Assuming maximum 4 passengers
            throw new CourseCompleteException("La course est complète");
        }
        
        // Check if user is a passenger
        if (passager.getProfil().getStatus() != Status.Passager) {
            throw new StatutInvalideException("L'utilisateur doit être un passager pour rejoindre une course");
        }

        passagers.add(passager);
    }

    public static ArrayList<Course> getCoursesCompatible(Utilisateur passager) {
        Course course;
        ArrayList<Course> maListe = new ArrayList<>();
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierCoursePlanifiee"));

            while (true) { 
                try {
                    course = (Course) in.readObject();
                    if (course.chauffeur.getProfil().getPreferences().acceptable(passager.getProfil().getPreferences())) {
                        for (Point elem : course.chauffeur.getProfil().getItenairaireChauffeur()) {
                            if (elem == passager.getProfil().getItenairairePassager()) {
                                maListe.add(course);
                                break;
                            }
                        }
                        
                    }
                } catch (EOFException | ClassNotFoundException e) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return maListe;
    }

    public void commencer() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierCoursePlanifiee"));

            ArrayList<Course> maListe = new ArrayList<>();
            Course temp;

            Course maCourse = null;

            while (true) { 
                try {
                    temp = (Course) in.readObject();

                    if((temp.chauffeur.getMatricule() == null ? this.chauffeur.getMatricule() == null : temp.chauffeur.getMatricule().equals(this.chauffeur.getMatricule())) && temp.dateHeurePrevu == this.dateHeurePrevu) {
                        maCourse = temp;
                    }
                    else {
                        maListe.add(temp);
                    }

                } catch (EOFException e) {
                    break;
                }

            }

            if(maCourse == null ) {
                return;
            }

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("../FichiersDeSauvegarde/fichierCoursePlanifiee"));

            for (Course elem : maListe) {
                out.writeObject((Course) elem);
            }

            maListe.clear();

            maListe.add(maCourse);

            in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierCourseEnCours"));

            while (true) { 
                try {
                    temp = (Course) in.readObject();
                    maListe.add(temp);

                } catch (EOFException e) {
                    break;
                }

            }

            out = new ObjectOutputStream(new FileOutputStream("../FichiersDeSauvegarde/fichierCourseEnCours"));

            for (Course elem : maListe) {
                out.writeObject((Course) elem);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void terminer(int evaluationC, int[] evaluationsP, String commentaireChauffeur, ArrayList<String> commentairesPassagers) throws InvalidParameterSpecException, EvaluationInvalideException {
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

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierCourseEnCours"));

            ArrayList<Course> maListe = new ArrayList<>();
            Course temp;

            Course maCourse = null;

            while (true) { 
                try {
                    temp = (Course) in.readObject();

                    if((temp.chauffeur.getMatricule() == null ? this.chauffeur.getMatricule() == null : temp.chauffeur.getMatricule().equals(this.chauffeur.getMatricule())) && temp.dateHeurePrevu == this.dateHeurePrevu) {
                        maCourse = temp;
                    }
                    else {
                        maListe.add(temp);
                    }

                } catch (EOFException e) {
                    break;
                }

            }

            if(maCourse == null ) {
                return;
            }

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("../FichiersDeSauvegarde/fichierCourseEnCours"));

            for (Course elem : maListe) {
                out.writeObject((Course) elem);
            }

            maListe.clear();

            maListe.add(maCourse);

            in = new ObjectInputStream(new FileInputStream("../FichiersDeSauvegarde/fichierCourseTerminee"));

            while (true) { 
                try {
                    temp = (Course) in.readObject();
                    maListe.add(temp);

                } catch (EOFException e) {
                    break;
                }

            }

            out = new ObjectOutputStream(new FileOutputStream("../FichiersDeSauvegarde/fichierCourseTerminee"));

            for (Course elem : maListe) {
                out.writeObject((Course) elem);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public TypeTrajet getTypeTrajet() {
        return typeTrajet;
    }

    public ArrayList<Point> getPointsArret() {
        return pointsArret;
    }

    public int getEvaluationChauffeur() {
        return evaluationChauffeur;
    }

    public String getCommentaireChauffeur() {
        return commentaireChauffeur;
    }
}
