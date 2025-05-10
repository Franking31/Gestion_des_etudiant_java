import org.java_websocket.WebSocket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import serveur.Etudiant;
import serveur.GestionEtudiants;
import serveur.Serveur;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class ServeurMessageTest {
    @Mock
    private WebSocket mockWebSocket;
    @Mock
    private GestionEtudiants gestionEtudiants;

    private Serveur serveur;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        // Créer une instance du serveur
        serveur = new Serveur();
        // Injecter le mock de GestionEtudiants via réflexion
        Field field = Serveur.class.getDeclaredField("gestionEtudiants");
        field.setAccessible(true);
        field.set(serveur, gestionEtudiants);
    }

    @Test
    void testAjouterEtudiantValide() {
        serveur.onMessage(mockWebSocket, "AJOUTER Alice 15.5");
        verify(gestionEtudiants).ajouterEtudiant("Alice", 15.5);
        verify(mockWebSocket).send("Étudiant ajouté: Alice");
    }

    @Test
    void testAjouterEtudiantNoteInvalide() {
        serveur.onMessage(mockWebSocket, "AJOUTER Bob abc");
        verifyNoInteractions(gestionEtudiants);
        verify(mockWebSocket).send("Erreur: Note invalide");
    }

    @Test
    void testGetNoteEtudiantExistant() {
        when(gestionEtudiants.getEtudiant("Alice")).thenReturn(new Etudiant("Alice", 15.5));
        serveur.onMessage(mockWebSocket, "NOTE Alice");
        verify(gestionEtudiants).getEtudiant("Alice");
        verify(mockWebSocket).send("Note de Alice: 15.5");
    }

    @Test
    void testGetNoteEtudiantInexistant() {
        when(gestionEtudiants.getEtudiant("Bob")).thenReturn(null);
        serveur.onMessage(mockWebSocket, "NOTE Bob");
        verify(gestionEtudiants).getEtudiant("Bob");
        verify(mockWebSocket).send("Étudiant non trouvé");
    }

    @Test
    void testListeEtudiants() {
        when(gestionEtudiants.getListeEtudiants()).thenReturn("Alice: 15.5\nBob: 12.0");
        serveur.onMessage(mockWebSocket, "LIST");
        verify(gestionEtudiants).getListeEtudiants();
        verify(mockWebSocket).send("Liste des étudiants:\nAlice: 15.5\nBob: 12.0");
    }

    @Test
    void testCalculerMoyenne() {
        when(gestionEtudiants.calculerMoyenne()).thenReturn(14.25);
        serveur.onMessage(mockWebSocket, "MOYENNE");
        verify(gestionEtudiants).calculerMoyenne();
        verify(mockWebSocket).send("Moyenne de la classe: 14.25");
    }

    @Test
    void testQuitter() {
        serveur.onMessage(mockWebSocket, "QUITTER");
        verify(mockWebSocket).send("Déconnexion");
        verify(mockWebSocket).close();
    }

    @Test
    void testCommandeInconnue() {
        serveur.onMessage(mockWebSocket, "INVALIDE");
        verify(mockWebSocket).send("Commande inconnue. Commandes: AJOUTER, NOTE, LIST, MOYENNE, QUITTER");
    }
}