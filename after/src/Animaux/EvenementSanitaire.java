package Animaux;

import java.util.Date;

import Enumérations.EtatSante;
import Enumérations.TypeEvenementSanitaire;

// Représente un événement sanitaire consigné pour un animal
// (contrôle, maladie, vaccination, etc.)
public class EvenementSanitaire {

    private Date date;
    private TypeEvenementSanitaire type;
    private EtatSante etatApres;
    private String description;

    public EvenementSanitaire(TypeEvenementSanitaire type, EtatSante etatApres, String description) {
        this.date = new Date(); // on horodate automatiquement à la création
        this.type = type;
        this.etatApres = etatApres;
        this.description = description;
    }

    public Date getDate()                       { return date; }
    public TypeEvenementSanitaire getType()     { return type; }
    public EtatSante getEtatApres()             { return etatApres; }
    public String getDescription()              { return description; }

    public String toString() {
        return "EvenementSanitaire{type=" + type + ", etatApres=" + etatApres
                + ", description='" + description + "', date=" + date + "}";
    }
}
