

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import serveur.GestionEtudiants;

import static org.junit.jupiter.api.Assertions.*;

public class GestionEtudiantsTest {
    private GestionEtudiants gestionEtudiants;

    @BeforeEach
    void setUp() {
        gestionEtudiants = new GestionEtudiants();
    }

    @Test
    void testAjouterEtudiant() {
        gestionEtudiants.ajouterEtudiant("Alice", 15.5);
        assertEquals(15.5, gestionEtudiants.getNote("Alice"));
        assertEquals(1, gestionEtudiants.getNombreEtudiants());
    }

    @Test
    void testGetNoteEtudiantInexistant() {
        assertNull(gestionEtudiants.getNote("Bob"));
    }

    @Test
    void testCalculerMoyenneClasseVide() {
        assertEquals(0.0, gestionEtudiants.calculerMoyenne());
    }

    @Test
    void testCalculerMoyenne() {
        gestionEtudiants.ajouterEtudiant("Alice", 15.5);
        gestionEtudiants.ajouterEtudiant("Bob", 12.0);
        assertEquals((15.5 + 12.0) / 2, gestionEtudiants.calculerMoyenne());
    }

    @Test
    void testSynchronisation() throws InterruptedException {
        Runnable ajouter = () -> {
            for (int i = 0; i < 100; i++) {
                gestionEtudiants.ajouterEtudiant("Etudiant1" + i, i);
            }
        };
        
        Runnable ajouter2 = () -> {
            for (int i = 0; i < 100; i++) {
                gestionEtudiants.ajouterEtudiant("Etudiant2" + i, i);
            }
        };
        Thread t1 = new Thread(ajouter);
        Thread t2 = new Thread(ajouter2);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        assertEquals(200, gestionEtudiants.getNombreEtudiants());
    }
}