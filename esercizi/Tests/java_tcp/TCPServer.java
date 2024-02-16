import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HelloWorldServer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HelloWorldServer <port>");
            return;
        }

        int portNumber;
        try {
            portNumber = Integer.parseInt(args[0]);

            if (portNumber < 1024 || portNumber > 65535) {
                System.out.println("La porta deve essere compresa tra 1024 e 65535.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Il parametro della porta deve essere un numero intero.");
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server in ascolto sulla porta " + portNumber);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                     DataInputStream in = new DataInputStream(clientSocket.getInputStream())) {

                    String inputLine;
                    while ((inputLine = in.readUTF()) != null) {
                        System.out.println("Client dice: " + inputLine);
                        out.writeUTF("Hello, Client!");
                    }
                } catch (EOFException e) {
                    System.out.println("Il client ha chiuso la connessione.");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
