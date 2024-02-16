package Es00_Base;

import java.io.*;

public class Produttore
{
	public static void main(String[] args) { 
		BufferedReader in = null;
		
		// fare controllo argomenti
		if (args.length != 1){
			System.out.println("Utilizzo: produttore <inputFilename>");
			System.exit(0);
		}
		
		in = new BufferedReader(new InputStreamReader(System.in));
		FileWriter fout; String inputl;
		
		try { 
			fout = new FileWriter(args[0]);
			
			int c = 1;
			System.out.println("Inserisci la riga 1");
			while ((inputl = in.readLine())!=null) { 
				c++;
				
				System.out.println("Questo è ciò che hai inserito: " + (inputl));
				fout.write(inputl+"\n", 0, inputl.length()+1);
				
				System.out.println("Inserisci la riga " + String.valueOf(c) + " o premi CTRL+D per terminare");
			}
			
			fout.close();
		}
		catch (NumberFormatException nfe) {
			nfe.printStackTrace(); System.exit(1);
		}
		catch (IOException e) {
			e.printStackTrace(); System.exit(2);
		}
	}
}