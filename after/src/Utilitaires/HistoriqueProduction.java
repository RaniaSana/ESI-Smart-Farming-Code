package Utilitaires;
import java.util.Date;

import Enumérations.TypeProduction;

// Enregistre une entrée de production pour une zone (quantité produite à une date donnée)
public class HistoriqueProduction {

    private Date date;
    private TypeProduction typeProduction;
    private double quantite;
    private String unite;

    public HistoriqueProduction(Date date, TypeProduction typeProduction, double quantite, String unite) {
        this.date = date;
        this.typeProduction = typeProduction;
        this.quantite = quantite;
        this.unite = unite;
    }

    public Date getDate()                   { return date; }
    public TypeProduction getTypeProduction() { return typeProduction; }
    public double getQuantite()             { return quantite; }
    public String getUnite()               { return unite; }

    // Retourne un résumé lisible de l'enregistrement
    public String getDetails() {
        return typeProduction + " - " + quantite + " " + unite + " (" + date + ")";
    }

    public String toString() {
        return "HistoriqueProduction{type=" + typeProduction + ", quantite=" + quantite + " " + unite + ", date=" + date + "}";
    }
}