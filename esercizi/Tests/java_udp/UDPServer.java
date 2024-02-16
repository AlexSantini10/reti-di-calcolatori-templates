import java.io.*;
import java.net.*;

public class UDPServer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java UDPServer <port>");
            System.exit(1);
        }

        try {
            int port = Integer.parseInt(args[0]);
            
            if (port < 1024 || port > 65535) {
                System.out.println("Usage: java UDPServer <port>, port must be between 1024 and 65535");
                System.exit(1);
            }
        }
        catch (NumberFormatException e) {
            System.out.println("Usage: java UDPServer <port>, port must be an integer");
            System.exit(1);
        }

        DatagramSocket socket = null;

        try {
            // Crea una socket UDP alla porta specificata
            socket = new DatagramSocket(Integer.parseInt(args[0]));
            System.out.println("UDPServer: in attesa di richieste sulla porta " + args[0]);

            while (true) {
                // Buffer per ricevere i dati
                byte[] receiveData = new byte[1024];

                // Crea il pacchetto per ricevere i dati
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // Attendi finché non ricevi un pacchetto
                socket.receive(receivePacket);

                // Ottieni i dati ricevuti e stampali
                String request = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Messaggio ricevuto: " + request);

                // Esegui la logica del server e invia una risposta al client
                String response = "Hello from Server!";
                byte[] sendData = response.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
                socket.send(sendPacket);
                System.out.println("Risposta inviata al client: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Chiudi la socket se è stata aperta
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}