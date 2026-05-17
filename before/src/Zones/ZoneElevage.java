package before.src.Zones;
import java.util.ArrayList;
import java.util.List;

import before.src.Utilitaires.*;
import before.src.Animaux.*;    
import before.src.Enumérations.TypeElevage;
import before.src.Enumérations.EtatSante;   

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
    public void afficherAnimauxParEtat(EtatSante etat) {
        List<Animal> filtres = filtrerParEtat(etat);
        System.out.println("  Animaux en état " + etat + " dans " + getNom() + " :");
        if (filtres.isEmpty()) {
            System.out.println("    Aucun animal dans cet état.");
            return;
        }
        for (Animal a : filtres) {
            System.out.println("    " + a);
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

    public void afficherDetails() {
        System.out.println("  ZoneElevage [" + getCode() + "] " + getNom()
                + " | Statut: " + getStatut()
                + " | Animaux: " + animaux.size()
                + " | Malades/Quarantaine: " + getNbAnimauxMalades());
        System.out.println("  Programme: " + programme);
        afficherAnimaux();
    }

    public String toString() {
        return "ZoneElevage{code='" + getCode() + "', nom='" + getNom()
                + "', statut=" + getStatut()
                + ", typeElevage=" + typeElevage
                + ", animaux=" + animaux.size() + "}";
    }
}