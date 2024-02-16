package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ServerImpl implements RemOp {

	private int portNumber;
	private int lastEndpoint = 1024;

	public ServerImpl() throws RemoteException {
		super();
	}

	public static void main(String[] args) {
        try {
			ServerImpl obj = new ServerImpl();

			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("Server", obj);

			System.out.println("Server bound in registry");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.getMessage());
			e.printStackTrace();
		}
    }

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

	public List<Filedesc> getFilesWithActiveServer(String dirName, EndpointMio clientEndpoint) throws RemoteException {
		return null;
		//ActServerThread actServerThread = new ActServerThread(dirName, clientEndpoint);
		
		//actServerThread.start();

	}
    
}
