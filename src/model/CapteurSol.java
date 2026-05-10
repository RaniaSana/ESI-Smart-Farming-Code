package model;

public class CapteurSol extends CapteurNumerique {
    public CapteurSol(String code, String zone, PlageSeuils plageSeuils, TypeMesure typeMesure, String uniteMesure) {
        super(code, zone, plageSeuils, typeMesure, uniteMesure);
    }

    @Override
    public double lireValeur() {
        return 0.0;
    }
}
