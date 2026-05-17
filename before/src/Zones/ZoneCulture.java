package before.src.Zones;
import java.util.ArrayList;
import java.util.List;
import before.src.Enumérations.TypeCulture;
import before.src.Utilitaires.*;
import before.src.Cultures.Culture;

// Zone dédiée aux cultures. Elle héberge plusieurs cultures et est équipée
// de capteurs environnementaux et de capteurs de sol.
public class ZoneCulture extends Zone {

    private List<Culture> cultures;

    public ZoneCulture(String code, String nom, LimitesGeographiques limites) {
        super(code, nom, limites);
        this.cultures = new ArrayList<>();
    }

    // --- Getters ---

    public List<Culture> getCultures() { return cultures; }

    // --- Gestion des cultures ---

    public void ajouterCulture(Culture culture) {
        cultures.add(culture);
    }

    public void supprimerCulture(Culture culture) {
        cultures.remove(culture);
    }

    // Filtre les cultures par famille (TypeCulture)
    public List<Culture> filtrerParFamille(TypeCulture type) {
        List<Culture> resultat = new ArrayList<>();
        for (Culture c : cultures) {
            if (c.getType() == type) {
                resultat.add(c);
            }
        }
        return resultat;
    }

    // Affiche les cultures filtrées par famille
    public void afficherCulturesParFamille(TypeCulture type) {
        List<Culture> filtrees = filtrerParFamille(type);
        System.out.println("  Cultures de type " + type + " dans " + getNom() + " :");
        if (filtrees.isEmpty()) {
            System.out.println("    Aucune culture de ce type.");
            return;
        }
        for (Culture c : filtrees) {
            System.out.println("    [" + c.getCode() + "] " + c.getNom() + " | Stade: " + c.getStadeCroissance());
        }
    }

    // Affiche l'évolution des stades de toutes les cultures de la zone
    public void afficherEvolutionToutesLesCultures() {
        System.out.println("  Evolution des stades - zone " + getNom() + " :");
        for (Culture c : cultures) {
            c.afficherEvolutionStades();
        }
    }

    // Affiche un rapport de l'état de toutes les cultures de cette zone
    public void afficherRapportCultures() {
        System.out.println("  Rapport cultures - " + getNom() + " :");
        if (cultures.isEmpty()) {
            System.out.println("    Aucune culture enregistrée.");
            return;
        }
        for (Culture c : cultures) {
            System.out.println("    [" + c.getCode() + "] " + c.getNom()
                    + " | Type: " + c.getType()
                    + " | Stade: " + c.getStadeCroissance()
                    + " | Récolte prévue: " + c.getDateRecolte());
        }
    }

    public int getNbEntites() { return cultures.size(); }

    public String getTypeZone() { return "ZoneCulture"; }

    public void afficherDetails() {
        System.out.println("  ZoneCulture [" + getCode() + "] " + getNom()
                + " | Statut: " + getStatut()
                + " | Cultures: " + cultures.size());
        afficherRapportCultures();
    }

    public String toString() {
        return "ZoneCulture{code='" + getCode() + "', nom='" + getNom()
                + "', statut=" + getStatut() + ", cultures=" + cultures.size() + "}";
    }
}