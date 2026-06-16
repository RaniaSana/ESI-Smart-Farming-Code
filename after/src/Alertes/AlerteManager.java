package Alertes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import Capteurs.Capteur;
import Capteurs.PlageSeuils;
import Capteurs.TypeMesure;
import Releves.NiveauReleve;
import Releves.ReleveNumerique;

public class AlerteManager {
    public static class EntreeAlerte {
        private final Capteur capteur;
        private final Alerte alerte;

        public EntreeAlerte(Capteur capteur, Alerte alerte) {
            this.capteur = capteur;
            this.alerte = alerte;
        }

        public Capteur getCapteur() {
            return capteur;
        }

        public Alerte getAlerte() {
            return alerte;
        }
    }

    private final List<Alerte> alertes = new ArrayList<>();
    private final List<EntreeAlerte> historiqueAlertes = new ArrayList<>();

    public void ajouterAlerte(Alerte alerte) {
        if (alerte != null) {
            alertes.add(alerte);
        }
    }

    public Alerte creerAlerteSiNecessaire(
        Capteur capteur,
        ReleveNumerique releve,
        PlageSeuils plageSeuils
    ) {
        if (capteur == null || releve == null) {
            return null;
        }

        NiveauReleve niveau = releve.evaluerNiveau(plageSeuils);
        if (niveau == NiveauReleve.NORMAL) {
            return null;
        }

        NiveauGravite gravite = (niveau == NiveauReleve.CRITICAL)
            ? NiveauGravite.CRITICAL
            : NiveauGravite.WARNING;

        String message = "Capteur " + capteur.getCode()
            + " hors seuil: " + releve.getValeur() + " " + releve.getUnite();

        Alerte alerte = new Alerte(gravite, message);
        alertes.add(alerte);
        historiqueAlertes.add(new EntreeAlerte(capteur, alerte));
        return alerte;
    }

    public List<Alerte> getAlertesActives() {
        List<Alerte> actives = new ArrayList<>();
        for (Alerte alerte : alertes) {
            if (alerte.getStatut() == StatutAlerte.ACTIVE) {
                actives.add(alerte);
            }
        }
        return actives;
    }

    public int countActiveAlerts() {
        return getAlertesActives().size();
    }

    // Alias methods for compatibility with the provided scenario naming.
    public List<Alerte> getActiveAlerts() {
        return getAlertesActives();
    }

    public void displayActiveAlerts() {
        List<Alerte> actives = getAlertesActives();
        if (actives.isEmpty()) {
            System.out.println("Aucune alerte active.");
            return;
        }
        for (Alerte alerte : actives) {
            System.out.println("- [" + alerte.getNiveau() + "] " + alerte.getMessage());
        }
    }

    public List<EntreeAlerte> consulterHistoriqueAlertes() {
        return new ArrayList<>(historiqueAlertes);
    }

    public List<EntreeAlerte> filtrerAlertes(
        String zone,
        TypeMesure typeMesure,
        NiveauGravite gravite,
        LocalDateTime debut,
        LocalDateTime fin
    ) {
        return historiqueAlertes.stream()
            .filter(e -> zone == null || e.getCapteur().getZone().equals(zone))
            .filter(e -> typeMesure == null || typeMesure == e.getCapteur().getTypeMesureCapteur())
            .filter(e -> gravite == null || gravite == e.getAlerte().getNiveau())
            .filter(e -> debut == null || !e.getAlerte().getDateCreation().isBefore(debut))
            .filter(e -> fin == null || !e.getAlerte().getDateCreation().isAfter(fin))
            .collect(Collectors.toList());
    }

    public List<Alerte> getAlertesActivesTrieesParGravite() {
        Comparator<Alerte> comp = Comparator
            .comparing((Alerte a) -> a.getNiveau() == NiveauGravite.CRITICAL ? 0 : 1)
            .thenComparing(Alerte::getDateCreation);
        return getAlertesActives().stream().sorted(comp).collect(Collectors.toList());
    }

    public void afficherAlertesActivesTrieesParGravite() {
        List<Alerte> triees = getAlertesActivesTrieesParGravite();
        if (triees.isEmpty()) {
            System.out.println("Aucune alerte active.");
            return;
        }
        System.out.println("=== ALERTES ACTIVES (TRIEES PAR GRAVITE) ===");
        for (Alerte a : triees) {
            System.out.println("- [" + a.getNiveau() + "] " + a.getMessage());
        }
    }
}
