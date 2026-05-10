package model;

public class CapteurBiometrique extends Capteur {
    private final TypeBiom type;

    public CapteurBiometrique(String code, double seuilMin, double seuilMax, TypeBiom type) {
        super(code, seuilMin, seuilMax);
        this.type = type;
    }

    public TypeBiom getType() {
        return type;
    }

    @Override
    public double lireValeur() {
        return 0.0;
    }

}
