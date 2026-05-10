package model;

public class CapteurGPS extends Capteur {
    private double latitude;
    private double longitude;

    public CapteurGPS(String code, double seuilMin, double seuilMax, double latitude, double longitude) {
        super(code, seuilMin, seuilMax);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public double lireValeur() {
        return 0.0;
    }

    public String localiser() {
        return latitude + ", " + longitude;
    }
}
