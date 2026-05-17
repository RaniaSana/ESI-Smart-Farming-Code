package before.src.Zones;
import java.util.ArrayList;
import java.util.List;
import before.src.Enumérations.StatutZone;
import before.src.Enumérations.TypeProduction;
import before.src.Utilitaires.*;

// Classe abstraite représentant une zone géographique de la ferme.
// Chaque zone a un code unique, un nom, des limites GPS et un statut (active ou suspendue).
public abstract class Zone {

    private String code;
    private String nom;
    private LimitesGeographiques limites;
    private StatutZone statut;
    private List<HistoriqueProduction> historiqueProduction;

    public Zone(String code, String nom, LimitesGeographiques limites) {
        this.code = code;
        this.nom = nom;
        this.limites = limites;
        this.statut = StatutZone.ACTIVE; // toute zone est active à sa création
        this.historiqueProduction = new ArrayList<>();
    }

    // --- Getters ---

    public String getCode()                         { return code; }
    public String getNom()                          { return nom; }
    public LimitesGeographiques getLimites()        { return limites; }
    public StatutZone getStatut()                   { return statut; }
    public List<HistoriqueProduction> getHistoriqueProduction() { return historiqueProduction; }

    // --- Gestion du statut ---

    // Suspend la zone. Lance une exception si elle est déjà suspendue.
    public void suspendre() {
        if (statut == StatutZone.SUSPENDUE) {
            throw new ZoneDejaInactiveException("La zone " + code + " est déjà suspendue.");
        }
        this.statut = StatutZone.SUSPENDUE;
    }

    // Réactive une zone suspendue
    public void activer() {
        this.statut = StatutZone.ACTIVE;
    }

    // --- Production ---

    public void ajouterProduction(HistoriqueProduction record) {
        historiqueProduction.add(record);
    }

    // Retourne les enregistrements de production entre deux dates (incluses)
    public List<HistoriqueProduction> filtrerProductionParPeriode(java.util.Date debut, java.util.Date fin) {
        List<HistoriqueProduction> resultat = new ArrayList<>();
        for (HistoriqueProduction h : historiqueProduction) {
            // on garde les enregistrements dont la date est dans [debut, fin]
            if (!h.getDate().before(debut) && !h.getDate().after(fin)) {
                resultat.add(h);
            }
        }
        return resultat;
    }

    // Filtre aussi par type de production en plus de la période
    public List<HistoriqueProduction> filtrerProductionParTypeEtPeriode(TypeProduction type,
                                                                         java.util.Date debut,
                                                                         java.util.Date fin) {
        List<HistoriqueProduction> resultat = new ArrayList<>();
        for (HistoriqueProduction h : filtrerProductionParPeriode(debut, fin)) {
            if (h.getTypeProduction() == type) {
                resultat.add(h);
            }
        }
        return resultat;
    }

    // Calcule la quantité totale produite sur une période donnée (tous types confondus)
    public double calculerTotalProduction(java.util.Date debut, java.util.Date fin) {
        double total = 0;
        for (HistoriqueProduction h : filtrerProductionParPeriode(debut, fin)) {
            total += h.getQuantite();
        }
        return total;
    }

    // Affiche l'historique de production filtré par période
    public void afficherHistoriqueProduction(java.util.Date debut, java.util.Date fin) {
        List<HistoriqueProduction> filtres = filtrerProductionParPeriode(debut, fin);
        System.out.println("  Historique de production [" + getNom() + "] du " + debut + " au " + fin + " :");
        if (filtres.isEmpty()) {
            System.out.println("    Aucune production sur cette période.");
            return;
        }
        for (HistoriqueProduction h : filtres) {
            System.out.println("    " + h.getDetails());
        }
    }

    // Affiche tout l'historique sans filtre de date
    public void afficherHistoriqueProduction() {
        if (historiqueProduction.isEmpty()) {
            System.out.println("  Aucune production enregistrée.");
            return;
        }
        for (HistoriqueProduction h : historiqueProduction) {
            System.out.println("  " + h.getDetails());
        }
    }

    // --- Méthodes abstraites à implémenter par chaque type de zone ---

    // Retourne le nombre d'entités hébergées (cultures, animaux...)
    public abstract int getNbEntites();

    // Retourne le type de zone sous forme lisible
    public abstract String getTypeZone();

    // Affiche les détails spécifiques à la zone
    public abstract void afficherDetails();

    public String toString() {
        return getTypeZone() + "{code='" + code + "', nom='" + nom + "', statut=" + statut + "}";
    }
}
