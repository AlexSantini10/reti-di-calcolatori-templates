package Es01_Base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Hashtable;

public class DiscoveryServer {
	
	private final static String usage = "Utilizzo: DiscoveryServer [portaServer] [fileRowServer1] [portaRowServer1] ... [fileRowServerN] [portaRowServerN]";
	
	public static void main(String[] args) {
		int[] porte = new int[1007];
		String[] files = new String[1007];
		int numOfThreads = (args.length-1)/2;
		
		if (args.length<1) {
			System.out.println(usage);
			System.exit(1);
		}
		
		int port = -1;
		
		try {
			port = Integer.valueOf(args[0]);
			
			
			for (int i=2, k=0; i<args.length; i+=2) {
				porte[k] = Integer.valueOf(args[i]);
				
				if (porte[k]<=1024) {
					System.out.println("La porta deve essere >1024 " + usage);
					System.exit(5);
				}
				
				k++;
			}
			
			for (int i=1, k=0; i<args.length; i+=2) {
				files[k] = args[i];
				k++;
			}
		}
		catch (NumberFormatException e) {
			System.out.println("La porta deve essere un intero " + usage);
			System.exit(2);
		}
		
		if (args.length%2==0) {
			System.out.println("Errore, almeno un filename o porta mancante " + usage);
			System.exit(3);
		}
		
		// Check univocità porte/file
		for (int i=0; i<numOfThreads; i++) {
			for (int j=i+1; j<numOfThreads; j++) {
				if (porte[i] == porte[j]) {
					System.out.println("Le porte devono essere univoche");
					System.exit(6);
				}
				
				if (files[i].equals(files[j])) {
					System.out.println("I filename devono essere univoci");
					System.exit(7);
				}
			}
		}
		
		RowSwapServer[] rowServers = new RowSwapServer[numOfThreads];
		
		for (int i=0; i<numOfThreads; i++) {
			rowServers[i] = new RowSwapServer(porte[i], files[i]);
			
			rowServers[i].start();
		}
		
		DatagramSocket socket = null;
		DatagramPacket packet = null;
		byte[] buf = new byte[256];
		
		try {
			socket = new DatagramSocket(port);
			packet = new DatagramPacket(buf, buf.length);
			System.out.println("Creata la socket: " + socket);
		}
		catch (SocketException e) {
			System.out.println("Problemi nella creazione della socket: ");
			e.printStackTrace();
			System.exit(8);
		}
		
		try {
			while (true) {
				System.out.println("\nIn attesa di richieste...");
				
				// ricezione del datagramma
				try {
					packet.setData(buf);
					socket.receive(packet);
					
					ByteArrayOutputStream boStream = new ByteArrayOutputStream();
					DataOutputStream doStream = new DataOutputStream(boStream);
					ByteArrayInputStream biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
					DataInputStream diStream = new DataInputStream(biStream);
					
					String request = diStream.readUTF();
					
					boolean found = false;
					int foundPort = -1;
					
					for (int i=0; i<numOfThreads; i++) {
						if (request.equals(files[i])) {			
							doStream.writeUTF(String.valueOf(porte[i]));
							buf = boStream.toByteArray();
							packet.setData(buf);
							socket.send(packet);
							
							foundPort = porte[i];
							
							found = true;
							break;
						}
					}
					
					if (!found) {
						System.out.println("Nessun file trovato con nome " + request);
					}
					else {
						System.out.println("File " + request + " trovato alla porta " + foundPort);
					}
				}
				catch (IOException e) {
					System.err.println("Problemi nella ricezione del datagramma: "
							+ e.getMessage());
					e.printStackTrace();
					continue;
					// il server continua a fornire il servizio ricominciando dall'inizio
					// del ciclo
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
