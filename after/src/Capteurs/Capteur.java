package Capteurs;

public abstract class Capteur {
    private final String code;
    private final String zone;
    private StatutCapteur statut;

    protected Capteur(String code) {
        this(code, "INCONNUE");
    }

    protected Capteur(String code, String zone) {
        this.code = code;
        this.zone = zone;
        this.statut = StatutCapteur.ACTIVE;
    }

    public String getCode() {
        return code;
    }

    public StatutCapteur getStatut() {
        return statut;
    }

    public String getZone() {
        return zone;
    }

    // Alias kept for compatibility with sample scripts.
    public StatutCapteur getStatus() {
        return statut;
    }

    public abstract double lireValeur();

    public void activer() {
        this.statut = StatutCapteur.ACTIVE;
    }

    public void marquerDefaillant() {
        this.statut = StatutCapteur.FAULTY;
    }

    public void suspendre() {
        this.statut = StatutCapteur.SUSPENDED;
    }

    // Generic status switch used by sample scenarios.
    public void changeStatus(StatutCapteur nouveauStatut) {
        if (nouveauStatut != null) {
            this.statut = nouveauStatut;
        }
    }

    public TypeMesure getTypeMesureCapteur() {
        return null;
    }
}
