import java.io.*;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java UDPClient <serverIp> <port>");
            System.exit(1);
        }

        DatagramSocket socket = null;

        try {
            // Crea una socket UDP
            socket = new DatagramSocket();

            // Indirizzo IP del server
            InetAddress serverAddress = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);

            // Messaggio da inviare
            String message = "Hello from Client!";
            byte[] sendData = message.getBytes();

            // Crea un pacchetto da inviare al server
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, port);

            // Invia il pacchetto
            socket.send(sendPacket);
            System.out.println("Messaggio inviato al server: " + message);

            // Ricevi la risposta dal server
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            // Ottieni i dati ricevuti e stampali
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Risposta ricevuta dal server: " + response);

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