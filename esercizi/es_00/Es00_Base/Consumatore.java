package Es00_Base;

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
		
		String prefix = args[0];
		
		if (args.length == 2) {
			System.out.println("Utilizzo a due argomenti, input da file");
			
			try {
				r = new FileReader(args[1]);
			} catch(FileNotFoundException e){
				System.out.println("File non trovato");
				System.exit(1);
			}
		}
		else if (args.length == 1) {
			System.out.println("Utilizzo a singolo argomento, input da stdin");
			
			r = new FileReader(FileDescriptor.in);
			
		}
		else if (args.length != 2){
			System.out.println("Utilizzo: consumatore <filterPrefix> <inputFilename>");
			System.exit(0);
		}
	  
		
		try {

			while ((x = r.read()) >= 0) { 
				ch = (char) x;
				
				if (prefix.indexOf(ch) == -1)
					System.out.print(ch);
			}
			r.close();
		} catch(IOException ex){
			System.out.println("Errore di input");
			System.exit(2);
		}
}}
