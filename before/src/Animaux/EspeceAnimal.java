package before.src.Animaux;

import before.src.Enumérations.TypeElevage;

// Décrit l'espèce d'un animal (son nom et la catégorie d'élevage à laquelle il appartient)
public class EspeceAnimal {

    private String nom;
    private TypeElevage typeElevage;

    public EspeceAnimal(String nom, TypeElevage typeElevage) {
        this.nom = nom;
        this.typeElevage = typeElevage;
    }

    public String getNom()              { return nom; }
    public TypeElevage getTypeElevage() { return typeElevage; }

    public String toString() {
        return "EspeceAnimal{nom='" + nom + "', type=" + typeElevage + "}";
    }
}
