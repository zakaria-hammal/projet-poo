import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Etudiant extends Utilisateur {
    LocalDate anneeAdmistion;
    Faculte fac;
    String specialite;
    public Etudiant(String nom, String prenom, String password, String mat,
             double repC, double repP,LocalDate anneeAdmistion,Faculte fac,String specialite)
            throws MatriculeException, ReputationException{
        super(nom, prenom, password, mat, repC, repP);
        this.anneeAdmistion=anneeAdmistion;
        this.specialite=specialite;
        getFaculte();
    }
   void getFaculte() {
        Scanner obj = new Scanner(System.in);
        System.out.println("Inserez votre Faculte: " + "1)Math,\n" +
                "2)Informatique,\n" +
                "3)Physique,\n" +
                "4)Chimie,\n" +
                "5)Biologie,\n" +
                " 6)GénieC,\n" + "7)GénieE,\n" +
                "8)GénieMP,\n" +
                "9)Géologie");
        int choix = obj.nextInt();
       if (choix>=1 && choix<=9){
           switch (choix) {
                case 1: this.fac = Faculte.Math; break;
                case 2: this.fac = Faculte.Informatique; break;
                case 3: this.fac = Faculte.Physique; break;
                case 4: this.fac = Faculte.Chimie; break;
                case 5: this.fac = Faculte.Biologie; break;
                case 6: this.fac = Faculte.GénieC; break;
                case 7: this.fac = Faculte.GénieE; break;
                case 8: this.fac = Faculte.GénieMP; break;
                case 9: this.fac = Faculte.Géologie; break;}}
            else System.out.println("Erreur");}
    @Override
    public String toString() {
        return "Etudiant{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", matricule='" + matricule + '\'' +
                ", reputationCivique=" + reputationCivique +
                ", reputationProfessionnelle=" +reputationProfessionnelle +
                ", anneeAdmistion=" + anneeAdmistion +
                ", faculte=" + fac +
                ", specialite='" + specialite + '\'' +
                '}';
    }
    public static void RajouterEtudiant(String filePath, String x) {
            File file = new File(filePath);
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(x);
                writer.write(System.lineSeparator());
                System.out.println("Vous etes bien inscris.");
            } catch (IOException e) {
                System.err.println("An error occurred while writing to the file: " + e.getMessage());
            }
        }
    // (fichier tae etudiant,object_etudiant.toString())
    //ecriture de string donnee en argument fel fichier tae etudiant
        public boolean VerifierEtudiant(String filePath, String x) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equals(x)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                System.err.println("An error occurred while reading the file: " + e.getMessage());
            }
            return false;
        }}
// (fichier tae etudiant,object_etudiant.toString())
//lecture de fichier etudiant ligne pour verifier si le compte existe deja