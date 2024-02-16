package server;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ActClientThread extends Thread {

    private ServerSocket threadSocket;
    private String dirName;

    public ActClientThread(ServerSocket threadSocket, String dirName) {
        this.threadSocket = threadSocket;
        this.dirName = dirName;
    }

    @Override
    public void run() {
        try {
            Socket clientSocket = threadSocket.accept();

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

        } 
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
