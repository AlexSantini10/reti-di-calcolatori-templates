import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HelloWorldClient {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java HelloWorldClient <serverIp> <serverPort>");
            return;
        }

        String serverAddress = args[0];

        int portNumber;
        try {
            portNumber = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Il parametro della porta deve essere un numero intero.");
            return;
        }

        try (Socket socket = new Socket(serverAddress, portNumber);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            String userInput;
            while (true) {
                // Legge l'input dall'utente
                userInput = System.console().readLine("Inserisci un messaggio: ");

                if (userInput.equals("exit")) {
                    break;
                }
                
                // Invia l'input al server
                out.writeUTF(userInput);
                
                // Legge la risposta dal server
                String serverResponse = in.readUTF();
                System.out.println("Server dice: " + serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
