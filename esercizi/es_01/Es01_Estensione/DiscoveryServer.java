package Es01_Estensione;

// DiscoveryServer.java

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class DiscoveryServer {

	private static int PORT_CLIENT_REQ = -1;
	private static int PORT_SERVER_REG = -1;

	private static final String USAGE = "Usage: java DiscoveryServer [Porta richieste client] [Porta registrazione server]";

	public static void main(String[] args) {

		/* Controllo argomenti */
		// Uso: java DiscoveryServer portaClient portaServer
		if (args.length != 2) {
			System.out.println(USAGE);
			System.exit(1);
		}

		PORT_CLIENT_REQ = Integer.parseInt(args[0]);
		PORT_SERVER_REG = Integer.parseInt(args[1]);
		
		if (PORT_CLIENT_REQ <= 1024 || PORT_CLIENT_REQ > 65535) {
			System.out.println("DiscoveryServer: La porta per le richieste client non è valida: " + args[0]);
			System.exit(2);
		}

		if (PORT_SERVER_REG <= 1024 || PORT_SERVER_REG > 65535) {
			System.out.println("DiscoveryServer: La porta per la registrazione dei server non è valida: " + args[1]);
			System.exit(3);
		}

		// Creazione mapping indirizzi
		MappingIndirizzi mappingIndirizzi = new MappingIndirizzi();

		// Creazione DiscoveryClientRequests e DiscoveryRegisterSwap
		DiscoveryClientRequests clientRequests = new DiscoveryClientRequests(PORT_CLIENT_REQ, mappingIndirizzi);
		DiscoveryRegisterSwap registerSwap = new DiscoveryRegisterSwap(PORT_SERVER_REG, mappingIndirizzi);

		// Avvio dei thread
		clientRequests.start();
		registerSwap.start();
		
	}
}