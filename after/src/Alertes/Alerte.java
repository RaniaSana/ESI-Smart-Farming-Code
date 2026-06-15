package Alertes;

import java.time.LocalDateTime;

public class Alerte {
    private final NiveauGravite niveau;
    private StatutAlerte statut;
    private final String message;
    private final LocalDateTime dateCreation;

    public Alerte(NiveauGravite niveau, String message) {
        this.niveau = niveau;
        this.message = message;
        this.statut = StatutAlerte.ACTIVE;
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

    public boolean estActive() {
        return statut == StatutAlerte.ACTIVE || statut == StatutAlerte.ACKNOWLEDGED;
    }

    public void acquitter() {
        this.statut = StatutAlerte.ACKNOWLEDGED;
    }

    public void supprimer() {
        this.statut = StatutAlerte.DISMISSED;
    }
}
