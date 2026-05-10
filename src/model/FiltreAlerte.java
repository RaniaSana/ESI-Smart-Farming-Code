package model;

import java.time.LocalDateTime;

public class FiltreAlerte {
    private final String zone;
    private final String typeCapteur;
    private final NiveauGravite niveau;
    private final LocalDateTime debut;
    private final LocalDateTime fin;

    public FiltreAlerte(String zone, String typeCapteur, NiveauGravite niveau, LocalDateTime debut, LocalDateTime fin) {
        this.zone = zone;
        this.typeCapteur = typeCapteur;
        this.niveau = niveau;
        this.debut = debut;
        this.fin = fin;
    }

    public boolean correspond(Alerte alerte) {
        if (alerte == null) {
            return false;
        }
        if (zone != null && !zone.isBlank() && !zone.equalsIgnoreCase(alerte.getZone())) {
            return false;
        }
        if (typeCapteur != null && !typeCapteur.isBlank() && !typeCapteur.equalsIgnoreCase(alerte.getTypeCapteur())) {
            return false;
        }
        if (niveau != null && niveau != alerte.getNiveau()) {
            return false;
        }
        if (debut != null && alerte.getDateCreation().isBefore(debut)) {
            return false;
        }
        if (fin != null && alerte.getDateCreation().isAfter(fin)) {
            return false;
        }
        return true;
    }
}
