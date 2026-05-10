package model;

public class CapteurBiometrique extends CapteurNumerique {
    public CapteurBiometrique(String code, String zone, PlageSeuils plageSeuils, TypeMesure typeMesure, String uniteMesure) {
        super(code, zone, plageSeuils, typeMesure, uniteMesure);
    }

    @Override
    public double lireValeur() {
        return 0.0;
    }
}
