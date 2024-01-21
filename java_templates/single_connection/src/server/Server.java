package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    /*
     * Stampa a video la sintassi corretta per l'esecuzione del programma
     * 
     * @param void
     * 
     * @return void
     */
    private static void printUsage() {
        System.out.println("Usage: java Server <port>");
        System.out.println("1024 <= port <= 65535");
    }

    /*
     * Stampa a video l'errore passato come parametro
     * 
     * @param Exception e
     * 
     * @return void
     */
    private static void printError(String info, Exception e) {
        System.out.println("[Server Error] " + info);
        e.printStackTrace();
    }

    /*
     * Stampa a video l'errore passato come parametro
     * 
     * @param Exception e
     * 
     * @return void
     */
    private static void printError(Exception e) {
        System.out.println("[Server Error] ");
        e.printStackTrace();
    }

    /*
     * Stampa a video un'info lato server
     * 
     * @param String info
     * 
     * @return void
     */
    private static void printInfo(String info) {
        System.out.println("[Server] " + info);
    }

    public static void main(String[] args) {
        int port = -1;

        // Controllo argomenti
        try {
            if (args.length == 1) {
                port = Integer.parseInt(args[0]);
                // controllo che la porta sia nel range consentito 1024-65535
                if (port < 1024 || port > 65535) {
                    printUsage();
                    System.exit(1);
                }
            } else {
                printUsage();
                System.exit(1);
            }
        } catch (Exception e) {
            printError(e);
            printUsage();
            System.exit(1);
        }

        // Variabili socket
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            printInfo("creata la server socket " + serverSocket);
        } catch (Exception e) {
            printError("problemi nella creazione della server socket", e);

            try {
                serverSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            System.exit(2);
        }

        // Ciclo di ascolto
        try {
            printInfo("avviato");

            while (true) {
                // Creazione socket ed accettazione connessione
                try {
                    clientSocket = serverSocket.accept();
                    //clientSocket.setSoTimeout(30000);
                    printInfo("connessione accettata: " + clientSocket);
                } catch (Exception e) {
                    printError("problemi nell'accettazione della connessione", e);
                    continue;
                }

                // Creazione thread
                try {
                    new MyThread(clientSocket).start();
                } catch (Exception e) {
                    printError("problemi nel server thread", e);
                    continue;
                }
            }
        } catch (Exception e) {
            printError("errore durante il ciclo d'ascolto", e);
        }
    }

}
