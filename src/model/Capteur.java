package model;

import java.time.LocalDateTime;

public abstract class Capteur {
    private final String code;
    private final String zone;
    private StatutCapteur statut;
    private final PlageSeuils plageSeuils;

    protected Capteur(String code, String zone, PlageSeuils plageSeuils) {
        this.code = code;
        this.zone = zone;
        this.plageSeuils = plageSeuils;
        this.statut = StatutCapteur.ACTIF;
    }

    protected Capteur(String code, double seuilMin, double seuilMax) {
        this(code, "NON_DEFINIE", new PlageSeuils(seuilMin, seuilMax));
    }

    public String getCode() {
        return code;
    }

    public String getZone() {
        return zone;
    }

    public StatutCapteur getStatut() {
        return statut;
    }

    public double getSeuilMin() {
        return plageSeuils.getMin();
    }

    public double getSeuilMax() {
        return plageSeuils.getMax();
    }

    public PlageSeuils getPlageSeuils() {
        return plageSeuils;
    }

    public abstract double lireValeur();

    public ReleveNumerique creerReleve(double valeur, String unite) {
        return new ReleveNumerique(valeur, unite, LocalDateTime.now());
    }

    public boolean estHorsSeuil(double valeur) {
        return plageSeuils.horsSeuil(valeur);
    }

    public Alerte genererAlerte(ReleveNumerique releve) {
        if (!estHorsSeuil(releve.getValeur())) {
            return null;
        }
        String message = "Capteur " + code + " hors seuil: " + releve.toTexte();
        return new Alerte(NiveauGravite.CRITIQUE, StatutAlerte.OUVERTE, message, zone, getClass().getSimpleName());
    }

    public void activer() {
        this.statut = StatutCapteur.ACTIF;
    }

    public void suspendre() {
        this.statut = StatutCapteur.SUSPENDU;
    }
}
