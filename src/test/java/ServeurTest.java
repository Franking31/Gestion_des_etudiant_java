import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import serveur.Serveur;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServeurTest {

    @Mock
    private WebSocket mockWebSocket;
    @Mock
    private ClientHandshake mockHandshake;

    @Test
    void testServeurAcceptsClient() {
        // Créer une instance du serveur
        Serveur serveur = new Serveur();

        // Simuler une connexion WebSocket
        serveur.onOpen(mockWebSocket, mockHandshake);

        // Vérifier que le message de bienvenue est envoyé
        verify(mockWebSocket, times(1)).send("Connecté au serveur. Commandes: AJOUTER nom note, NOTE nom, LIST, MOYENNE, QUITTER");
    }

    @Test
    void testServeurHandlesMessage() {
        // Créer une instance du serveur
        Serveur serveur = new Serveur();

        // Simuler l'envoi d'un message
        serveur.onMessage(mockWebSocket, "MOYENNE");

        // Vérifier que le serveur répond avec la moyenne
        verify(mockWebSocket, times(1)).send(contains("Moyenne de la classe:"));
    }
}