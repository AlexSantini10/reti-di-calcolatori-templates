import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Client <RegistryHost> <RegistryPort>");
            return;
        }

        String registryHost = args[0];
        int registryPort = Integer.parseInt(args[1]);

        try {
            Registry registry = LocateRegistry.getRegistry(registryHost, registryPort);
            RemOp server = (RemOp) registry.lookup("ServerImpl");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Scegli un'operazione:");
                System.out.println("1. Conta righe con parole superiori a un certo numero");
                System.out.println("2. Elimina una riga da un file");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear the buffer

                switch (choice) {
                    case 1:
                        System.out.println("Inserisci il nome del file:");
                        String fileCount = scanner.nextLine();
                        System.out.println("Inserisci il numero di parole:");
                        int numWords = scanner.nextInt();
                        scanner.nextLine(); // Clear the buffer

                        try {
                            int count = server.conta_righe(fileCount, numWords);
                            System.out.println("Numero di righe con parole superiori a " + numWords + ": " + count);
                        } catch (RemoteException e) {
                            System.err.println("Errore: " + e.getMessage());
                        }
                        break;
                    case 2:
                        System.out.println("Inserisci il nome del file:");
                        String fileDelete = scanner.nextLine();
                        System.out.println("Inserisci il numero della riga da eliminare:");
                        int lineNum = scanner.nextInt();
                        scanner.nextLine(); // Clear the buffer

                        try {
                            String result = server.elimina_riga(fileDelete, lineNum);
                            System.out.println("Esito eliminazione: " + result);
                        } catch (RemoteException e) {
                            System.err.println("Errore: " + e.getMessage());
                        }
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
