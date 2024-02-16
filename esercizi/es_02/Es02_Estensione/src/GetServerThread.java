package Es02_Base;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class GetServerThread extends Thread {

    private Socket clientSocket = null;
    // Opzionalmente, anche questo potrebbe diventare un parametro (opzionale)!
    private int buffer_size = 4096;

    public GetServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        System.out.println("Attivazione figlio GET: " + Thread.currentThread().getName());

        DataInputStream inSock;
        DataOutputStream outSock;

        byte[] buffer = new byte[buffer_size];
        int cont = 0;
        int read_bytes = 0;
        DataOutputStream dest_stream = null;
        long daTrasferire = 0;
        DataInputStream src_stream = null;
        String requestType = null;

        try {
            inSock = new DataInputStream(clientSocket.getInputStream());
            outSock = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("Problemi nella creazione degli stream di input/output su socket: ");
            ioe.printStackTrace();
            return;
        }

        try {
            System.out.println("Server: ricevo nome directory");
            String nomeDirectory = inSock.readUTF();
            File dirCorr = new File(nomeDirectory);


            if (dirCorr.exists() && dirCorr.isDirectory()){
                // Invio stringa di conferma
                outSock.writeUTF("found");
            }
            else {
                System.out.println("Error: the specified directory either does not exist or is not a directory");

                // Invio stringa di terminazione
                outSock.writeUTF("notfound");
            }

            System.out.println("Server: ricevo dimensione minima");
            int dim_min = inSock.readInt();

            outSock.writeUTF("ok");


            File[] files = dirCorr.listFiles();

            System.out.println("Server: invio numero file: " + files.length);

            for (int i = 0; i < files.length; i++) {
                File fileCorr = files[i];

                if (fileCorr.isFile() && dim_min <= fileCorr.length()) {
                    System.out.println("File con nome: " + fileCorr.getName());
                    outSock.writeUTF(fileCorr.getName());
                    String result = inSock.readUTF();

                    if (result.equals("attiva")) {
                        System.out.println("Il file " + fileCorr.getName() + " NON e' presente sul client: inizio il trasferimento");
                        daTrasferire = fileCorr.length();
                        outSock.writeLong(daTrasferire);
                        // In linea trasferiamo il file
                        src_stream = new DataInputStream(new FileInputStream(fileCorr.getAbsolutePath()));

                        // ciclo di lettura da sorgente e scrittura su destinazione
                        try {
                            // esco dal ciclo quando ho letto il numero di byte da trasferire
                            cont = 0;

                            while (cont < daTrasferire) {
                                read_bytes = src_stream.read(buffer);
                                outSock.write(buffer, 0, read_bytes);
                                cont += read_bytes;
                            }
                            outSock.flush();
                            System.out.println("Byte trasferiti: " + cont);
                        }
                        // l'eccezione dovrebbe scattare solo se ci aspettiamo un numero sbagliato di byte da leggere
                        catch (EOFException e) {
                            System.out.println("Problemi, i seguenti: ");
                            e.printStackTrace();
                        }
                    } else if (result.equals("salta file"))
                        System.out.println("Il file " + fileCorr.getName() + " era gia' presente sul client e non e' stato sovrascritto");
                    else {
                        System.out.println("MPutFileClient: violazione protocollo...");
                        System.exit(4);
                    }
                } else {
                    System.out.println("File saltato");
                }
            }
            
            System.out.println("Server: termino");
            clientSocket.close();
        }
        catch (IOException ioe) {
            System.out.println("Problemi nella ricezione: ");
            ioe.printStackTrace();
            System.exit(1);
        }
        catch (Error e) {
            System.err.println("Server: problemi nel ciclo di ascolto: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
}
