package source.scratch.java.webserver.src;

import java.io.*;
import java.net.*;
import java.util.*;

public class WebServer {

	ServerSocket serverSocket;
	Response response;

	public WebServer(int port) throws IOException {
		//crea un server socket
		//che ascolta sulla porta port
		serverSocket = new ServerSocket(port);
	}

	public void setResponse(Response r) {
		this.response = r;
	}

	public void start() {
		while (true) {
			try {
				//accetta una connessione
				Socket socket = serverSocket.accept();

				//crea un thread che invia la pagina al browser
				//e che chiude la connessione
				//quando ha finito

				System.out.println("Connection accepted from " + socket.getInetAddress() + ", port: " + socket.getPort());

				ConnectionHandler handler = new ConnectionHandler(socket, response);
				handler.start();
				

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}





}
