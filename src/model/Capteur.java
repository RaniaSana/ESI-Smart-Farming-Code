package model;

import java.time.LocalDateTime;

public abstract class Capteur {
    
    private final String code;
    private StatutCapteur statut;
    private final double seuilMin;
    private final double seuilMax;



    protected Capteur(String code, double seuilMin, double seuilMax) {
        this.code = code;
        this.seuilMin = seuilMin;
        this.seuilMax = seuilMax;
        this.statut = StatutCapteur.ACTIF;
    }

    public String getCode() {
        return code;
    }

    public StatutCapteur getStatut() {
        return statut;
    }

    public double getSeuilMin() {
        return seuilMin;
    }

    public double getSeuilMax() {
        return seuilMax;
    }

    public abstract double lireValeur();
    public Releve creerReleve(double valeur, String unite) {
        return new Releve(valeur, unite, LocalDateTime.now());
    }

    public boolean estHorsSeuil(double valeur) {
        return valeur < seuilMin || valeur > seuilMax;
    }

    public Alerte genererAlerte(Releve releve) {
        if (!estHorsSeuil(releve.getValeur())) {
            return null;
        }
        String message = "Capteur " + code + " hors seuil: " + releve.toTexte();
        return new Alerte(NiveauGravite.CRITIQUE, message);
    }
    public void activer() {
        this.statut = StatutCapteur.ACTIF;
    }
    public void suspendre() {
        this.statut = StatutCapteur.SUSPENDU;
    }
}
