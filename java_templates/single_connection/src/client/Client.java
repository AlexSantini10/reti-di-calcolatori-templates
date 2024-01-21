package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static void printUsage() {
        System.out.println("Usage: java Client <address> <port>");
        System.out.println("1024 <= port <= 65535");
    }

    private static void printError(String info, Exception e) {
        System.out.println("[Client Error] " + info);
        e.printStackTrace();
    }

    private static void printError(Exception e) {
        System.out.println("[Client Error] ");
        e.printStackTrace();
    }

    private static void printInfo(String info) {
        System.out.println("[Client] " + info);
    }
    
    public static void main(String[] args) {
        InetAddress addr = null;
        int port = -1;

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        // Controllo argomenti
        try {
            if (args.length == 2) {
                addr = InetAddress.getByName(args[0]);
                port = Integer.parseInt(args[1]);
                if (port < 1024 || port > 65535) {
                    printUsage();
                    System.exit(1);
                }
            } else {
                printUsage();
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("Problemi, i seguenti: ");
            e.printStackTrace();
            System.out.println("Usage: java MPutFileClient serverAddr serverPort minFileSize [transferBufferSize]");
            System.exit(2);
        }

        // Socket
        Socket socket;

        // Data Stream
        DataInputStream dataInput;
        DataOutputStream dataOutput;

        // Variabili per la lettura e scrittura dei dati binari
        /*
        * byte[] buffer = new byte[1024];
        * int read_bytes = 0;
        */

        // Connessione
        try {
            socket = new Socket(addr, port);
            dataInput = new DataInputStream(socket.getInputStream());
            dataOutput = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            printError("Errore nella connessione al server", e);
            return;
        }

        // Ciclo di lettura/scrittura
        try {
            while (true) {
                // Leggo il messaggio
                System.out.println("Inserisci un messaggio: ");
                String message = stdIn.readLine();
                printInfo("Messaggio inviato: " + message);

                // Invio il messaggio
                dataOutput.writeUTF(message);

                // Leggo la risposta
                String response = dataInput.readUTF();
                printInfo("Risposta ricevuta: " + response);
            }
        } catch (Exception e) {
            printError(e);
        }
        finally {
            try {
                dataInput.close();
                dataOutput.close();
                socket.close();
            } catch (Exception e) {
                printError(e);
            }
        }
    }
}
