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

    public Course(Utilisateur chauffeur, LocalDateTime dateHeure, String typeTrajet, List<Point> pointsArret) 
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
    
    /**EN
     * Start the course, changing its state to EN_COURS
     * @param utilisateur User attempting to start the course
     * @return true if the state was changed successfully
     * @throws StatutInvalideException if the user is not the chauffeur
     * @throws EtatCourseInvalideException if the course is not in PLANIFIEE state
     */
    /**FR
     * Démarre la course, en changeant son état à EN_COURS
     * @param utilisateur Utilisateur tentant de démarrer la course
     * @return true si l'état a été modifié avec succès
     * @throws StatutInvalideException si l'utilisateur n'est pas le chauffeur
     * @throws EtatCourseInvalideException si la course n'est pas dans l'état PLANIFIEE
     */  
    public boolean demarrer(Utilisateur utilisateur) 
            throws StatutInvalideException, EtatCourseInvalideException {
        
        // Verify that the user is the chauffeur
        if (!utilisateur.equals(chauffeur)) {
            throw new StatutInvalideException("Seul le chauffeur peut démarrer la course");
        }
        
        if (etat != Etat.PLANIFIEE) {
            throw new EtatCourseInvalideException("La course ne peut être démarrée que si elle est planifiée");
        }
        
        etat = Etat.EN_COURS;
        return true;
    }
    
    public boolean terminer(Utilisateur utilisateur) 
            throws StatutInvalideException, EtatCourseInvalideException {
        
        // Verify that the user is the chauffeur
        if (!utilisateur.equals(chauffeur)) {
            throw new StatutInvalideException("Seul le chauffeur peut terminer la course");
        }
        
        if (etat != Etat.EN_COURS) {
            throw new EtatCourseInvalideException("La course ne peut être terminée que si elle est en cours");
        }
        etat = Etat.TERMINEE;
        // Update number of courses for the chauffeur + for each passenger
        chauffeur.incrementerNombreCoursesChauffeur();
        for (Utilisateur passager : passagers) {
            passager.incrementerNombreCoursesPassager();
        }
        
        return true;
    }
    
    /**EN
     * Cancel the course, changing its state to ANNULEE
     * @param utilisateur User attempting to cancel the course
     * @return true if the state was changed successfully
     * @throws StatutInvalideException if the user is not the chauffeur
     * @throws EtatCourseInvalideException if the course is not in PLANIFIEE state
     */
    /**FR
     * Annule la course, en changeant son état à ANNULEE
     * @param utilisateur Utilisateur tentant d'annuler la course
     * @return true si l'état a été modifié avec succès
     * @throws StatutInvalideException si l'utilisateur n'est pas le chauffeur
     * @throws EtatCourseInvalideException si la course n'est pas dans l'état PLANIFIEE
     */
    
     
    public boolean annuler(Utilisateur utilisateur) 
            throws StatutInvalideException, EtatCourseInvalideException {
        if (!utilisateur.equals(chauffeur)) {
            throw new StatutInvalideException("Seul le chauffeur peut annuler la course");
        }

        if (etat != Etat.PLANIFIEE) {
            throw new EtatCourseInvalideException("La course ne peut être annulée que si elle est planifiée");
        }
        
        etat = Etat.ANNULEE;
        return true;
    }
    
    /**EN
     * Rate the driver of the course
     * @param note The rating (0-5)
     * @param commentaire The comment
     * @param passager The passenger giving the rating
     * @return true if the rating was recorded successfully
     * @throws EvaluationInvalideException if the rating is invalid
     * @throws EtatCourseInvalideException if the course is not TERMINEE
     * @throws StatutInvalideException if the evaluator is not a passenger of this course
     */
    /**FR
     * Évalue le chauffeur de la course
     * @param note La note (0-5)
     * @param commentaire Le commentaire
     * @param passager Le passager donnant la note
     * @return true si l'évaluation a été enregistrée avec succès
     * @throws EvaluationInvalideException si la note est invalide
     * @throws EtatCourseInvalideException si la course n'est pas à l'état TERMINEE
     * @throws StatutInvalideException si l'évaluateur n'est pas un passager de cette course
     */
    public boolean evaluerChauffeur(int note, String commentaire, Utilisateur passager) 
            throws EvaluationInvalideException, EtatCourseInvalideException, StatutInvalideException {
        if (etat != Etat.TERMINEE) {
            throw new EtatCourseInvalideException("La course doit être terminée pour pouvoir évaluer");
        }
        if (!passagers.contains(passager)) {
            throw new StatutInvalideException("Seuls les passagers de cette course peuvent évaluer le chauffeur");
        }
        if (note < 0 || note > 5) {
            throw new EvaluationInvalideException("L'évaluation doit être entre 0 et 5");
        }
        this.evaluationChauffeur = note;
        this.commentaireChauffeur = commentaire;
        // Update driver's reputation as a driver
        chauffeur.updateReputationChauffeur(note);
        return true;
    }
    
    /**EN
     * Rate a passenger of the course
     * @param passager The passenger to be rated
     * @param note The rating (0-5)
     * @param commentaire The comment
     * @param evaluateur The user doing the evaluation (should be the driver)
     * @return true if the rating was recorded successfully
     * @throws EvaluationInvalideException if the rating is invalid
     * @throws EtatCourseInvalideException if the course is not TERMINEE
     * @throws StatutInvalideException if the evaluator is not the driver
     */
    /**FR
     * Évalue un passager de la course
     * @param passager Le passager à évaluer
     * @param note La note (0-5)
     * @param commentaire Le commentaire
     * @param evaluateur L'utilisateur effectuant l'évaluation (doit être le chauffeur)
     * @return true si l'évaluation a été enregistrée avec succès
     * @throws EvaluationInvalideException si la note est invalide
     * @throws EtatCourseInvalideException si la course n'est pas à l'état TERMINEE
     * @throws StatutInvalideException si l'évaluateur n'est pas le chauffeur
     */
    public boolean evaluerPassager(Utilisateur passager, int note, String commentaire, Utilisateur evaluateur) 
            throws EvaluationInvalideException, EtatCourseInvalideException, StatutInvalideException {
        if (etat != Etat.TERMINEE) {
            throw new EtatCourseInvalideException("La course doit être terminée pour pouvoir évaluer");
        }
        if (!evaluateur.equals(chauffeur)) {
            throw new StatutInvalideException("Seul le chauffeur peut évaluer les passagers");
        }
        
        int index = passagers.indexOf(passager);// // Recherche dans la liste des passagers le passager spécifique à évaluer
        if (index == -1) {
            throw new StatutInvalideException("Le passager évalué doit avoir participé à cette course");
        }
        
        if (note < 0 || note > 5) {
            throw new EvaluationInvalideException("L'évaluation doit être entre 0 et 5");
        }
        
        // Ensure the lists are properly sized
        //This code makes sure that our evaluation lists (evaluationsPassagers and commentairesPassagers) are big enough to store a rating at the specific position (index) we need.
        while (evaluationsPassagers.size() <= index) {
            evaluationsPassagers.add(0);
            commentairesPassagers.add("");
        }
        
        evaluationsPassagers.set(index, note);
        commentairesPassagers.set(index, commentaire);
        
        // Update passenger's reputation 
        passager.updateReputationPassager(note);
        
        return true;
    }
    
    /**EN
     * Checks if this course is active at the specified date and time
     * @param moment The moment to check
     * @return true if the course is active at that moment
     */
    /**FR
     * Vérifie si cette course est active à la date et heure spécifiées
     * @param moment Le moment à vérifier
     * @return true si la course est active à ce moment
     */
    public boolean estActifA(LocalDateTime moment) {
        return etat == Etat.EN_COURS && 
              dateHeure.isBefore(moment) && 
              moment.isBefore(dateHeure.plusHours(2)); // Assuming course duration of 2 hours
    }
    
    // Getters
    
    public Etat getEtat() {
        return etat;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public Utilisateur getChauffeur() {
        return chauffeur;
    }

    public List<Utilisateur> getPassagers() {
        return passagers;
    }

    public String getTypeTrajet() {
        return typeTrajet;
    }

    public int getEvaluationChauffeur() {
        return evaluationChauffeur;
    }

    public List<Integer> getEvaluationsPassagers() {
        return evaluationsPassagers;
    }
    
    public List<Point> getPointsArret() {
        return pointsArret;
    }
    
    /**EN
     * Get the course information as a string
     * @return A string representation of the course
     */
    /**FR
     * Obtient les informations de la course sous forme de chaîne de caractères
     * @return Une représentation textuelle de la course
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Course: ")
          .append("État: ").append(etat)
          .append(", Date: ").append(dateHeure)
          .append(", Type: ").append(typeTrajet)
          .append(", Chauffeur: ").append(chauffeur.getNom()).append(" ").append(chauffeur.getPrenom())
          .append(", Passagers: ").append(passagers.size());
        
        if (etat == Etat.TERMINEE) {
            sb.append(", Évaluation chauffeur: ").append(evaluationChauffeur);
        }
        
        return sb.toString();
    }
}
