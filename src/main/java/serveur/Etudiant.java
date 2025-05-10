package serveur;

public class Etudiant {
    private String nom;
    private double note;

    public Etudiant(String nom, double note) {
        this.nom = nom;
        this.note = note;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Etudiant{nom='" + nom + "', note=" + note + "}";
    }
}