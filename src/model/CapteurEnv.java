package model;

public class CapteurEnv extends Capteur {
    private final TypeEnv type;

    public CapteurEnv(String code, double seuilMin, double seuilMax, TypeEnv type) {
        super(code, seuilMin, seuilMax);
        this.type = type;
    }

    public TypeEnv getType() {
        return type;
    }

    @Override
    public double lireValeur() {
        return 0.0;
    }
}
