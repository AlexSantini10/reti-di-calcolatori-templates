package Es01_Estensione;

import java.io.*;
import java.net.*;

public class SwapClient {

	public static void main(String[] args){
		// Per il momento, invia solo una richiesta LIST ad un server scelto

		// Controllo argomenti
		if (args.length != 2) {
			System.out.println("Usage: java SwapClient <indirizzo discovery> <porta discovery>");
			System.exit(1);
		}

		// Inizializzazione variabili
		String addressString = args[0];
		InetAddress address = null;
		int port = Integer.parseInt(args[1]);
		DatagramSocket socket = null;
		DatagramPacket packet = null;
		byte[] buf = new byte[256];

		try {
			address = InetAddress.getByName(addressString);
		}
		catch (Exception e) {
			System.out.println("Problemi, i seguenti: ");
			e.printStackTrace();
			System.exit(1);
		}

		ByteArrayOutputStream boStream = null;
		DataOutputStream doStream = null;
		String risposta = null;
		ByteArrayInputStream biStream = null;
		DataInputStream diStream = null;
		byte[] data = null;

		String fileName = null;

		// Creazione socket
		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(30000);
			packet = new DatagramPacket(buf, buf.length, address, port);
			System.out.println("\nSwapClient: avviato");
			System.out.println("Creata la socket: " + socket);
		}
		catch (SocketException e) {
			System.out.println("Problemi nella creazione della socket: ");
			e.printStackTrace();
			System.out.println("SwapClient: interrompo...");
			System.exit(1);
		}

		// Invio richiesta della lista
		try {
			boStream = new ByteArrayOutputStream();
			doStream = new DataOutputStream(boStream);
			doStream.writeUTF("LIST");
			data = boStream.toByteArray();
			packet.setData(data);
			socket.send(packet);
			System.out.println("Richiesta inviata a " + addressString + ", " + port);
		}
		catch (IOException e) {
			System.out.println("Problemi nell'invio della richiesta: ");
			e.printStackTrace();
		}

		try {// settaggio del buffer di ricezione
			packet.setData(buf);
			socket.receive(packet);
			// sospensiva solo per i millisecondi indicati, dopo solleva una SocketException
		}
		catch (IOException e) {
			System.out.println("Problemi nella ricezione del datagramma: ");
			e.printStackTrace();
		}

		try {//Estrazione risposta
			biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
			diStream = new DataInputStream(biStream);
			risposta = diStream.readUTF();
		}
		catch (IOException e) {
			System.out.println("Problemi nella lettura della risposta: ");
			e.printStackTrace();
		}

		String indirizzoEPortaSwap = "NOT FOUND";

		// Finchè non trovo un indirizzo e una porta corrispondenti al file richiesto
		while (indirizzoEPortaSwap.equals("NOT FOUND")){
			System.out.println("\n\nLista dei file disponibili: ");
			System.out.println(risposta);

			System.out.println("Inserisci il nome del file desiderato: ");
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

			// Invio richiesta
			try {
				fileName = stdIn.readLine().trim();

				boStream = new ByteArrayOutputStream();
				doStream = new DataOutputStream(boStream);
				doStream.writeUTF(fileName);
				data = boStream.toByteArray();
				packet.setData(data);
				socket.send(packet);
				System.out.println("Richiesta inviata a " + addressString + ", " + port);
			}
			catch (NullPointerException e){
				System.out.println("SwapClient: interrotto");
				System.exit(2);
			}
			catch (IOException e) {
				System.out.println("Problemi nell'invio della richiesta: ");
				e.printStackTrace();
			}

			try {// settaggio del buffer di ricezione
				packet.setData(buf);
				socket.receive(packet);
				// sospensiva solo per i millisecondi indicati, dopo solleva una SocketException
			}
			catch (IOException e) {
				System.out.println("Problemi nella ricezione del datagramma: ");
				e.printStackTrace();
			}

			try {//Estrazione risposta
				biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
				diStream = new DataInputStream(biStream);
				indirizzoEPortaSwap = diStream.readUTF();
			}
			catch (IOException e) {
				System.out.println("Problemi nella lettura della risposta: ");
				e.printStackTrace();
			}
		} // Fine while


		// Connessione allo swap server
		String[] indirizzoEPorta = indirizzoEPortaSwap.split(":");
		String indirizzoSwapString = indirizzoEPorta[0];
		InetAddress indirizzoSwap = null;
		int portaSwap = Integer.parseInt(indirizzoEPorta[1]);

		try {
			indirizzoSwap = InetAddress.getByName(indirizzoSwapString);
		}
		catch (Exception e) {
			System.out.println("Problemi, i seguenti: ");
			e.printStackTrace();
			System.exit(1);
		}

		// Creazione socket
		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(30000);
			packet = new DatagramPacket(buf, buf.length, indirizzoSwap, portaSwap);
			System.out.println("\nSwapClient: avviato");
			System.out.println("Creata la socket: " + socket);
		}
		catch (SocketException e) {
			System.out.println("Problemi nella creazione della socket: ");
			e.printStackTrace();
			System.out.println("SwapClient: interrompo...");
			System.exit(1);
		}

		// riutilizzo stesse variabili
		packet = null;
		buf = new byte[256];
		packet = new DatagramPacket(buf, buf.length, indirizzoSwap, portaSwap);
		boStream = new ByteArrayOutputStream();
		doStream = new DataOutputStream(boStream);
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Inserisci prima riga da scambiare contenuta nel file " + fileName + " oppure Ctrl+D(Unix)/Ctrl+Z(Win)+invio per uscire");
		try {
			String firstRow = null;
			String secondRow = null;
			int req;
			while ((firstRow = stdIn.readLine()) != null) {
				System.out.println("Inserisci seconda riga da scambiare contenuta nel file " + fileName + " oppure Ctrl+D(Unix)/Ctrl+Z(Win)+invio per uscire");
				secondRow = stdIn.readLine();
				String firstAndSecondRows = firstRow + "-" + secondRow;

				boStream.reset();
				doStream.writeUTF(firstAndSecondRows);

				//Send request
				try {					
					doStream.writeUTF(firstAndSecondRows);
					data = boStream.toByteArray();
					packet.setData(data);
					socket.send(packet);
					System.out.println("Richiesta inviata a " + indirizzoSwapString + ", " + portaSwap);
				} catch (IOException e) {
					System.out.println("Problemi nell'invio della richiesta: ");
					e.printStackTrace();
					System.out.println("Inserisci seconda riga da scambiare contenuta nel file " + fileName + " oppure Ctrl+D(Unix)/Ctrl+Z(Win)+invio per uscire");
					continue;
				}

				//set buffer and receive answer
				try {
					packet.setData(buf);
					socket.receive(packet);
					// sospensiva solo per i millisecondi indicati, dopo solleva una SocketException
				} catch (IOException e) {
					System.out.println("Problemi nella ricezione del datagramma: ");
					e.printStackTrace();
					System.out.println("Inserisci seconda riga da scambiare contenuta nel file " + fileName + " oppure Ctrl+D(Unix)/Ctrl+Z(Win)+invio per uscire");
					continue;
					// il client continua l'esecuzione riprendendo dall'inizio del ciclo
				}
				try {
					biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
					diStream = new DataInputStream(biStream);
					int esitoScambioRighe = diStream.readInt();
					System.out.println("Esito scambio righe: " + esitoScambioRighe);
					
				} catch (IOException e) {
					System.out.println("Problemi nella lettura della risposta: ");
					e.printStackTrace();
					System.out.println("Inserisci seconda riga da scambiare contenuta nel file " + fileName + " oppure Ctrl+D(Unix)/Ctrl+Z(Win)+invio per uscire");
					continue;
					// il client continua l'esecuzione riprendendo dall'inizio del ciclo
				}
				// tutto ok, pronto per nuova richiesta
				System.out.println("Inserisci prima riga da scambiare contenuta nel file " + fileName + " oppure Ctrl+D(Unix)/Ctrl+Z(Win)+invio per uscire");
			} // while
		}
		// qui catturo le eccezioni non catturate all'interno del while
		// in seguito alle quali il client termina l'esecuzione
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("\nSwapClient: termino...");
		socket.close();
	}
}