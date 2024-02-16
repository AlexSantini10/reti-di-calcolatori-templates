package Es02_Prof;

import java.io.*;
import java.net.*;

class PutFileServerThread extends Thread {
    private Socket clientSocket = null;
    // Opzionalmente, anche questo potrebbe diventare un parametro (opzionale)!
    private int buffer_size = 4096;

    public PutFileServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        System.out.println("Attivazione figlio: " + Thread.currentThread().getName());

        DataInputStream inSock;
        DataOutputStream outSock;

        byte[] buffer = new byte[buffer_size];
        int cont = 0;
        int read_bytes = 0;
        DataOutputStream dest_stream = null;

        try {
            inSock = new DataInputStream(clientSocket.getInputStream());
            outSock = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("Problemi nella creazione degli stream di input/output su socket: ");
            ioe.printStackTrace();
            return;
        }
        try {
            try {
                String nomeFileRicevuto;
                long numeroByte;
                File fileCorr;
                FileOutputStream outFileCorr;

                while ((nomeFileRicevuto = inSock.readUTF()) != null) {
                    fileCorr = new File(nomeFileRicevuto);
                    if (fileCorr.exists()) {
                        outSock.writeUTF("salta file");
                    } else {
                        outSock.writeUTF("attiva");
                        numeroByte = inSock.readLong();
                        System.out.println("Scrivo il file " + nomeFileRicevuto + " di " + numeroByte + " byte");
                        outFileCorr = new FileOutputStream(nomeFileRicevuto);

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
            } catch (EOFException eof) {
                System.out.println("Raggiunta la fine delle ricezioni, chiudo...");
                clientSocket.close();
                System.out.println("PutFileServer: termino...");
                System.exit(0);
            } catch (SocketTimeoutException ste) {
                System.out.println("Timeout scattato: ");
                ste.printStackTrace();
                clientSocket.close();
                System.exit(1);
            } catch (Exception e) {
                System.out.println("Problemi, i seguenti : ");
                e.printStackTrace();
                System.out.println("Chiudo ed esco...");
                clientSocket.close();
                System.exit(2);
            }
        } catch (IOException ioe) {
            System.out.println("Problemi nella chiusura della socket: ");
            ioe.printStackTrace();
            System.out.println("Chiudo ed esco...");
            System.exit(3);
        }
    }

}// thread