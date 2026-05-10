package model;

import java.time.LocalDateTime;

public class Alerte {
    private final NiveauGravite niveau;
    private StatutAlerte statut;
    private final String message;
    private final LocalDateTime dateCreation;
    private final String zone;
    private final String typeCapteur;

    public Alerte(NiveauGravite niveau, String message) {
        this(niveau, StatutAlerte.OUVERTE, message, "NON_DEFINIE", "NON_DEFINI");
    }

    public Alerte(NiveauGravite niveau, StatutAlerte statut, String message, String zone, String typeCapteur) {
        this.niveau = niveau;
        this.statut = statut;
        this.message = message;
        this.zone = zone;
        this.typeCapteur = typeCapteur;
        this.dateCreation = LocalDateTime.now();
    }

    public NiveauGravite getNiveau() {
        return niveau;
    }

    public StatutAlerte getStatut() {
        return statut;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public String getZone() {
        return zone;
    }

    public String getTypeCapteur() {
        return typeCapteur;
    }

    public boolean estActive() {
        return statut != StatutAlerte.SUPPRIMEE && statut != StatutAlerte.RESOLUE;
    }

    public void acquitter() {
        this.statut = StatutAlerte.EN_COURS;
    }

    public void resoudre() {
        this.statut = StatutAlerte.RESOLUE;
    }

    public void supprimer() {
        this.statut = StatutAlerte.SUPPRIMEE;
    }
}
