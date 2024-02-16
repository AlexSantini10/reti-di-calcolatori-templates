package Es01_Estensione;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class DiscoveryRegisterSwap extends Thread {

    private int PORT = -1;
    MappingIndirizzi mappingIndirizzi;
    
    public DiscoveryRegisterSwap(int PORT, MappingIndirizzi mappingIndirizzi){
        this.PORT = PORT;
        this.mappingIndirizzi = mappingIndirizzi;
    }


    @Override
    public void run() {
        //Inizializzazione e apertura Socket per la ricezione delle richieste da parte dei client
		DatagramSocket socket = null;
		DatagramPacket packet = null;
		byte[] buf = new byte[256];

        // Creazione socket
		try {
			socket = new DatagramSocket(PORT);
			packet = new DatagramPacket(buf, buf.length);
			System.out.println("DiscoveryRegisterSwap: Server di nomi avviato con socket port: " + socket.getLocalPort()); 
		} catch (SocketException e) {
			System.out.println("DiscoveryRegisterSwap: Problemi nella creazione della socket: ");
			e.printStackTrace();
			System.exit(1);
		}
        

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

            // Estrazione dati ricevuti
            try {
                biStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                diStream = new DataInputStream(biStream);
                richiesta = diStream.readUTF();
                System.out.println("DiscoveryRegisterSwap: Inviati dati da: " + packet.getAddress() + ", porta: " + packet.getPort() + ", dati: " + richiesta);

                // Estrazione nome file e porta swap server
                String[] dati = richiesta.split(":");

                // Controllo se è una richiesta di register o di unregister
                if (dati[0].equals("register")){
                    // Register
                    // Estrazione nome file e porta swap server
                    String[] dati2 = dati[1].split("-");
                    String swapServerIP = packet.getAddress().toString().substring(1);
                    int swapServerPort = Integer.parseInt(dati2[1]);
                    String swapServerFileName = dati2[0];

                    if (mappingIndirizzi.isNamePresent(swapServerFileName.trim()) || mappingIndirizzi.areIndirizzoAndPortPresent(swapServerIP.trim(), swapServerPort)){
                        System.out.println("DiscoveryRegisterSwap: Nome file o indirizzo:port già presente nel mapping indirizzi");
                        continue;
                    }

                    // Aggiunta al mapping indirizzi
                    mappingIndirizzi.addIndirizzo(swapServerIP.trim(), swapServerPort, swapServerFileName.trim());

                    System.out.println("DiscoveryRegisterSwap: Aggiunto al mapping indirizzi: " + swapServerIP + ":" + swapServerPort + " -" + swapServerFileName);

                    // Invio ok allo swap server
                    try {
                        // Creazione risposta
                        boStream = new ByteArrayOutputStream();
                        doStream = new DataOutputStream(boStream);
                        doStream.writeUTF("ok");
                        data = boStream.toByteArray();
                        packet.setData(data);
                        socket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }

                }
                else if (dati[0].equals("unregister")){
                    // Unregister
                    // Estrazione nome file e porta swap server
                    String[] dati2 = dati[1].split("-");
                    String swapServerIP = packet.getAddress().toString().substring(1);
                    int swapServerPort = Integer.parseInt(dati2[1]);
                    String swapServerFileName = dati2[0];

                    // Rimozione dal mapping indirizzi
                    mappingIndirizzi.removeIndirizzo(dati2[0]);

                    System.out.println("DiscoveryRegisterSwap: Rimosso dal mapping indirizzi: " + swapServerIP + ":" + swapServerPort + " -" + swapServerFileName);

                    // Invio ok allo swap server
                    try {
                        // Creazione risposta
                        boStream = new ByteArrayOutputStream();
                        doStream = new DataOutputStream(boStream);
                        doStream.writeUTF("ok");
                        data = boStream.toByteArray();
                        packet.setData(data);
                        socket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }

                }
                else {
                    System.out.println("DiscoveryRegisterSwap: Richiesta non valida");

                    // Invio risposta al client
                    try {
                        // Creazione risposta
                        boStream = new ByteArrayOutputStream();
                        doStream = new DataOutputStream(boStream);
                        doStream.writeUTF("Richiesta non valida");
                        data = boStream.toByteArray();
                        packet.setData(data);
                        socket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }
                    continue;
                }


            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        
    }

}
