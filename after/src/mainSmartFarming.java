

import java.util.Date;
import java.util.List;

import Animaux.*;
import Classe_Principale.SmartFarming;
import Cultures.*;
import Enumérations.*;
import Utilitaires.*;
import Zones.*;

public class mainSmartFarming {

    public static void main(String[] args) {

        // ============================================================
        // 1. CREATION DE LA FERME
        // ============================================================
        System.out.println("--- 1. Création de la ferme ---");

        LimitesGeographiques limitesGenerales = new LimitesGeographiques(36.0, 2.5, 37.0, 4.0);
        SmartFarming ferme = new SmartFarming("F001", "Green Valley Farm", limitesGenerales);

        System.out.println("Ferme créée : " + ferme);
        ferme.displayFarm();



        // ============================================================
        // 2. CREATION DES ZONES
        // ============================================================
        System.out.println("\n--- 2. Création des zones ---");

        LimitesGeographiques limite1 = new LimitesGeographiques(36.5, 3.0, 36.8, 3.5);
        LimitesGeographiques limite2 = new LimitesGeographiques(36.6, 3.1, 36.9, 3.6);
        LimitesGeographiques limite3 = new LimitesGeographiques(36.4, 2.9, 36.7, 3.3);

        ZoneCulture zoneCulture = new ZoneCulture("Z001", "North Fields", limite1);

        ProgrammeAlimentation progElevage = new ProgrammeAlimentation("Foin", 10.0);
        ZoneElevage zoneElevage = new ZoneElevage("Z002", "East Pasture", limite2, TypeElevage.RUMINANT, progElevage);

        ProgrammeAlimentation progAquacole = new ProgrammeAlimentation("Seeds", 20.0);
        ZoneAquacole zoneAquacole = new ZoneAquacole("Z003", "South Pond", "Thon", limite3, 100, progAquacole);

        ferme.ajouterZone(zoneCulture);
        ferme.ajouterZone(zoneElevage);
        ferme.ajouterZone(zoneAquacole);

        System.out.println("Zones ajoutées : " + zoneCulture);
        System.out.println("               " + zoneElevage);
        System.out.println("               " + zoneAquacole);



        // ============================================================
        // 3. VUE D'ENSEMBLE DES ZONES
        // ============================================================
        
        System.out.println("\n--- 3. Vue d'ensemble des zones ---");
        ferme.displayOverviewFarm();


        // ============================================================
        // 4. GESTION DES CULTURES
        // ============================================================
        System.out.println("\n--- 4. Gestion des cultures ---");

        ExigencePedologique exigence1 = new ExigencePedologique(6.0, 7.5, 40.0, 70.0);
        ExigencePedologique exigence2 = new ExigencePedologique(5.5, 7.0, 50.0, 80.0);
        ExigencePedologique exigence3 = new ExigencePedologique(6.0, 7.0, 45.0, 75.0);

        Culture wheat  = new Culture("C001", "Wheat",  TypeCulture.CEREALE,
                java.time.LocalDate.of(2025, 3, 1),  java.time.LocalDate.of(2025, 8, 20), exigence1);
        Culture tomato = new Culture("C002", "Tomato", TypeCulture.LEGUME,
                java.time.LocalDate.of(2025, 4, 10), java.time.LocalDate.of(2025, 8, 20), exigence2);
        Culture apple  = new Culture("C003", "Apple",  TypeCulture.FRUIT,
                java.time.LocalDate.of(2025, 3, 15), java.time.LocalDate.of(2025, 9, 10), exigence3);

        zoneCulture.ajouterCulture(wheat);
        zoneCulture.ajouterCulture(tomato);
        zoneCulture.ajouterCulture(apple);

        System.out.println("Cultures ajoutées à " + zoneCulture.getNom());
        zoneCulture.afficherRapportCultures();

        // Vérification des exigences pédologiques
        System.out.println("\nExigences wheat compatibles (pH=6.5, hum=55) ? " + exigence1.estCompatible(6.5, 55.0));
        System.out.println("Exigences wheat compatibles (pH=4.0, hum=30) ? " + exigence1.estCompatible(4.0, 30.0));


        // ============================================================
        // 5. EVOLUTION DES STADES DE CROISSANCE
        // ============================================================
        System.out.println("\n--- 5. Evolution des stades de croissance ---");

        System.out.println("Stade initial wheat : " + wheat.getStadeCroissance());
        wheat.changerStade(StadeCroissance.GERMINATION);
        System.out.println("Après changerStade  : " + wheat.getStadeCroissance());
        wheat.changerStade(StadeCroissance.CROISSANCE);

        tomato.changerStade(StadeCroissance.GERMINATION);

        //Affichage de l'évolution dans le temps pour une culture
        wheat.afficherEvolutionStades();

        // Evolution de toutes les cultures de la zone d'un coup
        System.out.println();
        zoneCulture.afficherEvolutionToutesLesCultures();


        // ============================================================
        // 6. FILTRAGE DES CULTURES PAR FAMILLE
        // ============================================================
        System.out.println("\n--- 6. Filtrage des cultures par famille ---");

        zoneCulture.afficherCulturesParFamille(TypeCulture.CEREALE);
        zoneCulture.afficherCulturesParFamille(TypeCulture.LEGUME);
        zoneCulture.afficherCulturesParFamille(TypeCulture.FRUIT);

        List<Culture> cereales = zoneCulture.filtrerParFamille(TypeCulture.CEREALE);
        System.out.println("Nb céréales trouvées : " + cereales.size());


        // ============================================================
        // 7. GESTION DES ANIMAUX
        // ============================================================
        System.out.println("\n--- 7. Gestion des animaux ---");

        EspeceAnimal especeVache  = new EspeceAnimal("Vache",  TypeElevage.RUMINANT);
        EspeceAnimal especeMouton = new EspeceAnimal("Mouton", TypeElevage.RUMINANT);

        // Tous les animaux sont sains à la création
        Animal vache1  = new Animal("A001", especeVache,  4, 520.0);
        Animal vache2  = new Animal("A002", especeVache,  3, 480.0);
        Animal mouton1 = new Animal("A003", especeMouton, 2,  65.0);

        zoneElevage.ajouterAnimal(vache1);
        zoneElevage.ajouterAnimal(vache2);
        zoneElevage.ajouterAnimal(mouton1);

        System.out.println("Animal : " + vache1);
        System.out.println("Animal : " + vache2);
        System.out.println("Animal : " + mouton1);

        // On place mouton1 en quarantaine directement
        mouton1.changerEtat(EtatSante.QUARANTAINE);
        System.out.println("\nAprès changerEtat mouton1 : " + mouton1.getEtat());

        zoneElevage.afficherAnimaux();


        // ============================================================
        // 8. EVENEMENTS SANITAIRES
        // ============================================================
        System.out.println("\n--- 8. Evénements sanitaires ---");

        // Contrôle qui révèle une maladie sur vache2
        vache2.declarerEvenementSanitaire(
                TypeEvenementSanitaire.CONTROLE,
                EtatSante.MALADE,
                "Perte de poids anormale détectée"
        );
        System.out.println("Evénement consigné pour vache2. Nouvel état : " + vache2.getEtat());

        // Traitement mis en place
        vache2.declarerEvenementSanitaire(
                TypeEvenementSanitaire.TRAITEMENT,
                EtatSante.MALADE,
                "Administration d'antibiotiques"
        );

        // Historique sanitaire complet
        //vache2.afficherHistoriqueSanitaire();

        // Historique filtré par période
        Date debut = new Date(System.currentTimeMillis() - 5000);
        Date fin   = new Date(System.currentTimeMillis() + 5000);
        //System.out.println("\nHistorique sanitaire vache2 filtré par période :");
        //vache2.afficherHistoriqueSanitaire(debut, fin);


        // ============================================================
        // 9. [EVAL A1] — isHealthy
        // ============================================================
        System.out.println("\n--- 9. [EVAL A1] isHealthy ---"); 

        System.out.println("vache1 isHealthy(500.0) -> " + vache1.isHealthy(500.0)); // true  : SAIN, poids 520 >= 500
        System.out.println("vache1 isHealthy(600.0) -> " + vache1.isHealthy(600.0)); // false : poids 520 < 600
        System.out.println("vache2 isHealthy(400.0) -> " + vache2.isHealthy(400.0)); // false : MALADE
        System.out.println("mouton1 isHealthy(50.0) -> " + mouton1.isHealthy(50.0)); // false : QUARANTAINE


        // ============================================================
        // 10. FILTRAGE DES ANIMAUX PAR ETAT DE SANTE
        // ============================================================
        System.out.println("\n--- 10. Filtrage des animaux par état de santé ---");

        zoneElevage.afficherAnimauxParEtat(EtatSante.SAIN);
        zoneElevage.afficherAnimauxParEtat(EtatSante.MALADE);

        // On place mouton1 en quarantaine directement
        mouton1.changerEtat(EtatSante.QUARANTAINE);
        System.out.println("\nSheep Health : " + mouton1.getEtat());

        zoneElevage.afficherAnimauxParEtat(EtatSante.QUARANTAINE);



        System.out.println("Nb malades/quarantaine : " + zoneElevage.getNbAnimauxMalades());


        // ============================================================
        // 11. PROGRAMME D'ALIMENTATION
        // ============================================================
        System.out.println("\n--- 11. Programme d'alimentation ---");

        System.out.println("Programme actuel    : " + zoneElevage.getProgramme());

        ProgrammeAlimentation nouveauProg = new ProgrammeAlimentation("Foin enrichi", 15.0);
        zoneElevage.setProgramme(nouveauProg);

        System.out.println("Programme mis à jour : " + zoneElevage.getProgramme());


        
        // New display
        zoneCulture.afficherDetails();
        zoneElevage.afficherDetails();
        zoneAquacole.afficherDetails();



        // ============================================================
        // 12. [EVAL A2] — Suspension / Réactivation de zone
        // ============================================================
        System.out.println("\n--- 12. [EVAL A2] Suspension et réactivation ---");

        System.out.println("Statut zoneAquacole avant : " + zoneAquacole.getStatut());
        zoneAquacole.suspendre();
        System.out.println("Statut après suspension   : " + zoneAquacole.getStatut());

        // Deuxième suspension → doit lever ZoneDejaInactiveException
        try {
            zoneAquacole.suspendre();
        } catch (ZoneDejaInactiveException e) {
            System.out.println("OK - ZoneDejaInactiveException : " + e.getMessage());
        }

        zoneAquacole.activer();
        System.out.println("Statut après réactivation : " + zoneAquacole.getStatut());


        // ============================================================
        // 13. [EVAL A3] — Comptage malades / quarantaine
        // ============================================================
        System.out.println("\n--- 13. [EVAL A3] Comptage animaux malades/quarantaine ---");

        // mouton1 est en quarantaine, vache2 est malade → 2
        System.out.println("Nb malades/quarantaine        : " + zoneElevage.getNbAnimauxMalades());

        vache1.changerEtat(EtatSante.MALADE);
        System.out.println("Après vache1 -> MALADE        : " + zoneElevage.getNbAnimauxMalades()); // 3


        // ============================================================
        // 14. HISTORIQUE DE PRODUCTION + FILTRES
        // ============================================================
        System.out.println("\n--- 14. Historique de production ---");

        Date maintenant = new Date();
        zoneElevage.ajouterProduction(new HistoriqueProduction(maintenant, TypeProduction.LAIT,  120.5, "litres"));
        zoneElevage.ajouterProduction(new HistoriqueProduction(maintenant, TypeProduction.LAIT,   80.0, "litres"));
        zoneElevage.ajouterProduction(new HistoriqueProduction(maintenant, TypeProduction.OEUFS,  50.0, "unités"));
        zoneCulture.ajouterProduction(new HistoriqueProduction(maintenant, TypeProduction.RENDEMENT_CULTURE, 300.0, "kg"));

        // Historique complet sans filtre
        System.out.println("Historique complet zoneElevage :");
        zoneElevage.afficherHistoriqueProduction();

        // Filtré par période
        Date debutPeriode = new Date(maintenant.getTime() - 10000);
        Date finPeriode   = new Date(maintenant.getTime() + 10000);

        System.out.println();
        zoneElevage.afficherHistoriqueProduction(debutPeriode, finPeriode);

        // Filtré par type ET période
        List<HistoriqueProduction> laits = zoneElevage.filtrerProductionParTypeEtPeriode(
                TypeProduction.LAIT, debutPeriode, finPeriode);
        System.out.println("Enregistrements LAIT sur la période : " + laits.size());

        // Total de production sur la période
        double total = zoneElevage.calculerTotalProduction(debutPeriode, finPeriode);
        System.out.println("Total production zoneElevage (toutes unités) : " + total);


        // ============================================================
        // 15. TOTAL PRODUCTION PAR ZONE
        // ============================================================
        System.out.println("\n--- 15. Total production par zone ---");
        ferme.afficherTotalProductionParZone(debutPeriode, finPeriode);


        // ============================================================
        // 16. RECHERCHE D'UNE ZONE PAR CODE
        // ============================================================
        System.out.println("\n--- 16. Recherche d'une zone par code ---");

        Zone trouvee = ferme.rechercherZone("Z002");
        System.out.println("Recherche Z002 : " + (trouvee != null ? trouvee : "introuvable"));

        Zone introuvable = ferme.rechercherZone("Z999");
        System.out.println("Recherche Z999 : " + (introuvable == null ? "introuvable" : introuvable));


        // ============================================================
        // 17. VUE D'ENSEMBLE FINALE
        // ============================================================
        System.out.println("\n--- 17. Vue d'ensemble finale ---");
        ferme.displayOverviewFarm();


        // ============================================================
        // 18. RAPPORT COMPLET DE LA FERME
        // ============================================================
        System.out.println("\n--- 18. Rapport complet de la ferme ---");
        ferme.genererRapportComplet();
    }
}