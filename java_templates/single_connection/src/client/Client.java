package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public static final String USAGE = "Usage: java Client <address> <port> \n1024 <= port <= 65535";
    
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
            System.out.println("Problemi nella creazione degli stream di input/output: ");
            e.printStackTrace();
            return;
        }

        // Ciclo di lettura/scrittura
        try {
            while (true) {
                // Leggo il messaggio
                System.out.println("Inserisci un messaggio: ");
                String message = stdIn.readLine();
                System.out.println("Messaggio inserito: " + message);

                // Invio il messaggio
                dataOutput.writeUTF(message);

                // Leggo la risposta
                String response = dataInput.readUTF();
                System.out.println("Risposta: " + response);
            }
        } catch (Exception e) {
            System.out.println("Errore durante la comunicazione con il server: ");
            e.printStackTrace();
        }
        finally {
            try {
                dataInput.close();
                dataOutput.close();
                socket.close();
            } catch (Exception e) {
                System.out.println("Errore durante la chiusura della connessione: ");
                e.printStackTrace();
            }
        }
    }
}
