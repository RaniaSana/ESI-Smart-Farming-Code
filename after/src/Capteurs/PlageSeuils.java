package Capteurs;

public class PlageSeuils {
    private final double min;
    private final double max;

    public PlageSeuils(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("min doit etre <= max");
        }
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public boolean contient(double valeur) {
        return valeur >= min && valeur <= max;
    }

    public boolean horsSeuil(double valeur) {
        return !contient(valeur);
    }
}
