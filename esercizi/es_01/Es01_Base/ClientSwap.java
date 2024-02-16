package Es01_Base;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class ClientSwap {

	
	public static void main(String[] args) {


		InetAddress addr = null;
		int port = -1;
		String fileName = null; 
		
		try {
			if (args.length == 3) {
		    addr = InetAddress.getByName(args[0]);
		    port = Integer.parseInt(args[1]);
		    fileName = args[2]; 
			} else {
				System.out.println("Usage: java clientSwap serverHost serverPort file.txt");
			    System.exit(1);
			}
		} catch (UnknownHostException e) {
			System.out
		      .println("Problemi nella determinazione dell'endpoint del server : ");
			e.printStackTrace();
			System.out.println("LineClient: interrompo...");
			System.exit(2);
		}
	
		DatagramSocket socket = null;
		DatagramPacket packet = null;
		byte[] buf = new byte[256];

		// creazione della socket datagram, settaggio timeout di 30s
		// e creazione datagram packet
		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(30000);
			packet = new DatagramPacket(buf, buf.length, addr, port);
			System.out.println("\nClientSwap: avviato");
			System.out.println("Creata la socket: " + socket);
		} catch (SocketException e) {
			System.out.println("Problemi nella creazione della socket: ");
			e.printStackTrace();
			System.out.println("LineClient: interrompo...");
			System.exit(1);
		}

		 ByteArrayOutputStream boStream = null;
		         	DataOutputStream doStream = null;
			        byte[] data = null;		
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	         String risposta = null;		
				// riempimento e invio del pacchetto
				try {
                   	        
			       	      
					String richiest =null; 
					richiest = fileName;
					boStream = new ByteArrayOutputStream();
					doStream = new DataOutputStream(boStream);
					doStream.writeUTF(richiest);
					data = boStream.toByteArray();
					packet.setData(data);
					socket.send(packet);
					System.out.println("Richiesta inviata a " + addr + ", " + port);
				} catch (IOException e) {
					System.out.println("Problemi nell'invio della richiesta: ");
					e.printStackTrace();
					System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, altrimenti inserisci nome file (con estensione): ");
			
					// il client continua l'esecuzione riprendendo dall'inizio del ciclo
				}

				try {
					// settaggio del buffer di ricezione
					packet.setData(buf);
					socket.receive(packet);
					// sospensiva solo per i millisecondi indicati, dopodich� solleva una
					// SocketException
				} catch (IOException e) {
					System.out.println("Problemi nella ricezione del datagramma: ");
					e.printStackTrace();
					System.out
						.print("\n^D(Unix)/^Z(Win)+invio per uscire, altrimenti inserisci nome file (con estensione): ");
					
					// il client continua l'esecuzione riprendendo dall'inizio del ciclo
				}
				ByteArrayInputStream biStream = null;
				DataInputStream diStream = null;
				try {
					StringTokenizer st = null;
				    int portaSwap = -1; 
					
					biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
					diStream = new DataInputStream(biStream);
					risposta = diStream.readUTF();
					st = new StringTokenizer(risposta);
					portaSwap = Integer.parseInt(st.nextToken()); 
					System.out.println("La porta dello swapServer è: " + portaSwap);
					packet.setPort(portaSwap); 
				} catch (IOException e) {
					System.out.println("Problemi nella lettura dellaDataOutputStrea risposta: ");
					e.printStackTrace();
					System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, altrimenti inserisci nome file (con estensione): ");
					
					// il client continua l'esecuzione riprendendo dall'inizio del ciclo
				}
				/*Il Cliente fa ogni chiamata successiva allo stesso RS, indicando i
                  due interi che rappresentano le righe da scambiare
                  Il cliente riceve in risposta un intero con l’esito dell’operazione
                  Al termine di un ciclo di richieste, il cliente termina come un filtro*/
					String linea = null; 
					try {
			System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire,inserisci le linee :  ");
						
					while ((linea = stdIn.readLine()) != null) {
						// interazione con l'utente
						
						StringTokenizer str = new StringTokenizer(linea);
						String linea1 = str.nextToken(); 
						String linea2 = str.nextToken(); 
						String richiest =null; 
						richiest = linea1 +" "+ linea2;
						boStream = new ByteArrayOutputStream();
						doStream = new DataOutputStream(boStream);
						doStream.writeUTF(richiest);
						data = boStream.toByteArray();
						packet.setData(data);
						socket.send(packet);
						
						
						packet.setData(buf);
						socket.receive(packet);
						biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
						diStream = new DataInputStream(biStream);
						risposta = diStream.readUTF();
						System.out.println(risposta);
						
					}	} catch (IOException e) {
						System.out.println("Problemi nell'interazione da console: ");
						e.printStackTrace();
						System.out
							.print("\n^D(Unix)/^Z(Win)+invio per uscire, altrimenti inserisci nome file (con estensione): ");
	                  
					}
				
			
				// tutto ok, pronto per nuova richiesta
				System.out.println("ClientSwap: termino...");
		        socket.close();
			} 

		
	} 

