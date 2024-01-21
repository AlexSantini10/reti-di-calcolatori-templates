package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class MyThread extends Thread {

    private Socket clientSocket;

    // Utility methods
    private static void printError(String info, Exception e) {
        System.out.println("[Server Thread Error] " + info);
        e.printStackTrace();
    }

    private static void printError(Exception e) {
        System.out.println("[Server Thread Error] ");
        e.printStackTrace();
    }

    private static void printInfo(String info) {
        System.out.println("[Server Thread] " + info);
    }

    // Constructor
    public MyThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            DataInputStream dataInput;
            DataOutputStream dataOutput;

            try {
                dataInput = new DataInputStream(clientSocket.getInputStream());
                dataOutput = new DataOutputStream(clientSocket.getOutputStream());
            } catch (Exception e) {
                printError("Errore nella creazione dei data stream", e);
                return;
            }

            // Variabili per la lettura e scrittura dei dati binari
            /*
             * byte[] buffer = new byte[1024];
             * int read_bytes = 0;
             */

            while (true) {
                // Leggo il messaggio
                String message = dataInput.readUTF();
                printInfo("Messaggio ricevuto: " + message);

                // Invio il messaggio
                dataOutput.writeUTF("Messaggio ricevuto: " + message);
            }
        } 
        catch (SocketException e){
            printInfo("Connessione chiusa dal client");
        }
        catch (Exception e) {
            printError(e);
        }
    }

}
