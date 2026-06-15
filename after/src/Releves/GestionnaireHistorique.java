package Releves;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Capteurs.Capteur;
import Capteurs.PlageSeuils;
import Capteurs.TypeMesure;

public class GestionnaireHistorique {
    public static class EntreeReleve {
        private final Capteur capteur;
        private final ReleveNumerique releve;
        private final NiveauReleve niveau;

        public EntreeReleve(Capteur capteur, ReleveNumerique releve, NiveauReleve niveau) {
            this.capteur = capteur;
            this.releve = releve;
            this.niveau = niveau;
        }

        public Capteur getCapteur() {
            return capteur;
        }

        public ReleveNumerique getReleve() {
            return releve;
        }

        public NiveauReleve getNiveau() {
            return niveau;
        }
    }

    private final List<EntreeReleve> historiqueReleves = new ArrayList<>();

    public void ajouterReleve(Capteur capteur, ReleveNumerique releve, PlageSeuils plage) {
        if (capteur == null || releve == null) {
            return;
        }
        NiveauReleve niveau = releve.evaluerNiveau(plage);
        historiqueReleves.add(new EntreeReleve(capteur, releve, niveau));
    }

    public List<EntreeReleve> consulterHistoriqueRelevesCapteur(
        String codeCapteur,
        LocalDateTime debut,
        LocalDateTime fin
    ) {
        return historiqueReleves.stream()
            .filter(e -> codeCapteur == null || e.getCapteur().getCode().equals(codeCapteur))
            .filter(e -> debut == null || !e.getReleve().getDateHeure().isBefore(debut))
            .filter(e -> fin == null || !e.getReleve().getDateHeure().isAfter(fin))
            .sorted(Comparator.comparing(e -> e.getReleve().getDateHeure()))
            .collect(Collectors.toList());
    }

    public List<EntreeReleve> filtrerReleves(
        NiveauReleve niveau,
        String zone,
        TypeMesure typeMesure,
        LocalDateTime debut,
        LocalDateTime fin
    ) {
        return historiqueReleves.stream()
            .filter(e -> niveau == null || e.getNiveau() == niveau)
            .filter(e -> zone == null || e.getCapteur().getZone().equals(zone))
            .filter(e -> typeMesure == null || typeMesure == e.getCapteur().getTypeMesureCapteur())
            .filter(e -> debut == null || !e.getReleve().getDateHeure().isBefore(debut))
            .filter(e -> fin == null || !e.getReleve().getDateHeure().isAfter(fin))
            .sorted(Comparator.comparing(e -> e.getReleve().getDateHeure()))
            .collect(Collectors.toList());
    }

    public void afficherTableauBordZone(String zone) {
        System.out.println("=== TABLEAU DE BORD ZONE " + zone + " ===");
        Map<String, EntreeReleve> dernierParCapteur = historiqueReleves.stream()
            .filter(e -> zone == null || e.getCapteur().getZone().equals(zone))
            .collect(Collectors.toMap(
                e -> e.getCapteur().getCode(),
                e -> e,
                (a, b) -> a.getReleve().getDateHeure().isAfter(b.getReleve().getDateHeure()) ? a : b
            ));

        if (dernierParCapteur.isEmpty()) {
            System.out.println("Aucun relevé pour cette zone.");
            return;
        }

        for (EntreeReleve e : dernierParCapteur.values()) {
            System.out.println(
                e.getCapteur().getCode()
                    + " zone=" + e.getCapteur().getZone()
                    + "type=" + e.getCapteur().getTypeMesureCapteur()
                    + " valeur=" + e.getReleve().getValeur() + " " + e.getReleve().getUnite()
                    + " niveau=" + e.getNiveau()
            );
        }


    }
}
