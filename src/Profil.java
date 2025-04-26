
import java.time.LocalDate;
import java.util.ArrayList;

public class Profil {
    private Status status;
    private Point itenairairePassager;
    private ArrayList<Point> itenairaireChauffeur;
    private ArrayList<LocalDate>[] horaires;
    private TypeCourse typeCourse;

    public Profil(Status stat, Point itPass, ArrayList<Point> itChauf, ArrayList<LocalDate>[] hor, TypeCourse typeCourse) {
        this.status = stat;
        this.itenairairePassager = itPass;
        this.itenairaireChauffeur = itChauf;
        this.horaires = hor;
        this.typeCourse = typeCourse;
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

    public ArrayList<LocalDate>[] getHoraires() {
        return horaires;
    }

    public TypeCourse getTypeCourse() {
        return typeCourse;
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

    public void setHoraires(ArrayList<LocalDate>[] horaires) {
        this.horaires = horaires;
    }

    public void setTypeCourse(TypeCourse typeCourse) {
        this.typeCourse = typeCourse;
    }

    
}
