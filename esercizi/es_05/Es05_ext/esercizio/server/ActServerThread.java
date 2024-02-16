package server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class ActServerThread extends Thread {

    private String dirName;
    private EndpointMio clientEndpoint;

    public ActServerThread(String dirName, EndpointMio clientEndpoint) {
        this.dirName = dirName;
        this.clientEndpoint = clientEndpoint;
    }

    @Override
    public void run() {
        try {
            System.out.println("Sending files to client " + clientEndpoint.getEIp() + ":" + clientEndpoint.getEPort());
            //Socket clientSocket = new Socket(clientEndpoint.getEIp(), clientEndpoint.getEPort());
            Socket clientSocket = new Socket("127.0.0.1", clientEndpoint.getEPort());

            File dir = new File(dirName);
            
            if (dir.exists() && dir.isDirectory()){
                File[] files = dir.listFiles();

                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());


                for (File file : files) {
                    // Invio il file al client
                    out.writeUTF(file.getName());
                    
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int read = 0;

                    while ((read = fis.read(buffer)) > 0) {
                        out.write(buffer, 0, read);
                    }

                    fis.close();
                    
                }

                out.close();
            }

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
