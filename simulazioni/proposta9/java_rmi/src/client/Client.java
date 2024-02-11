package client;

import server.RemoteServer;

public class Client {

    public static void main(String[] args) {
        try {
            RemoteServer server = (RemoteServer) java.rmi.Naming.lookup("rmi://localhost:2000/RemoteServer");
            System.out.println();
        } catch (Exception e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
