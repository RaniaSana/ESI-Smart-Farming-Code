package before.src.Zones;


import before.src.Utilitaires.*;    
import before.src.Animaux.*;

// Zone aquacole : un bassin avec une espèce aquacole (poissons, crevettes...)
// surveillée par des capteurs d'eau (température, oxygène, pH).
public class ZoneAquacole extends Zone {

    private String espece;
    private int nombreAnimaux;
    private ProgrammeAlimentation programme;

    // L'ordre des paramètres : code, nom, espece, limites, nombreAnimaux, programme
    // (correspondant à l'appel dans le main)
    public ZoneAquacole(String code, String nom, String espece,
                        LimitesGeographiques limites,
                        int nombreAnimaux, ProgrammeAlimentation programme) {
        super(code, nom, limites);
        this.espece = espece;
        this.nombreAnimaux = nombreAnimaux;
        this.programme = programme;
    }

    // --- Getters / Setters ---

    public String getEspece()                       { return espece; }
    public int getNombreAnimaux()                   { return nombreAnimaux; }
    public ProgrammeAlimentation getProgramme()     { return programme; }

    public void setNombreAnimaux(int nombreAnimaux) { this.nombreAnimaux = nombreAnimaux; }
    public void setProgramme(ProgrammeAlimentation programme) { this.programme = programme; }

    public int getNbEntites() { return nombreAnimaux; }

    public String getTypeZone() { return "ZoneAquacole"; }

    public void afficherDetails() {
        System.out.println("  ZoneAquacole [" + getCode() + "] " + getNom()
                + " | Statut: " + getStatut()
                + " | Espèce: " + espece
                + " | Nb: " + nombreAnimaux);
        System.out.println("  Programme: " + programme);
    }

    public String toString() {
        return "ZoneAquacole{code='" + getCode() + "', nom='" + getNom()
                + "', statut=" + getStatut()
                + ", espece='" + espece + "', nb=" + nombreAnimaux + "}";
    }
}