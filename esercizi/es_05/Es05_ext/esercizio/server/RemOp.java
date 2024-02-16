package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RemOp extends Remote {
    public abstract EndpointMio getFilesWithActiveClient(String dirName) throws RemoteException;
    public abstract List<Filedesc> getFilesWithActiveServer(String dirName, EndpointMio clientEndpoint) throws RemoteException;
}
