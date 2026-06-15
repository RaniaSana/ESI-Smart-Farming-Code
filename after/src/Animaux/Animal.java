package Animaux;

import java.util.ArrayList;
import java.util.List;

import Enumérations.EtatSante;
import Enumérations.TypeEvenementSanitaire;  



// Représente un animal individuel suivi dans une zone d'élevage.
// Chaque animal a un numéro unique, une espèce, un âge, un poids et un état de santé.
public class Animal {

    private String numero;
    private EspeceAnimal espece;
    private int age;
    private double poids;
    private EtatSante etat;
    private List<EvenementSanitaire> historiqueSanitaire;

    // Par défaut, tout animal créé est considéré sain
    public Animal(String numero, EspeceAnimal espece, int age, double poids) {
        this.numero = numero;
        this.espece = espece;
        this.age = age;
        this.poids = poids;
        this.etat = EtatSante.SAIN;
        this.historiqueSanitaire = new ArrayList<>();
    }

    // --- Getters ---

    public String getNumero()                           { return numero; }
    public EspeceAnimal getEspece()                     { return espece; }
    public int getAge()                                 { return age; }
    public double getPoids()                            { return poids; }
    public EtatSante getEtat()                          { return etat; }
    public List<EvenementSanitaire> getHistoriqueSanitaire() { return historiqueSanitaire; }

    // --- Setters ---

    public void setPoids(double poids)                  { this.poids = poids; }

    // Change l'état de santé de l'animal directement (sans créer d'événement)
    public void changerEtat(EtatSante nouvelEtat) {
        this.etat = nouvelEtat;
    }

    // Consigne un événement sanitaire et met à jour l'état de l'animal en conséquence
    public void declarerEvenementSanitaire(TypeEvenementSanitaire type,  EtatSante nouvelEtat, String description) {
        EvenementSanitaire evenement = new EvenementSanitaire(type, nouvelEtat, description);
        historiqueSanitaire.add(evenement);
        this.etat = nouvelEtat;
    }

    // [EVAL A1] Un animal est "healthy" s'il est sain ET que son poids atteint le seuil de référence
    public boolean isHealthy(double poidsReference) {
        return etat == EtatSante.SAIN && poids >= poidsReference;
    }

    // Retourne les événements sanitaires entre deux dates (incluses)
    public List<EvenementSanitaire> filtrerHistoriqueSanitaire(java.util.Date debut, java.util.Date fin) {
        List<EvenementSanitaire> resultat = new ArrayList<>();
        for (EvenementSanitaire e : historiqueSanitaire) {
            if (!e.getDate().before(debut) && !e.getDate().after(fin)) {
                resultat.add(e);
            }
        }
        return resultat;
    }

    // Affiche l'historique des événements sanitaires de cet animal
    public void afficherHistoriqueSanitaire() {
        System.out.println("  Historique sanitaire de " + numero + " (" + espece.getNom() + ") :");
        if (historiqueSanitaire.isEmpty()) {
            System.out.println("    Aucun événement enregistré.");
            return;
        }
        for (EvenementSanitaire e : historiqueSanitaire) {
            System.out.println("    -> " + e);
        }
    }

    // Affiche l'historique filtré par plage de dates
    public void afficherHistoriqueSanitaire(java.util.Date debut, java.util.Date fin) {
        List<EvenementSanitaire> filtres = filtrerHistoriqueSanitaire(debut, fin);
        System.out.println("  Historique sanitaire de " + numero + " du " + debut + " au " + fin + " :");
        if (filtres.isEmpty()) {
            System.out.println("    Aucun événement sur cette période.");
            return;
        }
        for (EvenementSanitaire e : filtres) {
            System.out.println("    -> " + e);
        }
    }

public String toString() {
        return "Animal{numero='" + numero + "', espece=" + espece.getNom()
                + ", age=" + age + ", poids=" + poids + ", etat=" + etat + "}";
   
}
}