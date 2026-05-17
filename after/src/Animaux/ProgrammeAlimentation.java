package after.src.Animaux;

// Définit le régime alimentaire d'une zone : type d'aliment et quantité par repas
public class ProgrammeAlimentation {

    private String typeAliment;
    private double quantiteParRepas;

    public ProgrammeAlimentation(String typeAliment, double quantiteParRepas) {
        this.typeAliment = typeAliment;
        this.quantiteParRepas = quantiteParRepas;
    }

    public String getTypeAliment()       { return typeAliment; }
    public double getQuantiteParRepas()  { return quantiteParRepas; }

    public void setTypeAliment(String typeAliment)        
      { this.typeAliment = typeAliment; }

    public void setQuantiteParRepas(double quantiteParRepas) 
    { this.quantiteParRepas = quantiteParRepas; }

    public String toString() {
        return "ProgrammeAlimentation{aliment='" + typeAliment + "', quantite=" + quantiteParRepas + " kg/repas}";
    }
}
