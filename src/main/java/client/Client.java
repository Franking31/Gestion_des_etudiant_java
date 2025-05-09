package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connecté au serveur. Commandes disponibles:");
            System.out.println("AJOUTER nom note - Ajouter un étudiant");
            System.out.println("NOTE nom - Voir la note d'un étudiant");
            System.out.println("MOYENNE - Voir la moyenne de la classe");
            System.out.println("QUITTER - Quitter");

            String reponse;
            while (true) {
                System.out.print("> ");
                String commande = scanner.nextLine();
                
                out.println(commande);
                
                if (commande.equalsIgnoreCase("QUITTER")) {
                    System.out.println(in.readLine());
                    break;
                }
                
                while ((reponse = in.readLine()) != null && !reponse.isEmpty()) {
                    System.out.println(reponse);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur client: " + e.getMessage());
        }
    }
}