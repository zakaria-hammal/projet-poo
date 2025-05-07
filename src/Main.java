import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {int choix;
        Scanner obj = new Scanner(System.in);
        System.out.println("1) Connexion");
        System.out.println("2) Inscription");
        System.out.println("3) Exit");
        choix=obj.nextInt();
        switch(choix){
            case 1 :{System.out.println("Veuillez vous inserer vos coordonnes:");
                System.out.println("Matricule : \t");
                String mat=obj.nextLine();
                System.out.println("Mot de Pass : \t");
                String pw=obj.nextLine();
                if(Utilisateur.login(mat,pw)!=null){
                Profil user=Utilisateur.login(mat,pw);
                    System.out.println("Vous voulez etre : \n"+"1) Chauffeur .\n"+"2) Passager) ");
                    choix=obj.nextInt();
                    switch(choix){
                    case 1 :{ user.setStatus(Status.Chauffeur);
                    // methode et fonctionnement dun chauffeur
                    }
                    case 2 : {user.setStatus(Status.Passager);
                        // methode et fonctionemt dun passager
                    }
                     default:
                    System.out.println("Erreur");
                    }
                }else System.out.println("Vous n'etes pas inscris encore !");
            }
            }
            case 2 :{
                System.out.println("Vous etes un: \n"+"1) Etudiant\n"+"2) Enseignant\n"+"3) ATS\n");
                choix = obj.nextInt();
                switch(choix){

                    case 1:{
                        System.out.print("Entrez votre matricule : ");
                        String mat = obj.nextLine();
                        System.out.print("Entrez votre mot de passe : ");
                        String password = obj.nextLine();
                        if(Utilisateur.login(mat,password)==null){
                             System.out.print("Entrez votre nom : ");
                        String nom = obj.nextLine();
                        System.out.print("Entrez votre prénom : ");
                        String prenom = obj.nextLine();
                        double repC = 2.5;
                        double repP = 2.5;
                        System.out.print("Entrez l'année vous avez admets votre bac : ");
                        int annee = obj.nextInt();
                        System.out.println("Entrez votre specialite : " );
                        String specialite= obj.nextLine();
                        System.out.println("Choisissez votre faculté parmi les suivantes :");
                        System.out.println("Inserez votre Faculte: " +
                                "1)Math,\n" +
                                "2)Informatique,\n" +
                                "3)Physique,\n" +
                                "4)Chimie,\n" +
                                "5)Biologie,\n" +
                                "6)GénieC,\n" +
                                "7)GénieE,\n" +
                                "8)GénieMP,\n" +
                                "9)Géologie");
                        choix= obj.nextInt();
                        Faculte fac;
                        switch (choix) {
                            case 1: fac= Faculte.Math;
                            case 2: fac= Faculte.Informatique;
                            case 3: fac= Faculte.Physique;
                            case 4: fac= Faculte.Chimie;
                            case 5: fac= Faculte.Biologie;
                            case 6: fac= Faculte.GénieC;
                            case 7: fac= Faculte.GénieE;
                            case 8: fac= Faculte.GénieMP;
                            case 9: fac= Faculte.Géologie;
                            default:
                                System.out.println("Erreur");
                        }
                        Etudiant etudiant = new Etudiant(nom, prenom, password, mat, repC, repP, anneeAdmistion,specialite, fac);

                        etudiant.RajouterEtudiant("C:\\Users\\dell\\Desktop\\k.txt\\",etudiant);}
                        else System.out.println("Vos coordonnes existent deja!");}//le path est juste un exemple ca depand lpc li npresentiw bih
                    case 2:{
                        System.out.print("Entrez votre matricule : ");
                        String mat = obj.nextLine();
                        System.out.print("Entrez votre mot de passe : ");
                        String password = obj.nextLine();
                        if(Utilisateur.login(mat,password)==null){
                        System.out.print("Entrez votre nom : ");
                        String nom = obj.nextLine();
                        System.out.print("Entrez votre prénom : ");
                        String prenom = obj.nextLine();
                        double repC = 2.5;
                        double repP = 2.5;
                        System.out.print("Entrez l'année vous ete recrute : ");
                        int annee = obj.nextInt();
                        System.out.println("Choisissez votre faculté parmi les suivantes :");
                        System.out.println("Inserez votre Faculte: " +
                                "1)Math,\n" +
                                "2)Informatique,\n" +
                                "3)Physique,\n" +
                                "4)Chimie,\n" +
                                "5)Biologie,\n" +
                                "6)GénieC,\n" +
                                "7)GénieE,\n" +
                                "8)GénieMP,\n" +
                                "9)Géologie");
                        choix= obj.nextInt();
                        Faculte fac;
                        switch (choix) {
                            case 1: fac= Faculte.Math;
                            case 2: fac= Faculte.Informatique;
                            case 3: fac= Faculte.Physique;
                            case 4: fac= Faculte.Chimie;
                            case 5: fac= Faculte.Biologie;
                            case 6: fac= Faculte.GénieC;
                            case 7: fac= Faculte.GénieE;
                            case 8: fac= Faculte.GénieMP;
                            case 9: fac= Faculte.Géologie;
                            default:
                                System.out.println("Erreur");
                        }
                        Enseignant enseignant = new Enseignant(nom, prenom, password, mat, repC, repP, annee, fac);
                        enseignant.RajouterEnseignant("C:\\Users\\dell\\Desktop\\k.txt\\",enseignant);}
                        else System.out.println("Vos coordonnes existent deja!");}
                        case 3:{System.out.print("Entrez votre matricule : ");
                            String mat = obj.nextLine();
                            System.out.print("Entrez votre mot de passe : ");
                            String password = obj.nextLine();
                            if(Utilisateur.login(mat,password)==null){
                                System.out.print("Entrez votre nom : ");
                            String nom = obj.nextLine();
                                System.out.print("Entrez votre prénom : ");
                                String prenom = obj.nextLine();

                                System.out.print("Entrez la valeur de réponse C : ");
                                double repC = obj.nextDouble();

                                System.out.print("Entrez la valeur de réponse P : ");
                                double repP = obj.nextDouble();

                                System.out.print("Entrez l'année vous ete recrutes : ");
                                int  AnneeDeRecrutement= obj.nextInt();
                                System.out.println("Entrez votre service : " );
                                String  ServiceDeRattachement= obj.nextLine();
                                System.out.println("Choisissez votre faculté parmi les suivantes :");
                                System.out.println("Inserez votre Faculte: " +
                                        "1)Math,\n" +
                                        "2)Informatique,\n" +
                                        "3)Physique,\n" +
                                        "4)Chimie,\n" +
                                        "5)Biologie,\n" +
                                        "6)GénieC,\n" +
                                        "7)GénieE,\n" +
                                        "8)GénieMP,\n" +
                                        "9)Géologie");
                                choix= obj.nextInt();
                                Faculte fac;
                                switch (choix) {
                                    case 1: fac= Faculte.Math;
                                    case 2: fac= Faculte.Informatique;
                                    case 3: fac= Faculte.Physique;
                                    case 4: fac= Faculte.Chimie;
                                    case 5: fac= Faculte.Biologie;
                                    case 6: fac= Faculte.GénieC;
                                    case 7: fac= Faculte.GénieE;
                                    case 8: fac= Faculte.GénieMP;
                                    case 9: fac= Faculte.Géologie;
                                    default:
                                        System.out.println("Erreur");
                                }
                                ATS ats= new ATS(nom, prenom, password, mat, repC, repP, AnneeDeRecrutement,ServiceDeRattachement, fac);

                                ats.AjouterATS("C:\\Users\\dell\\Desktop\\k.txt\\",ats);}
                            else System.out.println("Vos coordonnes existent deja!");}
                }
            }

            case 3 :System.exit(0);
           default:
            System.out.println("Erreur");}
    }
}
