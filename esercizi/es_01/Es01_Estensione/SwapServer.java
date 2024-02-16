package Es01_Estensione;

// SwapServer.java

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import java.net.InetAddress;

public class SwapServer {

	public static void main(String[] args) throws UnknownHostException{

		// Controllo argomenti
		if (args.length != 4) {
			System.out.println("Usage: java SwapServer IPDS portDS portRS nomeFile");
			System.exit(1);
		}

		// Inizializzazione variabili
		String discoveryServerIPString = args[0];
		final InetAddress discoveryServerIP = InetAddress.getByName(discoveryServerIPString);
		int discoveryServerPort = Integer.parseInt(args[1]);
		int swapServerPort = Integer.parseInt(args[2]);
		String fileName = args[3];

		// Socket
		DatagramSocket socket = null;
		DatagramPacket packet = null;
		byte[] buf = new byte[256];


		// Invio richiesta di registrazione al discovery server -------------------------------------------------------------------------------------
		try {
			// Creazione socket
			socket = new DatagramSocket(swapServerPort);
			packet = new DatagramPacket(buf, buf.length, discoveryServerIP, discoveryServerPort);
			System.out.println("SwapServer: invio richiesta di registrazione al discovery server...");
			// Creazione richiesta
			ByteArrayOutputStream boStream = new ByteArrayOutputStream();
			DataOutputStream doStream = new DataOutputStream(boStream);

			// Invio richiesta di registrazione al discovery server
			// ! Sarà il server a prendere il mio IP attraverso i dati inviati
			doStream.writeUTF("register" + ": " + fileName + "-" + swapServerPort);
			buf = boStream.toByteArray();
			packet.setData(buf);
			socket.send(packet);
			System.out.println("SwapServer: richiesta di registrazione inviata al discovery server");

			// Ricezione risposta
			packet.setData(buf);
			socket.receive(packet);
			ByteArrayInputStream biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
			DataInputStream diStream = new DataInputStream(biStream);
			String risposta = diStream.readUTF();

			// Controllo risposta
			if (risposta.equals("ok")) {
				System.out.println("SwapServer: registrazione avvenuta con successo");
			} else {
				System.out.println("SwapServer: registrazione fallita");
				System.exit(1);
			}

			socket.close();
		}
		catch (Exception e) {
			System.out.println("SwapServer: Problemi, i seguenti: ");
			e.printStackTrace();
			System.exit(1);
		}

		// Alla chiusura ----------------------------------------------------------------------------------------------------------------------------
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run(){
				// Alla chiusura dello SwapServer, invio richiesta di deregistrazione al discovery server
				try {
					// Socket di deregistrazione
					DatagramSocket socketDer = null;
					DatagramPacket packetDer = null;
					byte[] bufDer = new byte[256];

					// Creazione socket
					socketDer = new DatagramSocket();
					packetDer = new DatagramPacket(bufDer, bufDer.length, discoveryServerIP, discoveryServerPort);
					System.out.println("SwapServer: invio richiesta di deregistrazione al discovery server...");
					// Creazione richiesta
					ByteArrayOutputStream boStream = new ByteArrayOutputStream();
					DataOutputStream doStream = new DataOutputStream(boStream);

					// Invio richiesta di deregistrazione al discovery server
					// ! Sarà il server a prendere il mio IP attraverso i dati inviati
					doStream.writeUTF("unregister" + ": " + fileName + "-" + swapServerPort);
					bufDer = boStream.toByteArray();
					packetDer.setData(bufDer);
					socketDer.send(packetDer);
					System.out.println("SwapServer: richiesta di deregistrazione inviata al discovery server");

					socketDer.close();
				}
				catch (Exception e) {
					System.out.println("SwapServer: Problemi, i seguenti: ");
					e.printStackTrace();
					System.exit(1);
				}
			}
		});

		// Parte di ricezione richieste da parte dei client -----------------------------------------------------------------------------------------
		try {
			//apertura Socket di ricezione
			socket = new DatagramSocket(swapServerPort);
			packet = new DatagramPacket(buf, buf.length);
			System.out.println("SwapServer: per file " + fileName + " avviato con socket port: " + socket.getLocalPort()); 
		} catch (SocketException e) {
			System.out.println("SwapServer: Problemi nella creazione della socket: ");
			e.printStackTrace();
			System.exit(1);
		}


		// Ricezione richieste da parte dei client
		try {
			ByteArrayInputStream biStream = null;
			DataInputStream diStream = null;
			ByteArrayOutputStream boStream = null;
			DataOutputStream doStream = null;
			byte req = (byte) 0;
			int result = 0;
			byte[] data = null;
			String mask = null;
			int firstRow=0, secondRow=0;

			while (true) {
				System.out.println("\nSwapServer in attesa di richieste...");

				// ricezione del datagramma
				try {
					packet.setData(buf);
					socket.receive(packet);

				} catch (IOException e) {
					System.err.println("SwapServer: Problemi nella ricezione del datagramma: "+ e.getMessage());
					e.printStackTrace();
					continue;
					// il server continua a fornire il servizio ricominciando dall'inizio
					// del ciclo
				}

				// Estrazione prima e seconda riga da scambiare
				try {
					biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
					diStream = new DataInputStream(biStream);
					String firstAndSecondRows = diStream.readUTF();
					StringTokenizer st = new StringTokenizer(firstAndSecondRows, "-");
					firstRow = Integer.parseInt((String)st.nextElement());
					secondRow = Integer.parseInt((String)st.nextElement());			
					System.out.println("SwapServer: FirstRow: " + firstRow + " SecondRow: " + secondRow);
				}
				catch (IOException e) {
					System.out.println("SwapServer: Problemi nella lettura della risposta: ");
					e.printStackTrace();
				}

				// swape invio dell'esito
				try {
					// Swap parole
					BufferedReader br = null;
					BufferedWriter bw = null;
					String line;
					int count = 0;
					String firstLineCache = null;
					String secondLineCache = null;

					System.out.println("SwapServer: Swapping rows...");
					// associazione di uno stream di input al file da cui estrarre la parola
					br = new BufferedReader(new FileReader(fileName));
					bw = new BufferedWriter(new FileWriter("temp"));
					
					// Read the file once to get the lines
					while ((line = br.readLine())!=null) {
						count++;
						if (count == firstRow) {
							firstLineCache = line;
						} else if(count == secondRow){
							secondLineCache = line;
						}

						// Stop the first read as soon as we have the lines.
						if(firstLineCache != null && secondLineCache != null) {
							break;
						}
					}
					br.close();

					// Controllo se le righe sono state trovate
					if (count < firstRow || count < secondRow) {
						System.out.println("SwapServer: Righe non trovate");
						result = -1;
						// Preparo l'esito
						boStream = new ByteArrayOutputStream();
						doStream = new DataOutputStream(boStream);
						doStream.writeInt(result);
						data = boStream.toByteArray();
						packet.setData(data, 0, data.length);
						socket.send(packet);
						continue;
					}
				
					// Read the file a second time to swap the lines
					br = new BufferedReader(new FileReader(fileName));
					count = 0;
					while ((line = br.readLine())!=null){
						count++;
						if (count == firstRow) {
							line = secondLineCache;
						} else if(count == secondRow){
							line = firstLineCache;
						}
						bw.write(line + "\n");
					}
					bw.close(); 
					br.close();
				
					// Rename the file
					// 1- Delete the old file
					File fileorig = new File(fileName);
					fileorig.delete();
					// 2 - Create a new file with the same name
					File file = new File(fileName);
					File tempFile = new File("temp");		
					// 3 - Call rename and delete the temp file
					tempFile.renameTo(file);
					tempFile.delete();		

					result = 0;	
					// Preparo l'esito
					boStream = new ByteArrayOutputStream();
					doStream = new DataOutputStream(boStream);
					doStream.writeInt(result);
					data = boStream.toByteArray();
					packet.setData(data, 0, data.length);
					socket.send(packet);
				} catch (IOException e) {
					System.err.println("SwapServer: Problemi, i seguenti: "+ e.getMessage());
					e.printStackTrace();
					continue;
					// il server continua a fornire il servizio ricominciando dall'inizio
					// del ciclo
				}

			} // while

		}
		// qui catturo le eccezioni non catturate all'interno del while
		// in seguito alle quali il server termina l'esecuzione
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("SwapServer: termino...");
		socket.close();
	}

}