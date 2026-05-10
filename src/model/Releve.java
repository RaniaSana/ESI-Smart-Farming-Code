package model;

import java.time.LocalDateTime;

public abstract class Releve {
    private final LocalDateTime dateHeure;

    protected Releve(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public LocalDateTime getDateHeure() {
        return dateHeure;
    }

    public boolean valider() {
        return dateHeure != null;
    }

    public abstract String toTexte();
}
