package Cultures;

// Définit les conditions de sol optimales pour une culture : plage de pH et d'humidité
public class ExigencePedologique {

    private double phMin;
    private double phMax;
    private double humiditeMin;
    private double humiditeMax;

    public ExigencePedologique(double phMin, double phMax, double humiditeMin, double humiditeMax) {
        this.phMin = phMin;
        this.phMax = phMax;
        this.humiditeMin = humiditeMin;
        this.humiditeMax = humiditeMax;
    }

    public double getPhMin()        { return phMin; }
    public double getPhMax()        { return phMax; }
    public double getHumiditeMin()  { return humiditeMin; }
    public double getHumiditeMax()  { return humiditeMax; }

    // Vérifie si les conditions actuelles du sol sont dans les plages acceptables
    public boolean estCompatible(double ph, double humidite) {
        return ph >= phMin && ph <= phMax && humidite >= humiditeMin && humidite <= humiditeMax;
    }

    public String toString() {
        return "ExigencePedologique{pH=[" + phMin + ", " + phMax + "], humidite=[" + humiditeMin + ", " + humiditeMax + "]}";
    }
}
