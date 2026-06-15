package Cultures;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Enumérations.StadeCroissance;
import Enumérations.TypeCulture;

// Représente une culture plantée dans une zone de culture.
// On y suit son stade de croissance et ses exigences pédologiques.
public class Culture {

    private String code;
    private String nom;
    private TypeCulture type;
    private LocalDate datePlantation;
    private LocalDate dateRecolte;
    private ExigencePedologique exigencePedologique;
    private StadeCroissance stadeCroissance;

    // Historique des changements de stade : chaque entrée = (date, stade)
    private List<String> historiqueStades;

    public Culture(String code, String nom, TypeCulture type,
                   LocalDate datePlantation, LocalDate dateRecolte,
                   ExigencePedologique exigencePedologique) {
        this.code = code;
        this.nom = nom;
        this.type = type;
        this.datePlantation = datePlantation;
        this.dateRecolte = dateRecolte;
        this.exigencePedologique = exigencePedologique;
        this.stadeCroissance = StadeCroissance.SEMIS; // stade initial par défaut
        this.historiqueStades = new ArrayList<>();
        // On enregistre le stade initial dans l'historique
        historiqueStades.add(LocalDate.now() + " -> " + StadeCroissance.SEMIS);
    }

    // --- Getters ---

    public String getCode()                         { return code; }
    public String getNom()                          { return nom; }
    public TypeCulture getType()                    { return type; }
    public LocalDate getDatePlantation()            { return datePlantation; }
    public LocalDate getDateRecolte()               { return dateRecolte; }
    public ExigencePedologique getExigencePedologique() { return exigencePedologique; }
    public StadeCroissance getStadeCroissance()     { return stadeCroissance; }
    public List<String> getHistoriqueStades()        { return historiqueStades; }

    // Met à jour le stade de croissance et l'enregistre dans l'historique
    public void changerStade(StadeCroissance nouveauStade) {
        this.stadeCroissance = nouveauStade;
        historiqueStades.add(LocalDate.now() + " -> " + nouveauStade);
    }

    // Affiche l'évolution du stade de croissance au fil du temps
    public void afficherEvolutionStades() {
        System.out.println("  Evolution des stades de [" + nom + "] :");
        for (String entree : historiqueStades) {
            System.out.println("    " + entree);
        }
    }

    public String toString() {
        return "Culture{code='" + code + "', nom='" + nom + "', type=" + type
                + ", stade=" + stadeCroissance
                + ", plantation=" + datePlantation + ", recolte=" + dateRecolte + "}";
    }
}