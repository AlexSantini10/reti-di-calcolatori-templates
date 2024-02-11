package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {
    public String[] lista_nomi_file_contenenti_parola_in_linea(String nome_dir, String parola) throws RemoteException;
    public int conta_numero_linee(String nome_file, String parola) throws RemoteException;
}
