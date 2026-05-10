package model;

import java.time.LocalDateTime;

public class Alerte {
    private NiveauGravite niveau;
    private StatutAlerte statut;
    private String message;
    private final LocalDateTime dateCreation;

    public Alerte(NiveauGravite niveau, String message) {
        this.niveau = niveau;
        this.message = message;
        this.statut = StatutAlerte.OUVERTE;
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
