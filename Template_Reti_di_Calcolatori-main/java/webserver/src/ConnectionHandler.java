package source.scratch.java.webserver.src;

import java.net.*;
import java.io.*;

public class ConnectionHandler extends Thread {
	Socket socket;
	Response response;

	public ConnectionHandler(Socket socket, Response response) {
		this.socket = socket;
		this.response = response;
	}

	public void run() {
		try {
			//invia la pagina al browser
			//e chiude la connessione
			//quando ha finito
			//...
			
			//crea un oggetto di tipo PrintWriter
			//che scrive sullo stream di output del socket
			//la pagina html e il codice di risposta

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			//scrive l'header http della risposta
			//con il codice di risposta
			
			out.println("HTTP/1.1 " + response.replyCode + " OK");
			//la riga vuota indica la fine dell'header
			out.println("");


			//read the file and write it to the socket
			BufferedReader in = new BufferedReader(new FileReader(response.path));
			String line;
			while ((line = in.readLine()) != null) {
				out.println(line);
			}

			in.close();
			out.close();

			//chiude lo stream di output del socket
			//e quindi la connessione



		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
