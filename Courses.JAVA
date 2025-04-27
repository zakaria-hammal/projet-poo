import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Course {
    
    // Enum for course state/status
    public enum Etat {
        PLANIFIEE,     // Planned
        EN_COURS,      // In progress
        TERMINEE,      // Completed
        ANNULEE        // Canceled
    }
    
    // Attributes
    private Etat etat;
    private LocalDateTime dateHeure;
    private Utilisateur chauffeur;
    private List<Utilisateur> passagers;
    private String typeTrajet; // "aller-retour", "aller simple", "retour simple"
    
    // Evaluations
    private int evaluationChauffeur;
    private List<Integer> evaluationsPassagers;
    private String commentaireChauffeur;
    private List<String> commentairesPassagers;
    
    // Points for pickup/dropoff
    private List<Point> pointsArret;
    
    // Reference to Administration for blacklist checking
    private static Administration administration;
    
    /**en
     * Sets the Administration instance for blacklist checking
     */
    /**fr
     * Définit l'instance d'Administration pour la vérification des listes noires
     */
    public static void setAdministration(Administration admin) {
        administration = admin;
    }
    
    /**EN
     * Constructor for a new planned course
     * @throws StatutInvalideException if the user is not a chauffeur
     * @throws UtilisateurBlacklisteException if the chauffeur is blacklisted
     */
    /**Fr
     * Constructeur pour un nouveau trajet planifié
     * @throws StatutInvalideException si l'utilisateur n'est pas un chauffeur
     * @throws UtilisateurBlacklisteException si le chauffeur est blacklisté
     */

    public Courses(Utilisateur chauffeur, LocalDateTime dateHeure, String typeTrajet, List<Point> pointsArret) 
            throws StatutInvalideException, UtilisateurBlacklisteException {
        
        // Check if user is a chauffeur
        if (chauffeur.getProfil().getStatus() != Profil.Status.CHAUFFEUR) {
            throw new StatutInvalideException("L'utilisateur doit être un chauffeur pour créer une course");
        }
        
        // Check if chauffeur is blacklisted
        if (administration != null && administration.estBlackliste(chauffeur)) {
            throw new UtilisateurBlacklisteException("Ce chauffeur est sur la liste noire");
        }
        
        this.chauffeur = chauffeur;
        this.dateHeure = dateHeure;
        this.typeTrajet = typeTrajet;
        this.etat = Etat.PLANIFIEE;
        this.passagers = new ArrayList<>();
        this.evaluationsPassagers = new ArrayList<>();
        this.commentairesPassagers = new ArrayList<>();
        this.pointsArret = pointsArret;
    }
    
    /**En
     * Add a passenger to the course
     * @param passager The passenger to add
     * @return true if the passenger was added successfully
     * @throws CourseCompleteException if the course is full
     * @throws EtatCourseInvalideException if the course is not in PLANIFIEE state
     * @throws StatutInvalideException if the user is not a passager
     * @throws UtilisateurBlacklisteException if the passenger is blacklisted
     */
    /**FR
     * Ajouter un passager au trajet
     * @param passager Le passager à ajouter
     * @return true si le passager a été ajouté avec succès
     * @throws CourseCompleteException si le trajet est complet
     * @throws EtatCourseInvalideException si le trajet n'est pas dans l'état PLANIFIEE
     * @throws StatutInvalideException si l'utilisateur n'est pas un passager
     * @throws UtilisateurBlacklisteException si le passager est blacklisté
      */

    public boolean ajouterPassager(Utilisateur passager) 
            throws CourseCompleteException, EtatCourseInvalideException, 
                   StatutInvalideException, UtilisateurBlacklisteException {
        
        // Check if course is still planned
        if (etat != Etat.PLANIFIEE) {
            throw new EtatCourseInvalideException("Impossible d'ajouter un passager à une course " + etat);
        }
        
        // Check if course is not full
        if (passagers.size() >= 4) { // Assuming maximum 4 passengers
            throw new CourseCompleteException("La course est complète");
        }
        
        // Check if user is a passenger
        if (passager.getProfil().getStatus() != Profil.Status.PASSAGER) {
            throw new StatutInvalideException("L'utilisateur doit être un passager pour rejoindre une course");
        }
        
        // Check if passenger is blacklisted
        if (administration != null && administration.estBlackliste(passager)) {
            throw new UtilisateurBlacklisteException("Ce passager est sur la liste noire");
        }
        
        // Check compatibility of preferences
        boolean compatible = verifierCompatibilitePreferences(passager);
        if (!compatible) {
            return false;
        }
        
        passagers.add(passager);
        return true;
    }
    
    /**EN
     * Verify if passenger preferences are compatible with the course and driver
     * @param passager The passenger to check
     * @return true if preferences are compatible
     */
    /**FR
     * Vérifie si les préférences du passager sont compatibles avec le trajet et le chauffeur
     * @param passager Le passager à vérifier
     * @return true si les préférences sont compatibles
     */

    private boolean verifierCompatibilitePreferences(Utilisateur passager) {
        Preferences prefsPassager = passager.getProfil().getPreferences();
        Preferences prefsChauffeur = chauffeur.getProfil().getPreferences();
        
        // Check gender preferences
        if (prefsChauffeur.accepterFilles() == false && passager instanceof Etudiant) {
            Etudiant etudiant = (Etudiant) passager;
            // Assuming there's a method to check gender
            if (etudiant.estFeminin()) {
                return false;
            }
        }
        
        // Check garcon preferences
        if (prefsChauffeur.accepterGarcons() == false && passager instanceof Etudiant) {
            Etudiant etudiant = (Etudiant) passager;
            if (!etudiant.estFeminin()) {
                return false;
            }
        }
        
        // N9DRO NZIDO LES PREFERENCES HNA
        
        return true;
    }
}
