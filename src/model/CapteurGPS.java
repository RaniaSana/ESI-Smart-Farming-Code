package model;

import java.time.LocalDateTime;

public class CapteurGPS extends Capteur {
    private double latitude;
    private double longitude;

    public CapteurGPS(String code, String zone, PlageSeuils plageSeuils, double latitude, double longitude) {
        super(code, zone, plageSeuils);
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

    public ReleveGPS creerReleveGPS() {
        return new ReleveGPS(latitude, longitude, LocalDateTime.now());
    }

    public String localiser() {
        return latitude + ", " + longitude;
    }
}
