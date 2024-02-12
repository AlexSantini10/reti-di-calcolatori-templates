package client;

import server.RemoteServer;

public class Client {
	
	public static final String USAGE = "Utilizzo: Client <server_addr> <server_port>";

    public static void main(String[] args) {
    	if (args.length != 1) {
			System.out.println(USAGE);
			return;
		}
		
		int port = -1;
		try {
			port = Integer.valueOf(args[0]);
		}
		catch (NumberFormatException e) {
			System.out.println(USAGE);
			System.out.println("<port> deve essere un numero intero");
		}
    	
        try {
            RemoteServer server = (RemoteServer) java.rmi.Naming.lookup("rmi://localhost:2000/RemoteServer");
            System.out.println();
        } catch (Exception e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
