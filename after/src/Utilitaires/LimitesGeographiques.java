package Utilitaires;

// Délimite une zone géographique par ses coordonnées GPS (coin bas-gauche / coin haut-droit)
public class LimitesGeographiques {

    private double latMin;
    private double lonMin;
    private double latMax;
    private double lonMax;

    public LimitesGeographiques(double latMin, double lonMin, double latMax, double lonMax) {
        this.latMin = latMin;
        this.lonMin = lonMin;
        this.latMax = latMax;
        this.lonMax = lonMax;
    }

    public double getLatMin() { return latMin; }
    public double getLonMin() { return lonMin; }
    public double getLatMax() { return latMax; }
    public double getLonMax() { return lonMax; }

    // Vérifie si une position GPS donnée se trouve dans ces limites
    public boolean contient(double lat, double lon) {
        return lat >= latMin && lat <= latMax && lon >= lonMin && lon <= lonMax;
    }

    public String toString() {
        return "[(" + latMin + ", " + lonMin + ") -> (" + latMax + ", " + lonMax + ")]";
    }
}