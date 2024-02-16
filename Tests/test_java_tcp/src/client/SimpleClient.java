package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class SimpleClient {

	private static final int N_ARGS = 2;

	public static void main(String[] args) {
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

		// Controllo numero argomenti
		if (args.length != N_ARGS) {
			System.out.println("Utilizzo: SimpleClient <host> <port>");
			System.exit(1);
		}

		// Variabili
		String host = args[0];
		int port = -1;

		// Port
		try {
			port = Integer.parseInt(args[1]);
			if (port < 1024 || port > 65535) {
				System.out.println("Utilizzo: SimpleClient <host> <port>");
				System.exit(1);
			}
		} 
		catch (NumberFormatException e) {
			System.out.println("<port> deve essere un intero");
			System.exit(1);
		}
		catch (Exception e) {
			System.out.println("Utilizzo: SimpleClient <host> <port>");
			System.exit(1);
		}

		// Variabili socket
        Socket socket = null;
        DataInputStream inSock = null;
        DataOutputStream outSock = null;

		try {
			socket = new Socket(host, port);
			socket.setSoTimeout(30000);
			System.out.println("SimpleClient: avviato ");
			System.out.println("SimpleClient: creata la socket: " + socket);
		} 
		catch (Exception e) {
			System.err.println("SimpleClient: problemi nella creazione della socket: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		try {
			inSock = new DataInputStream(socket.getInputStream());
			outSock = new DataOutputStream(socket.getOutputStream());
		} 
		catch (Exception e) {
			System.err.println("SimpleClient: problemi nell'istanziamento degli stream: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		// Lettura dati da stdIn e invio al server
		String line = null;
		try {
			System.out.println("Inserisci una stringa: ");
			line = stdIn.readLine();
			outSock.writeUTF(line);
			System.out.println("SimpleClient: stringa inviata: " + line);
		} 
		catch (Exception e) {
			System.err.println("SimpleClient: problemi nell'invio della stringa: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("SimpleClient: SONO QUI");

		// Lettura risposta dal server
		try {
			line = inSock.readUTF();
			System.out.println("SimpleClient: risposta dal server: " + line);
		} 
		catch (Exception e) {
			System.err.println("SimpleClient: problemi nella lettura della risposta: " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

}
