package Es01_Estensione;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class DiscoveryClientRequests extends Thread {

    private int PORT = -1;
    private MappingIndirizzi mappingIndirizzi;
    
    public DiscoveryClientRequests(int PORT, MappingIndirizzi mappingIndirizzi){
        this.PORT = PORT;
        this.mappingIndirizzi = mappingIndirizzi;
    }

    @Override
    public void run() {
        //Inizializzazione e apertura Socket per la ricezione delle richieste da parte dei client
		DatagramSocket socket = null;
		DatagramPacket packet = null;
		byte[] buf = new byte[256];

		try {
			socket = new DatagramSocket(PORT);
			packet = new DatagramPacket(buf, buf.length);
			System.out.println("DiscoveryClientRequests: Server di nomi avviato con socket port: " + socket.getLocalPort()); 
		} catch (SocketException e) {
			System.out.println("DiscoveryClientRequests: Problemi nella creazione della socket: ");
			e.printStackTrace();
			System.exit(1);
		}

        // - Alla connessione di un client, invio tutti gli indirizzi e le porte degli swap server (il client invia una richiesta con scritto "LIST")
        // - Ricezione richiesta da parte di un client
        // - Controllo se il nome del file è presente nel mapping indirizzi
        // - Se è presente, invio l'indirizzo e la porta al client
        // - Se non è presente, invio -1 al client
        

        // Creazione variabili di ricezione ed invio dati
        String richiesta = null;
        ByteArrayInputStream biStream = null;
        DataInputStream diStream = null;
        ByteArrayOutputStream boStream = null;
        DataOutputStream doStream = null;
        String risposta = null;
        byte[] data = null;

        // Ricezione richiesta da parte di un client

        while (true){
            // Ricezione richiesta
            try {
                packet.setData(buf);
                socket.receive(packet);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            // Lettura richiesta da parte del client
            try {
                biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                diStream = new DataInputStream(biStream);
                richiesta = diStream.readUTF();
                System.out.println("DiscoveryClientRequests: Richiesto server per nome file: " + richiesta);
            } catch (Exception e) {
                System.err.println("DiscoveryClientRequests: Problemi nella lettura della richiesta: ");
                e.printStackTrace();
                continue;
                // il server continua a fornire il servizio ricominciando dall'inizio
                // del ciclo
            }

            // Se la richiesta è "LIST", invio tutti gli indirizzi e le porte al client
            if (richiesta.equals("LIST")){
                String indirizzi = mappingIndirizzi.toString();

                risposta = indirizzi;

                // Invio indirizzi al client
                try {
                    boStream = new ByteArrayOutputStream();
					doStream = new DataOutputStream(boStream);
					doStream.writeUTF(risposta);
					data = boStream.toByteArray();
					packet.setData(data, 0, data.length);
					socket.send(packet);
                }
                catch (IOException ioe) {
                    System.out.println("DiscoveryClientRequests: Problemi nell'invio della risposta: ");
                    ioe.printStackTrace();
                    continue;
                } 
                catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            // Altrimenti, controllo se il nome del file è presente nel mapping indirizzi
            else{
                // Cerco richiesta nel mapping indirizzi
                String indirizzo = mappingIndirizzi.getIndirizzo(richiesta);
                int porta = mappingIndirizzi.getPorta(richiesta);

                risposta = (indirizzo + ":" + porta);

                if (indirizzo != null && porta != -1){
                    // Invio indirizzo e porta al client
                    try {
                        boStream = new ByteArrayOutputStream();
                        doStream = new DataOutputStream(boStream);
                        doStream.writeUTF(risposta);
                        data = boStream.toByteArray();
                        packet.setData(data, 0, data.length);
                        socket.send(packet);
                    }
                    catch (IOException ioe) {
                        System.out.println("DiscoveryClientRequests: Problemi nell'invio della risposta: ");
                        ioe.printStackTrace();
                        continue;
                    } 
                    catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                
                }
                else{
                    System.out.println("DiscoveryClientRequests: Indirizzo non presente");

                    // Invio NOT FOUND al client
                    try {
                        boStream = new ByteArrayOutputStream();
                        doStream = new DataOutputStream(boStream);
                        doStream.writeUTF("NOT FOUND");
                        data = boStream.toByteArray();
                        packet.setData(data, 0, data.length);
                        socket.send(packet);
                    }
                    catch (IOException ioe) {
                        System.out.println("DiscoveryClientRequests: Problemi nell'invio della risposta: ");
                        ioe.printStackTrace();
                        continue;
                    } 
                    catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }
        
    }

}
