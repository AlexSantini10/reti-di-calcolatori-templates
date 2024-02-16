package Es00_Estensione;

import java.io.*;

public class Produttore
{
	public static void main(String[] args) { 
		BufferedReader in = null;

		String userFormatString = "<numeroFile>:<dato>";
		
		// Controllo argomenti
		if (args.length < 1){
			System.out.println("Utilizzo: produttore <inputFilename1> ... <inputFilenameN>");
			System.exit(0);
		}
		
		in = new BufferedReader(new InputStreamReader(System.in));
		FileWriter fout[]; String inputl;

		// Apertura dei file
		fout = new FileWriter[args.length];
		
		for (int i = 0; i < args.length; i++) {
			try {
				fout[i] = new FileWriter(args[i], true);
			}
			catch (IOException e) {
				System.out.println("Errore di apertura file " + String.valueOf(args[i]));
				System.exit(1);
			}
		}
		
		// Ciclo di lettura da standard input e scrittura su file
		try {
			int c = 1;

			System.out.println("Inserisci la riga " + String.valueOf(c) + " o premi CTRL+Z + Enter (CTRL+D in Linux) per terminare, formato: " + userFormatString);

			while ((inputl = in.readLine())!=null) { 
				// Template numeroFile:dato
				try {
					int numeroFile = Integer.parseInt(inputl.substring(0, inputl.indexOf(":")));
					String toWrite = inputl.substring(inputl.indexOf(":")+1, inputl.length());

					// Scrittura sul file
					fout[numeroFile-1].write(toWrite+"\n", 0, toWrite.length()+1);

					c++;

					System.out.println("Hai scritto: " + toWrite + " sul file " + args[numeroFile-1]);
				}
				catch (NumberFormatException | ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException e) {
					System.out.println("Errore: numero file non valido");
				}
				catch (IOException e) {
					System.out.println("Errore di scrittura sul file");
					System.exit(2);
				}
				finally{
					System.out.println("Inserisci la riga " + String.valueOf(c) + " o premi CTRL+Z + Enter (CTRL+D in Linux) per terminare, formato: " + userFormatString);
				}
				
			}

			// Chiusura dei file
			for (int i = 0; i < args.length; i++) {
				fout[i].close();
			}
		}
		catch (NumberFormatException nfe) {
			nfe.printStackTrace(); 
			System.exit(3);
		}
		catch (IOException e) {
			e.printStackTrace(); System.exit(4);
		}
	}
}