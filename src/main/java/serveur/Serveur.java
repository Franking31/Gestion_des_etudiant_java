package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {
    private static final int PORT = 8080;
    private final GestionEtudiants gestionEtudiants;

    public Serveur() {
        this.gestionEtudiants = new GestionEtudiants();
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur démarré sur le port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouveau client connecté: " + clientSocket);
                ClientHandler clientHandler = new ClientHandler(clientSocket, gestionEtudiants);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur: " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) {
        try {
            new Serveur().start();
        } catch (IOException e) {
            System.err.println("Échec du démarrage du serveur: " + e.getMessage());
        }
    }
}