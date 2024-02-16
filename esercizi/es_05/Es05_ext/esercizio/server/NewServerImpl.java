package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class NewServerImpl extends UnicastRemoteObject implements RemOp {

    private int portNumber;
    private int lastEndpoint = 1024;

    public NewServerImpl() throws RemoteException {
        // Constructor
        super();
    }

    public String sayHello() throws RemoteException {
        return "Hello, World!";
    }

    public static void main(String args[]) {
        try {
            NewServerImpl obj = new NewServerImpl();

            // Bind the remote object's stub in the registry
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind("NewServerImpl", obj);

            System.out.println("NewServerImpl bound in registry");
        } catch (Exception e) {
            System.err.println("NewServerImpl exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public EndpointMio getFilesWithActiveClient(String dirName) throws RemoteException {
		lastEndpoint++;

		if (lastEndpoint == portNumber) {
			lastEndpoint++;
		}

		if (lastEndpoint > 65535) {
			throw new RemoteException("No more ports available");
		}

		try {
			ServerSocket threadSocket = new ServerSocket(lastEndpoint);

			ActClientThread actClientThread = new ActClientThread(threadSocket, dirName);
			actClientThread.start();
			
		} catch (RemoteException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		EndpointMio res = new EndpointMio(lastEndpoint);
		res.setFilesFromDirName(dirName);

		return res;
	}

    @Override
    public List<Filedesc> getFilesWithActiveServer(String dirName, EndpointMio clientEndpoint) throws RemoteException {
        String clientIp;
        try {
            clientIp = RemoteServer.getClientHost();
            System.out.println("Client IP: " + clientIp);

            ActServerThread actServerThread = new ActServerThread(dirName, clientEndpoint);
            
            actServerThread.start();
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
        
        EndpointMio serverEndpoint = new EndpointMio(portNumber);
        serverEndpoint.setFilesFromDirName(dirName);

        return serverEndpoint.getFiles();
    }
}

