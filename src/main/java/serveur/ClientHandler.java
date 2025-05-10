package serveur;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private GestionEtudiants gestionEtudiants;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket clientSocket, GestionEtudiants gestionEtudiants) throws IOException {
        this.clientSocket = clientSocket;
        this.gestionEtudiants = gestionEtudiants;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            String ligne;
            while ((ligne = in.readLine()) != null) {
                String[] commande = ligne.split(" ");
                
                switch (commande[0].toUpperCase()) {
                    case "AJOUTER":
                        if (commande.length == 3) {
                            String nom = commande[1];
                            try {
                                double note = Double.parseDouble(commande[2]);
                                gestionEtudiants.ajouterEtudiant(nom, note);
                                out.println("Étudiant ajouté: " + nom);
                            } catch (NumberFormatException e) {
                                out.println("Erreur: Note invalide");
                            }
                        } else {
                            out.println("Erreur: Format AJOUTER nom note");
                        }
                        break;

                    case "NOTE":
                        if (commande.length == 2) {
                            Etudiant etudiant = gestionEtudiants.getEtudiant(commande[1]);
                            if (etudiant != null) {
                                out.println("Note de " + commande[1] + ": " + etudiant.getNote());
                            } else {
                                out.println("Étudiant non trouvé");
                            }
                        } else {
                            out.println("Erreur: Format NOTE nom");
                        }
                        break;

                    case "MOYENNE":
                        double moyenne = gestionEtudiants.calculerMoyenne();
                        out.println("Moyenne de la classe: " + moyenne);
                        break;

                    case "QUITTER":
                        out.println("Déconnexion");
                        return;

                    default:
                        out.println("Commande inconnue. Commandes disponibles: AJOUTER, NOTE, MOYENNE, QUITTER");
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture: " + e.getMessage());
            }
        }
    }
}