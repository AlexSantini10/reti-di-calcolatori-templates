package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;

public class ServerImpl implements RemoteServer, Serializable {

	public static String USAGE = "Utilizzo: ServerImpl <port>";
	
	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println(USAGE);
			return;
		}
		
		int port = -1;
		try {
			port = Integer.valueOf(args[0]);
		}
		catch (NumberFormatException e) {
			System.out.println(USAGE);
			System.out.println("<port> deve essere un numero intero");
		}
		
		try {
			//java.rmi.registry.LocateRegistry.createRegistry(port);
			ServerImpl server = new ServerImpl();
			java.rmi.Naming.rebind("rmi://localhost:" + port + "/RemoteServer", server);
			System.out.println("Server ready");
		} catch (Exception e) {
			System.out.println("Server exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public String[] lista_nomi_file_contenenti_parola_in_linea(String nome_dir, String parola) throws RemoteException {
		File dir = new File(nome_dir);
		
		File[] files = dir.listFiles();
		String[] nomi_file = new String[files.length];
		int cont = 0;
		
		for (int i=0; i<files.length; i++) {
			FileReader fr;
			String fileContent = "";
			
			try {
			    fileContent = new String(Files.readAllBytes(files[i].toPath()));
			    
			    if (fileContent.contains(parola)) {
			    	nomi_file[cont] = files[i].getName();
			    	cont++;
			    }
			} catch (IOException e) {
			    e.printStackTrace();
			}
			
		}
		
		return nomi_file;
	}

	@Override
	public int conta_numero_linee(String nome_file, String parola) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

}
