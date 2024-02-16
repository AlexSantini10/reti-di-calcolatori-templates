package server;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
	
	private static final int N_ARGS = 1;

	public static void main(String[] args) throws IOException {
		// Args
		int port = -1;

		// Controllo numero argomenti
		if (args.length != N_ARGS) {
			System.out.println("Utilizzo: SimpleServer <port>");
			System.exit(1);
		}


		// Port
		try {
			port = Integer.parseInt(args[0]);
			if (port < 1024 || port > 65535) {
				System.out.println("Utilizzo: SimpleServer <port>");
				System.exit(1);
			}
		} 
		catch (NumberFormatException e) {
			System.out.println("<port> deve essere un intero");
			System.exit(1);
		}
		catch (Exception e) {
			System.out.println("Utilizzo: SimpleServer <port>");
			System.exit(1);
		}


		// Variabili socket
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
            System.out.println("SimpleServer: avviato ");
            System.out.println("Server: creata la server socket: " + serverSocket);
        } catch (Exception e) {
            System.err.println("Server: problemi nella creazione della server socket: " + e.getMessage());
            e.printStackTrace();
            serverSocket.close();
            System.exit(1);
        }


		// Cicli di ascolto
		try {
			while (true) {
				System.out.println("Server: in attesa di richieste...\n");

				try {
					clientSocket = serverSocket.accept(); // bloccante!!!
					System.out.println("Server: connessione accettata: " + clientSocket);
				} catch (Exception e) {
					System.err.println("Server: problemi nella accettazione della connessione: " + e.getMessage());
					e.printStackTrace();
					continue;
				}


				// Creazione stream di input e output
				DataInputStream inSock = null;
				DataOutputStream outSock = null;

				try {
					inSock = new DataInputStream(clientSocket.getInputStream());
					outSock = new DataOutputStream(clientSocket.getOutputStream());
				} catch (IOException e) {
					System.err.println("Server: problemi nella creazione degli stream: " + e.getMessage());
					e.printStackTrace();
					continue;
				}


				// Ricezione richiesta
				String request = null;
				try {
					request = inSock.readUTF();
					System.out.println("Server: ricevuta richiesta: " + request);
				}
				catch (Exception e) {
					System.err.println("Server: problemi nella ricezione della richiesta: " + e.getMessage());
					e.printStackTrace();
					continue;
				}


				// Invio risposta
				try {
					outSock.writeUTF("Risposta alla richiesta: " + request);
					System.out.println("Server: inviata risposta: " + request);
				}
				catch (Exception e) {
					System.err.println("Server: problemi nell'invio della risposta: " + e.getMessage());
					e.printStackTrace();
					continue;
				}
			}
		}
		catch (Exception e) {
			System.err.println("Server: problemi nel ciclo di ascolto: " + e.getMessage());
			e.printStackTrace();
			serverSocket.close();
			System.exit(1);
		}
		finally {
			serverSocket.close();
		}
		
	}

}
