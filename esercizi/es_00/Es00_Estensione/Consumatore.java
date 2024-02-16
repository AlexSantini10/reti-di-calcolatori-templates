package Es00_Estensione;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// Consumatore e' un filtro

// Stdin ha descrittore 0
public class Consumatore {
	public static void main(String[] args) {
		FileReader r = null;
		char ch;
		int x;
		
		if (args.length < 2) {
			System.out.println("Utilizzo: consumatore <filterPrefix> <inputFilename1> ... <inputFilenameN>");
			System.exit(1);
		}	

		String prefix = args[0];

		ConsumerThread[] threads = new ConsumerThread[args.length-1];
		for (int i=1; i<args.length; i++){
			// Apertura del file
			try {
				FileReader fileReader = new FileReader(args[i]);

				threads[i-1] = new ConsumerThread(prefix, args[i], fileReader);

				threads[i-1].start();
			} 
			catch (FileNotFoundException e) {
				System.out.println("File " + args[i] + " non trovato");
			}
		}
	
	}
}
