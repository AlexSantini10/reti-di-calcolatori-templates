package source.scratch.java.webserver.src;

import java.io.*;

public class Main{
	//crea un webserver che ascolta sulla porta 8080
	//e che risponde con index.html
	//quando riceve una richiesta
	//da un browser
	
	public static void main(String[] args) throws IOException{
		//crea un webserver
		WebServer server = new WebServer(8080);
		//crea un file index.html
		File index = new File("index.html");
		//crea un file di risposta
		//che contiene index.html
		//e che risponde con il codice 200
		//quando riceve una richiesta
		//da un browser
		Response response = new Response(index, 200);
		//aggiunge la risposta al webserver
		server.setResponse(response);
		//avvia il webserver
		server.start();
	}
}
