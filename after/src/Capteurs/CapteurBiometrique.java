package Capteurs;

import Releves.NiveauReleve;
import Releves.ReleveNumerique;

public class CapteurBiometrique extends Capteur {
    private final TypeMesure typeMesure;
    private final PlageSeuils plageSeuils;

    public CapteurBiometrique(String code, TypeMesure typeMesure, PlageSeuils plageSeuils) {
        this(code, "INCONNUE", typeMesure, plageSeuils);
    }

    public CapteurBiometrique(String code, String zone, TypeMesure typeMesure, PlageSeuils plageSeuils) {
        super(code, zone);
        this.typeMesure = typeMesure;
        this.plageSeuils = plageSeuils;
    }

    public TypeMesure getTypeMesure() {
        return typeMesure;
    }

    public PlageSeuils getPlageSeuils() {
        return plageSeuils;
    }

    @Override
    public double lireValeur() {
        return 0.0;
    }

    public NiveauReleve getReadingLevel(double valeur) {
        return new ReleveNumerique(valeur, typeMesure.name(), java.time.LocalDateTime.now())
            .evaluerNiveau(plageSeuils);
    }

    @Override
    public TypeMesure getTypeMesureCapteur() {
        return typeMesure;
    }
}
