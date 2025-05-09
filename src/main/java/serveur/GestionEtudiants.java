package serveur;

import java.util.HashMap;
import java.util.Map;

public class GestionEtudiants {
    private Map<String, Double> etudiants;

    public GestionEtudiants() {
        this.etudiants = new HashMap<>();
    }

    public synchronized void ajouterEtudiant(String nom, double note) {
        etudiants.put(nom, note);
    }

    public synchronized Double getNote(String nom) {
        return etudiants.get(nom);
    }

    public synchronized double calculerMoyenne() {
        if (etudiants.isEmpty()) return 0.0;
        return etudiants.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public synchronized int getNombreEtudiants() {
        return etudiants.size();
    }
}