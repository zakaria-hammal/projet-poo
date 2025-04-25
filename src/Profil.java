
import java.time.LocalDate;
import java.util.ArrayList;

public class Profil {
    private Status status;
    private Point itenairairePassager;
    private ArrayList<Point> itenairaireChauffeur;
    private ArrayList<LocalDate>[] horaires;

    public Profil(Status stat, Point itPass, ArrayList<Point> itChauf, ArrayList<LocalDate>[] hor) {
        this.status = stat;
        this.itenairairePassager = itPass;
        this.itenairaireChauffeur = itChauf;
        this.horaires = hor;
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

    
}
