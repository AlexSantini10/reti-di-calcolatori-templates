package Es00_Estensione;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class ConsumerThread extends Thread {

    private String prefix;
    private String fileName;
    private FileReader fileReader;
    
    public ConsumerThread(String prefix, String fileName) {
        this.prefix = prefix;
        this.fileName = fileName;

        // Apertura del file
        try {
            fileReader = new FileReader(fileName);
        } 
        catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " non trovato");
        }
    }

    public ConsumerThread(String prefix, String fileName, FileReader fileReader) {
        this.prefix = prefix;
        this.fileName = fileName;
        this.fileReader = fileReader;
    }

    @Override
    public void run() {
        // Se il file non esiste, termina
        if (fileReader == null) {
            return;
        }

        // Lettura e stampa del file
        try {
            int x;
            char ch;

            String newFileName = fileName.substring(0, fileName.indexOf(".txt")) + "Without" + prefix + ".txt";
            FileWriter newFile = new FileWriter(newFileName);
            
            // Stampa su file temporaneo
            System.out.println("Stampa del file " + fileName + " senza il prefisso \"" + prefix + "\":");

            while ((x = fileReader.read()) >= 0) {
                ch = (char) x;

                if (prefix.indexOf(ch) == -1) {
                    //System.out.print(ch);
                    newFile.write(ch);
                }
            }

            fileReader.close();
            newFile.close();
            System.out.println("Fine stampa del file " + fileName);

            // Eliminazione del file originale e rinominazione del file temporaneo
            File oldFile = new File(fileName);
            File newFile2 = new File(newFileName);

            oldFile.delete();
            newFile2.renameTo(oldFile);
            newFile2.delete();

        } catch (Exception e) {
            System.out.println("Errore di lettura del file " + fileName);
            System.exit(2);
        }

        // Chiusura del file
        try {
            fileReader.close();
        } catch (Exception e) {
            System.out.println("Errore di chiusura del file " + fileName);
            System.exit(3);
        }
    }

}
