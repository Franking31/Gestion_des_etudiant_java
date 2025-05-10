
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serveur.Etudiant;
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
        Etudiant etudiant = gestionEtudiants.getEtudiant("Alice");
        assertNotNull(etudiant);
        assertEquals(15.5, etudiant.getNote());
        assertEquals(1, gestionEtudiants.getNombreEtudiants());
    }

    @Test
    void testGetEtudiantInexistant() {
        assertNull(gestionEtudiants.getEtudiant("Bob"));
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
    void testGetListeEtudiants() {
        gestionEtudiants.ajouterEtudiant("Alice", 15.5);
        gestionEtudiants.ajouterEtudiant("Bob", 12.0);
        String liste = gestionEtudiants.getListeEtudiants();
        assertTrue(liste.contains("Alice: 15.5"));
        assertTrue(liste.contains("Bob: 12.0"));
    }

    @Test
    void testGetListeEtudiantsVide() {
        String liste = gestionEtudiants.getListeEtudiants();
        assertEquals("Aucun étudiant enregistré.", liste);
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
