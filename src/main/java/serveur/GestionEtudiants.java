package serveur;

import java.util.HashMap;
import java.util.Map;

public class GestionEtudiants {
    private Map<String, Etudiant> etudiants;

    public GestionEtudiants() {
        this.etudiants = new HashMap<>();
    }

    public synchronized void ajouterEtudiant(String nom, double note) {
        etudiants.put(nom, new Etudiant(nom, note));
    }

    public synchronized Etudiant getEtudiant(String nom) {
        return etudiants.get(nom);
    }

    public synchronized double calculerMoyenne() {
        if (etudiants.isEmpty()) return 0.0;
        return etudiants.values().stream()
                .mapToDouble(Etudiant::getNote)
                .average()
                .orElse(0.0);
    }

    public synchronized int getNombreEtudiants() {
        return etudiants.size();
    }
}