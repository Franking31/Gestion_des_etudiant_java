import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import serveur.ClientHandler;
import serveur.GestionEtudiants;

import java.io.*;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientHandlerTest {
    @Mock
    private Socket clientSocket;
    @Mock
    private GestionEtudiants gestionEtudiants;
    
    private PipedOutputStream pipedOutputStream;
    private PipedInputStream pipedInputStream;
    private ByteArrayOutputStream outputStreamCaptor;
    
    private ClientHandler clientHandler;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        
        // Configurer le système de capture de sortie
        outputStreamCaptor = new ByteArrayOutputStream();
        when(clientSocket.getOutputStream()).thenReturn(outputStreamCaptor);
        
        // Ne pas configurer l'entrée ici - elle sera configurée par test
    }

    @Test
    void testAjouterEtudiantValide() throws IOException {
        // Préparer l'entrée simulée
        ByteArrayInputStream inputStream = new ByteArrayInputStream("AJOUTER Alice 15.5\nQUITTER\n".getBytes());
        when(clientSocket.getInputStream()).thenReturn(inputStream);
        
        // Créer et exécuter le handler
        clientHandler = new ClientHandler(clientSocket, gestionEtudiants);
        clientHandler.run();

        // Vérifier
        verify(gestionEtudiants).ajouterEtudiant("Alice", 15.5);
        assertTrue(outputStreamCaptor.toString().contains("Étudiant ajouté: Alice"));
    }

    @Test
    void testAjouterEtudiantNoteInvalide() throws IOException {
        // Préparer l'entrée simulée
        ByteArrayInputStream inputStream = new ByteArrayInputStream("AJOUTER Bob abc\nQUITTER\n".getBytes());
        when(clientSocket.getInputStream()).thenReturn(inputStream);
        
        // Créer et exécuter le handler
        clientHandler = new ClientHandler(clientSocket, gestionEtudiants);
        clientHandler.run();

        // Vérifier
        verifyNoInteractions(gestionEtudiants);
        assertTrue(outputStreamCaptor.toString().contains("Erreur: Note invalide"));
    }

    @Test
    void testGetNoteEtudiantExistant() throws IOException {
        // Préparer l'entrée simulée
        ByteArrayInputStream inputStream = new ByteArrayInputStream("NOTE Alice\nQUITTER\n".getBytes());
        when(clientSocket.getInputStream()).thenReturn(inputStream);
        when(gestionEtudiants.getNote("Alice")).thenReturn(15.5);
        
        // Créer et exécuter le handler
        clientHandler = new ClientHandler(clientSocket, gestionEtudiants);
        clientHandler.run();

        // Vérifier
        verify(gestionEtudiants).getNote("Alice");
        assertTrue(outputStreamCaptor.toString().contains("Note de Alice: 15.5"));
    }

    @Test
    void testGetNoteEtudiantInexistant() throws IOException {
        // Préparer l'entrée simulée
        ByteArrayInputStream inputStream = new ByteArrayInputStream("NOTE Bob\nQUITTER\n".getBytes());
        when(clientSocket.getInputStream()).thenReturn(inputStream);
        when(gestionEtudiants.getNote("Bob")).thenReturn(null);
        
        // Créer et exécuter le handler
        clientHandler = new ClientHandler(clientSocket, gestionEtudiants);
        clientHandler.run();

        // Vérifier
        verify(gestionEtudiants).getNote("Bob");
        assertTrue(outputStreamCaptor.toString().contains("Étudiant non trouvé"));
    }

    @Test
    void testCalculerMoyenne() throws IOException {
        // Préparer l'entrée simulée
        ByteArrayInputStream inputStream = new ByteArrayInputStream("MOYENNE\nQUITTER\n".getBytes());
        when(clientSocket.getInputStream()).thenReturn(inputStream);
        when(gestionEtudiants.calculerMoyenne()).thenReturn(14.25);
        
        // Créer et exécuter le handler
        clientHandler = new ClientHandler(clientSocket, gestionEtudiants);
        clientHandler.run();

        // Vérifier
        verify(gestionEtudiants).calculerMoyenne();
        assertTrue(outputStreamCaptor.toString().contains("Moyenne de la classe: 14.25"));
    }

    @Test
    void testQuitter() throws IOException {
        // Préparer l'entrée simulée
        ByteArrayInputStream inputStream = new ByteArrayInputStream("QUITTER\n".getBytes());
        when(clientSocket.getInputStream()).thenReturn(inputStream);
        
        // Créer et exécuter le handler
        clientHandler = new ClientHandler(clientSocket, gestionEtudiants);
        clientHandler.run();

        // Vérifier
        assertTrue(outputStreamCaptor.toString().contains("Déconnexion"));
        verify(clientSocket).close();
    }

    @Test
    void testCommandeInconnue() throws IOException {
        // Préparer l'entrée simulée
        ByteArrayInputStream inputStream = new ByteArrayInputStream("INVALIDE\nQUITTER\n".getBytes());
        when(clientSocket.getInputStream()).thenReturn(inputStream);
        
        // Créer et exécuter le handler
        clientHandler = new ClientHandler(clientSocket, gestionEtudiants);
        clientHandler.run();

        // Vérifier
        assertTrue(outputStreamCaptor.toString().contains("Commande inconnue"));
    }
}