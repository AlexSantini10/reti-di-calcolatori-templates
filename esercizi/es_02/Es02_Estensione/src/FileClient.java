package Es02_Base;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class FileClient {
    
    public static void main(String[] args) throws IOException {
        InetAddress addr = null;
        int port = -1;

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String nomeFolder = null;
        int dim_min = 0;
        // We set the buffer size for the file transfer as a static member.
        // This is the default value: the user has the option to modify it through a cmd
        // parameter. Note that adding OPTIONAL parameters does not violate the project
        // specification.
        int buffer_size = 4096;

        // Controllo argomenti
        try {
            if (args.length == 3 || args.length == 4) {
                addr = InetAddress.getByName(args[0]);
                port = Integer.parseInt(args[1]);
                if (port < 1024 || port > 65535) {
                    System.out.println(
                            "Usage: java MPutFileClient serverAddr serverPort minFileSize [transferBufferSize]");
                    System.exit(1);
                }
                dim_min = Integer.parseInt(args[2]);
                if (args.length == 4) {
                    buffer_size = Integer.parseInt(args[3]);
                }
            } else {
                System.out.println("Usage: java MPutFileClient serverAddr serverPort minFileSize [transferBufferSize]");
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("Problemi, i seguenti: ");
            e.printStackTrace();
            System.out.println("Usage: java MPutFileClient serverAddr serverPort minFileSize [transferBufferSize]");
            System.exit(2);
        }

        // Variables for reading and transferring files
        byte[] buffer = new byte[buffer_size];
        int cont = 0;
        int read_bytes = 0;
        long daTrasferire = 0;
        DataInputStream src_stream = null;
        String requestType = null;

        // Variables for sockets
        Socket socket = null;
        DataInputStream inSock = null;
        DataOutputStream outSock = null;
        
        
        
        try {
            // Ciclo di invii
            while (true) {
                // Creazione socket
                try {
                    socket = new Socket(addr, port);
                    socket.setSoTimeout(30000);
                    System.out.println("Creata la socket: " + socket);
                    inSock = new DataInputStream(socket.getInputStream());
                    outSock = new DataOutputStream(socket.getOutputStream());
                } catch (IOException ioe) {
                    System.out.println("Problemi nella creazione degli stream su socket: ");
                    ioe.printStackTrace();
                    System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, solo invio per continuare: ");
                    System.exit(1);
                }

                
                // Invio tipo di richiesta
                System.out.println("\nInserire il tipo di richiesta (get/put/close): ");

                requestType = stdIn.readLine();

                if (requestType.equals("close")) {
                    System.out.println("Chiudo il server e termino");

                    try {
                        outSock.writeUTF(requestType);
                    } catch (IOException ioe) {
                        System.out.println("Problemi nell'invio della richiesta: ");
                        ioe.printStackTrace();
                        System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, solo invio per continuare: ");
                        System.exit(1);
                    }

                    System.exit(1);
                }
                else
                
                if (!requestType.equals("get") && !requestType.equals("put")) {
                    System.out.println("Tipo di richiesta non valido, chiudo il client");
                    System.exit(1);
                }
        
                try {
                    outSock.writeUTF(requestType);
                } catch (IOException ioe) {
                    System.out.println("Problemi nell'invio della richiesta: ");
                    ioe.printStackTrace();
                    System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, solo invio per continuare: ");
                    System.exit(1);
                }

                System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, altrimenti immetti il nome della cartella: ");
                nomeFolder = stdIn.readLine();

                if (requestType.equals("put")){
                    // Invio nome della directory
                    try {
                        outSock.writeUTF(nomeFolder);
                    } catch (IOException ioe) {
                        System.out.println("Problemi nell'invio della richiesta: ");
                        ioe.printStackTrace();
                        System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, solo invio per continuare: ");
                        System.exit(1);
                    }

                    File dirCorr = new File(nomeFolder);

                    if (dirCorr.exists() && dirCorr.isDirectory()) {
                        File[] files = dirCorr.listFiles();

                        for (int i = 0; i < files.length; i++) {
                            File fileCorr = files[i];

                            if (fileCorr.isFile() && dim_min <= fileCorr.length()) {
                                System.out.println("File con nome: " + fileCorr.getName());
                                outSock.writeUTF(fileCorr.getName());
                                String result = inSock.readUTF();

                                if (result.equals("attiva")) {
                                    System.out.println("Il file " + fileCorr.getName()
                                            + " NON e' presente sul server: inizio il trasferimento");
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
                                    System.out.println("Il file " + fileCorr.getName()
                                            + " era gia' presente sul server e non e' stato sovrascritto");
                                else {
                                    System.out.println("MPutFileClient: violazione protocollo...");
                                    System.exit(4);
                                }
                            } else {
                                System.out.println("File saltato");
                            }
                        }
                    } else {
                        System.out.println("Error: the specified directory either does not exist or is not a directory");
                        continue;
                    }
                }
                else if (requestType.equals("get")){
                    // Invio nome della directory
                    try {
                        outSock.writeUTF(nomeFolder);
                    } catch (IOException ioe) {
                        System.out.println("Problemi nell'invio della richiesta: ");
                        ioe.printStackTrace();
                        System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, solo invio per continuare: ");
                        System.exit(1);
                    }

                    // Ricevo risposta per la directory
                    try {
                        String result = inSock.readUTF();
                        if (result.equals("notfound")) {
                            System.out.println("Directory non trovata");
                            continue;
                        }
                        else if (result.equals("found")){
                            System.out.println("Directory trovata");
                        }
                    } catch (IOException ioe) {
                        System.out.println("Problemi nella ricezione della risposta: ");
                        ioe.printStackTrace();
                        System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, solo invio per continuare: ");
                        System.exit(1);
                    }

                    // Invio dimensione minima
                    try {
                        outSock.writeInt(dim_min);
                    } catch (IOException ioe) {
                        System.out.println("Problemi nell'invio della richiesta: ");
                        ioe.printStackTrace();
                        System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, solo invio per continuare: ");
                        System.exit(1);
                    }

                    // Risposta per la dimensione minima
                    try {
                        String result = inSock.readUTF();
                        if (result.equals("notvalid")) {
                            System.out.println("Dimensione minima non valida");
                            continue;
                        }
                        else if (result.equals("ok")){
                            System.out.println("Dimensione minima valida");
                        }
                    } catch (IOException ioe) {
                        System.out.println("Problemi nella ricezione della risposta: ");
                        ioe.printStackTrace();
                        System.out.print("\n^D(Unix)/^Z(Win)+invio per uscire, solo invio per continuare: ");
                        System.exit(1);
                    }


                    String nomeFileRicevuto;
                    long numeroByte;
                    File fileCorr;
                    FileOutputStream outFileCorr;
                    DataOutputStream dest_stream = null;

                    fileCorr = new File(nomeFolder);

                    System.out.println("Ricezione dei file in corso...");
                    
                    try {

                        while ((nomeFileRicevuto = inSock.readUTF()) != null) {
                            // Creazione directory se non esiste
                            if (!fileCorr.exists()) {
                                fileCorr.mkdir();
                            }
        
                            fileCorr = new File(nomeFolder + "/" + nomeFileRicevuto);
                            if (fileCorr.exists()) {
                                outSock.writeUTF("salta file");
                            } else {
                                outSock.writeUTF("attiva");
                                numeroByte = inSock.readLong();
                                System.out.println("Scrivo il file " + nomeFolder + "/" + nomeFileRicevuto + " di " + numeroByte + " byte");
                                outFileCorr = new FileOutputStream(nomeFolder + "/" + nomeFileRicevuto);
    
                                // Ricevo il file (in linea)
                                dest_stream = new DataOutputStream(outFileCorr);
                                cont = 0;
                                try {
                                    // esco dal ciclo quando ho letto il numero di byte da trasferire
                                    while (cont < numeroByte) {
                                        read_bytes = inSock.read(buffer);
                                        dest_stream.write(buffer, 0, read_bytes);
                                        cont += read_bytes;
                                    }
                                    dest_stream.flush();
                                    System.out.println("Byte trasferiti: " + cont);
                                }
                                // l'eccezione dovrebbe scattare solo se ci aspettiamo un numero sbagliato di byte da leggere
                                catch (EOFException e) {
                                    System.out.println("Problemi, i seguenti: ");
                                    e.printStackTrace();
                                }
    
                                outFileCorr.close();
                            }
                        
                        } // while
                    }
                    catch (EOFException eoe) {
                        System.out.println("Raggiunta la fine delle ricezioni, chiudo...");
                        socket.close();
                        System.out.println("PutFileServer: termino...");
                    }
                    catch (SocketTimeoutException ste) {
                        System.out.println("Timeout scattato: ");
                        ste.printStackTrace();
                        socket.close();
                        System.exit(1);
                    }
                }

            }
        }
        catch (Error e){
            e.printStackTrace();
            System.exit(1);
        }
        
    }

}
