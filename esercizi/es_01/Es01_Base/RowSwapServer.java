package Es01_Base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.StringTokenizer;

public class RowSwapServer extends Thread {
	
	private int port;
	private String fileName;
	private String identifier;
	
	public RowSwapServer(int port, String fileName) {
		this.port = port;
		this.fileName = fileName;
		this.identifier = port + ":" + fileName;
	}

	@Override
	public void run() {
		System.out.println("Creato RowServer [porta]:[file] " + port + ":" + fileName);
		
		DatagramSocket socket = null;
		DatagramPacket packet = null;
		byte[] buf = new byte[256];

		int result = 0;
		
		try {
			socket = new DatagramSocket(this.port);
			packet = new DatagramPacket(buf, buf.length);
			System.out.println("Creata la socket: " + socket);
		}
		catch (SocketException e) {
			System.out.println("Problemi nella creazione della socket nel thread: " + this.identifier);
			e.printStackTrace();
			System.exit(8);
		}
		
		try {
			while (true) {
				System.out.println("\nIn attesa di richieste... " + this.identifier);
				
				try {
					packet.setData(buf);
					socket.receive(packet);
					
					ByteArrayOutputStream boStream = new ByteArrayOutputStream();
					DataOutputStream doStream = new DataOutputStream(boStream);
					ByteArrayInputStream biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
					DataInputStream diStream = new DataInputStream(biStream);
					
					String request = diStream.readUTF();
					
					int firstRow = -1;
					int secondRow = -1;

					// Estrazione prima e seconda riga da scambiare
					try {
						biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
						diStream = new DataInputStream(biStream);
						String firstAndSecondRows = diStream.readUTF();
						StringTokenizer st = new StringTokenizer(firstAndSecondRows, "-");
						firstRow = Integer.parseInt((String)st.nextElement());
						secondRow = Integer.parseInt((String)st.nextElement());			
						System.out.println("FirstRow: " + firstRow + " SecondRow: " + secondRow);
					}
					catch (IOException e) {
						System.out.println("Problemi nella lettura della risposta: ");
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

						System.out.println("Swapping rows...");
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
						//TODO: controllare se le linee esistono effettivamente
					
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
						// TODO: controllare l'esito di queste operazioni
						result = 0;	
						// Preparo l'esito
						boStream = new ByteArrayOutputStream();
						doStream = new DataOutputStream(boStream);
						doStream.writeInt(result);
						buf = boStream.toByteArray();
						packet.setData(buf, 0, buf.length);
						socket.send(packet);
					} catch (IOException e) {
						System.err.println("Problemi, i seguenti: "+ e.getMessage());
						e.printStackTrace();
						continue;
						// il server continua a fornire il servizio ricominciando dall'inizio
						// del ciclo
					}
				}
				catch (IOException e) {
					System.err.println("Problemi nella ricezione del datagramma: " + this.identifier
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
