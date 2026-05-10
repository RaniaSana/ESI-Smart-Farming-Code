package model;

public abstract class CapteurNumerique extends Capteur {
    private final TypeMesure typeMesure;
    private final String uniteMesure;

    protected CapteurNumerique(String code, String zone, PlageSeuils plageSeuils, TypeMesure typeMesure, String uniteMesure) {
        super(code, zone, plageSeuils);
        this.typeMesure = typeMesure;
        this.uniteMesure = uniteMesure;
    }

    public TypeMesure getTypeMesure() {
        return typeMesure;
    }

    public String getUniteMesure() {
        return uniteMesure;
    }
}
