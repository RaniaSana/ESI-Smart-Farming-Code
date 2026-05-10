package model;

public class CapteurSol extends Capteur {
    private final TypeSol type;
    public CapteurSol(String code, double seuilMin, double seuilMax, TypeSol type) {
        super(code, seuilMin, seuilMax);
        this.type = type;
    }

    public TypeSol getType() {
        return type;
    }

    @Override
    public double lireValeur() {
        return 0.0;
    }
}
