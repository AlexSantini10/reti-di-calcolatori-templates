package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {
    public String sayHello() throws RemoteException;
}
