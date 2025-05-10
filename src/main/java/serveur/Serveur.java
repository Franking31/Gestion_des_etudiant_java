package serveur;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

<<<<<<< HEAD
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
=======
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class Serveur extends WebSocketServer {
    private static final int PORT = 12345;
    private GestionEtudiants gestionEtudiants = new GestionEtudiants();
    private Set<WebSocket> clients = new HashSet<>();

    public Serveur() {
        super(new InetSocketAddress(PORT));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
        System.out.println("Nouveau client connecté: " + conn.getRemoteSocketAddress());
        conn.send("Connecté au serveur. Commandes: AJOUTER nom note, NOTE nom, LIST, MOYENNE, QUITTER");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        System.out.println("Client déconnecté: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        String[] commande = message.trim().split(" ");
        
        switch (commande[0].toUpperCase()) {
            case "AJOUTER":
                if (commande.length == 3) {
                    try {
                        String nom = commande[1];
                        double note = Double.parseDouble(commande[2]);
                        gestionEtudiants.ajouterEtudiant(nom, note);
                        conn.send("Étudiant ajouté: " + nom);
                    } catch (NumberFormatException e) {
                        conn.send("Erreur: Note invalide");
                    }
                } else {
                    conn.send("Erreur: Format AJOUTER nom note");
                }
                break;

            case "NOTE":
                if (commande.length == 2) {
                    Etudiant etudiant = gestionEtudiants.getEtudiant(commande[1]);
                    if (etudiant != null) {
                        conn.send("Note de " + commande[1] + ": " + etudiant.getNote());
                    } else {
                        conn.send("Étudiant non trouvé");
                    }
                } else {
                    conn.send("Erreur: Format NOTE nom");
                }
                break;

            case "LIST":
                String liste = gestionEtudiants.getListeEtudiants();
                conn.send("Liste des étudiants:\n" + liste);
                break;

            case "MOYENNE":
                double moyenne = gestionEtudiants.calculerMoyenne();
                conn.send("Moyenne de la classe: " + moyenne);
                break;

            case "QUITTER":
                conn.send("Déconnexion");
                conn.close();
                break;

            default:
                conn.send("Commande inconnue. Commandes: AJOUTER, NOTE, LIST, MOYENNE, QUITTER");
>>>>>>> origin/pange
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Erreur WebSocket: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("Serveur WebSocket démarré sur le port " + PORT);
    }

    public static void main(String[] args) {
        Serveur serveur = new Serveur();
        serveur.start();
    }
}