
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Profil implements Serializable {

    private Status status;
    private Point itenairairePassager;
    private ArrayList<Point> itenairaireChauffeur;
    private ArrayList<LocalDateTime>[] horaires;
    private TypeCourse typeCourse;
    private Preferences preferences;

    public Profil() {
    }

    public Profil(Status stat, Point itPass, ArrayList<Point> itChauf, ArrayList<LocalDateTime>[] hor, TypeCourse typeCourse, Preferences preferences) {
        this.status = stat;
        this.itenairairePassager = itPass;
        this.itenairaireChauffeur = itChauf;
        this.horaires = hor;
        this.typeCourse = typeCourse;
        this.preferences = preferences;
    }

    // getteurs :

    public Status getStatus() {
        return status;
    }

    public Point getItenairairePassager() {
        return itenairairePassager;
    }

    public ArrayList<Point> getItenairaireChauffeur() {
        return itenairaireChauffeur;
    }

    public ArrayList<LocalDateTime>[] getHoraires() {
        return horaires;
    }

    public TypeCourse getTypeCourse() {
        return typeCourse;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    // setteurs :

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setItenairairePassager(Point itenairairePassager) {
        this.itenairairePassager = itenairairePassager;
    }

    public void setItenairaireChauffeur(ArrayList<Point> itenairaireChauffeur) {
        this.itenairaireChauffeur = itenairaireChauffeur;
    }

    public void setHoraires(ArrayList<LocalDateTime>[] horaires) {
        this.horaires = horaires;
    }

    public void setTypeCourse(TypeCourse typeCourse) {
        this.typeCourse = typeCourse;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }
}
