package client;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import server.EndpointMio;
import server.Filedesc;
import server.RemOp;

public class Client {

    static RemOp server;
    static String registryHost;
    static int registryPort;

    static int clientPort = 10294;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Client <RegistryHost> <RegistryPort>");
            return;
        }

        registryHost = args[0];
        registryPort = Integer.parseInt(args[1]);

        try {
            Registry registry = LocateRegistry.getRegistry(registryHost, registryPort);
            server = (RemOp) registry.lookup("NewServerImpl");
        
            String dirName;
            String method;

            // Solo per testare il metodo in locale
            EndpointMio clientEndpoint = new EndpointMio("127.0.0.1", clientPort);

            // Creo la socket di ascolto
            ServerSocket serverSocket = new ServerSocket(clientEndpoint.getEPort());

            while (true) {
                System.out.println("Inserisci il metodo che vuoi utilizzare o exit per uscire: \n"
                    + "1. getFilesWithActiveClient\n"
                    + "2. getFilesWithActiveServer");
                
                method = System.console().readLine();

                if (method.equals("exit")) {
                    break;
                }

                System.out.println("Inserisci il nome della directory: ");
                dirName = System.console().readLine();

                if (method.equals("1")) {
                    getFilesWithActiveClient(dirName);
                }
                else if (method.equals("2")) {
                    getFilesWithActiveServer(dirName, clientEndpoint, serverSocket);
                }
                else {
                    System.out.println("Metodo non valido");
                }

                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void getFilesWithActiveClient(String dirName) throws RemoteException {
        EndpointMio endpoint = server.getFilesWithActiveClient(dirName);

        
        try{
            Socket socket = new Socket(registryHost, endpoint.getEPort());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            if (endpoint.getFiles().size() == 0) {
                System.out.println("Non ci sono file da ricevere\n");
                return;
            }
            
            // Ricevo i file dal server
            for (Filedesc file : endpoint.getFiles()) {
                // Ricevo il file dal server
                String fileName = in.readUTF();

                File dir = new File(dirName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // Creo il file
                File newFile = new File(dirName + "/" + fileName);

                System.out.println("Ricevo il file: " + newFile.getAbsolutePath());

                // Creo il file se non esiste
                if (!newFile.exists()) {
                    newFile.createNewFile();
                }

                // Scrivo il file
                FileOutputStream fos = new FileOutputStream(newFile);
                byte[] buffer = new byte[1024];
                int read = 0;

                while ((read = in.read(buffer)) > 0) {
                    fos.write(buffer, 0, read);
                }

                fos.close();
            }

            in.close();
            socket.close();
        } 
        catch (RemoteException e) {
            throw e;
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Files ricevuti e scritti correctamente nella directory: " + dirName + "\n");
    }

    public static void getFilesWithActiveServer(String dirName, EndpointMio clientEndpoint, ServerSocket serverSocket) throws RemoteException {
        List<Filedesc> files = server.getFilesWithActiveServer(dirName, clientEndpoint);

        try {
            

            System.out.println("In attesa di connessione da parte del server");
            System.out.println("IP: " + Inet4Address.getLocalHost().getHostAddress() + " Port: " + serverSocket.getLocalPort());

            // Creo la socket di connessione
            Socket socket = serverSocket.accept();

            System.out.println("Connessione stabilita con il server");

            // Creo il data input stream
            DataInput in = new DataInputStream(socket.getInputStream());

            // Ricevo i file dal server
            for (Filedesc file : files) {
                // Ricevo il file dal server
                String fileName = in.readUTF();

                File dir = new File(dirName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // Creo il file
                File newFile = new File(dirName + "/" + fileName);

                System.out.println("Ricevo il file: " + newFile.getAbsolutePath());

                // Creo il file se non esiste
                if (!newFile.exists()) {
                    newFile.createNewFile();
                }

                // Scrivo il file
                FileOutputStream fos = new FileOutputStream(newFile);
                byte[] buffer = new byte[1024];
                int read = 0;

                while ((read = ((DataInputStream) in).read(buffer)) > 0) {
                    fos.write(buffer, 0, read);
                }

                fos.close();
            }
        }
        catch (RemoteException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
