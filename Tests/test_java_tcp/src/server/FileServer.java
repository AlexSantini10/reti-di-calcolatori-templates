package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class FileServer {
    
    public static void main(String[] args) throws IOException {
        int port = -1;

        // Controllo argomenti
        try {
            if (args.length == 1) {
                port = Integer.parseInt(args[0]);
                // controllo che la porta sia nel range consentito 1024-65535
                if (port < 1024 || port > 65535) {
                    System.out.println("Usage: java PutFileServerCon [serverPort>1024]");
                    System.exit(1);
                }
            } else {
                System.out.println("Usage: java PutFileServerCon port");
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("Problemi, i seguenti: ");
            e.printStackTrace();
            System.out.println("Usage: java PutFileServerCon port");
            System.exit(1);
        }

        // Variabili socket
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            System.out.println("PutFileServerCon: avviato ");
            System.out.println("Server: creata la server socket: " + serverSocket);
        } catch (Exception e) {
            System.err.println("Server: problemi nella creazione della server socket: " + e.getMessage());
            e.printStackTrace();
            serverSocket.close();
            System.exit(1);
        }


        // Ciclo di ascolto
        try {
            while (true) {
                System.out.println("Server: in attesa di richieste...\n");

                try {
                    clientSocket = serverSocket.accept(); // bloccante!!!
                    System.out.println("Server: connessione accettata: " + clientSocket);
                } catch (Exception e) {
                    System.err.println("Server: problemi nella accettazione della connessione: " + e.getMessage());
                    e.printStackTrace();
                    continue;
                }

                // Ricezione tipo di richiesta
                String requestType = null;
                DataInputStream inSock = null;
                try {
                    inSock = new DataInputStream(clientSocket.getInputStream());
                    requestType = inSock.readUTF();
                } 
                // reset connessione
                catch (SocketException se){
                    System.out.println("Server: connessione chiusa dal client: " + se.getMessage());
                    continue;
                }
                catch (IOException ioe) {
                    System.out.println("Problemi nella creazione degli stream di input/output su socket: ");
                    ioe.printStackTrace();
                    continue;
                }
                catch (Exception e) {
                    System.out.println("Chiudo e esco...");
                    requestType = "close";
                }

                // Gestione richiesta
                // get o put
                if (requestType.equals("get")) {
                    System.out.println("Server: richiesta get");
                    try {
                        GetServerThread getServerThread = new GetServerThread(clientSocket);
                        getServerThread.start();
                    } catch (Exception e) {
                        System.err.println("Server: problemi nel server thread: " + e.getMessage());
                        e.printStackTrace();
                        continue;
                    }

                } 
                else if (requestType.equals("put")) {
                    System.out.println("Server: richiesta put");
                    try {
                        PutServerThread putFileServerThread = new PutServerThread(clientSocket);
                        putFileServerThread.start();
                    } catch (Exception e) {
                        System.err.println("Server: problemi nel server thread: " + e.getMessage());
                        e.printStackTrace();
                        continue;
                    }
                } 
                else if (requestType.equals("close")) {
                    System.out.println("Server: richiesta di chiusura");
                    clientSocket.close();
                    System.out.println("Server: chiusura connessione effettuata");
                    System.exit(0);
                }
                else {
                    System.out.println("Server: richiesta non valida");
                    continue;
                }
            }
        }
        catch (Error e){
            System.err.println("Server: problemi nel ciclo di ascolto: " + e.getMessage());
            e.printStackTrace();
            serverSocket.close();
            System.exit(1);
        }
    }

}
