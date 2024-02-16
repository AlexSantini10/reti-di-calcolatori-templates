package tests;

import java.net.InetAddress;

public class TestMyIP {

	public static void main(String[] args) {
		// Scrivo il mio IP

		// Inizializzazione variabili
		String myIP = null;
		String myIPString = null;
		InetAddress myIPInetAddress = null;

		// Ottengo il mio IP
		try {
			myIPInetAddress = InetAddress.getLocalHost();
			myIP = myIPInetAddress.getHostAddress();
			myIPString = myIPInetAddress.toString();
		}
		catch (Exception e) {
			System.out.println("Problemi, i seguenti: ");
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Il mio IP è: " + myIP);

	}

}
