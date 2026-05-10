package model;

import java.time.LocalDateTime;

public class ReleveGPS extends Releve {
    private final double latitude;
    private final double longitude;

    public ReleveGPS(double latitude, double longitude, LocalDateTime dateHeure) {
        super(dateHeure);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toTexte() {
        return "ReleveGPS{latitude=" + latitude + ", longitude=" + longitude + ", dateHeure=" + getDateHeure() + "}";
    }
}
