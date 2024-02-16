package test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements Hello {
    public HelloImpl() throws RemoteException {
        // Constructor
        super();
    }

    public String sayHello() throws RemoteException {
        return "Hello, World!";
    }

    public static void main(String args[]) {
        try {
            HelloImpl obj = new HelloImpl();

            // Bind the remote object's stub in the registry
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind("HelloServer", obj);

            System.out.println("HelloServer bound in registry");
        } catch (Exception e) {
            System.err.println("HelloServer exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

