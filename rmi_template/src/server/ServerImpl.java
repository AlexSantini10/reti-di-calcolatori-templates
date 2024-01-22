package server;

import java.io.Serializable;
import java.rmi.RemoteException;

public class ServerImpl implements RemoteServer, Serializable {

	@Override
	public String sayHello() throws RemoteException {
		return "CIAO";
	}

	public static void main(String[] args) {
		try {
			//java.rmi.registry.LocateRegistry.createRegistry(1099);
			ServerImpl server = new ServerImpl();
			java.rmi.Naming.rebind("rmi://localhost:1099/RemoteServer", server);
			System.out.println("Server ready");
		} catch (Exception e) {
			System.out.println("Server exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
