
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
}
