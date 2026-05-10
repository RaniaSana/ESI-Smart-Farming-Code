package model;

import java.time.LocalDateTime;

public class ReleveNumerique extends Releve {
    private final double valeur;
    private final String unite;

    public ReleveNumerique(double valeur, String unite, LocalDateTime dateHeure) {
        super(dateHeure);
        this.valeur = valeur;
        this.unite = unite;
    }

    public double getValeur() {
        return valeur;
    }

    public String getUnite() {
        return unite;
    }

    @Override
    public boolean valider() {
        return super.valider() && unite != null && !unite.isBlank();
    }

    public NiveauReleve evaluerNiveau(PlageSeuils plageSeuils) {
        if (plageSeuils == null || plageSeuils.contient(valeur)) {
            return NiveauReleve.NORMAL;
        }
        return NiveauReleve.CRITIQUE;
    }

    public boolean estCritique(PlageSeuils plageSeuils) {
        return evaluerNiveau(plageSeuils) == NiveauReleve.CRITIQUE;
    }

    @Override
    public String toTexte() {
        return "ReleveNumerique{valeur=" + valeur + ", unite='" + unite + "', dateHeure=" + getDateHeure() + "}";
    }
}
