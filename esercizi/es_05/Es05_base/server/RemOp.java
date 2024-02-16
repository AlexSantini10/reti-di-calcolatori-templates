import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemOp extends Remote {
    int conta_righe(String nomeFile, int numParole) throws RemoteException;
    String elimina_riga(String nomeFile, int numRiga) throws RemoteException;
}
