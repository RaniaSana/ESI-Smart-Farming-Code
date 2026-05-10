package model;
import java.time.LocalDateTime;

public class Releve {
    private final double valeur;
    private final String unite;
    private final LocalDateTime dateHeure;



    public Releve(double valeur, String unite, LocalDateTime dateHeure) {
        this.valeur = valeur;
        this.unite = unite;
        this.dateHeure = dateHeure; //record
    }
    public double getValeur() {
        return valeur; //like we get a measurement record
    }
    public String getUnite() {
        return unite; // the unite we need
    }
    public LocalDateTime getDateHeure() {
        return dateHeure; //timestampp
    }

//unité non nulle and date non nulle
    public boolean valider() { 
        return unite != null && !unite.isBlank() &&
         dateHeure != null;
    }


    public boolean estCritique(double seuilMin, double seuilMax) {
        return valeur < seuilMin || valeur > seuilMax;
    }

    public String toTexte() {
        return "Releve{valeur=" + valeur +
         ", unite='" + unite + "', dateHeure=" 
         + dateHeure + "}";
    }
}
