package after.src.Classe_Principale;

import java.util.ArrayList;
import java.util.List;

import after.src.Utilitaires.*;
import after.src.Zones.*;



// Classe principale représentant la ferme intelligente.
// Elle regroupe toutes les zones et offre des méthodes de gestion et d'affichage.
public class SmartFarming {

    private String id;
    private String nom;
    private LimitesGeographiques limites;
    private List<Zone> zones;

    public SmartFarming(String id, String nom, LimitesGeographiques limites) {
        this.id = id;
        this.nom = nom;
        this.limites = limites;
        this.zones = new ArrayList<>();
    }

    // --- Getters ---

    public String getId()                   { return id; }
    public String getNom()                  { return nom; }
    public LimitesGeographiques getLimites(){ return limites; }
    public List<Zone> getZones()            { return zones; }

    // --- Gestion des zones ---

    public void ajouterZone(Zone zone) {
        zones.add(zone);
    }

    public void supprimerZone(Zone zone) {
        zones.remove(zone);
    }

    // Recherche une zone par son code, retourne null si introuvable
    public Zone rechercherZone(String code) {
        for (Zone z : zones) {
            if (z.getCode().equals(code)) {
                return z;
            }
        }
        return null;
    }

    // Affiche les informations de base de la ferme
    public void displayFarm() {
        System.out.println("Ferme       : " + nom + " (ID: " + id + ")");
        System.out.println("Limites GPS : " + limites);
        System.out.println("Zones       : " + zones.size());
    }

    // Génère un rapport complet de l'état de la ferme :
    // pour chaque zone, on affiche son statut, ses entités et son historique de production
    public void genererRapportComplet() {
        System.out.println("===================================================================");
        System.out.println("  RAPPORT COMPLET - " + nom + " (ID: " + id + ")");
        System.out.println("===================================================================");
        System.out.println("Limites GPS : " + limites);
        System.out.println("Nombre de zones : " + zones.size());
        System.out.println("-------------------------------------------------------------------");

        for (Zone z : zones) {
            System.out.print("Zone  : ");
            z.afficherDetails();

            // Si c'est une zone d'élevage, on affiche aussi le bilan sanitaire
            /*if (z instanceof ZoneElevage) {
                ZoneElevage ze = (ZoneElevage) z;
                System.out.println("  Animaux malades/quarantaine : " + ze.getNbAnimauxMalades());
                System.out.println("  Programme : " + ze.getProgramme());
            }

            // Historique de production (tout afficher sans filtre)
            System.out.println("  Productions enregistrées :");
            z.afficherHistoriqueProduction();*/
            System.out.println("--------------------------------------------------------");
        }

        System.out.println("=========================================================cd ..==");
    }

    // Calcule et affiche le total de production de chaque zone sur une période donnée
    public void afficherTotalProductionParZone(java.util.Date debut, java.util.Date fin) {
        System.out.println("  Total de production par zone du " + debut + " au " + fin + " :");
        for (Zone z : zones) {
            double total = z.calculerTotalProduction(debut, fin);
            System.out.println("    [" + z.getNom() + "] : " + total);
        }
    }

    // Affiche une vue d'ensemble de toutes les zones (statut + nb entités)
    // Retourne aussi la chaîne pour permettre un éventuel System.out.println(ferme.displayOverviewFarm())
    public String displayOverviewFarm() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Vue d'ensemble : ").append(nom).append(" ===\n");
        sb.append("Nombre de zones : ").append(zones.size()).append("\n");

        for (Zone z : zones) {
            sb.append("  [").append(z.getTypeZone()).append("] ")
              .append(z.getNom())
              .append(" | Statut: ").append(z.getStatut())
              .append(" | Entités: ").append(z.getNbEntites())
              .append("\n");
        }

        System.out.print(sb);
        return sb.toString();
    }

    // Affiche les détails complets de chaque zone (délègue à chaque zone)
    public void afficherToutesLesZones() {
        System.out.println("=== Détail de toutes les zones : " + nom + " ===");
        for (Zone z : zones) {
            System.out.println("---");
            z.afficherDetails();
        }
    }

    /*public String toString() {
        return "SmartFarming{id='" + id + "', nom='" + nom + "', zones=" + zones.size() + "}";*/

    public String toString() {
        return "[" + id + "]" + "   " + nom ;
    }

}