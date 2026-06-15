package Zones;
import java.util.ArrayList;
import java.util.List;

import Animaux.*;
import Enumérations.EtatSante;
import Enumérations.TypeElevage;
import Utilitaires.*;   

// Zone dédiée à l'élevage. Accueille des animaux d'un même type (ruminants ou volaille)
// et dispose d'un programme d'alimentation défini.
public class ZoneElevage extends Zone {

    private TypeElevage typeElevage;
    private ProgrammeAlimentation programme;
    private List<Animal> animaux;

    public ZoneElevage(String code, String nom, LimitesGeographiques limites,
                       TypeElevage typeElevage, ProgrammeAlimentation programme) {
        super(code, nom, limites);
        this.typeElevage = typeElevage;
        this.programme = programme;
        this.animaux = new ArrayList<>();
    }

    // --- Getters / Setters ---

    public TypeElevage getTypeElevage()         { return typeElevage; }
    public ProgrammeAlimentation getProgramme() { return programme; }
    public List<Animal> getAnimaux()            { return animaux; }

    public void setProgramme(ProgrammeAlimentation programme) {
        this.programme = programme;
    }

    // --- Gestion des animaux ---

    public void ajouterAnimal(Animal animal) {
        animaux.add(animal);
    }

    public void supprimerAnimal(Animal animal) {
        animaux.remove(animal);
    }

    // Filtre les animaux par état de santé
    public List<Animal> filtrerParEtat(EtatSante etat) {
        List<Animal> resultat = new ArrayList<>();
        for (Animal a : animaux) {
            if (a.getEtat() == etat) {
                resultat.add(a);
            }
        }
        return resultat;
    }

    // Affiche les animaux filtrés par état de santé
    /*public void afficherAnimauxParEtat(EtatSante etat) {
        List<Animal> filtres = filtrerParEtat(etat);
        System.out.println("  Animaux en état " + etat + " dans " + getNom() + " :");
        if (filtres.isEmpty()) {
            System.out.println("    Aucun animal dans cet état.");
            return;
        }
        for (Animal a : filtres) {
            System.out.println("    " + a);
        }
    }*/

    // Affiche les animaux filtrés par état de santé
    public void afficherAnimauxParEtat(EtatSante etat) {
        List<Animal> filtres = filtrerParEtat(etat);
        System.out.println(etat + " animals in " + getCode() + " :" + filtres.size());
        if (filtres.isEmpty()) {
            System.out.println("   No animals in this state.");
            return;
        }
        for (Animal a : filtres) {
            System.out.println("    " + a.toString());
        }
    }

    // [EVAL A3] Compte les animaux malades ou en quarantaine
    public int getNbAnimauxMalades() {
        int count = 0;
        for (Animal a : animaux) {
            if (a.getEtat() == EtatSante.MALADE || a.getEtat() == EtatSante.QUARANTAINE) {
                count++;
            }
        }
        return count;
    }

    // Affiche la liste de tous les animaux avec leur état de santé
    public void afficherAnimaux() {
        System.out.println("  Animaux dans " + getNom() + " (" + typeElevage + ") :");
        if (animaux.isEmpty()) {
            System.out.println("    Aucun animal enregistré.");
            return;
        }
        for (Animal a : animaux) {
            System.out.println("    " + a);
        }
    }

    public int getNbEntites() { return animaux.size(); }

    public String getTypeZone() { return "ZoneElevage"; }

    /*public void afficherDetails() {
        System.out.println("  ZoneElevage [" + getCode() + "] " + getNom()
                + " | Statut: " + getStatut()
                + " | Animaux: " + animaux.size()
                + " | Malades/Quarantaine: " + getNbAnimauxMalades());
        System.out.println("  Programme: " + programme);
        afficherAnimaux();
    }*/

        public void afficherDetails() {
        System.out.print("[" + getCode() + "]  " + getNom() + "\t" + "| LivestockZone \t" + "| " + getStatut() + " | entities: " + animaux.size() + " | animals : "); 
        
        for (Animal a : animaux) {
            System.out.print( a + a.getNumero() +" (" + a.getEtat() + "), ");    
                }
        System.out.println( " ]") ;
        }
    


    public String toString() {
        return "ZoneElevage{code='" + getCode() + "', nom='" + getNom()
                + "', statut=" + getStatut()
                + ", typeElevage=" + typeElevage
                + ", animaux=" + animaux.size() + "}";
    }
}

