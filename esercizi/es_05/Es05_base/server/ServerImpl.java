import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerImpl extends UnicastRemoteObject implements RemOp {
    private ServerImpl() throws RemoteException {
        super();
    }

    public int conta_righe(String nomeFile, int numParole) throws RemoteException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(nomeFile));
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+"); // Splitting by space(s)
                if (words.length > numParole) {
                    count++;
                }
            }
            reader.close();
            return count;
        } catch (IOException e) {
            throw new RemoteException("Errore nella lettura del file: " + e.getMessage());
        }
    }

    public String elimina_riga(String nomeFile, int numRiga) throws RemoteException {
        try {
            File file = new File(nomeFile);
            if (!file.exists()) {
                throw new RemoteException("Il file non esiste");
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();

            if (lines.size() < numRiga) {
                throw new RemoteException("Il file ha meno righe di quella da eliminare");
            }

            lines.remove(numRiga - 1);

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String l : lines) {
                writer.write(l + "\n");
            }
            writer.close();

            return "File modificato: " + nomeFile + ". Numero righe rimaste: " + lines.size();
        } catch (IOException e) {
            throw new RemoteException("Errore durante l'eliminazione della riga: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            int registryPort = args.length > 0 ? Integer.parseInt(args[0]) : 1099;
            Registry registry = LocateRegistry.createRegistry(registryPort);

            ServerImpl server = new ServerImpl();
            registry.rebind("ServerImpl", server);

            System.out.println("Server in attesa...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
