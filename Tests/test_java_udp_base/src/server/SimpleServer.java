package server;

import java.io.IOException;
import java.net.*;

public class SimpleServer {

	// Costanti argomenti
	static final int N_ARGS = 1;
	static final String COMMAND = "SimpleServer <port>";
	
	// Costanti socket
	static final int BUF_SIZE = 1024;
	
	public static void main(String[] args) {
		
		// Check degli argomenti
		if (args.length != N_ARGS) {
			System.out.println("Errore negli argomenti, utilizzo: " + COMMAND);
			System.exit(1);
		}
		
		// Argomento <port>
		int socketPort = 6000;
		try {
			socketPort = Integer.valueOf(args[0]);
		}
		catch (NumberFormatException e) {
			System.out.println("Errore, <port> deve essere un intero");
			System.exit(2);
		}
		
		// Creazione socket
		try {
			DatagramSocket socket = new DatagramSocket(socketPort);
			
			// Buffer dati in entrata
			byte[] receiveData = new byte[BUF_SIZE];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			
			// Ricezione dati
			System.out.println("In attesa di dati...");
			socket.receive(receivePacket);
			
			// Estrazione dati
			String receivedMessage = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
			System.out.println("Dati ricevuti: " + receivedMessage);
			
		} 
		catch (SocketException se) {
			se.printStackTrace();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
