package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class SimpleClient {
	
	// Costanti argomenti
	static final int N_ARGS = 2;
	static final String COMMAND = "SimpleClient <serverIP> <serverPort>";

	public static void main(String[] args) {
		
		// Check degli argomenti
		if (args.length != N_ARGS) {
			System.out.println("Errore negli argomenti, utilizzo: " + COMMAND);
			System.exit(1);
		}
		
		String serverIP = args[0];
		
		// Argomento <port>
		int serverPort = 6000;
		try {
			serverPort = Integer.valueOf(args[1]);
		}
		catch (NumberFormatException e) {
			System.out.println("Errore, <serverPort> deve essere un intero");
			System.exit(2);
		}

		// Creazione socket
		try {
			DatagramSocket socket = new DatagramSocket();
			
			InetAddress serverAddress = InetAddress.getByName(serverIP);
			
			// Dati da inviare
			String messageToSend = "Ciao, server!";
			byte[] sendData = messageToSend.getBytes();
			
			// Creazione pacchetto di invio dati
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
			
			// Invio dati
			socket.send(sendPacket);
			System.out.println("Dati inviati al server");
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
