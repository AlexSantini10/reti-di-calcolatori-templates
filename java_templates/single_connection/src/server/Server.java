package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final String USAGE = "Usage: java Server <port> \n1024 <= port <= 65535";

    public static void main(String[] args) {
        int port = -1;

        // Controllo argomenti
        try {
            if (args.length == 1) {
                port = Integer.parseInt(args[0]);
                // controllo che la porta sia nel range consentito 1024-65535
                if (port < 1024 || port > 65535) {
                    System.out.println(USAGE);
                    System.exit(1);
                }
            } else {
                System.out.println(USAGE);
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("Problemi, i seguenti: ");
            e.printStackTrace();
            System.exit(1);
        }

        // Variabili socket
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            System.out.println("Socket creato: " + serverSocket + " - in ascolto...");
        } catch (Exception e) {
            System.out.println("Problemi nella creazione del server socket: ");
            e.printStackTrace();

            try {
                serverSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            System.exit(2);
        }

        // Ciclo di ascolto
        try {
            System.out.println();

            while (true) {
                // Creazione socket ed accettazione connessione
                try {
                    clientSocket = serverSocket.accept();
                    //clientSocket.setSoTimeout(30000);
                    System.out.println("Connessione accettata: " + clientSocket);
                } catch (Exception e) {
                    System.out.println("Problemi nella creazione del server socket: ");
                    e.printStackTrace();
                    continue;
                }

                // Creazione thread
                try {
                    new MyThread(clientSocket).start();
                } catch (Exception e) {
                    System.out.println("Problemi nella creazione del thread: ");
                    e.printStackTrace();
                    continue;
                }
            }
        } catch (Exception e) {
            System.out.println("Errore durante la comunicazione con il client: ");
            e.printStackTrace();
        }
    }

}
